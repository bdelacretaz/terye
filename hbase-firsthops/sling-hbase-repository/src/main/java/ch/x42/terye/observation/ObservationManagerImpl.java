package ch.x42.terye.observation;

import javax.jcr.RepositoryException;
import javax.jcr.observation.EventJournal;
import javax.jcr.observation.EventListener;
import javax.jcr.observation.EventListenerIterator;
import javax.jcr.observation.ObservationManager;

import ch.x42.terye.SessionImpl;

public class ObservationManagerImpl implements ObservationManager {

    private SessionImpl session;
    private ObservationDispatcher dispatcher;

    public ObservationManagerImpl(SessionImpl session) {
        this.session = session;
        this.dispatcher = new ObservationDispatcher();
    }

    @Override
    public void addEventListener(EventListener listener, int eventTypes,
            String absPath, boolean isDeep, String[] uuid,
            String[] nodeTypeName, boolean noLocal) throws RepositoryException {
        EventConsumer consumer = new EventConsumer(session, listener);
        dispatcher.addConsumer(consumer);
    }

    public void dispatchEvents(EventCollection events) {
        dispatcher.dispatchEvents(events);
    }

    @Override
    public void removeEventListener(EventListener listener)
            throws RepositoryException {
        EventConsumer consumer = new EventConsumer(session, listener);
        dispatcher.removeConsumer(consumer);
    }

    @Override
    public EventListenerIterator getRegisteredEventListeners()
            throws RepositoryException {
        // TODO Auto-generated method stub
        return null;
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
