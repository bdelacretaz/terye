package ch.x42.terye;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.jcr.PathNotFoundException;

import org.junit.Test;

public class PathTest {

    private void assertPathsEqual(String expected, Path path)
            throws PathNotFoundException {
        assertEquals(expected, path.getCanonical().toString());
    }

    @Test
    public void testIsNormalized() {
        assertTrue(new Path("/").isNormalized());
        assertFalse(new Path("/..").isNormalized());
        assertFalse(new Path("/.").isNormalized());
        assertTrue(new Path("/a/b").isNormalized());
        assertFalse(new Path("/a/b/..").isNormalized());
        assertFalse(new Path("/a/b/.").isNormalized());
        assertTrue(new Path("..").isNormalized());
        assertFalse(new Path(".").isNormalized());
        assertTrue(new Path("../../a").isNormalized());
        assertFalse(new Path("../../a/.").isNormalized());
        assertFalse(new Path("../../a/..").isNormalized());
        assertFalse(new Path("a/b/../..").isNormalized());
    }

    @Test
    public void testGetCanonical() throws PathNotFoundException {
        assertPathsEqual("/", new Path("/"));
        assertPathsEqual("/", new Path("/."));
        assertPathsEqual("/", new Path("/a/.."));
        assertPathsEqual("/", new Path("/a/../."));
        assertPathsEqual("/", new Path("/a/./.."));
        assertPathsEqual("/", new Path("/./././././././."));
        assertPathsEqual("/", new Path("/a/b/c/.././.././././../"));
        assertPathsEqual("/b", new Path("/a/.././b"));
        assertPathsEqual("/a/b/d", new Path("/a/b/c/../d"));
        assertPathsEqual("/c", new Path("/a/../b/../c"));
    }

    @Test(expected = PathNotFoundException.class)
    public void testGetCanonical2() throws PathNotFoundException {
        new Path("/a/b/../../../c").getCanonical().toString();
    }

}
