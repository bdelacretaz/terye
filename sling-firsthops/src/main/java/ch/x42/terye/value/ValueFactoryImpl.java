package ch.x42.terye.value;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Calendar;

import javax.jcr.Binary;
import javax.jcr.Node;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import javax.jcr.Value;
import javax.jcr.ValueFactory;
import javax.jcr.ValueFormatException;

public class ValueFactoryImpl implements ValueFactory {

    public ValueFactoryImpl() {

    }

    @Override
    public Binary createBinary(InputStream stream) throws RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Value createValue(BigDecimal value) {
        return new ValueImpl(value, PropertyType.DECIMAL);
    }

    @Override
    public Value createValue(Binary value) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Value createValue(boolean value) {
        return new ValueImpl(value, PropertyType.BOOLEAN);
    }

    @Override
    public Value createValue(Calendar value) {
        return new ValueImpl(value, PropertyType.DATE);
    }

    @Override
    public Value createValue(double value) {
        return new ValueImpl(value, PropertyType.DOUBLE);
    }

    @Override
    public Value createValue(InputStream value) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Value createValue(long value) {
        return new ValueImpl(value, PropertyType.LONG);
    }

    @Override
    public Value createValue(Node value, boolean weak)
            throws RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Value createValue(Node value) throws RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Value createValue(String value, int type)
            throws ValueFormatException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Value createValue(String value) {
        return new ValueImpl(value, PropertyType.STRING);
    }

}