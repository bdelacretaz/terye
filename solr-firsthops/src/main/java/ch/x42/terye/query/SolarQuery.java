package ch.x42.terye.query;

import javax.jcr.ItemExistsException;
import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.Value;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.query.InvalidQueryException;
import javax.jcr.query.Query;
import javax.jcr.query.QueryResult;
import javax.jcr.version.VersionException;

public class SolarQuery implements Query {

    @Override
    public QueryResult execute() throws InvalidQueryException,
            RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setLimit(long limit) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setOffset(long offset) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getStatement() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getLanguage() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getStoredQueryPath() throws ItemNotFoundException,
            RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Node storeAsNode(String absPath) throws ItemExistsException,
            PathNotFoundException, VersionException,
            ConstraintViolationException, LockException,
            UnsupportedRepositoryOperationException, RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void bindValue(String varName, Value value)
            throws IllegalArgumentException, RepositoryException {
        // TODO Auto-generated method stub

    }

    @Override
    public String[] getBindVariableNames() throws RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

}
