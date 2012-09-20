package ch.x42.terye;

import static org.junit.Assert.assertEquals;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class NodeTest {

    private Session session;

    @Before
    public void setup() throws RepositoryException {
        session = new RepositoryImpl().login();
    }

    @After
    public void cleanup() throws RepositoryException {
        if (session != null) {
            session.logout();
        }
    }

    @Test
    public void testAdd() throws RepositoryException {
        Node rootNode = session.getRootNode();
        Node a1 = rootNode.addNode("a");
        assertEquals("a", a1.getName());
        assertEquals("/a", a1.getPath());
    }

}
