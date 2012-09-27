package ch.x42.terye.persistence;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NodeState extends ItemState {

    private List<String> children = new LinkedList<String>();

    public NodeState() {
        super(ItemType.NODE);
    }

    public NodeState(String path, String parent) {
        this();
        put("path", path);
        put("parent", parent);
    }

    @Override
    public boolean containsField(String s) {
        if (s.equals("children") && children != null) {
            return true;
        }
        return super.containsField(s);
    }

    @Override
    public Object get(String key) {
        if (key.equals("children")) {
            return children;
        }
        return super.get(key);
    }

    @Override
    public Set<String> keySet() {
        Set<String> set = super.keySet();
        set.add("children");
        return set;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object put(String key, Object v) {
        if (key.equals("children")) {
            children = (List<String>) v;
            return v;
        }
        return super.put(key, v);
    }

    @Override
    public Object removeField(String key) {
        Object ret = null;
        if (key.equals("children")) {
            ret = children;
            children = null;
            return ret;
        }
        return super.removeField(key);
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map toMap() {
        Map<String, Object> ret = super.toMap();
        ret.put("children", children);
        return ret;
    }

    public List<String> getChildren() {
        return children;
    }

}
