package ch.x42.terye.mk.hbase;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.jackrabbit.mongomk.impl.json.DefaultJsopHandler;
import org.apache.jackrabbit.oak.commons.PathUtils;

public class Update {

    private class JsopHandler extends DefaultJsopHandler {

        @Override
        public void nodeAdded(String parentPath, String name) {
            String path = PathUtils.concat(parentPath, name);
            addNode(path);
            changeChildCount(parentPath, true);
        }

    }

    private Set<String> modifiedNodes;
    private Set<String> addedNodes;
    private Map<String, Long> changedChildCounts;

    public Update() {
        modifiedNodes = new TreeSet<String>();
        addedNodes = new TreeSet<String>();
        changedChildCounts = new LinkedHashMap<String, Long>();
    }
    
    public DefaultJsopHandler getJsopHandler() {
        return new JsopHandler();
    }

    public void addNode(String path) {
        addedNodes.add(path);
        modifiedNodes.add(path);
    }

    public void changeChildCount(String path, boolean increment) {
        if (!changedChildCounts.containsKey(path)) {
            changedChildCounts.put(path, 0L);
        }
        Long count = changedChildCounts.get(path);
        if (increment) {
            changedChildCounts.put(path, count + 1);
        } else {
            changedChildCounts.put(path, count - 1);
        }
        modifiedNodes.add(path);
    }

    public Set<String> getModifiedNodes() {
        return modifiedNodes;
    }

    public Set<String> getAddedNodes() {
        return addedNodes;
    }

    public Map<String, Long> getChangedChildCounts() {
        return changedChildCounts;
    }

}
