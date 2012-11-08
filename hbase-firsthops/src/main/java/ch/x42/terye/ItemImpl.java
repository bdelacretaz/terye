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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.x42.terye.persistence.ItemState;
import ch.x42.terye.store.ItemType;

public class ItemImpl implements Item {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    protected SessionImpl session;
    protected ItemState state;
    protected Path path;

    public ItemImpl(SessionImpl session, ItemState state) {
        this.session = session;
        this.state = state;
        this.path = path;
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
        return path.getLastSegment();
    }

    @Override
    public Node getParent() throws ItemNotFoundException,
            AccessDeniedException, RepositoryException {
        if (path.getParent() == null) {
            throw new ItemNotFoundException("The root node has no parent.");
        }
        return session.getItemManager().getNode(path.getParent());
    }

    @Override
    public String getPath() throws RepositoryException {
        return state.getPath();
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
        return this instanceof NodeImpl;
    }

    @Override
    public boolean isSame(Item arg0) throws RepositoryException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void refresh(boolean arg0) throws InvalidItemStateException,
            RepositoryException {
        logger.debug("refresh({})", arg0);
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
        logger.debug("save()");
        // XXX: temporarily save the whole session
        getSession().save();
    }

    public ItemType getItemType() {
        return isNode() ? ItemType.NODE : ItemType.PROPERTY;
    }
}
