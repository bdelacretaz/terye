package ch.x42.terye;

import javax.jcr.Credentials;
import javax.jcr.LoginException;
import javax.jcr.NoSuchWorkspaceException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RepositoryImpl implements Repository {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public String[] getDescriptorKeys() {
        return null;
    }

    @Override
    public boolean isStandardDescriptor(String key) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isSingleValueDescriptor(String key) {
        // TODO Auto-generated method stub
        return false;
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
    public String getDescriptor(String key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Session login(Credentials credentials, String workspaceName)
            throws LoginException, NoSuchWorkspaceException,
            RepositoryException {
        logger.debug("login()");
        return new SessionImpl(this, "default");
    }

    @Override
    public Session login(Credentials credentials) throws LoginException,
            RepositoryException {
        return login(null, null);
    }

    @Override
    public Session login(String workspaceName) throws LoginException,
            NoSuchWorkspaceException, RepositoryException {
        return login(null, null);
    }

    @Override
    public Session login() throws LoginException, RepositoryException {
        return login(null, null);
    }
}
