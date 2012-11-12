package ch.x42.terye.value;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.jcr.Binary;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import javax.jcr.Value;
import javax.jcr.ValueFormatException;

import org.apache.hadoop.hbase.util.Bytes;

public class ValueImpl implements Value {

    private Object value;
    private int type;

    protected ValueImpl(Object value, int type) {
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
        validate(PropertyType.BINARY);
        return (Binary) value;
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
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis((Long) value);
        return cal;
    }

    @Override
    public BigDecimal getDecimal() throws ValueFormatException,
            RepositoryException {
        validate(PropertyType.DECIMAL);
        return ((BigDecimal) value);
    }

    @Override
    public double getDouble() throws ValueFormatException, RepositoryException {
        validate(PropertyType.DOUBLE);
        return (Double) value;
    }

    @Override
    public long getLong() throws ValueFormatException, RepositoryException {
        switch (getType()) {
            case PropertyType.LONG:
            case PropertyType.DATE:
                return (Long) value;
        }
        throw new ValueFormatException("Couldn't convert value to long");
    }

    @Override
    public InputStream getStream() throws RepositoryException {
        validate(PropertyType.BINARY);
        return ((Binary) value).getStream();
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

    public byte[] getBytes() {
        // XXX: getType or instanceof?
        switch (getType()) {
            case PropertyType.BINARY:
                return ((BinaryImpl) value).getByteArray();
            case PropertyType.BOOLEAN:
                return Bytes.toBytes((Boolean) value);
            case PropertyType.DATE:
            case PropertyType.LONG:
                return Bytes.toBytes((Long) value);
            case PropertyType.DECIMAL:
                return Bytes.toBytes((BigDecimal) value);
            case PropertyType.DOUBLE:
                return Bytes.toBytes((Double) value);
            case PropertyType.STRING:
                return Bytes.toBytes((String) value);
        }
        return new byte[0];
    }
}
