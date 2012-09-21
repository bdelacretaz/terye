package ch.x42.terye;

import javax.jcr.AccessDeniedException;
import javax.jcr.InvalidItemStateException;
import javax.jcr.Item;
import javax.jcr.ItemExistsException;
import javax.jcr.ItemNotFoundException;
import javax.jcr.ItemVisitor;
import javax.jcr.Node;
import javax.jcr.ReferentialIntegrityException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.nodetype.NoSuchNodeTypeException;
import javax.jcr.version.VersionException;

public class ItemImpl implements Item {

    protected SessionImpl session;
    protected Path path;
    private String name;
    private NodeImpl parent;

    public ItemImpl(SessionImpl session, Path path, NodeImpl parent) {
        this.session = session;
        this.path = path;
        this.name = path.getLastSegment();
        this.parent = parent;
    }

    @Override
    public void accept(ItemVisitor visitor) throws RepositoryException {
        // TODO Auto-generated method stub

    }

    @Override
    public Item getAncestor(int depth) throws ItemNotFoundException,
            AccessDeniedException, RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getDepth() throws RepositoryException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String getName() throws RepositoryException {
        return name;
    }

    @Override
    public Node getParent() throws ItemNotFoundException,
            AccessDeniedException, RepositoryException {
        return parent;
    }

    @Override
    public String getPath() throws RepositoryException {
        return path.toString();
    }

    @Override
    public Session getSession() throws RepositoryException {
        return session;
    }

    @Override
    public boolean isModified() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isNew() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isNode() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isSame(Item arg0) throws RepositoryException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void refresh(boolean arg0) throws InvalidItemStateException,
            RepositoryException {
        // TODO Auto-generated method stub

    }

    @Override
    public void remove() throws VersionException, LockException,
            ConstraintViolationException, AccessDeniedException,
            RepositoryException {
        session.getItemManager().removeItem(path);
    }

    @Override
    public void save() throws AccessDeniedException, ItemExistsException,
            ConstraintViolationException, InvalidItemStateException,
            ReferentialIntegrityException, VersionException, LockException,
            NoSuchNodeTypeException, RepositoryException {
        // TODO Auto-generated method stub

    }
}