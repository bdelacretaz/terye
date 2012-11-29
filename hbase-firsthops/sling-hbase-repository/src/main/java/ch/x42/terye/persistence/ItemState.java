package ch.x42.terye.persistence;

import ch.x42.terye.persistence.id.ItemId;
import ch.x42.terye.persistence.id.NodeId;

public abstract class ItemState {

    private final ItemId id;
    private final NodeId parentId;

    public ItemState(ItemId id, NodeId parentId) {
        this.id = id;
        this.parentId = parentId;
    }

    public ItemId getId() {
        return id;
    }

    public NodeId getParentId() {
        return parentId;
    }

    public String getPath() {
        return id.toString();
    }

    public abstract boolean isNode();

}
