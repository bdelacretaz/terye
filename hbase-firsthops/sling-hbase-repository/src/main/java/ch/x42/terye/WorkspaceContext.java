package ch.x42.terye;

import javax.jcr.RepositoryException;

import ch.x42.terye.observation.ObservationDispatcher;
import ch.x42.terye.persistence.PersistenceManager;
import ch.x42.terye.persistence.hbase.HBasePersistenceManager;

/**
 * This class contains components that are unique for each workspace but shared
 * between different sessions.
 */
public class WorkspaceContext {

    private final String name;
    private final PersistenceManager persistenceManager;
    private final ObservationDispatcher dispatcher;

    public WorkspaceContext(String name) throws RepositoryException {
        this.name = name;
        this.persistenceManager = new HBasePersistenceManager();
        this.dispatcher = new ObservationDispatcher();
    }

    public String getName() {
        return name;
    }

    public PersistenceManager getPersistenceManager() {
        return persistenceManager;
    }

    public ObservationDispatcher getObservationDispatcher() {
        return dispatcher;
    }

}
