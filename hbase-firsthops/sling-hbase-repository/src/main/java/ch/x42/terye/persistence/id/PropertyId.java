package ch.x42.terye.persistence.id;

public class PropertyId extends ItemId {

    private static final long serialVersionUID = 1L;

    public PropertyId(String uuid) {
        super(uuid);
    }

    @Override
    public boolean denotesNode() {
        return false;
    }

}
