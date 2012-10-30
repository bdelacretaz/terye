package ch.x42.terye.iterator;

import javax.jcr.Property;
import javax.jcr.PropertyIterator;

import ch.x42.terye.ItemManager;
import ch.x42.terye.store.ItemType;

public class PropertyIteratorImpl extends RangeIteratorImpl implements
        PropertyIterator {

    public PropertyIteratorImpl(ItemManager itemManager,
            Iterable<String> properties) {
        super(itemManager, properties, ItemType.PROPERTY);
    }

    @Override
    public Property nextProperty() {
        return (Property) next();
    }

}
