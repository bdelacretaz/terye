package ch.x42.terye.path;

import java.util.StringTokenizer;

public class PathFactory {

    public static Path create(String pathString) {
        if (pathString == null || pathString.isEmpty()) {
            throw new IllegalArgumentException("Invalid path string");
        }

        Path path = null;
        if (pathString.startsWith(Path.DELIMITER)) {
            path = new RootPath();
        }

        StringTokenizer tokenizer = new StringTokenizer(pathString,
                Path.DELIMITER);
        while (tokenizer.hasMoreTokens()) {
            String element = tokenizer.nextToken();
            if (Path.CURRENT.equals(element)) {
                path = new CurrentPath(path);
            } else if (Path.PARENT.equals(element)) {
                path = new ParentPath(path);
            } else {
                path = new NamePath(path, element);
            }
        }

        return path;
    }

    public static Path create(String absPathString, String relPathString) {
        Path relPath = create(relPathString);
        if (relPath.isAbsolute()) {
            throw new IllegalArgumentException("relPathString is not relative");
        }
        Path absPath = create(absPathString);
        return absPath.resolve(relPath);
    }

}
