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

import ch.x42.terye.path.Path;
import ch.x42.terye.path.PathFactory;
import ch.x42.terye.persistence.ItemState;
import ch.x42.terye.persistence.id.ItemId;
import ch.x42.terye.persistence.id.NodeId;

public class ItemImpl implements Item {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private SessionImpl session;
    protected ItemState state;
    private boolean removed;

    public ItemImpl(SessionImpl session, ItemState state) {
        this.session = session;
        this.state = state;
        this.removed = false;
    }

    protected void sanityCheck() throws RepositoryException {
        session.check();
        if (removed) {
            throw new InvalidItemStateException("Item does not exist anymore: "
                    + getId());
        }
    }

    protected void setState(ItemState state) throws RepositoryException {
        this.state = state;
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

    public ItemId getId() {
        return getState().getId();
    }

    protected ItemManager getItemManager() {
        return session.getItemManager();
    }

    @Override
    public String getName() throws RepositoryException {
        sanityCheck();
        return PathFactory.create(getPath()).getLastElement();
    }

    @Override
    public Node getParent() throws ItemNotFoundException,
            AccessDeniedException, RepositoryException {
        sanityCheck();
        Path path = PathFactory.create(getPath());
        if (path.getParent() == null) {
            throw new ItemNotFoundException("The root node has no parent");
        }
        return getItemManager().getNode(path.getParent());
    }

    protected NodeId getParentId() {
        return state.getParentId();
    }

    @Override
    public String getPath() throws RepositoryException {
        sanityCheck();
        return state.getPath();
    }

    @Override
    public Session getSession() throws RepositoryException {
        sanityCheck();
        return session;
    }

    public ItemState getState() {
        return state;
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
        logger.debug("[{}].refresh({})", getPath(), arg0);
        Path path = PathFactory.create(getPath());
        getItemManager().refresh(path);
    }

    protected void markRemoved() {
        removed = true;
    }

    @Override
    public void remove() throws VersionException, LockException,
            ConstraintViolationException, AccessDeniedException,
            RepositoryException {
        getItemManager().removeItem(this);
    }

    @Override
    public void save() throws AccessDeniedException, ItemExistsException,
            ConstraintViolationException, InvalidItemStateException,
            ReferentialIntegrityException, VersionException, LockException,
            NoSuchNodeTypeException, RepositoryException {
        logger.debug("[{}].save()", getPath());
        getItemManager().persistChanges(PathFactory.create(getPath()));
    }

}
