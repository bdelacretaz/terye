package ch.x42.terye.persistence;

import ch.x42.terye.persistence.id.ItemId;
import ch.x42.terye.value.ValueImpl;

public class PropertyState extends ItemState {

    private int type;
    private byte[] bytes;

    public PropertyState(ItemId id, ValueImpl value) {
        super(id);
        this.type = value.getType();
        this.bytes = value.getBytes();
    }

    public PropertyState(ItemId id, int type, byte[] bytes) {
        super(id);
        this.type = type;
        this.bytes = bytes;
    }

    @Override
    public boolean isNode() {
        return false;
    }

    public int getType() {
        return type;
    }

    public byte[] getBytes() {
        return bytes;
    }

}
