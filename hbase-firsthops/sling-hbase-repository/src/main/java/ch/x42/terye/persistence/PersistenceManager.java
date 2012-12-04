package ch.x42.terye.persistence;

import javax.jcr.RepositoryException;

import ch.x42.terye.persistence.id.ItemId;
import ch.x42.terye.persistence.id.NodeId;
import ch.x42.terye.persistence.id.PropertyId;

public interface PersistenceManager {

    public ItemState loadItem(ItemId id) throws RepositoryException;

    public NodeState loadNode(NodeId id) throws RepositoryException;

    public PropertyState loadProperty(PropertyId id) throws RepositoryException;

    public void store(ItemState state) throws RepositoryException;

    public void store(NodeState state) throws RepositoryException;

    public void store(PropertyState state) throws RepositoryException;

    public void delete(ItemId id) throws RepositoryException;

    public void delete(NodeId id) throws RepositoryException;

    public void delete(PropertyId id) throws RepositoryException;

    public void persist(ChangeLog log) throws RepositoryException;

    public void dispose() throws Exception;

}
