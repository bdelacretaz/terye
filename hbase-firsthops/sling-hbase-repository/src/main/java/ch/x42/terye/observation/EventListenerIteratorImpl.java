package ch.x42.terye.observation;

import java.util.Set;

import javax.jcr.observation.EventListener;
import javax.jcr.observation.EventListenerIterator;

import ch.x42.terye.iterator.RangeIteratorImpl;

public class EventListenerIteratorImpl extends RangeIteratorImpl<EventConsumer>
        implements EventListenerIterator {

    public EventListenerIteratorImpl(Set<EventConsumer> consumers) {
        super(consumers);
        this.size = consumers.size();
    }

    @Override
    public Object next() {
        return ((EventConsumer) super.next()).getEventListener();
    }

    @Override
    public EventListener nextEventListener() {
        return (EventListener) next();
    }

}
