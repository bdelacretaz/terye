package ch.x42.terye.iterator;

import javax.jcr.Property;
import javax.jcr.PropertyIterator;

import ch.x42.terye.PropertyImpl;

public class PropertyIteratorImpl extends RangeIteratorImpl<PropertyImpl>
        implements PropertyIterator {

    public PropertyIteratorImpl(Iterable<PropertyImpl> properties) {
        super(properties);
    }

    @Override
    public Property nextProperty() {
        return (Property) next();
    }
}
