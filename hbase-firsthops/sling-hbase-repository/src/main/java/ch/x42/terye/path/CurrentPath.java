package ch.x42.terye.path;

import javax.jcr.RepositoryException;

/**
 * This class represents a path with its last element being the current element
 * (i.e. Path.CURRENT).
 */
public final class CurrentPath extends AbstractPath {

    CurrentPath(Path parent) {
        super(parent, Path.CURRENT);
    }

    @Override
    public boolean isNormalized() {
        return getParent() == null;
    }

    @Override
    protected int getDepthIncrease() {
        return 0;
    }

    @Override
    public Path getNormalizedPath() throws RepositoryException {
        // if this element is at the beginning
        if (getParent() == null) {
            // keep it in the path
            return this;
        } else {
            // else remove it from path
            return getParent().getNormalizedPath();
        }
    }
}
