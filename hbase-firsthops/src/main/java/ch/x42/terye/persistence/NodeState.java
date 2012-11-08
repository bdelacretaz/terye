package ch.x42.terye.persistence;

public class NodeState extends ItemState {

    private String nodeTypeName;

    public NodeState(String path, String nodeTypeName) {
        super(path);
        this.nodeTypeName = nodeTypeName;
    }

    public String getNodeTypeName() {
        return nodeTypeName;
    }

    public void setNodeTypeName(String nodeTypeName) {
        this.nodeTypeName = nodeTypeName;
    }

}
