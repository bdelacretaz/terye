package ch.x42.terye;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.junit.Before;
import org.junit.Test;

public class PropertyTest extends BaseTest {

    private Property property1;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        property1 = session.getProperty("/property1");
    }

    @Test
    public void testStringProperty() throws RepositoryException {
        String value = "abcdef";
        Property property = root.setProperty("string", value);
        assertEquals(PropertyType.STRING, property.getType());
        assertEquals(value, property.getString());
        assertEquals(PropertyType.STRING, property.getValue().getType());
        assertEquals(value, property.getValue().getString());
        session.save();

        // verify property when fetched from persistent state
        Session session2 = repository.login();
        property = session2.getRootNode().getProperty("string");
        assertEquals(PropertyType.STRING, property.getType());
        assertEquals(value, property.getString());
        assertEquals(PropertyType.STRING, property.getValue().getType());
        assertEquals(value, property.getValue().getString());
        session2.logout();
    }

    @Test
    public void testLongProperty() throws RepositoryException {
        long value = 1234567890123456L;
        Property property = root.setProperty("long", value);
        assertEquals(PropertyType.LONG, property.getType());
        assertEquals(value, property.getLong());
        assertEquals(PropertyType.LONG, property.getValue().getType());
        assertEquals(value, property.getValue().getLong());
        session.save();

        // verify property when fetched from persistent state
        Session session2 = repository.login();
        property = session2.getRootNode().getProperty("long");
        assertEquals(PropertyType.LONG, property.getType());
        assertEquals(value, property.getLong());
        assertEquals(PropertyType.LONG, property.getValue().getType());
        assertEquals(value, property.getValue().getLong());
        session2.logout();
    }

    @Test
    public void testDoubleProperty() throws RepositoryException {
        double value = 0.5D;
        Property property = root.setProperty("double", value);
        assertEquals(PropertyType.DOUBLE, property.getType());
        assertEquals(value, property.getDouble(), 0);
        assertEquals(PropertyType.DOUBLE, property.getValue().getType());
        assertEquals(value, property.getValue().getDouble(), 0);
        session.save();

        // verify property when fetched from persistent state
        Session session2 = repository.login();
        property = session2.getRootNode().getProperty("double");
        assertEquals(PropertyType.DOUBLE, property.getType());
        assertEquals(value, property.getDouble(), 0);
        assertEquals(PropertyType.DOUBLE, property.getValue().getType());
        assertEquals(value, property.getValue().getDouble(), 0);
        session2.logout();
    }

    @Test
    public void testDecimalProperty() throws RepositoryException {
        BigDecimal value = new BigDecimal(
                "3.141592653589793238462643383279502884197169399375105820974944592307816406286");
        Property property = root.setProperty("decimal", value);
        assertEquals(PropertyType.DECIMAL, property.getType());
        assertEquals(value, property.getDecimal());
        assertEquals(PropertyType.DECIMAL, property.getValue().getType());
        assertEquals(value, property.getValue().getDecimal());
        session.save();

        // verify property when fetched from persistent state
        Session session2 = repository.login();
        property = session2.getRootNode().getProperty("decimal");
        assertEquals(PropertyType.DECIMAL, property.getType());
        assertEquals(value, property.getDecimal());
        assertEquals(PropertyType.DECIMAL, property.getValue().getType());
        assertEquals(value, property.getValue().getDecimal());
        session2.logout();
    }

    @Test
    public void testBooleanProperty() throws RepositoryException {
        boolean value = true;
        Property property = root.setProperty("boolean", value);
        assertEquals(PropertyType.BOOLEAN, property.getType());
        assertEquals(value, property.getBoolean());
        assertEquals(PropertyType.BOOLEAN, property.getValue().getType());
        assertEquals(value, property.getValue().getBoolean());
        session.save();

        // verify property when fetched from persistent state
        Session session2 = repository.login();
        property = session2.getRootNode().getProperty("boolean");
        assertEquals(PropertyType.BOOLEAN, property.getType());
        assertEquals(value, property.getBoolean());
        assertEquals(PropertyType.BOOLEAN, property.getValue().getType());
        assertEquals(value, property.getValue().getBoolean());
        session2.logout();
    }

    @Test
    public void testDateProperty() throws RepositoryException {
        Calendar value = new GregorianCalendar(2012, Calendar.SEPTEMBER, 21);
        Property property = root.setProperty("date", value);
        assertEquals(PropertyType.DATE, property.getType());
        assertEquals(value, property.getDate());
        assertEquals(PropertyType.DATE, property.getValue().getType());
        assertEquals(value, property.getValue().getDate());
        session.save();

        // verify property when fetched from persistent state
        Session session2 = repository.login();
        property = session2.getRootNode().getProperty("date");
        assertEquals(PropertyType.DATE, property.getType());
        assertEquals(value, property.getDate());
        assertEquals(PropertyType.DATE, property.getValue().getType());
        assertEquals(value, property.getValue().getDate());
        session2.logout();
    }

    @Test
    public void testGetParent() throws RepositoryException {
        Node parent = property1.getParent();
        assertEquals(root.getName(), parent.getName());
        assertEquals(root.getPath(), parent.getPath());
    }

    @Test(expected = PathNotFoundException.class)
    public void testRemove() throws RepositoryException {
        property1.remove();
        assertFalse(session.propertyExists("/property1"));
        assertFalse(root.hasProperty("property1"));
        assertFalse(root.hasProperties());
        root.getProperty("property1");
    }

    @Test
    public void testRemoveSave() throws RepositoryException {
        property1.remove();
        session.save();

        Session session2 = repository.login();
        assertFalse(session2.propertyExists("/property1"));
        assertFalse(session2.getRootNode().hasProperty("property1"));
        assertFalse(session2.getRootNode().hasProperties());
        session2.logout();
    }

    @Test
    public void testIsNode() throws RepositoryException {
        assertFalse(property1.isNode());
    }

}