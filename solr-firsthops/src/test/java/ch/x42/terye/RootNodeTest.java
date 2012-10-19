package ch.x42.terye;

import static org.junit.Assert.assertEquals;

import javax.jcr.ItemNotFoundException;
import javax.jcr.RepositoryException;

import org.junit.Test;

public class RootNodeTest extends BaseTest {

    @Test
    public void testName() throws RepositoryException {
        assertEquals("The name of the root node must be the empty string", "",
                root.getName());
    }

    @Test
    public void testPath() throws RepositoryException {
        assertEquals("The path of the root node must be a single slash", "/",
                root.getPath());
    }

    @Test(expected = ItemNotFoundException.class)
    public void testGetParentRoot() throws RepositoryException {
        root.getParent();
    }

    @Test(expected = RepositoryException.class)
    public void testRemove() throws RepositoryException {
        root.remove();
    }

}
