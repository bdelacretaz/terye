package ch.x42.terye.path;

import javax.jcr.RepositoryException;

/**
 * This class represents a path with its last element being the current element
 * (i.e. Path.PARENT).
 */
public final class ParentPath extends AbstractPath {

    ParentPath(Path parent) {
        super(parent, Path.PARENT);
    }

    @Override
    public boolean isNormalized() {
        if (getParent() == null) {
            return true;
        }
        return getParent().isNormalized()
                && getParent().getClass() == ParentPath.class;
    }

    @Override
    protected int getDepthIncrease() {
        return -1;
    }

    @Override
    public Path getNormalizedPath() throws RepositoryException {
        if (isNormalized()) {
            return this;
        }
        // if this element is at the beginning
        if (getParent() == null) {
            // simply return it
            return this;
        }
        // else normalize parent
        Path normalized = getParent().getNormalizedPath();
        if (normalized.getClass() == ParentPath.class) {
            // if 'normalized' is a parent element
            return new ParentPath(normalized);
        } else if (normalized.getClass() == CurrentPath.class) {
            // if 'normalized' is a current element
            return new ParentPath(null);
        } else if (normalized.getClass() == NamePath.class) {
            if (normalized.getParent() != null) {
                // else cancel it out
                return normalized.getParent();
            } else {
                // normalized was the topmost element, add back a "."
                return new CurrentPath(null);
            }
        }
        // the path cannot be normalized
        throw new RepositoryException("Cannot normalize " + this);
    }

}
