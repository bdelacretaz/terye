package ch.x42.terye;

import java.net.UnknownHostException;

import javax.jcr.Credentials;
import javax.jcr.LoginException;
import javax.jcr.NoSuchWorkspaceException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;

import com.mongodb.MongoException;

public class RepositoryImpl implements Repository {

    private final String name;

    private RepositoryImpl(final String name) throws RepositoryException {
        this.name = name;
    }

    public RepositoryImpl() throws RepositoryException {
        this("repo");
    }

    protected String getName() {
        return name;
    }

    @Override
    public String getDescriptor(String key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String[] getDescriptorKeys() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Value getDescriptorValue(String key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Value[] getDescriptorValues(String key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isSingleValueDescriptor(String key) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isStandardDescriptor(String key) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Session login() throws LoginException, RepositoryException {
        try {
            return new SessionImpl(this, "default");
        } catch (UnknownHostException e) {
            throw new RepositoryException(e.getClass().getSimpleName()
                    + " in login()", e);
        } catch (MongoException e) {
            throw new RepositoryException(e.getClass().getSimpleName()
                    + " in login()", e);
        }
    }

    @Override
    public Session login(Credentials credentials, String workspaceName)
            throws LoginException, NoSuchWorkspaceException,
            RepositoryException {
        return login();
    }

    @Override
    public Session login(Credentials credentials) throws LoginException,
            RepositoryException {
        return login();
    }

    @Override
    public Session login(String workspaceName) throws LoginException,
            NoSuchWorkspaceException, RepositoryException {
        return login();
    }

}
