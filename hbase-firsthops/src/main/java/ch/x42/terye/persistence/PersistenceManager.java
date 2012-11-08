package ch.x42.terye.persistence;

import javax.jcr.RepositoryException;

public interface PersistenceManager {

    public NodeState loadNode(String path) throws RepositoryException;

}
