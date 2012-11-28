package ch.x42.terye.path;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.jcr.RepositoryException;

import org.junit.Test;

public class PathTest {

    @Test
    public void testIsNormalized() throws RepositoryException {
        assertTrue(PathFactory.create("/").isNormalized());
        assertTrue(PathFactory.create(".").isNormalized());
        assertTrue(PathFactory.create("..").isNormalized());
        assertTrue(PathFactory.create("/a").isNormalized());
        assertTrue(PathFactory.create("a").isNormalized());
        assertTrue(PathFactory.create("a/b").isNormalized());
        assertTrue(PathFactory.create("/a/b").isNormalized());
        assertTrue(PathFactory.create("../..").isNormalized());
        assertTrue(PathFactory.create("../../a").isNormalized());
        assertTrue(PathFactory.create("../../a/b/c").isNormalized());

        assertFalse(PathFactory.create("/.").isNormalized());
        assertFalse(PathFactory.create("./a").isNormalized());
        assertFalse(PathFactory.create("./..").isNormalized());
        assertFalse(PathFactory.create("../.").isNormalized());
        assertFalse(PathFactory.create("/..").isNormalized());
        assertFalse(PathFactory.create("/a/..").isNormalized());
        assertFalse(PathFactory.create("/a/.").isNormalized());
        assertFalse(PathFactory.create("../../a/.").isNormalized());
        assertFalse(PathFactory.create("../../a/..").isNormalized());
        assertFalse(PathFactory.create("a/b/../..").isNormalized());
        assertFalse(PathFactory.create("../a/../../a/b/c").isNormalized());
    }

    private void assertNormalizedPathsEqual(String expected, String pathString)
            throws RepositoryException {
        assertEquals(expected, PathFactory.create(pathString)
                .getNormalizedPath().toString());
    }

    @Test
    public void testGetNormalized() throws RepositoryException {
        assertNormalizedPathsEqual("/", "/");
        assertNormalizedPathsEqual("/", "/.");
        assertNormalizedPathsEqual("/", "/a/..");
        assertNormalizedPathsEqual("/", "/a/../.");
        assertNormalizedPathsEqual("/", "/a/./..");
        assertNormalizedPathsEqual("/", "/./././././.");
        assertNormalizedPathsEqual("/", "/a/b/c/.././.././././../");
        assertNormalizedPathsEqual("/b", "/a/.././b");
        assertNormalizedPathsEqual("/a/b/d", "/a/b/c/../d");
        assertNormalizedPathsEqual("/c", "/a/../b/../c");
        assertNormalizedPathsEqual(".", ".");
        assertNormalizedPathsEqual("a", "./a");
        assertNormalizedPathsEqual(".", "./a/..");
        assertNormalizedPathsEqual(".", "a/..");
        assertNormalizedPathsEqual("a", "a");
        assertNormalizedPathsEqual("..", "./a/b/.././a/../../..");
        assertNormalizedPathsEqual("../../..", "../../asdf/../..");
        assertNormalizedPathsEqual("..", "./..");
        assertNormalizedPathsEqual(".", "./");
        assertNormalizedPathsEqual("/", "/a/./b/../../");
        assertNormalizedPathsEqual("/a/b", "/a/b/");
    }

}
