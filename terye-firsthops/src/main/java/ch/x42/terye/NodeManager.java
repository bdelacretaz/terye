package ch.x42.terye;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.ItemExistsException;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;

public class NodeManager {

    private static NodeManager instance = null;
    private Map<String, NodeImpl> nodes = new HashMap<String, NodeImpl>();

    private NodeManager() {
    }

    public static NodeManager getInstance() {
        if (instance == null) {
            instance = new NodeManager();
        }
        return instance;
    }
    
    public static void reset() {
        instance = null;
    }

    public NodeImpl createNode(Path path) throws ItemExistsException, PathNotFoundException,
            RepositoryException {
        // check if node already exists
        if (nodes.get(path.toString()) != null) {
            throw new ItemExistsException("Node already exists");
        }

        NodeImpl node;
        // root node
        if (path.isRoot()) {
            node = new NodeImpl(path);
        }
        // node that have parents
        else {
            // get parent
            Path parentPath = path.getParent();
            if (parentPath == null) {
                throw new PathNotFoundException();
            }
            NodeImpl parent = nodes.get(parentPath.toString());
            if (parent == null) {
                throw new PathNotFoundException();
            }
            node = new NodeImpl(path);
            // add as child to parent
            parent.addChild(node);
        }
        nodes.put(path.toString(), node);
        return node;
    }
}