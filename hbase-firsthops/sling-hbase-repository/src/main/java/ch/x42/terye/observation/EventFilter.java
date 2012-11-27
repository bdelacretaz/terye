package ch.x42.terye.observation;

import javax.jcr.RepositoryException;

import ch.x42.terye.Path;
import ch.x42.terye.SessionImpl;

public class EventFilter {

    private int eventTypes;
    private Path absPath;
    private boolean isDeep;
    private String[] ids;
    private String[] nodeTypeNames;
    private boolean noLocal;
    private SessionImpl session;

    public EventFilter(int eventTypes, String absPath, boolean isDeep,
            String[] ids, String[] nodeTypeNames, boolean noLocal,
            SessionImpl session) throws RepositoryException {
        this.eventTypes = eventTypes;
        this.absPath = new Path(absPath);
        if (this.absPath.isRelative()) {
            throw new RepositoryException("Path must be absolute");
        }
        this.isDeep = isDeep;
        this.ids = ids;
        this.nodeTypeNames = nodeTypeNames;
        this.noLocal = noLocal;
        this.session = session;
    }

    public boolean filters(EventImpl event) {
        // event type
        if ((eventTypes & event.getType()) == 0) {
            return true;
        }

        // locality
        if (noLocal && !session.equals(event.getSession())) {
            return true;
        }

        // XXX: check path

        // XXX: check ids

        // XXX: check node types

        return false;
    }

}
