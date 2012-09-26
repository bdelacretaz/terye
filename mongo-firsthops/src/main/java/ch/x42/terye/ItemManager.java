package ch.x42.terye;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.RepositoryException;

import ch.x42.terye.persistence.ChangeLog;
import ch.x42.terye.persistence.NodeState;
import ch.x42.terye.persistence.PersistenceManager;

public class ItemManager {

    private PersistenceManager pm;
    private ChangeLog log;
    private Map<String, ItemImpl> cache = new HashMap<String, ItemImpl>();

    protected ItemManager() throws RepositoryException {
        pm = PersistenceManager.getInstance();
        log = new ChangeLog();
    }
    
    private boolean isCached(String path) {
        return cache.containsKey(path);
    }

    /**
     * @param path canonical path
     */
    public NodeImpl getNode(String path) {
        if (isCached(path)) {
            return (NodeImpl) cache.get(path);
        }
        NodeState ns = pm.load(path);
        if (ns == null) {
            return null;
        }
        NodeImpl node = new NodeImpl(this, ns.getPath(), ns.getParent());
        cache.put(path, node);
        return node;
    }

    /**
     * @param path canonical path
     * @param parent canonical path to parent
     */
    public NodeImpl createNode(String path, String parent) {
        NodeImpl node = new NodeImpl(this, path, parent);
        cache.put(path, node);
        log.nodeAdded(node);
        return node;
    }

    /**
     * @param path canonical path
     * @param parent canonical path to parent
     */
    public NodeImpl getOrCreateNode(String path, String parent) {
        NodeImpl node = getNode(path);
        if (node != null) {
            return node;
        }
        return createNode(path, parent);
    }
    
    /**
     * @param path canonical path
     */
    public boolean nodeExists(String path) {
        if (isCached(path)) {
            return true;
        }
        return getNode(path) != null;
    }

    public void save() throws RepositoryException {
        pm.persist(log);
    }

}
