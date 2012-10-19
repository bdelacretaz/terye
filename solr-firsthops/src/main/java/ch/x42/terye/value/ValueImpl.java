package ch.x42.terye.value;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import javax.jcr.Binary;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import javax.jcr.Value;
import javax.jcr.ValueFormatException;

public class ValueImpl implements Value {

    private Object value;
    private int type;

    public ValueImpl(Object value, int type) {
        this.value = value;
        this.type = type;

        // when called from PropertyState, convert back to...
        if (type == PropertyType.DATE && value instanceof Date) {
            // ... Calendar
            this.value = Calendar.getInstance();
            ((Calendar) this.value).setTime((Date) value);
        } else if (type == PropertyType.DECIMAL && value instanceof String) {
            // ... BigDecimal
            this.value = new BigDecimal((String) value);
        }
    }

    private void validate(int expectedType) throws ValueFormatException {
        if (getType() != expectedType) {
            throw new ValueFormatException("Expected a "
                    + PropertyType.nameFromValue(expectedType)
                    + " but found a " + PropertyType.nameFromValue(getType()));
        }
    }

    @Override
    public Binary getBinary() throws RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean getBoolean() throws ValueFormatException,
            RepositoryException {
        validate(PropertyType.BOOLEAN);
        return (Boolean) value;
    }

    @Override
    public Calendar getDate() throws ValueFormatException, RepositoryException {
        validate(PropertyType.DATE);
        return (Calendar) value;
    }

    @Override
    public BigDecimal getDecimal() throws ValueFormatException,
            RepositoryException {
        validate(PropertyType.DECIMAL);
        return (BigDecimal) value;
    }

    @Override
    public double getDouble() throws ValueFormatException, RepositoryException {
        validate(PropertyType.DOUBLE);
        return (Double) value;
    }

    @Override
    public long getLong() throws ValueFormatException, RepositoryException {
        validate(PropertyType.LONG);
        return (Long) value;
    }

    @Override
    public InputStream getStream() throws RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getString() throws ValueFormatException,
            IllegalStateException, RepositoryException {
        validate(PropertyType.STRING);
        return (String) value;
    }

    @Override
    public int getType() {
        return type;
    }

    public Object getObject() {
        return value;
    }
}
