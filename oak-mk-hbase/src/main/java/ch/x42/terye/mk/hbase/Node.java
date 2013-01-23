package ch.x42.terye.mk.hbase;

public class Node {

    private String path;
    private long lastRevision;
    private long childCount;

    public Node(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getLastRevision() {
        return lastRevision;
    }

    public void setLastRevision(long lastRevision) {
        this.lastRevision = lastRevision;
    }

    public long getChildCount() {
        return childCount;
    }

    public void setChildCount(long childCount) {
        this.childCount = childCount;
    }

}
