package ch.x42.terye.query;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.query.InvalidQueryException;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.qom.QueryObjectModelFactory;

public class QueryManagerImpl implements QueryManager {

    @Override
    public Query createQuery(String statement, String language)
            throws InvalidQueryException, RepositoryException {
        return new QueryImpl();
    }

    @Override
    public QueryObjectModelFactory getQOMFactory() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Query getQuery(Node node) throws InvalidQueryException,
            RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String[] getSupportedQueryLanguages() throws RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

}
