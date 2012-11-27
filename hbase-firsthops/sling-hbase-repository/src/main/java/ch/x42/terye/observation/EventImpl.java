package ch.x42.terye.observation;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.jcr.observation.Event;

import ch.x42.terye.SessionImpl;

public class EventImpl implements Event {

    private int type;
    private long date;
    private String path;
    private Map<String, String> info;
    private SessionImpl session;

    protected EventImpl(int type, long date, String path, SessionImpl session) {
        this.type = type;
        this.date = date;
        this.path = path;
        this.info = new HashMap<String, String>();
        this.session = session;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public String getPath() throws RepositoryException {
        return path;
    }
    
    protected SessionImpl getSession() {
        return session;
    }

    @Override
    public String getUserID() {
        return session.getUserID();
    }

    @Override
    public String getIdentifier() throws RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Map getInfo() throws RepositoryException {
        return info;
    }

    @Override
    public String getUserData() throws RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getDate() throws RepositoryException {
        return date;
    }

    @Override
    public String toString() {
        String str = "[";
        switch (getType()) {
            case Event.NODE_ADDED:
                str += "NODE_ADDED";
                break;
            case Event.NODE_MOVED:
                str += "NODE_MOVED";
                break;
            case Event.NODE_REMOVED:
                str += "NODE_REMOVED";
                break;
            case Event.PERSIST:
                str += "PERSIST";
                break;
            case Event.PROPERTY_ADDED:
                str += "PROPERTY_ADDED";
                break;
            case Event.PROPERTY_CHANGED:
                str += "PROPERTY_ADDED";
                break;
            case Event.PROPERTY_REMOVED:
                str += "PROPERTY_REMOVED";
                break;
        }
        try {
            str += ", " + getPath();
        } catch (RepositoryException e) {
            // ignore
        }
        str += "]";

        return str;
    }

}
