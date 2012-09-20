package ch.x42.terye;

import static org.junit.Assert.assertEquals;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;

import org.junit.Test;

public class PropertyTest extends ItemTest {

    @Test
    public void testSet() throws RepositoryException {
        root.addNode("a");
        Node b = root.addNode("a/b");
        
        Property p = root.setProperty("p", "string");
        assertEquals("p", p.getName());
        assertEquals("/p", p.getPath());
        
        Property q = b.setProperty("q", "string");
        assertEquals("q", q.getName());
        assertEquals("/a/b/q", q.getPath());
    }
}
