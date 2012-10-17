package ch.x42.terye.query;

import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.query.QueryResult;
import javax.jcr.query.RowIterator;

import ch.x42.terye.ItemManager;
import ch.x42.terye.iterator.NodeIteratorImpl;

public class QueryResultImpl implements QueryResult {

    private final ItemManager itemManager;
    private final Iterable<String> nodes;

    public QueryResultImpl(ItemManager itemManager, Iterable<String> nodes) {
        this.itemManager = itemManager;
        this.nodes = nodes;
    }

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
        new NodeIteratorImpl(itemManager, nodes);
        return null;
    }

    @Override
    public String[] getSelectorNames() throws RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

}
