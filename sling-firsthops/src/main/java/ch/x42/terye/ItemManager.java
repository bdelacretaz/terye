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

import ch.x42.terye.store.ChangeLog;
import ch.x42.terye.store.ItemStore;
import ch.x42.terye.store.ItemType;

public class ItemManager {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private SessionImpl session;
    private ItemStore store;
    private ChangeLog log;
    private NavigableMap<String, ItemImpl> cache;
    // stores paths of items that have been removed in this session
    private Set<String> removed = new HashSet<String>();

    protected ItemManager(SessionImpl session) {
        this.session = session;
        this.store = ItemStore.getInstance();
        this.log = new ChangeLog();
        this.cache = new TreeMap<String, ItemImpl>();
    }

    public ItemImpl getItem(Path path, ItemType type)
            throws PathNotFoundException {
        // check if the item or one of its ancestors has
        // been removed in this session
        String pathStr = path.toString();
        Iterator<String> iterator = removed.iterator();
        while (iterator.hasNext()) {
            String prefix = iterator.next();
            if (pathStr.startsWith(prefix)) {
                throw new PathNotFoundException(pathStr);
            }
        }

        // check if the item is cached
        ItemImpl item = cache.get(pathStr);
        if (item != null) {
            // if type matters, then the types must match
            if (type == null || item.getItemType().equals(type)) {
                return item;
            }
            throw new PathNotFoundException(pathStr);
        }

        // load item from store
        item = store.load(pathStr, type);
        if (item == null) {
            throw new PathNotFoundException(pathStr);
        }

        // instantiate new in-memory copy and cache it
        if (item.getItemType().equals(ItemType.NODE)) {
            item = new NodeImpl(session, (NodeImpl) item);
        } else {
            item = new PropertyImpl(session, (PropertyImpl) item);
        }
        cache.put(pathStr, item);

        return item;
    }

    public ItemImpl getItem(Path path) throws PathNotFoundException {
        logger.debug("getItem(" + path.toString() + ")");
        return getItem(path, null);
    }

    public NodeImpl getNode(Path path) throws PathNotFoundException {
        return (NodeImpl) getItem(path, ItemType.NODE);
    }

    public PropertyImpl getProperty(Path path) throws PathNotFoundException {
        return (PropertyImpl) getItem(path, ItemType.PROPERTY);
    }

    public boolean nodeExists(Path path) {
        try {
            getNode(path);
        } catch (PathNotFoundException e) {
            return false;
        }
        return true;
    }

    public boolean propertyExists(Path path) {
        try {
            getProperty(path);
        } catch (PathNotFoundException e) {
            return false;
        }
        return true;
    }

    public boolean itemExists(Path path) {
        return nodeExists(path) || propertyExists(path);
    }

    public NodeImpl createNode(Path path) throws ItemExistsException,
            PathNotFoundException, RepositoryException {
        logger.debug("createNode(" + path.toString() + ")");
        // check if path already exists
        if (itemExists(path)) {
            throw new ItemExistsException("An item already exists at: " + path);
        }

        // create new node
        NodeImpl node = new NodeImpl(session, path);
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
        PropertyImpl property = new PropertyImpl(session, path, value);
        cache.put(path.toString(), property);
        log.itemAdded(property);
        removed.remove(path.toString());

        // add to parent
        NodeImpl parent = getNode(path.getParent());
        parent.addProperty(property);
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
        store.persist(log);
        log.purge();
    }

    public boolean hasPendingChanges() {
        return !log.isEmpty();
    }

}
