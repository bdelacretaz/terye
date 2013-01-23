package ch.x42.terye.mk.hbase;

import java.util.LinkedHashMap;

public class NodeCache {

    private class LRUCache extends LinkedHashMap<String, Node> {

        private static final long serialVersionUID = 1L;

        private final int maxEntries;

        public LRUCache(int maxEntries) {
            super(maxEntries, 0.75f, true);
            this.maxEntries = maxEntries;
        }

        @Override
        protected boolean removeEldestEntry(
                java.util.Map.Entry<String, Node> eldest) {
            return size() > maxEntries;
        }
    }

    private final LRUCache cache;

    public NodeCache(int maxEntries) {
        this.cache = new LRUCache(maxEntries);
    }

    public Node get(long revisionId, String path) {
        String key = String.valueOf(revisionId) + path;
        return cache.get(key);
    }

    public void put(long revisionId, String path, Node node) {
        String key = String.valueOf(revisionId) + path;
        cache.put(key, node);
    }

}
