package ch.x42.terye.persistence;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bson.BSONObject;

import com.mongodb.DBObject;

public class NodeState implements DBObject {

    private String path;
    private String parent;
    private boolean isPartial;

    public Object put(String key, Object v) {
        if (key.equals("path")) {
            this.path = (String) v;
        } else if (key.equals("parent")) {
            this.parent = (String) v;
        } else {
        }
        return v;
    }

    public void putAll(BSONObject o) {
        putAll(o.toMap());
    }

    public void putAll(Map m) {
        Set<String> keySet = m.keySet();
        for (String key : keySet) {
            put(key, m.get(key));
        }
    }

    public Object get(String key) {
        Object ret = null;
        if (key.equals("path")) {
            ret = this.path;
        } else if (key.equals("parent")) {
            ret = this.parent;
        } else {
        }
        return ret;
    }

    public Map toMap() {
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("path", this.path);
        ret.put("parent", this.parent);
        return ret;
    }

    public Object removeField(String key) {
        Object ret = null;
        if (key.equals("path")) {
            ret = this.path;
            this.path = null;
        } else if (key.equals("parent")) {
            ret = this.parent;
            this.parent = null;
        } else {
        }
        return ret;
    }

    public boolean containsKey(String key) {
        return this.containsField(key);
    }

    public boolean containsField(String s) {
        boolean ret = false;
        if (s.equals("path") && this.path != null) {
            ret = true;
        } else if (s.equals("parent") && this.parent != null) {
            ret = true;
        } else {
        }
        return ret;
    }

    public Set<String> keySet() {
        Set<String> ret = new HashSet<String>();
        ret.add("path");
        ret.add("parent");
        return ret;
    }

    public void markAsPartialObject() {
        this.isPartial = true;
    }

    public boolean isPartialObject() {
        return this.isPartial;
    }

    public String getPath() {
        return path;
    }
    
    public String getParent() {
        return parent;
    }

}
