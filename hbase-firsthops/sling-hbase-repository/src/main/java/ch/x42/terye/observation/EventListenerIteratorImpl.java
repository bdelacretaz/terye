package ch.x42.terye.observation;

import java.util.Iterator;
import java.util.Set;

import javax.jcr.observation.EventListener;
import javax.jcr.observation.EventListenerIterator;

public class EventListenerIteratorImpl implements EventListenerIterator {

    private Iterator<EventConsumer> iterator;
    private long position;

    public EventListenerIteratorImpl(Set<EventConsumer> consumers) {
        iterator = consumers.iterator();
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
        return ((EventConsumer) iterator.next()).getEventListener();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public EventListener nextEventListener() {
        return (EventListener) next();
    }

}
