package ch.x42.terye;

import java.util.Iterator;
import java.util.NavigableMap;
import java.util.TreeMap;

import javax.jcr.ItemExistsException;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Value;

public class ItemManager {

    private SessionImpl session;
    private NavigableMap<String, ItemImpl> items = new TreeMap<String, ItemImpl>();

    protected ItemManager(SessionImpl session) {
        this.session = session;
    }

    public NodeImpl createNode(Path path) throws ItemExistsException,
            PathNotFoundException, RepositoryException {
        // check if path already exists
        if (itemExists(path)) {
            throw new ItemExistsException("An item at that path already exists");
        }

        // get parent
        Path parentPath = path.getParent();
        NodeImpl parent = null;
        if (parentPath != null) {
            parent = getNode(parentPath);
        }
        // create node and add to parent
        NodeImpl node = new NodeImpl(session, path, parent);
        if (parent != null) {
            parent.addChild(node);
        }
        items.put(path.toString(), node);
        return node;
    }

    public PropertyImpl createProperty(Path path, Value value) throws ItemExistsException,
            PathNotFoundException, RepositoryException {
        // check if a node already exists at that path
        // (properties get overwritten)
        if (nodeExists(path)) {
            throw new ItemExistsException("An node at that path already exists");
        }

        // get path to parent
        Path parentPath = path.getParent();
        NodeImpl parent = getNode(parentPath);
        // create property and add to parent
        PropertyImpl property = new PropertyImpl(session, path, value, parent);
        parent.addProperty(property);
        items.put(path.toString(), property);
        return property;
    }
    
    public boolean itemExists(Path path) {
        return items.containsKey(path.toString());
    }

    public ItemImpl getItem(Path path) throws PathNotFoundException {
        if (!itemExists(path)) {
            throw new PathNotFoundException();
        }
        return items.get(path.toString());
    }

    public boolean nodeExists(Path path) {
        try {
            return itemExists(path) && getItem(path) instanceof NodeImpl;
        } catch (PathNotFoundException e) {
            // should not happen
            e.printStackTrace();
        }
        return false;
    }

    public NodeImpl getNode(Path path) throws PathNotFoundException {
        if (!nodeExists(path)) {
            throw new PathNotFoundException();
        }
        return (NodeImpl) getItem(path);
    }
    
    public boolean propertyExists(Path path) {
        try {
            return itemExists(path) && getItem(path) instanceof PropertyImpl;
        } catch (PathNotFoundException e) {
            // should not happen
            e.printStackTrace();
        }
        return false;
    }
    
    public PropertyImpl getProperty(Path path) throws PathNotFoundException {
        if (!propertyExists(path)) {
            throw new PathNotFoundException();
        }
        return (PropertyImpl) getItem(path);
    }
    
    public void removeItem(Path path) throws RepositoryException {
        // get item
        ItemImpl item = getItem(path);        
        // remove reference in parent node
        ((NodeImpl) item.getParent()).removeChild(item);
        // remove item from map
        String pathStr = path.toString();
        items.remove(pathStr);
        
        // remove all items from the map that are further down the path tree
        Iterator<String> iterator = items.tailMap(pathStr, true).navigableKeySet().iterator();
        boolean done = false;
        while (iterator.hasNext() && !done) {
            String key = iterator.next();
            if(!key.startsWith(pathStr)) {
                done = true;
            } else {
                iterator.remove();
            }
        }
    }
    
}