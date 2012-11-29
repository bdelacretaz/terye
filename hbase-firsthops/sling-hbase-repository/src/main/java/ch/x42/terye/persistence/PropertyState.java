package ch.x42.terye.persistence;

import ch.x42.terye.persistence.id.ItemId;
import ch.x42.terye.persistence.id.NodeId;
import ch.x42.terye.persistence.id.PropertyId;
import ch.x42.terye.value.ValueImpl;

public class PropertyState extends ItemState {

    private int type;
    private byte[] bytes;

    public PropertyState(ItemId id, NodeId parentId, ValueImpl value) {
        super(id, parentId);
        this.type = value.getType();
        this.bytes = value.getBytes();
    }

    public PropertyState(ItemId id, NodeId parentId, int type, byte[] bytes) {
        super(id, parentId);
        this.type = type;
        this.bytes = bytes;
    }

    public byte[] getBytes() {
        return bytes;
    }

    @Override
    public PropertyId getId() {
        return (PropertyId) super.getId();
    }

    public int getType() {
        return type;
    }

    @Override
    public boolean isNode() {
        return false;
    }

}
