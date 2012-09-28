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

import ch.x42.terye.persistence.ChangeLog;
import ch.x42.terye.persistence.ItemState;
import ch.x42.terye.persistence.ItemType;
import ch.x42.terye.persistence.NodeState;
import ch.x42.terye.persistence.PersistenceManager;
import ch.x42.terye.persistence.PropertyState;

public class ItemManager {

    private SessionImpl session;
    private PersistenceManager pm;
    private ChangeLog log;
    private NavigableMap<String, ItemImpl> cache = new TreeMap<String, ItemImpl>();
    private Set<String> removed = new HashSet<String>();

    protected ItemManager(SessionImpl session) throws RepositoryException {
        this.session = session;
        pm = PersistenceManager.getInstance();
        log = new ChangeLog();
    }

    /**
     * Returns an item by looking up the cache and if not present fetching the
     * item from the database.
     * 
     * @param path canonical path
     * @param type item type
     * @return the item
     * @throws PathNotFoundException when no item at that path exists or the
     *             types don't match
     */
    public ItemImpl getItem(String path, ItemType type)
            throws PathNotFoundException {
        // check if the item has been loaded and removed in this session
        if (removed.contains(path)) {
            throw new PathNotFoundException();
        }
        // check if the item is cached
        ItemImpl item = cache.get(path);
        if (item != null) {
            if (item.getState().getType().equals(type)) {
                return item;
            }
            throw new PathNotFoundException();
        }
        // load item state from db
        ItemState state = pm.load(path, type);
        if (state == null) {
            throw new PathNotFoundException();
        }
        // instantiate, cache and return item
        if (type.equals(ItemType.NODE)) {
            item = new NodeImpl(session, (NodeState) state);
        } else {
            item = new PropertyImpl(session, (PropertyState) state);
        }
        cache.put(path, item);
        return item;
    }

    public NodeImpl getNode(String path) throws PathNotFoundException {
        return (NodeImpl) getItem(path, ItemType.NODE);
    }

    public PropertyImpl getProperty(String path) throws PathNotFoundException {
        return (PropertyImpl) getItem(path, ItemType.PROPERTY);
    }

    /**
     * @param path canonical path
     * @param parent canonical path to parent
     */
    public NodeImpl createNode(String path, String parentPath)
            throws PathNotFoundException, ItemExistsException {
        if (nodeExists(path)) {
            throw new ItemExistsException();
        }
        NodeState state = new NodeState(path, parentPath);
        NodeImpl node = new NodeImpl(session, state);
        cache.put(path, node);
        removed.remove(path);
        log.itemAdded(node);
        if (parentPath == null) {
            // only the case for the root node
            return node;
        }
        NodeImpl parent = getNode(parentPath);
        parent.getState().getChildren().add(path);
        log.itemModified(parent);
        return node;
    }

    /**
     * 
     * @param path canonical path
     * @param parentPath canonical path to parent
     */
    public PropertyImpl createProperty(String path, String parentPath,
            Value value) throws PathNotFoundException, ItemExistsException {
        // disallow nodes and properties having the same path
        if (nodeExists(path)) {
            throw new ItemExistsException();
        }
        PropertyState state = new PropertyState(path, parentPath, value);
        PropertyImpl property = new PropertyImpl(session, state);
        cache.put(path, property);
        removed.remove(path);
        log.itemAdded(property);
        NodeImpl parent = getNode(parentPath);
        if (!parent.getState().getProperties().contains(path)) {
            parent.getState().getProperties().add(path);
        }
        log.itemModified(parent);
        return property;
    }

    /**
     * Removes an item from cache and the database (on persist). All descendents
     * are automatically being removed from the database.
     * 
     * @param path canonical path
     */
    public void removeItem(String path) throws RepositoryException {
        // item must be in cache, since we're being called from it
        ItemImpl item = cache.get(path);
        cache.remove(path);
        removed.add(path);
        // takes care of removing descendents from db
        log.itemRemoved(item);

        // remove reference in parent
        NodeImpl parent = (NodeImpl) item.getParent();
        if (item.isNode()) {
            parent.getState().getChildren().remove(path);
        } else {
            parent.getState().getProperties().remove(path);
        }
        log.itemModified(parent);

        // only for nodes: remove descendents from cache
        if (!item.isNode()) {
            return;
        }
        Iterator<String> iterator = cache.tailMap(path, true).navigableKeySet()
                .iterator();
        boolean done = false;
        while (iterator.hasNext() && !done) {
            String key = iterator.next();
            if (!key.startsWith(path)) {
                done = true;
            } else {
                iterator.remove();
            }
        }
    }

    /**
     * @param path canonical path
     */
    public boolean nodeExists(String path) {
        try {
            getNode(path);
        } catch (PathNotFoundException e) {
            return false;
        }
        return true;
    }

    /**
     * @param path canonical path
     */
    public boolean propertyExists(String path) {
        try {
            getProperty(path);
        } catch (PathNotFoundException e) {
            return false;
        }
        return true;
    }

    /**
     * @param path canonical path
     */
    public boolean itemExists(String path) {
        // XXX: not optimal
        return nodeExists(path) || propertyExists(path);
    }

    public void save() throws RepositoryException {
        pm.persist(log);
    }

}
