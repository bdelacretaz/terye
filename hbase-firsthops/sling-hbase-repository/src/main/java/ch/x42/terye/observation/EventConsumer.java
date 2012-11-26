package ch.x42.terye.observation;

import javax.jcr.observation.EventListener;

import ch.x42.terye.SessionImpl;

public class EventConsumer {

    private SessionImpl session;
    private EventListener listener;

    protected EventConsumer(SessionImpl session, EventListener listener) {
        this.session = session;
        this.listener = listener;
    }

    protected void consume(EventCollection events) {

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
