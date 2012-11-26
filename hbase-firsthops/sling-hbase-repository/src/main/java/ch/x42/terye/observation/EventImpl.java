package ch.x42.terye.observation;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.jcr.observation.Event;

public class EventImpl implements Event {

    private int type;
    private long date;
    private String path;
    private Map<String, String> info;

    protected EventImpl(int type, long date, String path) {
        this.type = type;
        this.date = date;
        this.path = path;
        this.info = new HashMap<String, String>();
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public String getPath() throws RepositoryException {
        return path;
    }

    @Override
    public String getUserID() {
        // TODO Auto-generated method stub
        return null;
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
