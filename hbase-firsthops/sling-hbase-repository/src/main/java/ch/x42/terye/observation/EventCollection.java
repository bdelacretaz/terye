package ch.x42.terye.observation;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.jcr.observation.Event;

import ch.x42.terye.persistence.ChangeLog;
import ch.x42.terye.persistence.ItemState;

public class EventCollection {

    private List<EventImpl> events;

    public EventCollection(ChangeLog log) {
        createEvents(log);
    }

    private void createEvents(ChangeLog log) {
        events = new LinkedList<EventImpl>();
        for (ItemState state : log.getAddedStates()) {
            int type;
            if (state.isNode()) {
                type = Event.NODE_ADDED;
            } else {
                type = Event.PROPERTY_ADDED;
            }
            events.add(new EventImpl(type, System.currentTimeMillis(), state
                    .getPath()));
        }
        for (ItemState state : log.getModifiedStates()) {
            if (!state.isNode()) {
                events.add(new EventImpl(Event.PROPERTY_CHANGED, System
                        .currentTimeMillis(), state.getPath()));
            }
        }
        for (ItemState state : log.getRemovedStates()) {
            int type;
            if (state.isNode()) {
                type = Event.NODE_REMOVED;
            } else {
                type = Event.PROPERTY_REMOVED;
            }
            events.add(new EventImpl(type, System.currentTimeMillis(), state
                    .getPath()));
        }
    }

    public Iterator<EventImpl> iterator() {
        return events.iterator();
    }

}
