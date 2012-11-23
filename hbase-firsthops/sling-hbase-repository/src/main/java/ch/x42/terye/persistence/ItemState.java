package ch.x42.terye.persistence;

import ch.x42.terye.persistence.id.ItemId;

public abstract class ItemState {

    private ItemId id;

    public ItemState(ItemId id) {
        this.id = id;
    }

    public ItemId getId() {
        return id;
    }

    public String getPath() {
        return id.toString();
    }

    public abstract boolean isNode();

}
