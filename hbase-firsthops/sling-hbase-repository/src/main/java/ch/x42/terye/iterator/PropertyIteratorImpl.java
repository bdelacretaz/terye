package ch.x42.terye.iterator;

import javax.jcr.Property;
import javax.jcr.PropertyIterator;

import ch.x42.terye.ItemManager;
import ch.x42.terye.persistence.id.PropertyId;

public class PropertyIteratorImpl extends ItemIteratorImpl implements
        PropertyIterator {

    public PropertyIteratorImpl(ItemManager itemManager,
            Iterable<PropertyId> propertyIds) {
        super(itemManager, propertyIds);
    }

    @Override
    public Property nextProperty() {
        return (Property) next();
    }

}
