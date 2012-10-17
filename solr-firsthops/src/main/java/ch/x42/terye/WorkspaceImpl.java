package ch.x42.terye;

import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;

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

import ch.x42.terye.query.QueryManagerImpl;

import com.mongodb.MongoException;

public class WorkspaceImpl implements Workspace {

    private final String name;
    private final SessionImpl session;
    private final ItemManager itemManager;
    private final QueryManagerImpl queryManager;

    public WorkspaceImpl(String name, SessionImpl session)
            throws UnknownHostException, MongoException, RepositoryException {
        this.name = name;
        this.session = session;
        itemManager = new ItemManager(session);
        queryManager = new QueryManagerImpl(itemManager);
    }

    protected ItemManager getItemManager() {
        return itemManager;
    }

    @Override
    public Session getSession() {
        return session;
    }

    @Override
    public String getName() {
        return name;
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
        // TODO Auto-generated method stub

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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ObservationManager getObservationManager()
            throws UnsupportedRepositoryOperationException, RepositoryException {
        // TODO Auto-generated method stub
        return null;
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
