package ch.x42.terye.path;

import javax.jcr.RepositoryException;

public abstract class AbstractPath implements Path {

    private final Path parent;
    private final String name;
    private final int depth;
    private String pathString;

    public AbstractPath(Path parent, String name) {
        this.parent = parent;
        this.name = name;
        int depth = 0;
        if (parent != null) {
            depth += parent.getDepth();
        }
        this.depth = depth + getDepthIncrease();
    }

    @Override
    public boolean isRoot() {
        return false;
    }

    @Override
    public boolean isRelative() {
        return !isAbsolute();
    }

    @Override
    public boolean isAbsolute() {
        if (getParent() != null) {
            return getParent().isAbsolute();
        }
        return false;
    }

    @Override
    public boolean isCanonical() {
        return isAbsolute() && isNormalized();
    }

    protected int getDepthIncrease() {
        return 1;
    }

    @Override
    public int getDepth() {
        return depth;
    }

    @Override
    public Path getAncestor(int degree) throws RepositoryException {
        if (degree < 0) {
            throw new IllegalArgumentException(
                    "Invalid degree: must be non-negative");
        } else if (degree == 0) {
            return getNormalizedPath();
        } else if (getParent() == null) {
            throw new IllegalArgumentException(
                    "Invalid degree: reached topmost ancestor");
        }

        return getParent().getAncestor(degree - 1);
    }

    @Override
    public Path getParent() {
        return parent;
    }

    @Override
    public Path getCanonicalPath() throws RepositoryException {
        if (isRelative()) {
            throw new RepositoryException(
                    "Cannot make a relative path canonical");
        }
        return getNormalizedPath();
    }

    @Override
    public boolean isEquivalentTo(Path path) throws RepositoryException {
        return getNormalizedPath().equals(path.getNormalizedPath());
    }

    @Override
    public boolean isDescendantOf(Path path) throws RepositoryException {
        if (isRelative() || path.isRelative()) {
            // XXX: true?
            throw new RepositoryException("Both paths must be absolute");
        }
        int d = getDepth() - path.getDepth();
        return d > 0 && getAncestor(d).isEquivalentTo(path);
    }

    @Override
    public boolean isAncestorOf(Path path) throws RepositoryException {
        return path.isDescendantOf(this);
    }

    @Override
    public String getLastElement() {
        return name;
    }

    @Override
    public String toString() {
        if (pathString == null) {
            pathString = "";
            if (getParent() != null) {
                pathString = getParent().toString();
                if (!getParent().isRoot()) {
                    pathString += Path.DELIMITER;
                }
            }
            pathString += name;
        }
        return pathString;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + toString().hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        AbstractPath other = (AbstractPath) obj;
        return toString().equals(other.toString());
    }

}
