package ch.x42.terye.persistence;

import java.util.List;

import javax.jcr.RepositoryException;

import ch.x42.terye.persistence.id.ItemId;
import ch.x42.terye.persistence.id.NodeId;
import ch.x42.terye.persistence.id.PropertyId;

public interface PersistenceManager {

    public ItemState loadItem(ItemId id) throws RepositoryException;

    public NodeState loadNode(NodeId id) throws RepositoryException;

    public List<NodeState> loadNodes(NodeId parentId)
            throws RepositoryException;

    public PropertyState loadProperty(PropertyId id) throws RepositoryException;

    public List<PropertyState> loadProperties(NodeId parentId)
            throws RepositoryException;

    public void persist(ChangeLog log) throws RepositoryException;

}
