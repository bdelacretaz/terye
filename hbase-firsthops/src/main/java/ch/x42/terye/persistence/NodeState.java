package ch.x42.terye.persistence;

import ch.x42.terye.persistence.id.NodeId;

public class NodeState extends ItemState {

    private String nodeTypeName;

    public NodeState(NodeId id, String nodeTypeName) {
        super(id);
        this.nodeTypeName = nodeTypeName;
    }

    public String getNodeTypeName() {
        return nodeTypeName;
    }

    public void setNodeTypeName(String nodeTypeName) {
        this.nodeTypeName = nodeTypeName;
    }

    @Override
    public boolean isNode() {
        return true;
    }

}
