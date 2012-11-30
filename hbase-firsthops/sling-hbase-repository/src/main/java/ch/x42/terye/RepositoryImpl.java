package ch.x42.terye;

import java.util.HashMap;
import java.util.Map;

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

    private static final String DEFAULT_WORKSPACE = "default";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Map<String, WorkspaceContext> wsContexts = new HashMap<String, WorkspaceContext>();

    private void createWorkspaceContext(String name) throws RepositoryException {
        WorkspaceContext wsContext = new WorkspaceContext(name);
        wsContexts.put(name, wsContext);
    }

    private WorkspaceContext getWorkspaceContext(String name) {
        return wsContexts.get(name);
    }

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
        if (key.equals(Repository.OPTION_OBSERVATION_SUPPORTED)) {
            return "true";
        }
        return "false";
    }

    @Override
    public Session login(Credentials credentials, String workspaceName)
            throws LoginException, NoSuchWorkspaceException,
            RepositoryException {
        if (workspaceName == null) {
            workspaceName = RepositoryImpl.DEFAULT_WORKSPACE;
        }
        WorkspaceContext wsContext;
        synchronized (wsContexts) {
            if (!wsContexts.containsKey(workspaceName)) {
                createWorkspaceContext(workspaceName);
            }
            wsContext = getWorkspaceContext(workspaceName);
        }
        logger.debug("login(" + workspaceName + ")");
        return new SessionImpl(this, wsContext);
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

    public void dispose() throws Exception {
        synchronized (wsContexts) {
            for (WorkspaceContext wsContext : wsContexts.values()) {
                wsContext.getPersistenceManager().dispose();
            }
        }

    }
}
