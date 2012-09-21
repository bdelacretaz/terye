package ch.x42.terye;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.ValueFormatException;

import org.junit.Test;

public class PropertyTest extends ItemTest {

    @Test
    public void testSetAndGetValues() throws RepositoryException {
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
    public void testIllicitConversions() throws RepositoryException {
        Property p1 = root.setProperty("p1", "string");
        p1.getDate();
        // XXX: improve
    }

    @Test
    public void testGetParent() throws RepositoryException {
        Property p = root.setProperty("p", "string");
        assertEquals(root, p.getParent());
    }

    @Test
    public void testIsNode() throws RepositoryException {
        Property p = root.setProperty("p", "string");
        assertFalse(p.isNode());
    }
    
}