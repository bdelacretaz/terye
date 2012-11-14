package ch.x42.terye.persistence;

import java.util.LinkedList;
import java.util.List;

import ch.x42.terye.persistence.id.NodeId;
import ch.x42.terye.persistence.id.PropertyId;

public class NodeState extends ItemState {

    private String nodeTypeName;
    private List<NodeId> childNodes;
    private List<PropertyId> properties;

    public NodeState(NodeId id, String nodeTypeName, List<NodeId> childNodes,
            List<PropertyId> properties) {
        super(id);
        this.nodeTypeName = nodeTypeName;
        this.childNodes = childNodes;
        this.properties = properties;
    }

    public NodeState(NodeId id, String nodeTypeName) {
        this(id, nodeTypeName, new LinkedList<NodeId>(),
                new LinkedList<PropertyId>());
    }

    @Override
    public NodeId getId() {
        return (NodeId) super.getId();
    }

    public String getNodeTypeName() {
        return nodeTypeName;
    }

    public List<NodeId> getChildNodes() {
        return childNodes;
    }

    public List<PropertyId> getProperties() {
        return properties;
    }

    @Override
    public boolean isNode() {
        return true;
    }

}
