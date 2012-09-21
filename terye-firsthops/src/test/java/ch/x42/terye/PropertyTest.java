package ch.x42.terye;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.jcr.ItemExistsException;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.ValueFormatException;

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

    @Test
    public void testOverwrite() throws RepositoryException {
        root.setProperty("p", "string1");
        Property p = root.setProperty("p", "string2");
        assertEquals(p, root.getProperty("p"));
        assertEquals("string2", p.getString());
    }

    @Test
    public void testValues() throws RepositoryException {
        root.setProperty("p1", "string");
        root.setProperty("p2", 1234567890123456L);
        root.setProperty("p3", 3.14);
        BigDecimal pi = new BigDecimal(
                "3.141592653589793238462643383279502884197169399375105820974944592307816406286");
        root.setProperty("p4", pi);
        root.setProperty("p5", false);
        Calendar cal = new GregorianCalendar(2012, Calendar.SEPTEMBER, 21);
        root.setProperty("p6", cal);

        assertEquals("string", root.getProperty("p1").getString());
        assertEquals(1234567890123456L, root.getProperty("p2").getLong());
        assertEquals(3.14, root.getProperty("p3").getDouble(), 0.0);
        assertEquals(pi, root.getProperty("p4").getDecimal());
        assertEquals(false, root.getProperty("p5").getBoolean());
        assertEquals(cal, root.getProperty("p6").getDate());
    }

    @Test(expected = ValueFormatException.class)
    public void testConversions() throws RepositoryException {
        Property p1 = root.setProperty("p1", "string");
        p1.getDate();
        // XXX: improve
    }
    
    @Test
    public void testParent() throws RepositoryException {
        Property p = root.setProperty("p", "string");
        assertEquals(root, p.getParent());
    }

}