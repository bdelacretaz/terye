package ch.x42.terye.observation;

import javax.jcr.RepositoryException;
import javax.jcr.observation.EventJournal;
import javax.jcr.observation.EventListener;
import javax.jcr.observation.EventListenerIterator;
import javax.jcr.observation.ObservationManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.x42.terye.SessionImpl;

public class ObservationManagerImpl implements ObservationManager {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private SessionImpl session;
    private ObservationDispatcher dispatcher;

    public ObservationManagerImpl(SessionImpl session,
            ObservationDispatcher dispatcher) {
        this.session = session;
        this.dispatcher = dispatcher;
    }

    @Override
    public void addEventListener(EventListener listener, int eventTypes,
            String absPath, boolean isDeep, String[] uuid,
            String[] nodeTypeName, boolean noLocal) throws RepositoryException {
        logger.debug("addEventListener(" + listener.getClass() + ", "
                + Integer.toBinaryString(eventTypes) + ", " + absPath + ")");
        EventFilter filter = new EventFilter(eventTypes, absPath, isDeep, uuid,
                nodeTypeName, noLocal, session);
        EventConsumer consumer = new EventConsumer(session, listener, filter);
        dispatcher.addConsumer(consumer);
    }

    public void dispatchEvents(EventCollection events) {
        dispatcher.dispatchEvents(events);
    }

    @Override
    public void removeEventListener(EventListener listener)
            throws RepositoryException {
        EventConsumer consumer = new EventConsumer(session, listener, null);
        dispatcher.removeConsumer(consumer);
    }

    @Override
    public EventListenerIterator getRegisteredEventListeners()
            throws RepositoryException {
        return new EventListenerIteratorImpl(dispatcher.getConsumers());
    }

    @Override
    public void setUserData(String userData) throws RepositoryException {
        // TODO Auto-generated method stub

    }

    @Override
    public EventJournal getEventJournal() throws RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public EventJournal getEventJournal(int eventTypes, String absPath,
            boolean isDeep, String[] uuid, String[] nodeTypeName)
            throws RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

}
