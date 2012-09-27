package ch.x42.terye.persistence;

import java.util.Map;
import java.util.Set;

public class PropertyState extends ItemState {

    private Object value;

    public PropertyState() {
        super(ItemType.PROPERTY);
    }

    public PropertyState(String path, String parent, Object value) {
        this();
        put("path", path);
        put("parent", parent);
        put("value", value);
    }

    @Override
    public boolean containsField(String s) {
        if (s.equals("value") && value != null) {
            return true;
        }
        return super.containsField(s);
    }

    @Override
    public Object get(String key) {
        if (key.equals("value")) {
            return value;
        }
        return super.get(key);
    }

    @Override
    public Set<String> keySet() {
        Set<String> set = super.keySet();
        set.add("value");
        return set;
    }

    @Override
    public Object put(String key, Object v) {
        if (key.equals("value")) {
            value = v;
            return v;
        }
        return super.put(key, v);
    }

    @Override
    public Object removeField(String key) {
        Object ret = null;
        if (key.equals("value")) {
            ret = value;
            value = null;
        } else {
            return super.removeField(key);
        }
        return ret;
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map toMap() {
        Map<String, Object> ret = super.toMap();
        ret.put("value", value);
        return ret;
    }

    public Object getValue() {
        return value;
    }

}
