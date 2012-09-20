package ch.x42.terye;

import static org.junit.Assert.assertEquals;

import javax.jcr.ItemExistsException;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class NodeTest {

    private Session session;
    private Node root;

    @Before
    public void setup() throws RepositoryException {
        session = new RepositoryImpl().login();
        root = session.getRootNode();
    }

    @After
    public void cleanup() throws RepositoryException {
        if (session != null) {
            session.logout();
        }
    }

    @Test
    public void testAdd() throws RepositoryException {
        Node a = root.addNode("a");
        assertEquals("a", a.getName());
        assertEquals("/a", a.getPath());
        
        Node b = root.addNode("a/b");
        assertEquals("b", b.getName());
        assertEquals("/a/b", b.getPath());
        
        Node c = b.addNode("c");
        assertEquals("c", c.getName());
        assertEquals("/a/b/c", c.getPath());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testAddIllegalArgument() throws RepositoryException {
        root.addNode("/not/allowed");
    }
    
    @Test(expected=PathNotFoundException.class)
    public void testAddPathNotFound() throws RepositoryException {
        root.addNode("leads/to/nowhere");
    }

    @Test(expected=ItemExistsException.class)
    public void testAddItemExists() throws RepositoryException {
        root.addNode("a");
        root.addNode("a");
    }
    
    @Test
    public void testGet() throws RepositoryException {
        // add some nodes
        Node a1 = root.addNode("a");
        Node b1 = root.addNode("a/b");
        Node c1 = b1.addNode("c");
        
        // get them back
        Node a2 = root.getNode("a");
        assertEquals("Added and queried nodes are not equal", a1, a2);
        Node b2 = root.getNode("a/b");
        assertEquals("Added and queried nodes are not equal", b1, b2);
        Node c2 = b2.getNode("c");
        assertEquals("Added and queried nodes are not equal", c1, c2);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testGetIllegalArgument() throws RepositoryException {
        root.getNode("/not/allowed");
    }
    
    @Test(expected=PathNotFoundException.class)
    public void testGetPathNotFound() throws RepositoryException {
        root.getNode("leads/to/nowhere");
    }
}