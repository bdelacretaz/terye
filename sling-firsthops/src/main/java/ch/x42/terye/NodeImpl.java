package ch.x42.terye;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.jcr.AccessDeniedException;
import javax.jcr.Binary;
import javax.jcr.InvalidItemStateException;
import javax.jcr.InvalidLifecycleTransitionException;
import javax.jcr.Item;
import javax.jcr.ItemExistsException;
import javax.jcr.ItemNotFoundException;
import javax.jcr.MergeException;
import javax.jcr.NoSuchWorkspaceException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.RepositoryException;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.Value;
import javax.jcr.ValueFormatException;
import javax.jcr.lock.Lock;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.nodetype.NoSuchNodeTypeException;
import javax.jcr.nodetype.NodeDefinition;
import javax.jcr.nodetype.NodeType;
import javax.jcr.version.ActivityViolationException;
import javax.jcr.version.Version;
import javax.jcr.version.VersionException;
import javax.jcr.version.VersionHistory;

import ch.x42.terye.iterator.NodeIteratorImpl;
import ch.x42.terye.iterator.PropertyIteratorImpl;

public class NodeImpl extends ItemImpl implements Node {

    // store children and properties as lists of path strings
    private List<String> children = new LinkedList<String>();
    private Set<String> properties = new LinkedHashSet<String>();
    private NodeTypeImpl primaryType;

    public NodeImpl(SessionImpl session, Path path, String primaryTypeName) {
        super(session, path);
        children = new LinkedList<String>();
        properties = new LinkedHashSet<String>();
        primaryType = new NodeTypeImpl(primaryTypeName);
    }

    public NodeImpl(SessionImpl session, NodeImpl node) {
        super(session, node.path);
        // copy state
        children = new LinkedList<String>(node.children);
        properties = new LinkedHashSet<String>(node.properties);
        primaryType = node.primaryType;
    }

    protected void addChild(ItemImpl child) {
        if (child.isNode()) {
            children.add(child.path.toString());
        } else {
            properties.add(child.path.toString());
        }
    }

    protected void removeChild(ItemImpl child) {
        if (child.isNode()) {
            children.remove(child.path.toString());
        } else {
            properties.remove(child.path.toString());
        }
    }

    @Override
    public void addMixin(String mixinName) throws NoSuchNodeTypeException,
            VersionException, ConstraintViolationException, LockException,
            RepositoryException {
        // TODO Auto-generated method stub

    }

    @Override
    public Node addNode(String relPath) throws ItemExistsException,
            PathNotFoundException, VersionException,
            ConstraintViolationException, LockException, RepositoryException {
        return addNode(relPath, "nt:unstructured");
    }

    @Override
    public Node addNode(String relPath, String primaryNodeTypeName)
            throws ItemExistsException, PathNotFoundException,
            NoSuchNodeTypeException, LockException, VersionException,
            ConstraintViolationException, RepositoryException {
        Path absPath = path.concat(relPath).getCanonical();
        NodeImpl node = session.getItemManager().createNode(absPath,
                primaryNodeTypeName);
        // XXX: temporarily add jcr:created to all nodes in order to prevent
        // Sling from throwing an exception
        node.setProperty("jcr:created", Calendar.getInstance());
        return node;
    }

    @Override
    public boolean canAddMixin(String mixinName)
            throws NoSuchNodeTypeException, RepositoryException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void cancelMerge(Version version) throws VersionException,
            InvalidItemStateException, UnsupportedRepositoryOperationException,
            RepositoryException {
        // TODO Auto-generated method stub

    }

    @Override
    public Version checkin() throws VersionException,
            UnsupportedRepositoryOperationException, InvalidItemStateException,
            LockException, RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void checkout() throws UnsupportedRepositoryOperationException,
            LockException, ActivityViolationException, RepositoryException {
        // TODO Auto-generated method stub

    }

    @Override
    public void doneMerge(Version version) throws VersionException,
            InvalidItemStateException, UnsupportedRepositoryOperationException,
            RepositoryException {
        // TODO Auto-generated method stub

    }

    @Override
    public void followLifecycleTransition(String transition)
            throws UnsupportedRepositoryOperationException,
            InvalidLifecycleTransitionException, RepositoryException {
        // TODO Auto-generated method stub

    }

