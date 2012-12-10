package ch.x42.terye.persistence;

import ch.x42.terye.persistence.id.ItemId;
import ch.x42.terye.persistence.id.PropertyId;
import ch.x42.terye.value.ValueImpl;

public class PropertyState extends ItemState {

    private int type;
    private byte[] bytes;

    public PropertyState(PropertyId id, ValueImpl value) {
        super(id);
        this.type = value.getType();
        this.bytes = value.getBytes();
    }

    public PropertyState(PropertyId id, int type, byte[] bytes) {
        super(id);
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

    @Override
    public ItemState clone(ItemId newId) {
        return new PropertyState((PropertyId) newId, getType(), getBytes());
    }

}
