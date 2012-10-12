package ch.x42.terye;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.jcr.Item;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.junit.Test;

public class SessionTest extends BaseTest {

    @Test
    public void testLiveness() throws RepositoryException {
        assertTrue("Expecting session to be initially live", session.isLive());
        session.logout();
        assertFalse("Session should not be live after logout", session.isLive());
    }

    @Test
    public void testGetItemNode() throws RepositoryException {
        Item item = session.getItem("/node1");
        assertEquals("node1", item.getName());
        assertEquals("/node1", item.getPath());
    }

    @Test
    public void testGetItemProperty() throws RepositoryException {
        Item item = session.getItem("/property1");
        assertEquals("property1", item.getName());
        assertEquals("/property1", item.getPath());
    }

    @Test(expected = RepositoryException.class)
    public void testGetItemIllegalArgument() throws RepositoryException {
        session.getItem("node1");
    }

    @Test(expected = PathNotFoundException.class)
    public void testGetItemPathNotFound() throws RepositoryException {
        session.getItem("/node1/item");
    }

    @Test
    public void testGetNode() throws RepositoryException {
        Node node = session.getNode("/node1/node2");
        assertEquals("node2", node.getName());
        assertEquals("/node1/node2", node.getPath());
    }

    @Test(expected = RepositoryException.class)
    public void testGetNodeIllegalArgument() throws RepositoryException {
        session.getNode("node1");
    }

    @Test(expected = PathNotFoundException.class)
    public void testGetNodePathNotFound() throws RepositoryException {
        session.getNode("/node1/node4");
    }

    @Test
    public void testGetProperty() throws RepositoryException {
        Property property = session.getProperty("/node1/property2");
        assertEquals("property2", property.getName());
        assertEquals("/node1/property2", property.getPath());
    }

    @Test(expected = RepositoryException.class)
    public void testGetPropertyIllegalArgument() throws RepositoryException {
        session.getProperty("property1");
    }

    @Test(expected = PathNotFoundException.class)
    public void testGetPropertyPathNotFound() throws RepositoryException {
        session.getNode("/node1/property4");
    }

    @Test
    public void testItemExistsNode() throws RepositoryException {
        assertTrue(session.itemExists("/node1/node3"));
        assertFalse(session.itemExists("/node1/node4"));
    }

    @Test
    public void testItemExistsProperty() throws RepositoryException {
        assertTrue(session.itemExists("/node1/property3"));
        assertFalse(session.itemExists("/node1/property4"));
    }

    @Test(expected = RepositoryException.class)
    public void testItemExistsIllegalArgument() throws RepositoryException {
        session.itemExists("item");
    }

    @Test
    public void testNodeExists() throws RepositoryException {
        assertTrue(session.nodeExists("/"));
        assertTrue(session.nodeExists("/node1"));
        assertTrue(session.nodeExists("/node1/node3"));
        assertFalse(session.nodeExists("/node1/node4"));
    }

    @Test(expected = RepositoryException.class)
    public void testNodeExistsIllegalArgument() throws RepositoryException {
        session.nodeExists("node1");
    }

    @Test
    public void testPropertyExists() throws RepositoryException {
        assertTrue(session.propertyExists("/property1"));
        assertTrue(session.propertyExists("/node1/property3"));
        assertFalse(session.propertyExists("/node1/property4"));
    }

    @Test(expected = RepositoryException.class)
    public void testPropertyExistsIllegalArgument() throws RepositoryException {
        session.propertyExists("property1");
    }

    @Test(expected = PathNotFoundException.class)
    public void testRemoveItemNode() throws RepositoryException {
        session.removeItem("/node1/node2");
        assertFalse(session.itemExists("/node1/node2"));
        session.getNode("/node1/node2");
    }

    @Test
    public void testRemoveItemNodeSave() throws RepositoryException {
        session.removeItem("/node1/node2");
        session.save();

        Session session2 = repository.login();
        assertFalse(session2.itemExists("/node1/node2"));
        session2.logout();
    }

    @Test(expected = PathNotFoundException.class)
    public void testRemoveItemProperty() throws RepositoryException {
        session.removeItem("/node1/property2");
        assertFalse(session.itemExists("/node1/property2"));
        session.getNode("/node1/property2");
    }

    public void testRemoveItemPropertySave() throws RepositoryException {
        session.removeItem("/node1/property2");
        session.save();

        Session session2 = repository.login();
        assertFalse(session2.itemExists("/node1/property2"));
        session2.logout();
    }

    @Test(expected = RepositoryException.class)
    public void testRemoveItemIllegalArgument() throws RepositoryException {
        session.removeItem("item");
    }

    @Test(expected = PathNotFoundException.class)
    public void testRemovePathNOtFound() throws RepositoryException {
        session.removeItem("/node1/item");
    }

}