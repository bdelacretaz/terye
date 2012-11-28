package ch.x42.terye.path;

import javax.jcr.RepositoryException;

/**
 * This class represents a path with its last element being an ordinary path
 * name element.
 */
public final class NamePath extends AbstractPath {

    NamePath(Path parent, String name) {
        super(parent, name);
    }

    @Override
    public boolean isNormalized() {
        return getParent() == null
                || (getParent().isNormalized() && getParent().getClass() != CurrentPath.class);
    }

    @Override
    protected int getDepthIncrease() {
        return 1;
    }

    @Override
    public Path getNormalizedPath() throws RepositoryException {
        if (isNormalized()) {
            return this;
        }
        // if this element is at the beginning
        if (getParent() == null) {
            return this;
        }
        // normalize recursively
        Path normalized = getParent().getNormalizedPath();
        // special case: ./a -> a
        if (normalized.getClass() == CurrentPath.class) {
            normalized = null;
        }
        return new NamePath(normalized, getLastElement());
    }
}
