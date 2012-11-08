package ch.x42.terye.persistence.id;

public class NodeId extends ItemId {

    public NodeId(String uuid) {
        super(uuid);
    }

    @Override
    public boolean denotesNode() {
        return true;
    }

}
