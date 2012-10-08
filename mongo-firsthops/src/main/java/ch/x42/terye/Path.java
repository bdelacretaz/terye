package ch.x42.terye;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import javax.jcr.PathNotFoundException;

public class Path {

    public static final String DELIMITER = "/";
    public static final String ROOT = "/";
    public static final String CURRENT = ".";
    public static final String PARENT = "..";

    private String path;
    private LinkedList<String> segments;
    private boolean isAbsolute = false;
    private Boolean isNormalized;

    public Path(String path) {
        segments = new LinkedList<String>();
        if (path == null || path.isEmpty()) {
            throw new IllegalArgumentException("Path not specified or empty");
        }
        if (path.startsWith(ROOT)) {
            path = path.substring(1, path.length());
            segments.add(ROOT);
            isAbsolute = true;
        }
        // remove trailing delimiter, if any
        if (path.endsWith(DELIMITER)) {
            path = path.substring(0, path.length() - 1);
        }
        // split into segments
        if (!path.isEmpty()) {
            segments.addAll(Arrays.asList(path.split(DELIMITER)));
        }
    }

    private Path(LinkedList<String> segments, boolean isAbsolute,
            Boolean isNormalized) {
        this.segments = segments;
        this.isAbsolute = isAbsolute;
        this.isNormalized = isNormalized;
    }

    public Path concat(String path) {
        return concat(new Path(path));
    }

    public Path concat(Path path) {
        if (!path.isRelative()) {
            throw new IllegalArgumentException(path.toString()
                    + "is not a relative path");
        }
        LinkedList<String> segments = new LinkedList<String>(this.segments);
        segments.addAll(path.segments);
        return new Path(segments, isAbsolute(), null);
    }

    public boolean isNormalized() {
        if (isNormalized == null) {
            // all ".."s must be at the beginning (possible in relative paths)
            // ".."s and "."s are not allowed after that
            boolean parentAllowed = !isAbsolute();
            for (String s : segments) {
                if (parentAllowed) {
                    if (s.equals(PARENT)) {
                        continue;
                    } else {
                        parentAllowed = false;
                    }
                }
                if (s.equals(PARENT) || s.equals(CURRENT)) {
                    isNormalized = false;
                    return isNormalized;
                }
            }
            isNormalized = true;
        }
        return isNormalized;
    }

    public Path getCanonical() throws PathNotFoundException {
        if (!isAbsolute) {
            throw new UnsupportedOperationException(
                    "Cannot make a relative path canonical");
        }

        LinkedList<String> segments = new LinkedList<String>(this.segments);
        ListIterator<String> i = segments.listIterator();
        int parents = 0;
        while (i.hasNext()) {
            String s = i.next();
            // remove CURRENT segment
            if (s.equals(CURRENT)) {
                i.remove();
            } else if (s.equals(PARENT)) {
                parents++;
                i.previous();
                if (i.hasPrevious()) {
                    String s2 = i.previous();
                    // remove PARENT segment if preceded by an identifier
                    if (!s2.equals(CURRENT) && !s2.equals(PARENT)
                            && !s2.equals(ROOT)) {
                        i.remove();
                        i.next();
                        i.remove();
                        parents--;
                    } else {
                        // else go on
                        i.next();
                        i.next();
                    }
                } else {
                    // else go on
                    i.next();
                }
            }
        }

        if (parents > 0) {
            // there are remaining PARENT segments
            // -> path cannot be normalized
            throw new PathNotFoundException(toString() + " is an invalid path");
        }

        return new Path(segments, true, true);
    }

    public String getLastSegment() {
        String seg = segments.getLast();
        return seg;
    }

    public Path getParent() {
        LinkedList<String> segments = new LinkedList<String>(this.segments);
        segments.removeLast();
        if (segments.isEmpty()) {
            return null;
        }
        return new Path(segments, isAbsolute(), null);
    }

    public boolean isAbsolute() {
        return isAbsolute;
    }

    public boolean isRelative() {
        return !isAbsolute();
    }

    public boolean isCanonical() {
        return isAbsolute() && isNormalized();
    }

    @Override
    public String toString() {
        if (path == null) {
            path = "";
            Iterator<String> iterator = segments.iterator();
            while (iterator.hasNext()) {
                String seg = iterator.next();
                path += seg;
                if (!seg.equals(ROOT) && iterator.hasNext()) {
                    path += DELIMITER;
                }
            }
        }
        return path;
    }
}
