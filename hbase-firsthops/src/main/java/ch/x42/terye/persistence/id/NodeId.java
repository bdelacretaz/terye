package ch.x42.terye.persistence.id;

public class NodeId extends ItemId {

    private static final long serialVersionUID = 1L;

    public NodeId(String uuid) {
        super(uuid);
    }

    @Override
    public boolean denotesNode() {
        return true;
    }

}
