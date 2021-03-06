package ch.x42.terye;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.UnknownHostException;
import java.security.AccessControlException;

import javax.jcr.AccessDeniedException;
import javax.jcr.Credentials;
import javax.jcr.InvalidItemStateException;
import javax.jcr.InvalidSerializedDataException;
import javax.jcr.Item;
import javax.jcr.ItemExistsException;
import javax.jcr.ItemNotFoundException;
import javax.jcr.LoginException;
import javax.jcr.NamespaceException;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.ReferentialIntegrityException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.ValueFactory;
import javax.jcr.Workspace;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.nodetype.NoSuchNodeTypeException;
import javax.jcr.retention.RetentionManager;
import javax.jcr.security.AccessControlManager;
import javax.jcr.version.VersionException;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import com.mongodb.MongoException;

import ch.x42.terye.value.ValueFactoryImpl;

public class SessionImpl implements Session {

    private RepositoryImpl repository;
    private ItemManager itemManager;
    private ValueFactoryImpl valueFactory;
    private boolean live = false;
    private NodeImpl rootNode;

    protected SessionImpl(RepositoryImpl repository) throws RepositoryException, UnknownHostException, MongoException {
        this.repository = repository;
        this.itemManager = new ItemManager(this);
        this.valueFactory = new ValueFactoryImpl();
        live = true;

        try {
            rootNode = getItemManager().getNode(new Path("/"));
        } catch (PathNotFoundException e) {
            rootNode = getItemManager().createNode(new Path("/"));
        }
    }

    protected ItemManager getItemManager() {
        return itemManager;
    }

    private Path makeAbsolutePath(String absPath) throws RepositoryException {
        Path path = new Path(absPath);
        if (!path.isAbsolute()) {
            throw new RepositoryException("Not an absolute path: " + absPath);
        }
        return path;
    }

    @Override
    public void addLockToken(String lt) {
        // TODO Auto-generated method stub

    }

    @Override
    public void checkPermission(String absPath, String actions)
            throws AccessControlException, RepositoryException {
        // TODO Auto-generated method stub

    }

    @Override
    public void exportDocumentView(String absPath,
            ContentHandler contentHandler, boolean skipBinary, boolean noRecurse)
            throws PathNotFoundException, SAXException, RepositoryException {
        // TODO Auto-generated method stub

    }

    @Override
    public void exportDocumentView(String absPath, OutputStream out,
            boolean skipBinary, boolean noRecurse) throws IOException,
            PathNotFoundException, RepositoryException {
        // TODO Auto-generated method stub

    }

    @Override
    public void exportSystemView(String absPath, ContentHandler contentHandler,
            boolean skipBinary, boolean noRecurse)
            throws PathNotFoundException, SAXException, RepositoryException {
        // TODO Auto-generated method stub

    }

    @Override
    public void exportSystemView(String absPath, OutputStream out,
            boolean skipBinary, boolean noRecurse) throws IOException,
            PathNotFoundException, RepositoryException {
        // TODO Auto-generated method stub

    }

    @Override
    public AccessControlManager getAccessControlManager()
            throws UnsupportedRepositoryOperationException, RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object getAttribute(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String[] getAttributeNames() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ContentHandler getImportContentHandler(String parentAbsPath,
            int uuidBehavior) throws PathNotFoundException,
            ConstraintViolationException, VersionException, LockException,
            RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Item getItem(String absPath) throws PathNotFoundException,
            RepositoryException {
        return getItemManager().getItem(makeAbsolutePath(absPath));
    }

    @Override
    public String[] getLockTokens() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getNamespacePrefix(String uri) throws NamespaceException,
            RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String[] getNamespacePrefixes() throws RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getNamespaceURI(String prefix) throws NamespaceException,
            RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Node getNode(String absPath) throws PathNotFoundException,
            RepositoryException {
        return getItemManager().getNode(makeAbsolutePath(absPath));
    }

    @Override
    public Node getNodeByIdentifier(String id) throws ItemNotFoundException,
            RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Node getNodeByUUID(String uuid) throws ItemNotFoundException,
            RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Property getProperty(String absPath) throws PathNotFoundException,
            RepositoryException {
        return getItemManager().getProperty(makeAbsolutePath(absPath));
    }

    @Override
    public Repository getRepository() {
        return repository;
    }

    @Override
    public RetentionManager getRetentionManager()
            throws UnsupportedRepositoryOperationException, RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Node getRootNode() throws RepositoryException {
        return rootNode;
    }

    @Override
    public String getUserID() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ValueFactory getValueFactory()
            throws UnsupportedRepositoryOperationException, RepositoryException {
        return valueFactory;
    }

    @Override
    public Workspace getWorkspace() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean hasCapability(String methodName, Object target,
            Object[] arguments) throws RepositoryException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean hasPendingChanges() throws RepositoryException {
        return getItemManager().hasPendingChanges();
    }

    @Override
    public boolean hasPermission(String absPath, String actions)
            throws RepositoryException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Session impersonate(Credentials credentials) throws LoginException,
            RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void importXML(String parentAbsPath, InputStream in, int uuidBehavior)
            throws IOException, PathNotFoundException, ItemExistsException,
            ConstraintViolationException, VersionException,
            InvalidSerializedDataException, LockException, RepositoryException {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isLive() {
        return live;
    }

    @Override
    public boolean itemExists(String absPath) throws RepositoryException {
        return getItemManager().itemExists(makeAbsolutePath(absPath));
    }

    @Override
    public void logout() {
        live = false;
    }

    @Override
    public void move(String srcAbsPath, String destAbsPath)
            throws ItemExistsException, PathNotFoundException,
            VersionException, ConstraintViolationException, LockException,
            RepositoryException {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean nodeExists(String absPath) throws RepositoryException {
        return getItemManager().nodeExists(makeAbsolutePath(absPath));
    }

    @Override
    public boolean propertyExists(String absPath) throws RepositoryException {
        return getItemManager().propertyExists(makeAbsolutePath(absPath));
    }

    @Override
    public void refresh(boolean keepChanges) throws RepositoryException {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeItem(String absPath) throws VersionException,
            LockException, ConstraintViolationException, AccessDeniedException,
            RepositoryException {
        getItemManager().removeItem(makeAbsolutePath(absPath));
    }

    @Override
    public void removeLockToken(String lt) {
        // TODO Auto-generated method stub

    }

    @Override
    public void save() throws AccessDeniedException, ItemExistsException,
            ReferentialIntegrityException, ConstraintViolationException,
            InvalidItemStateException, VersionException, LockException,
            NoSuchNodeTypeException, RepositoryException {
        getItemManager().save();
    }

    @Override
    public void setNamespacePrefix(String prefix, String uri)
            throws NamespaceException, RepositoryException {
        // TODO Auto-generated method stub

    }

}
