package ch.x42.terye.persistence.id;

public abstract class ItemId {

    private final String uuid;

    public ItemId(String uuid) {
        this.uuid = uuid;
    }

    public abstract boolean denotesNode();

    @Override
    public String toString() {
        return uuid;
    }

}
