package ch.x42.terye.mk.hbase;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.jackrabbit.mk.json.JsopBuilder;
import org.apache.jackrabbit.oak.commons.PathUtils;

public class Node {

    private String path;
    private long lastRevision;
    private long childCount;
    private List<Node> children;
    private Map<String, Object> properties;

    public Node(String path) {
        this.path = path;
        this.children = new LinkedList<Node>();
        this.properties = new LinkedHashMap<String, Object>();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getLastRevision() {
        return lastRevision;
    }

    public void setLastRevision(long lastRevision) {
        this.lastRevision = lastRevision;
    }

    public long getChildCount() {
        return childCount;
    }

    public void setChildCount(long childCount) {
        this.childCount = childCount;
    }

    public List<Node> getChildren() {
        return children;
    }

    public void addChild(Node node) {
        children.add(node);
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperty(String name, Object value) {
        properties.put(name, value);
    }

    /**
     * Builds the hierarchy tree from a flat map of nodes. The map entries must
     * be sorted lexicographically by path. This implies the root of the tree
     * represented by the map to be the first map entry.
     * 
     * @param nodes flat map representing a connected (sub)tree
     * @return the root node of the tree
     */
    public static Node toTree(Map<String, Node> nodes) {
        if (nodes.isEmpty()) {
            return null;
        }
        for (Entry<String, Node> entry : nodes.entrySet()) {
            if (!PathUtils.denotesRoot(entry.getKey())) {
                String parentPath = PathUtils.getParentPath(entry.getKey());
                if (nodes.containsKey(parentPath)) {
                    nodes.get(parentPath).addChild(entry.getValue());
                }
            }
        }
        // return first element of the map
        return nodes.entrySet().iterator().next().getValue();
    }

    /**
     * Generates the JSON representation of a tree of nodes.
     * 
     * @param root the root node of the tree
     * @param depth the desired depth or null for infinite depth
     * @return the generated JSON string
     */
    public static String toJson(Node root, Integer depth) {
        JsopBuilder builder = new JsopBuilder();
        toJson(builder, root, depth, true);
        return builder.toString();
    }

    private static void toJson(JsopBuilder builder, Node root, Integer depth,
            boolean excludeRoot) {
        if (!excludeRoot) {
            builder.key(PathUtils.getName(root.getPath()));
        }
        builder.object();
        if (depth != null && depth < 0) {
            builder.endObject();
            return;
        }
        // virtual properties
        builder.key(":childNodeCount").value(root.getChildCount());
        // properties
        for (Entry<String, Object> entry : root.getProperties().entrySet()) {
            builder.key(entry.getKey());
            Object value = entry.getValue();
            String encodedValue = value.toString();
            if (value instanceof String) {
                encodedValue = JsopBuilder.encode(value.toString());
            }
            builder.encodedValue(encodedValue);
        }
        // child nodes
        for (Node node : root.getChildren()) {
            toJson(builder, node, depth == null ? null : depth - 1, false);
        }
        builder.endObject();
    }

}
