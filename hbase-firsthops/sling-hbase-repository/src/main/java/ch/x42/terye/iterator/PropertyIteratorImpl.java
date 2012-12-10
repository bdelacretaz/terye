package ch.x42.terye.iterator;

import javax.jcr.Property;
import javax.jcr.PropertyIterator;

import ch.x42.terye.ItemManager;
import ch.x42.terye.path.Path;

public class PropertyIteratorImpl extends ItemIterator implements
        PropertyIterator {

    public PropertyIteratorImpl(ItemManager itemManager, Path basePath,
            Iterable<String> propertyIds) {
        super(itemManager, basePath, propertyIds);
    }

    @Override
    public Property nextProperty() {
        return (Property) next();
    }

}
