package ch.x42.terye.persistence;

import ch.x42.terye.persistence.id.ItemId;

public class PropertyState extends ItemState {

    public PropertyState(ItemId id) {
        super(id);
    }

    @Override
    public boolean isNode() {
        return false;
    }

}
