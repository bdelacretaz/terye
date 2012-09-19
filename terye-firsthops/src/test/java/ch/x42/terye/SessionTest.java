package ch.x42.terye;

import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class SessionTest
{
    @Test
    public void testSessionLiveness() throws RepositoryException
    {
        final Repository repository = new RepositoryImpl();
        final Session session = repository.login();
        assertTrue("Expecting session to be initially live", session.isLive());
        session.logout();
        assertFalse("Session should not be live after logout", session.isLive());
    }
}