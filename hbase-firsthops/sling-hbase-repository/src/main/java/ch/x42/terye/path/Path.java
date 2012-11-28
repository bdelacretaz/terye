package ch.x42.terye.path;

import javax.jcr.RepositoryException;

public interface Path {

    static final String DELIMITER = "/";
    static final String ROOT = "";
    static final String CURRENT = ".";
    static final String PARENT = "..";

    boolean isRoot();

    boolean isRelative();

    boolean isAbsolute();

    boolean isNormalized();

    boolean isCanonical();

    /**
     * Returns the depth of the normalized path.
     */
    int getDepth();

    /**
     * Returns the length (number of elements) of the path.
     */
    int getLength();

    /**
     * Returns the ancestor ('degree' steps up) of the normalized path.
     */
    Path getAncestor(int degree) throws RepositoryException;

    Path getParent();

    Path getNormalizedPath() throws RepositoryException;

    Path getCanonicalPath() throws RepositoryException;

    Path resolve(Path relative);

    Path resolve(String element);

    boolean isEquivalentTo(Path path) throws RepositoryException;

    boolean isDescendantOf(Path path) throws RepositoryException;

    boolean isAncestorOf(Path path) throws RepositoryException;

    String getLastElement();

}
