package ch.x42.terye;

import java.util.Arrays;

import javax.jcr.RepositoryException;

public class Path {

    public static final String DELIMITER = "/";

    // store path as a string for toString
    private String path;
    private String[] segments;
    private boolean isAbsolute = false;

    public Path(String path) {
        // check if path is absolute
        if (path.startsWith(Path.DELIMITER)) {
            isAbsolute = true;
            path = path.substring(1, path.length());
        }

        // remove trailing delimiter, if any
        if (path.endsWith(Path.DELIMITER)) {
            path = path.substring(0, path.length() - 1);
        }

        // split path into segments
        if (path.isEmpty()) {
            // split on empty string doesn't yield empty array
            segments = new String[0];
        } else {
            segments = path.split(Path.DELIMITER);
        }
    }

    private Path(String[] segments) {
        isAbsolute = true;
        this.segments = segments;
    }
    
    public static Path getCanonical(Path absBasePath, String relPath) throws RepositoryException {
        Path relativePath = new Path(relPath);
        if (!relativePath.isRelative()) {
            throw new RepositoryException("Not a relative path: " + relPath);
        }
        return absBasePath.concat(relPath).getCanonical();
    }

    public boolean isAbsolute() {
        return isAbsolute;
    }

    public boolean isRelative() {
        return !isAbsolute();
    }

    public boolean isRoot() {
        return isAbsolute() && segments.length == 0;
    }

    public Path concat(Path that) {
        // make sure that lhs is absolute and rhs is relative
        // -> yields an absolute path
        if (isRelative()) {
            throw new IllegalArgumentException("Concat cannot be called on a relative path");
        } else if (that.isAbsolute()) {
            throw new IllegalArgumentException("Specified path must be relative");
        }

        // concat segments and return new path
        String[] segments = new String[this.segments.length + that.segments.length];
        System.arraycopy(this.segments, 0, segments, 0, this.segments.length);
        System.arraycopy(that.segments, 0, segments, this.segments.length, that.segments.length);
        return new Path(segments);
    }
    
    public Path concat(String path) {
        return concat(new Path(path));
    }
    
    public Path getCanonical() throws RepositoryException {
        // XXX: implement
        if (!isAbsolute()) {
            throw new RepositoryException("Not an absolute path: " + toString());
        }
        return this;
    }
    
    public Path getParent() {
        // should only be called on absolute paths
        if (!isAbsolute()) {
            throw new UnsupportedOperationException();
        }

        // return null for empty paths (root node or empty relative path)
        if (segments.length == 0) {
            return null;
        }

        return new Path(Arrays.copyOfRange(segments, 0, segments.length - 1));
    }

    public String getLastSegment() {
        return segments.length > 0 ? segments[segments.length - 1] : "";
    }

    @Override
    public String toString() {
        if (path == null) {
            path = isAbsolute ? Path.DELIMITER : "";
            for (int i = 0; i < segments.length; i++) {
                path += segments[i];
                if (i < segments.length - 1) {
                    path += Path.DELIMITER;
                }
            }
        }
        return path;
    }
}
