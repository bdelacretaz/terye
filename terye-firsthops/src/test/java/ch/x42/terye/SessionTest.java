package ch.x42.terye;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.RepositoryException;

import org.junit.Test;

public class SessionTest extends BaseTest {

    @Test
    public void testSessionLiveness() throws RepositoryException {
        assertTrue("Expecting session to be initially live", session.isLive());
        session.logout();
        assertFalse("Session should not be live after logout", session.isLive());
        session = null;
    }

    @Test
    public void testRootNode() throws RepositoryException {
        assertFalse("Root node should not be null", root == null);
        assertEquals("The name of the root node should be the empty string", "", root.getName());
    }
    
    @Test
    public void testGetItem() throws RepositoryException {
        Node a = root.addNode("a");
        Property p = a.setProperty("p", "string");
        assertEquals(a, session.getItem("/a"));
        assertEquals(p, session.getItem("/a/p"));
    }

    @Test(expected = RepositoryException.class)
    public void testGetItemIllegalArgument() throws RepositoryException {
        session.getItem("not/allowed");
    }
    
    @Test(expected = PathNotFoundException.class)
    public void testGetItemPathNotFound() throws RepositoryException {
        session.getItem("leads/to/nowhere");
    }
    
    @Test
    public void testGetNode() throws RepositoryException {
        Node a = root.addNode("a");
        assertEquals(a, session.getNode("/a"));
    }

    @Test(expected = RepositoryException.class)
    public void testGetNodeIllegalArgument() throws RepositoryException {
        session.getNode("not/allowed");
    }
    
    @Test(expected = PathNotFoundException.class)
    public void testGetNodePathNotFound() throws RepositoryException {
        session.getNode("leads/to/nowhere");
    }
    
    @Test
    public void testGetProperty() throws RepositoryException {
        Property p = root.setProperty("p", "string");
        assertEquals(p, session.getProperty("/p"));
    }

    @Test(expected = RepositoryException.class)
    public void testGetPropertyIllegalArgument() throws RepositoryException {
        session.getProperty("not/allowed");
    }
    
    @Test(expected = PathNotFoundException.class)
    public void testGetPropertyPathNotFound() throws RepositoryException {
        session.getNode("leads/to/nowhere");
    }
    
    @Test
    public void testGetRepository() {
        assertEquals(repository, session.getRepository());
    }
    
    @Test
    public void testItemExists() throws RepositoryException {
        root.addNode("a");
        root.setProperty("p", "string");
        assertTrue(session.itemExists("/a"));
        assertTrue(session.itemExists("/p"));
        assertFalse(session.itemExists("/x"));
    }
    
    @Test(expected = RepositoryException.class)
    public void testItemExistsIllegalArgument() throws RepositoryException {
        session.itemExists("not/allowed");
    }
    
    @Test
    public void testNodeExists() throws RepositoryException {
        root.addNode("a");
        root.addNode("a/b");
        assertTrue(session.nodeExists("/a"));
        assertTrue(session.nodeExists("/a/b"));
        assertFalse(session.nodeExists("c"));
    }
    
    @Test(expected = RepositoryException.class)
    public void testNodeExistsIllegalArgument() throws RepositoryException {
        session.nodeExists("not/allowed");
    }
    
    @Test
    public void testPropertyExists() throws RepositoryException {
        root.addNode("a");
        root.setProperty("p", "string");
        root.setProperty("a/q", "string");
        assertTrue(session.propertyExists("/p"));
        assertTrue(session.propertyExists("/a/q"));
        assertFalse(session.propertyExists("/r"));
    }
    
    @Test(expected = RepositoryException.class)
    public void testPropertyExistsIllegalArgument() throws RepositoryException {
        session.propertyExists("not/allowed");
    }
    
}