package ch.x42.terye.persistence;

public class NodeState extends ItemState {

    public NodeState() {
        super(ItemType.NODE);
    }
    
    public NodeState(String path, String parent) {
        this();
        put("path", path);
        put("parent", parent);
    }

}
