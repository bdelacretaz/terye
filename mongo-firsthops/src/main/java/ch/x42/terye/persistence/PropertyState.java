package ch.x42.terye.persistence;

import java.util.Map;
import java.util.Set;

import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import javax.jcr.Value;

import ch.x42.terye.value.ValueImpl;

public class PropertyState extends ItemState {

    private Object value;
    private Integer propertyType;

    public PropertyState() {
        super(ItemType.PROPERTY);
    }

    public PropertyState(String path, Value value) {
        this();
        put("path", path);
        putValue(value);
        put("propertyType", value.getType());
    }

    /**
     * Since there is no way to directly get to the underlying data object..
     */
    private void putValue(Value value) {
        Object v = null;
        try {
            switch (value.getType()) {
                case PropertyType.STRING:
                    v = value.getString();
                    break;
                case PropertyType.LONG:
                    v = value.getLong();
                    break;
                case PropertyType.DOUBLE:
                    v = value.getDouble();
                    break;
                case PropertyType.DECIMAL:
                    v = value.getDecimal().toString();
                    break;
                case PropertyType.DATE:
                    v = value.getDate().getTime();
                    break;
                case PropertyType.BOOLEAN:
                    v = value.getBoolean();
                    break;
                case PropertyType.BINARY:
                case PropertyType.NAME:
                case PropertyType.PATH:
                case PropertyType.REFERENCE:
                case PropertyType.WEAKREFERENCE:
                case PropertyType.URI:
                    throw new UnsupportedOperationException(
                            "Value type not suppored");
            }
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
        put("value", v);
    }

    @Override
    public boolean containsField(String s) {
        if (s.equals("value") && value != null) {
            return true;
        } else if (s.equals("propertyType") && propertyType != null) {
            return true;
        }
        return super.containsField(s);
    }

    @Override
    public Object get(String key) {
        if (key.equals("value")) {
            return value;
        } else if (key.equals("propertyType")) {
            return propertyType;
        }
        return super.get(key);
    }

    @Override
    public Set<String> keySet() {
        Set<String> set = super.keySet();
        set.add("value");
        set.add("propertyType");
        return set;
    }

    @Override
    public Object put(String key, Object v) {
        if (key.equals("value")) {
            value = v;
            return v;
        } else if (key.equals("propertyType")) {
            propertyType = (Integer) v;
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
        } else if (key.equals("propertyType")) {
            ret = propertyType;
            propertyType = null;
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
        ret.put("propertyType", propertyType);
        return ret;
    }

    public Value getValue() {
        return new ValueImpl(value, propertyType);
    }

}
