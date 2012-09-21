package ch.x42.terye;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.jcr.ItemExistsException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.RepositoryException;

import org.junit.Test;

public class NodeTest extends ItemTest {

    @Test
    public void testAddNode() throws RepositoryException {
        Node a = root.addNode("a");
        assertEquals("a", a.getName());
        assertEquals("/a", a.getPath());

        Node b = root.addNode("a/b");
        assertEquals("b", b.getName());
        assertEquals("/a/b", b.getPath());

        Node c = b.addNode("c");
        assertEquals("c", c.getName());
        assertEquals("/a/b/c", c.getPath());

        Node d = b.addNode("d");
        assertEquals("d", d.getName());
        assertEquals("/a/b/d", d.getPath());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddINodellegalArgument() throws RepositoryException {
        root.addNode("/not/allowed");
    }

    @Test(expected = PathNotFoundException.class)
    public void testAddNodePathNotFound() throws RepositoryException {
        root.addNode("leads/to/nowhere");
    }

    @Test(expected = ItemExistsException.class)
    public void testAddNodeItemExists() throws RepositoryException {
        root.addNode("a");
        root.addNode("a");
    }

    @Test
    public void testGetNode() throws RepositoryException {
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

    @Test(expected = IllegalArgumentException.class)
    public void testGetNodeIllegalArgument() throws RepositoryException {
        root.getNode("/not/allowed");
    }

    @Test(expected = PathNotFoundException.class)
    public void testGetNodePathNotFound() throws RepositoryException {
        root.getNode("leads/to/nowhere");
    }

    @Test
    public void testGetNodes() throws RepositoryException {
        // add some nodes
        Node a = root.addNode("a");
        Node[] bs = new Node[] { 
            root.addNode("a/b1"), 
            root.addNode("a/b2"),
            root.addNode("a/b3")
        };

        // verify nodes
        NodeIterator iterator = a.getNodes();
        assertEquals(3, iterator.getSize());
        for (Node b : bs) {
            Node b1 = iterator.nextNode();
            assertEquals(b, b1);
        }
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testHasNode() throws RepositoryException {
        root.addNode("a");
        Node b = root.addNode("a/b");
        root.addNode("a/b/c1");
        root.addNode("a/b/c2");
        assertTrue(root.hasNode("a"));
        assertTrue(root.hasNode("a/b"));
        assertTrue(b.hasNode("c2"));
        assertFalse(b.hasNode("a/b/c"));
        assertFalse(b.hasNode("c3"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testHasNodeIllegalArgument() throws RepositoryException {
        root.hasNode("/not/allowed");
    }
    
    @Test
    public void testHasNodes() throws RepositoryException {
        root.addNode("a");
        Node b = root.addNode("a/b");
        Node c1 = root.addNode("a/b/c1");
        root.addNode("a/b/c2");
        assertTrue(root.hasNodes());
        assertTrue(b.hasNodes());
        assertFalse(c1.hasNodes());
    }

    @Test
    public void testSetProperty() throws RepositoryException {
        root.addNode("a");
        Node b = root.addNode("a/b");

        Property p = root.setProperty("p", "string1");
        assertEquals("p", p.getName());
        assertEquals("/p", p.getPath());

        Property q = b.setProperty("q", "string2");
        assertEquals("q", q.getName());
        assertEquals("/a/b/q", q.getPath());
    }

    @Test(expected = ItemExistsException.class)
    public void testSetPropertyItemExists() throws RepositoryException {
        root.addNode("a");
        root.setProperty("a", "string");
    }

    @Test
    public void testGetProperty() throws RepositoryException {
        root.addNode("a");
        Node b = root.addNode("a/b");
        Property p1 = root.setProperty("p", "string1");
        Property q1 = b.setProperty("q", "string2");

        Property p2 = root.getProperty("p");
        assertEquals(p1, p2);
        Property q2 = root.getProperty("a/b/q");
        assertEquals(q1, q2);
        q1 = b.getProperty("q");
        assertEquals(q1, q2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPropertyIllegalArgument() throws RepositoryException {
        root.getProperty("/not/allowed");
    }

    @Test(expected = PathNotFoundException.class)
    public void testGetPathNotFound() throws RepositoryException {
        root.getProperty("leads/to/nowhere");
    }

    @Test
    public void testGetProperties() throws RepositoryException {
        // add some properties
        Property[] ps = new Property[] { 
            root.setProperty("p1", "string1"), 
            root.setProperty("p2", "string2"),
            root.setProperty("p2", "string3")
        };
        
        // verify properties
        PropertyIterator iterator = root.getProperties();
        assertEquals(3, iterator.getSize());
        for (Property p : ps) {
            Property p1 = iterator.nextProperty();
            assertEquals(p, p1);
        }
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testOverwriteProperty() throws RepositoryException {
        root.setProperty("p", "string1");
        Property p = root.setProperty("p", "string2");
        assertEquals(p, root.getProperty("p"));
        assertEquals("string2", p.getString());
    }

    @Test
    public void testHasProperty() throws RepositoryException {
        Node a = root.addNode("a");
        a.setProperty("p", "string");
        a.setProperty("q", "string");
        assertFalse(root.hasProperty("p"));
        assertTrue(root.hasProperty("a/p"));
        assertTrue(a.hasProperty("p"));
        assertTrue(root.hasProperty("a/q"));
        assertTrue(a.hasProperty("q"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testHasPropertyIllegalArgument() throws RepositoryException {
        root.hasProperty("/not/allowed");
    }
    
    @Test
    public void testHasProperties() throws RepositoryException {
        Node a = root.addNode("a");
        assertFalse(a.hasProperties());
        a.setProperty("p", "string");
        assertTrue(a.hasProperties());
    }
    
    @Test
    public void testGetParent() throws RepositoryException {
        Node a = root.addNode("a");
        assertEquals(null, root.getParent());
        assertEquals(root, a.getParent());
    }
    
    @Test
    public void testRemove() throws RepositoryException {
        // XXX: todo
        Node a = root.addNode("a");
        Node b1 = root.addNode("a/b1");
        Node b2 = root.addNode("a/b2");
        Node b3 = root.addNode("a/b3");
        Node c = root.addNode("a/b3/c");
    }
    
    @Test
    public void testIsNode() throws RepositoryException {
        Node a = root.addNode("a");
        assertTrue(a.isNode());
    }
    
}