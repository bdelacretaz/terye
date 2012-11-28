package ch.x42.terye.path;

import javax.jcr.RepositoryException;

/**
 * This class represents the root path.
 */
public final class RootPath extends AbstractPath {

    public RootPath() {
        super(null, Path.ROOT);
    }

    @Override
    public boolean isRoot() {
        return true;
    }

    @Override
    public boolean isAbsolute() {
        return true;
    }

    @Override
    public boolean isNormalized() {
        return true;
    }

    @Override
    protected int getDepthIncrease() {
        return 0;
    }

    @Override
    public Path getNormalizedPath() throws RepositoryException {
        // root path is already normalized
        return this;
    }

    @Override
    public String toString() {
        return Path.ROOT + Path.DELIMITER;
    }

}
