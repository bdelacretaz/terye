package ch.x42.terye.observation;

import java.util.Iterator;

import javax.jcr.observation.Event;
import javax.jcr.observation.EventIterator;

public class EventIteratorImpl implements EventIterator {

    private Iterator<EventImpl> iterator;
    private long position;

    public EventIteratorImpl(EventCollection events) {
        iterator = events.iterator();
        position = 0;
    }

    @Override
    public void skip(long skipNum) {
        if (skipNum < 0) {
            throw new IllegalArgumentException("Parameter must be non-negative");
        }
        while (skipNum > 0 && iterator.hasNext()) {
            iterator.next();
            position++;
            skipNum--;
        }
    }

    @Override
    public long getSize() {
        return -1;
    }

    @Override
    public long getPosition() {
        return position;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public Object next() {
        position++;
        return iterator.next();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Event nextEvent() {
        return (Event) next();
    }

}
