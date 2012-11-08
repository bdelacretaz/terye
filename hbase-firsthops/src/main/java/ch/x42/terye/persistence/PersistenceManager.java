package ch.x42.terye.persistence;

import javax.jcr.RepositoryException;

import ch.x42.terye.persistence.id.NodeId;

public interface PersistenceManager {

    public NodeState loadNode(NodeId id) throws RepositoryException;

}