    @Override
    public String[] getAllowedLifecycleTransistions()
            throws UnsupportedRepositoryOperationException, RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Version getBaseVersion()
            throws UnsupportedRepositoryOperationException, RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getCorrespondingNodePath(String workspaceName)
            throws ItemNotFoundException, NoSuchWorkspaceException,
            AccessDeniedException, RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public NodeDefinition getDefinition() throws RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getIdentifier() throws RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getIndex() throws RepositoryException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Lock getLock() throws UnsupportedRepositoryOperationException,
            LockException, AccessDeniedException, RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public NodeType[] getMixinNodeTypes() throws RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Node getNode(String relPath) throws PathNotFoundException,
            RepositoryException {
        Path absPath = path.concat(relPath).getCanonical();
        return session.getItemManager().getNode(absPath);
    }

    @Override
    public NodeIterator getNodes() throws RepositoryException {
        return new NodeIteratorImpl(session.getItemManager(), children);
    }

    @Override
    public NodeIterator getNodes(String namePattern) throws RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public NodeIterator getNodes(String[] nameGlobs) throws RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Item getPrimaryItem() throws ItemNotFoundException,
            RepositoryException {
        throw new ItemNotFoundException();
    }

    @Override
    public NodeType getPrimaryNodeType() throws RepositoryException {
        return primaryType;
    }

    @Override
    public PropertyIterator getProperties() throws RepositoryException {
        return new PropertyIteratorImpl(session.getItemManager(), properties);
    }

    @Override
    public PropertyIterator getProperties(String namePattern)
            throws RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PropertyIterator getProperties(String[] nameGlobs)
            throws RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Property getProperty(String relPath) throws PathNotFoundException,
            RepositoryException {
        Path absPath = path.concat(relPath).getCanonical();
        return session.getItemManager().getProperty(absPath);
    }

    @Override
    public PropertyIterator getReferences() throws RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PropertyIterator getReferences(String name)
            throws RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public NodeIterator getSharedSet() throws RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getUUID() throws UnsupportedRepositoryOperationException,
            RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public VersionHistory getVersionHistory()
            throws UnsupportedRepositoryOperationException, RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PropertyIterator getWeakReferences() throws RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PropertyIterator getWeakReferences(String name)
            throws RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean hasNode(String relPath) throws RepositoryException {
        Path absPath = path.concat(relPath).getCanonical();
        return session.getItemManager().nodeExists(absPath);
    }

    @Override
    public boolean hasNodes() throws RepositoryException {
        return !children.isEmpty();
    }

    @Override
    public boolean hasProperties() throws RepositoryException {
        return !properties.isEmpty();
    }

    @Override
    public boolean hasProperty(String relPath) throws RepositoryException {
        Path absPath = path.concat(relPath).getCanonical();
        return session.getItemManager().propertyExists(absPath);
    }

    @Override
    public boolean holdsLock() throws RepositoryException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isCheckedOut() throws RepositoryException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isLocked() throws RepositoryException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isNodeType(String nodeTypeName) throws RepositoryException {
        return primaryType.isNodeType(nodeTypeName);
    }

    @Override
    public Lock lock(boolean isDeep, boolean isSessionScoped)
            throws UnsupportedRepositoryOperationException, LockException,
            AccessDeniedException, InvalidItemStateException,
            RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public NodeIterator merge(String srcWorkspace, boolean bestEffort)
            throws NoSuchWorkspaceException, AccessDeniedException,
            MergeException, LockException, InvalidItemStateException,
            RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void orderBefore(String srcChildRelPath, String destChildRelPath)
            throws UnsupportedRepositoryOperationException, VersionException,
            ConstraintViolationException, ItemNotFoundException, LockException,
            RepositoryException {
        // TODO Auto-generated method stub

    }

    @Override
    public void remove() throws VersionException, LockException,
            ConstraintViolationException, AccessDeniedException,
            RepositoryException {
        if (getPath().equals("/")) {
            throw new RepositoryException("Cannot remove the root node");
        }
        super.remove();
    }

