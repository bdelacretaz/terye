package ch.x42.terye.observation;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.jcr.observation.Event;

import ch.x42.terye.SessionImpl;
import ch.x42.terye.persistence.ChangeLog;
import ch.x42.terye.persistence.ItemState;

public class EventCollection {

    private SessionImpl session;
    private List<EventImpl> events;

    public EventCollection(ChangeLog log, SessionImpl session) {
        this.session = session;
        createEvents(log);
    }

    private void createEvents(ChangeLog log) {
        events = new LinkedList<EventImpl>();
        // NODE_ADDED, PROPERTY_ADDED
        for (ItemState state : log.getAddedStates()) {
            // if the state is the destination of a move, don't generate ADDED
            // event for it
            if (log.getMovedDestStates().contains(state)) {
                continue;
            }

            int type;
            if (state.isNode()) {
                type = Event.NODE_ADDED;
            } else {
                type = Event.PROPERTY_ADDED;
            }
            events.add(new EventImpl(type, System.currentTimeMillis(), state
                    .getPath(), session));
        }
        // PROPERTY_CHANGED
        for (ItemState state : log.getModifiedStates()) {
            if (!state.isNode()) {
                events.add(new EventImpl(Event.PROPERTY_CHANGED, System
                        .currentTimeMillis(), state.getPath(), session));
            }
        }
        // NODE_REMOVED, PROPERTY_REMOVED
        for (ItemState state : log.getRemovedStates()) {
            // if the state is the source of a move, don't generate REMOVED
            // event for it
            if (log.getMovedSrcStates().contains(state)) {
                continue;
            }

            int type;
            if (state.isNode()) {
                type = Event.NODE_REMOVED;
            } else {
                type = Event.PROPERTY_REMOVED;
            }
            events.add(new EventImpl(type, System.currentTimeMillis(), state
                    .getPath(), session));
        }
        // NODE_MOVED
        for (ItemState state : log.getMovedDestStates()) {
            if (state.isNode()) {
                events.add(new EventImpl(Event.NODE_MOVED, System
                        .currentTimeMillis(), state.getPath(), session));
            }
        }
    }

    public Iterator<EventImpl> iterator() {
        return events.iterator();
    }

}
