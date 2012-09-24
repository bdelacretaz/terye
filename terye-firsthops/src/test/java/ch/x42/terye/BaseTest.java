package ch.x42.terye;

import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.junit.After;
import org.junit.Before;

public class BaseTest {

    protected Repository repository;
    protected Session session;
    protected Node root;

    @Before
    public void setup() throws RepositoryException {
        repository = new RepositoryImpl();
        session = repository.login();
        root = session.getRootNode();
    }

    @After
    public void cleanup() throws RepositoryException {
        if (session != null) {
            session.logout();
        }
    }

}
