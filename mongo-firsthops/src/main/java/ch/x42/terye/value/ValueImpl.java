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
    }

    private void validate(int expectedType) throws ValueFormatException {
        if (getType() != expectedType) {
            throw new ValueFormatException("Expected a "
                    + PropertyType.nameFromValue(expectedType) + "but found a "
                    + PropertyType.nameFromValue(getType()));
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
        if (value instanceof Calendar) {
            return (Calendar) value;
        } else if (value instanceof Date) {
            Calendar cal = Calendar.getInstance();
            cal.setTime((Date) value);
            return cal;
        }
        throw new ValueFormatException();
    }

    @Override
    public BigDecimal getDecimal() throws ValueFormatException,
            RepositoryException {
        validate(PropertyType.DECIMAL);
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        } else if (value instanceof String) {
            return new BigDecimal((String) value);
        }

        throw new ValueFormatException();
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