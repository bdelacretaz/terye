package ch.x42.terye.observation;

import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.jcr.observation.Event;
import javax.jcr.observation.EventIterator;

public class FilteredEventIterator implements EventIterator {

    private Iterator<EventImpl> iterator;
    private EventFilter filter;
    private long position;
    private EventImpl next;

    public FilteredEventIterator(EventCollection events, EventFilter filter) {
        this.iterator = events.iterator();
        this.filter = filter;
        this.position = 0;
        this.next = null;
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

    private void fetchNext() {
        // next element is already fetched
        if (next != null) {
            return;
        }
        // fetch next
        while (iterator.hasNext()) {
            EventImpl event = iterator.next();
            if (!filter.filters(event)) {
                next = event;
                return;
            }
        }
    }

    @Override
    public boolean hasNext() {
        fetchNext();
        return next != null;
    }

    @Override
    public Object next() {
        fetchNext();
        if (next == null) {
            throw new NoSuchElementException();
        }
        position++;
        Event event = next;
        next = null;
        return event;
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
