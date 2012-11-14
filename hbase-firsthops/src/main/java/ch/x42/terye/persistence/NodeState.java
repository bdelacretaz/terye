package ch.x42.terye.persistence;

import java.util.LinkedList;
import java.util.List;

import ch.x42.terye.persistence.id.NodeId;

public class NodeState extends ItemState {

    private String nodeTypeName;
    private List<NodeId> childNodes;

    public NodeState(NodeId id, String nodeTypeName, List<NodeId> childNodes) {
        super(id);
        this.nodeTypeName = nodeTypeName;
        this.childNodes = childNodes;
    }

    public NodeState(NodeId id, String nodeTypeName) {
        this(id, nodeTypeName, new LinkedList<NodeId>());
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

    @Override
    public boolean isNode() {
        return true;
    }

    public void setNodeTypeName(String nodeTypeName) {
        this.nodeTypeName = nodeTypeName;
    }

}
