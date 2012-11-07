package ch.x42.terye.query;

import java.util.LinkedList;

import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.query.QueryResult;
import javax.jcr.query.RowIterator;

import ch.x42.terye.iterator.NodeIteratorImpl;

public class QueryResultImpl implements QueryResult {

    @Override
    public String[] getColumnNames() throws RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RowIterator getRows() throws RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public NodeIterator getNodes() throws RepositoryException {
        return new NodeIteratorImpl(null, new LinkedList<String>());
    }

    @Override
    public String[] getSelectorNames() throws RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

}
