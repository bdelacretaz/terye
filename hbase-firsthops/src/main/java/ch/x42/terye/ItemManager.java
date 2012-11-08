package ch.x42.terye;

import java.util.HashSet;
import java.util.Iterator;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;

import javax.jcr.ItemExistsException;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Value;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.x42.terye.persistence.ChangeLog;
import ch.x42.terye.persistence.ItemState;
import ch.x42.terye.persistence.NodeState;
import ch.x42.terye.persistence.PersistenceManager;
import ch.x42.terye.persistence.PropertyState;
import ch.x42.terye.persistence.id.ItemId;
import ch.x42.terye.persistence.id.NodeId;
import ch.x42.terye.persistence.id.PropertyId;

public class ItemManager {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private SessionImpl session;
    private PersistenceManager persistenceManager;
    private ChangeLog log;
    private NavigableMap<String, ItemImpl> cache;
    // stores paths of items that have been removed in this session
    private Set<String> removed = new HashSet<String>();

    protected ItemManager(SessionImpl session) {
        this.session = session;
        this.persistenceManager = ((WorkspaceImpl) session.getWorkspace())
                .getPersistenceManager();
        this.log = new ChangeLog();
        this.cache = new TreeMap<String, ItemImpl>();
    }

    private ItemImpl createNewInstance(ItemState state) {
        if (state.isNode()) {
            return new NodeImpl(session, (NodeState) state);
        }
        return null;
    }

    public ItemImpl getItem(ItemId id) throws PathNotFoundException,
            RepositoryException {
        logger.debug("getItem({})", id);
        String path = id.toString();
        // check if the item or one of its ancestors has
        // been removed in this session
        Iterator<String> iterator = removed.iterator();
        while (iterator.hasNext()) {
            String prefix = iterator.next();
            if (path.startsWith(prefix)) {
                throw new PathNotFoundException(path);
            }
        }

        // check if the item is cached
        ItemImpl item = cache.get(path);
        if (item != null) {
            return item;
        }

        // load item state from store
        ItemState state = persistenceManager.loadItem(id);
        if (state == null) {
            throw new PathNotFoundException(path);
        }

        // instantiate new in-memory copy and cache it
        item = createNewInstance(state);
        cache.put(path, item);

        return item;
    }

    public ItemImpl getItem(Path path) throws PathNotFoundException,
            RepositoryException {
        // XXX: hacky
        try {
            return getNode(path);
        } catch (PathNotFoundException e) {
            return getProperty(path);
        }
    }

    public NodeImpl getNode(Path path) throws PathNotFoundException,
            RepositoryException {
        NodeId id = new NodeId(path.getCanonical().toString());
        return (NodeImpl) getItem(id);
    }

    public PropertyImpl getProperty(Path path) throws RepositoryException {
        PropertyId id = new PropertyId(path.getCanonical().toString());
        return (PropertyImpl) getItem(id);
    }

    public boolean nodeExists(Path path) throws RepositoryException {
        try {
            getNode(path);
        } catch (PathNotFoundException e) {
            return false;
        }
        return true;
    }

    public boolean propertyExists(Path path) throws RepositoryException {
        try {
            getProperty(path);
        } catch (PathNotFoundException e) {
            return false;
        }
        return true;
    }

    public boolean itemExists(Path path) throws RepositoryException {
        return nodeExists(path) || propertyExists(path);
    }

    public NodeImpl createNode(Path path, String primaryNodeTypeName)
            throws ItemExistsException, PathNotFoundException,
            RepositoryException {
        logger.debug("createNode({})", path);
        // check if path already exists
        if (itemExists(path)) {
            throw new ItemExistsException("An item already exists at: " + path);
        }

        // create new node
        NodeId id = new NodeId(path.getCanonical().toString());
        NodeState state = new NodeState(id, primaryNodeTypeName);
        NodeImpl node = new NodeImpl(session, state);
        cache.put(path.toString(), node);
        log.itemAdded(node);
        removed.remove(path.toString());

        // add to parent
        Path parentPath = path.getParent();
        if (parentPath == null) {
            // only the case for the root node
            return node;
        }
        NodeImpl parent = getNode(parentPath);
        parent.addChild(node);
        log.itemModified(parent);

        return node;
    }

    public PropertyImpl createProperty(Path path, Value value)
            throws ItemExistsException, PathNotFoundException,
            RepositoryException {
        // disallow nodes and properties having the same path
        if (nodeExists(path)) {
            throw new ItemExistsException("A node already exists at: " + path);
        }

        // create new property
        PropertyId id = new PropertyId(path.getCanonical().toString());
        PropertyState state = new PropertyState(id);
        PropertyImpl property = new PropertyImpl(session, state);
        cache.put(path.toString(), property);
        log.itemAdded(property);
        removed.remove(path.toString());

        // add to parent
        NodeImpl parent = getNode(path.getParent());
        parent.addChild(property);
        log.itemModified(parent);

        return property;
    }

    public void removeItem(Path path) throws RepositoryException {
        ItemImpl item = getItem(path);
        cache.remove(path.toString());
        // takes care of removing descendants from store
        log.itemRemoved(item);
        // add to paths removed in this session
        removed.add(path.toString());

        // remove reference in parent
        NodeImpl parent = (NodeImpl) item.getParent();
        parent.removeChild(item);
        log.itemModified(parent);

        // if it is a node...
        if (!item.isNode()) {
            return;
        }
        // ...remove its descendants from cache
        Iterator<String> iterator = cache.tailMap(path.toString(), true)
                .navigableKeySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            if (!key.startsWith(path.toString())) {
                break;
            }
            iterator.remove();
        }
    }

    public void persistChanges() throws RepositoryException {
        // store.persist(log);
        log.purge();
    }

    public boolean hasPendingChanges() {
        return !log.isEmpty();
    }

}
