package ch.x42.terye;

import java.io.IOException;
import java.io.InputStream;

import javax.jcr.AccessDeniedException;
import javax.jcr.InvalidItemStateException;
import javax.jcr.InvalidSerializedDataException;
import javax.jcr.ItemExistsException;
import javax.jcr.NamespaceRegistry;
import javax.jcr.NoSuchWorkspaceException;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.Workspace;
import javax.jcr.lock.LockException;
import javax.jcr.lock.LockManager;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.nodetype.NodeTypeManager;
import javax.jcr.observation.ObservationManager;
import javax.jcr.query.QueryManager;
import javax.jcr.version.Version;
import javax.jcr.version.VersionException;
import javax.jcr.version.VersionManager;

import org.xml.sax.ContentHandler;

import ch.x42.terye.nodetype.NodeTypeManagerImpl;
import ch.x42.terye.observation.ObservationManagerImpl;
import ch.x42.terye.persistence.PersistenceManager;
import ch.x42.terye.query.QueryManagerImpl;

public class WorkspaceImpl implements Workspace {

    private final WorkspaceContext context;
    private final SessionImpl session;
    private final QueryManager queryManager;
    private final ObservationManagerImpl observationManager;

    public WorkspaceImpl(WorkspaceContext context, SessionImpl session)
            throws RepositoryException {
        this.context = context;
        this.session = session;
        this.queryManager = new QueryManagerImpl();
        this.observationManager = new ObservationManagerImpl(session,
                context.getObservationDispatcher());
    }

    protected PersistenceManager getPersistenceManager() {
        return context.getPersistenceManager();
    }

    @Override
    public Session getSession() {
        return session;
    }

    @Override
    public String getName() {
        return context.getName();
    }

    @Override
    public void copy(String srcAbsPath, String destAbsPath)
            throws ConstraintViolationException, VersionException,
            AccessDeniedException, PathNotFoundException, ItemExistsException,
            LockException, RepositoryException {
        // TODO Auto-generated method stub

    }

    @Override
    public void copy(String srcWorkspace, String srcAbsPath, String destAbsPath)
            throws NoSuchWorkspaceException, ConstraintViolationException,
            VersionException, AccessDeniedException, PathNotFoundException,
            ItemExistsException, LockException, RepositoryException {
        // TODO Auto-generated method stub

    }

    @Override
    public void clone(String srcWorkspace, String srcAbsPath,
            String destAbsPath, boolean removeExisting)
            throws NoSuchWorkspaceException, ConstraintViolationException,
            VersionException, AccessDeniedException, PathNotFoundException,
            ItemExistsException, LockException, RepositoryException {
        // TODO Auto-generated method stub

    }

    @Override
    public void move(String srcAbsPath, String destAbsPath)
            throws ConstraintViolationException, VersionException,
            AccessDeniedException, PathNotFoundException, ItemExistsException,
            LockException, RepositoryException {
        SessionImpl tmpSession = (SessionImpl) session.getRepository().login(
                getName());
        tmpSession.move(srcAbsPath, destAbsPath);
        tmpSession.save();
        tmpSession.logout();
    }

    @Override
    public void restore(Version[] versions, boolean removeExisting)
            throws ItemExistsException,
            UnsupportedRepositoryOperationException, VersionException,
            LockException, InvalidItemStateException, RepositoryException {
        // TODO Auto-generated method stub

    }

    @Override
    public LockManager getLockManager()
            throws UnsupportedRepositoryOperationException, RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public QueryManager getQueryManager() throws RepositoryException {
        return queryManager;
    }

    @Override
    public NamespaceRegistry getNamespaceRegistry() throws RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public NodeTypeManager getNodeTypeManager() throws RepositoryException {
        return new NodeTypeManagerImpl();
    }

    @Override
    public ObservationManager getObservationManager()
            throws UnsupportedRepositoryOperationException, RepositoryException {
        return observationManager;
    }

    @Override
    public VersionManager getVersionManager()
            throws UnsupportedRepositoryOperationException, RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String[] getAccessibleWorkspaceNames() throws RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ContentHandler getImportContentHandler(String parentAbsPath,
            int uuidBehavior) throws PathNotFoundException,
            ConstraintViolationException, VersionException, LockException,
            AccessDeniedException, RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void importXML(String parentAbsPath, InputStream in, int uuidBehavior)
            throws IOException, VersionException, PathNotFoundException,
            ItemExistsException, ConstraintViolationException,
            InvalidSerializedDataException, LockException,
            AccessDeniedException, RepositoryException {
        // TODO Auto-generated method stub

    }

    @Override
    public void createWorkspace(String name) throws AccessDeniedException,
            UnsupportedRepositoryOperationException, RepositoryException {
        // TODO Auto-generated method stub

    }

    @Override
    public void createWorkspace(String name, String srcWorkspace)
            throws AccessDeniedException,
            UnsupportedRepositoryOperationException, NoSuchWorkspaceException,
            RepositoryException {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteWorkspace(String name) throws AccessDeniedException,
            UnsupportedRepositoryOperationException, NoSuchWorkspaceException,
            RepositoryException {
        // TODO Auto-generated method stub

    }

}
