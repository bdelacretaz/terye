package ch.x42.terye;

import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class SessionTest {
    @Test
    public void testSessionLogout() throws RepositoryException
    {
        final Repository r = new RepositoryImpl();
        final Session s = r.login();
        assertTrue("Expecting Session to be initially live", s.isLive());
        s.logout();
        assertFalse("Expecting Session to be initially live", s.isLive());
    }
}
