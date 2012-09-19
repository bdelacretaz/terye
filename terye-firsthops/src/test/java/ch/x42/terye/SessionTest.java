package ch.x42.terye;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SessionTest
{
    private Session session;
    
    @Before
    public void setup() throws RepositoryException {
        session = new RepositoryImpl().login();
    }
    
    @After
    public void cleanup() {
        if(session != null) {
            session.logout();
        }
    }
    
    @Test
    public void testSessionLiveness()
    	throws RepositoryException
    {
        assertTrue("Expecting session to be initially live", session.isLive());
        session.logout();
        assertFalse("Session should not be live after logout", session.isLive());
        session = null;
    }
    
    @Test
    public void testRoot()
    	throws RepositoryException
    {
    	final Node root = session.getRootNode();
    	assertFalse("Root node should not be null", root == null);
    	assertEquals("The name of the root node should be the empty string", "", root.getName());
    }
}