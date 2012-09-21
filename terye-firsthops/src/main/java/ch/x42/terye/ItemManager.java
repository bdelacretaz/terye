package ch.x42.terye;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.ItemExistsException;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Value;

public class ItemManager {

    private static ItemManager instance = null;
    private Map<String, ItemImpl> items = new HashMap<String, ItemImpl>();

    private ItemManager() {
    }

    public static ItemManager getInstance() {
        if (instance == null) {
            instance = new ItemManager();
        }
        return instance;
    }

    public static void reset() {
        instance = null;
    }

    public NodeImpl createNode(Path path) throws ItemExistsException,
            PathNotFoundException, RepositoryException {
        // check if path already exists
        if (itemExists(path)) {
            throw new ItemExistsException("An item at that path already exists");
        }

        NodeImpl node = new NodeImpl(path);
        // get path to parent
        Path parentPath = path.getParent();
        // nodes that have a parent (all but the root node)
        if (parentPath != null) {
            NodeImpl parent = getNode(parentPath);
            // add as child to parent
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

        PropertyImpl property = new PropertyImpl(path, value);
        // get path to parent
        Path parentPath = path.getParent();
        NodeImpl parent = getNode(parentPath);
        // add property to parent
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
}