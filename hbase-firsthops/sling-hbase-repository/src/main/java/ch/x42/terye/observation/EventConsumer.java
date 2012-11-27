package ch.x42.terye.observation;

import javax.jcr.observation.EventListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.x42.terye.SessionImpl;

public class EventConsumer {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private SessionImpl session;
    private EventListener listener;
    private EventFilter filter;

    protected EventConsumer(SessionImpl session, EventListener listener,
            EventFilter filter) {
        this.session = session;
        this.listener = listener;
        this.filter = filter;
    }

    protected void consume(EventCollection events) {
        logger.debug("Calling listener {}", listener.getClass());
        listener.onEvent(new FilteredEventIterator(events, filter));
    }

    protected EventListener getEventListener() {
        return listener;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((listener == null) ? 0 : listener.hashCode());
        result = prime * result + ((session == null) ? 0 : session.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        EventConsumer other = (EventConsumer) obj;
        if (listener == null) {
            if (other.listener != null) {
                return false;
            }
        } else if (!listener.equals(other.listener)) {
            return false;
        }
        if (session == null) {
            if (other.session != null) {
                return false;
            }
        } else if (!session.equals(other.session)) {
            return false;
        }
        return true;
    }

}