    @Override
    public void removeMixin(String mixinName) throws NoSuchNodeTypeException,
            VersionException, ConstraintViolationException, LockException,
            RepositoryException {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeShare() throws VersionException, LockException,
            ConstraintViolationException, RepositoryException {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeSharedSet() throws VersionException, LockException,
            ConstraintViolationException, RepositoryException {
        // TODO Auto-generated method stub

    }

    @Override
    public void restore(String versionName, boolean removeExisting)
            throws VersionException, ItemExistsException,
            UnsupportedRepositoryOperationException, LockException,
            InvalidItemStateException, RepositoryException {
        // TODO Auto-generated method stub

    }

    @Override
    public void restore(Version version, boolean removeExisting)
            throws VersionException, ItemExistsException,
            InvalidItemStateException, UnsupportedRepositoryOperationException,
            LockException, RepositoryException {
        // TODO Auto-generated method stub

    }

    @Override
    public void restore(Version version, String relPath, boolean removeExisting)
            throws PathNotFoundException, ItemExistsException,
            VersionException, ConstraintViolationException,
            UnsupportedRepositoryOperationException, LockException,
            InvalidItemStateException, RepositoryException {
        // TODO Auto-generated method stub

    }

    @Override
    public void restoreByLabel(String versionLabel, boolean removeExisting)
            throws VersionException, ItemExistsException,
            UnsupportedRepositoryOperationException, LockException,
            InvalidItemStateException, RepositoryException {
        // TODO Auto-generated method stub

    }

    @Override
    public void setPrimaryType(String nodeTypeName)
            throws NoSuchNodeTypeException, VersionException,
            ConstraintViolationException, LockException, RepositoryException {
        // TODO Auto-generated method stub

    }

    @Override
    public Property setProperty(String name, String[] values)
            throws ValueFormatException, VersionException, LockException,
            ConstraintViolationException, RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Property setProperty(String name, Value value)
            throws ValueFormatException, VersionException, LockException,
            ConstraintViolationException, RepositoryException {
        PropertyImpl property = session.getItemManager().createProperty(
                path.concat(name), value);
        addChild(property);
        return property;
    }

    @Override
    public Property setProperty(String name, Value[] values)
            throws ValueFormatException, VersionException, LockException,
            ConstraintViolationException, RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Property setProperty(String name, String value)
            throws ValueFormatException, VersionException, LockException,
            ConstraintViolationException, RepositoryException {
        return setProperty(name,
                getSession().getValueFactory().createValue(value));
    }

    @Override
    public Property setProperty(String name, InputStream value)
            throws ValueFormatException, VersionException, LockException,
            ConstraintViolationException, RepositoryException {
        return setProperty(name,
                getSession().getValueFactory().createBinary(value));
    }

    @Override
    public Property setProperty(String name, Binary value)
            throws ValueFormatException, VersionException, LockException,
            ConstraintViolationException, RepositoryException {
        return setProperty(name,
                getSession().getValueFactory().createValue(value));
    }

    @Override
    public Property setProperty(String name, boolean value)
            throws ValueFormatException, VersionException, LockException,
            ConstraintViolationException, RepositoryException {
        return setProperty(name,
                getSession().getValueFactory().createValue(value));
    }

    @Override
    public Property setProperty(String name, double value)
            throws ValueFormatException, VersionException, LockException,
            ConstraintViolationException, RepositoryException {
        return setProperty(name,
                getSession().getValueFactory().createValue(value));
    }

    @Override
    public Property setProperty(String name, BigDecimal value)
            throws ValueFormatException, VersionException, LockException,
            ConstraintViolationException, RepositoryException {
        return setProperty(name,
                getSession().getValueFactory().createValue(value));
    }

    @Override
    public Property setProperty(String name, long value)
            throws ValueFormatException, VersionException, LockException,
            ConstraintViolationException, RepositoryException {
        return setProperty(name,
                getSession().getValueFactory().createValue(value));
    }

    @Override
    public Property setProperty(String name, Calendar value)
            throws ValueFormatException, VersionException, LockException,
            ConstraintViolationException, RepositoryException {
        return setProperty(name,
                getSession().getValueFactory().createValue(value));
    }

    @Override
    public Property setProperty(String name, Node value)
            throws ValueFormatException, VersionException, LockException,
            ConstraintViolationException, RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Property setProperty(String name, Value value, int type)
            throws ValueFormatException, VersionException, LockException,
            ConstraintViolationException, RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Property setProperty(String name, Value[] values, int type)
            throws ValueFormatException, VersionException, LockException,
            ConstraintViolationException, RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Property setProperty(String name, String[] values, int type)
            throws ValueFormatException, VersionException, LockException,
            ConstraintViolationException, RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Property setProperty(String name, String value, int type)
            throws ValueFormatException, VersionException, LockException,
            ConstraintViolationException, RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void unlock() throws UnsupportedRepositoryOperationException,
            LockException, AccessDeniedException, InvalidItemStateException,
            RepositoryException {
        // TODO Auto-generated method stub

    }

    @Override
    public void update(String arg0) throws NoSuchWorkspaceException,
            AccessDeniedException, LockException, InvalidItemStateException,
            RepositoryException {
        // TODO Auto-generated method stub

    }

}
