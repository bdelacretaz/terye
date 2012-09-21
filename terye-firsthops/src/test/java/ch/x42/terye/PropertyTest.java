package ch.x42.terye;

import static org.junit.Assert.assertEquals;

import javax.jcr.ItemExistsException;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.RepositoryException;

import org.junit.Test;

public class PropertyTest extends ItemTest {

    @Test
    public void testSet() throws RepositoryException {
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
    public void testSetItemExists() throws RepositoryException {
        root.addNode("a");
        root.setProperty("a", "string");
    }

    @Test
    public void testGet() throws RepositoryException {
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
    public void testGetIllegalArgument() throws RepositoryException {
        root.getProperty("/not/allowed");
    }

    @Test(expected = PathNotFoundException.class)
    public void testGetPathNotFound() throws RepositoryException {
        root.getProperty("leads/to/nowhere");
    }
}