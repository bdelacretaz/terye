package ch.x42.terye.value;

import java.io.ByteArrayInputStream;
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

    @Override
    public Binary getBinary() throws RepositoryException {
        if (PropertyType.BINARY == type) {
            return (Binary) value;
        } else {
            // XXX: todo: convert getString() to binary
            throw new ValueFormatException(
                    "Could not convert value to a binary");
        }
    }

    @Override
    public boolean getBoolean() throws ValueFormatException,
            RepositoryException {
        if (PropertyType.BOOLEAN == type) {
            return (Boolean) value;
        } else {
            return Boolean.parseBoolean(getString());
        }
    }

    @Override
    public Calendar getDate() throws ValueFormatException, RepositoryException {
        if (PropertyType.DATE == type || PropertyType.LONG == type) {
            Calendar calendar = new GregorianCalendar();
            calendar.setTimeInMillis((Long) value);
            return calendar;
        } else if (PropertyType.DOUBLE == type) {
            Calendar calendar = new GregorianCalendar();
            calendar.setTimeInMillis(((Double) value).longValue());
            return calendar;
        } else if (PropertyType.DECIMAL == type) {
            Calendar calendar = new GregorianCalendar();
            calendar.setTimeInMillis(((BigDecimal) value).longValue());
            return calendar;
        } else {
            // XXX: todo: parse date from getString()
            throw new ValueFormatException("Could not convert value to a date");
        }
    }

    @Override
    public BigDecimal getDecimal() throws ValueFormatException,
            RepositoryException {
        if (PropertyType.DECIMAL == type) {
            return (BigDecimal) value;
        } else if (PropertyType.DOUBLE == type) {
            return new BigDecimal((Double) value);
        } else if (PropertyType.LONG == type || PropertyType.DATE == type) {
            return new BigDecimal((Long) value);
        } else {
            try {
                return new BigDecimal(getString());
            } catch (NumberFormatException e) {
                throw new ValueFormatException(
                        "Could not convert value to a decimal");
            }
        }
    }

    @Override
    public double getDouble() throws ValueFormatException, RepositoryException {
        if (PropertyType.DOUBLE == type) {
            return (Double) value;
        } else if (PropertyType.LONG == type || PropertyType.DATE == type) {
            return ((Long) value).doubleValue();
        } else if (PropertyType.DECIMAL == type) {
            return ((BigDecimal) value).doubleValue();
        } else {
            try {
                return Double.parseDouble(getString());
            } catch (NumberFormatException ex) {
                throw new ValueFormatException(
                        "Could not convert value to a double");
            }
        }
    }

    @Override
    public long getLong() throws ValueFormatException, RepositoryException {
        if (PropertyType.LONG == type || PropertyType.DATE == type) {
            return (Long) value;
        } else if (PropertyType.DOUBLE == type) {
            return ((Double) value).longValue();
        } else if (PropertyType.DECIMAL == type) {
            return ((BigDecimal) value).longValue();
        } else {
            try {
                return Long.parseLong(getString());
            } catch (NumberFormatException ex) {
                throw new ValueFormatException(
                        "Could not convert value to a long");
            }
        }
    }

    @Override
    public InputStream getStream() throws RepositoryException {
        if (PropertyType.BINARY == type) {
            return ((Binary) value).getStream();
        } else {
            return new ByteArrayInputStream(getString().getBytes());
        }
    }

    @Override
    public String getString() throws ValueFormatException,
            IllegalStateException, RepositoryException {
        if (type == PropertyType.BINARY) {
            // XXX: convert binary to string
            throw new ValueFormatException(
                    "Could not convert value to a string");
        } else {
            return value.toString();
        }
    }

    @Override
    public int getType() {
        return type;
    }

    public Object getObject() {
        return value;
    }

    public byte[] getBytes() {
        if (value == null) {
            return null;
        }

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

        return null;
    }
}
