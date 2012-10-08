package ch.x42.terye;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Calendar;

import javax.jcr.Binary;
import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Value;
import javax.jcr.ValueFormatException;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.nodetype.PropertyDefinition;
import javax.jcr.version.VersionException;

import ch.x42.terye.persistence.PropertyState;

public class PropertyImpl extends ItemImpl implements Property {

    protected PropertyImpl(SessionImpl session, PropertyState state) {
        super(session, state);
    }

    @Override
    public Binary getBinary() throws ValueFormatException, RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean getBoolean() throws ValueFormatException,
            RepositoryException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Calendar getDate() throws ValueFormatException, RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BigDecimal getDecimal() throws ValueFormatException,
            RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PropertyDefinition getDefinition() throws RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public double getDouble() throws ValueFormatException, RepositoryException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long getLength() throws ValueFormatException, RepositoryException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long[] getLengths() throws ValueFormatException, RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getLong() throws ValueFormatException, RepositoryException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Node getNode() throws ItemNotFoundException, ValueFormatException,
            RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Property getProperty() throws ItemNotFoundException,
            ValueFormatException, RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PropertyState getState() {
        return (PropertyState) super.getState();
    }

    @Override
    public InputStream getStream() throws ValueFormatException,
            RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getString() throws ValueFormatException, RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getType() throws RepositoryException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Value getValue() throws ValueFormatException, RepositoryException {
        return getState().getValue();
    }

    @Override
    public Value[] getValues() throws ValueFormatException, RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isMultiple() throws RepositoryException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setValue(BigDecimal value) throws ValueFormatException,
            VersionException, LockException, ConstraintViolationException,
            RepositoryException {
        setValue(getSession().getValueFactory().createValue(value));
    }

    @Override
    public void setValue(Binary value) throws ValueFormatException,
            VersionException, LockException, ConstraintViolationException,
            RepositoryException {
        // TODO Auto-generated method stub

    }

    @Override
    public void setValue(boolean value) throws ValueFormatException,
            VersionException, LockException, ConstraintViolationException,
            RepositoryException {
        setValue(getSession().getValueFactory().createValue(value));
    }

    @Override
    public void setValue(Calendar value) throws ValueFormatException,
            VersionException, LockException, ConstraintViolationException,
            RepositoryException {
        setValue(getSession().getValueFactory().createValue(value));
    }

    @Override
    public void setValue(double value) throws ValueFormatException,
            VersionException, LockException, ConstraintViolationException,
            RepositoryException {
        setValue(getSession().getValueFactory().createValue(value));
    }

    @Override
    public void setValue(InputStream value) throws ValueFormatException,
            VersionException, LockException, ConstraintViolationException,
            RepositoryException {
        // TODO Auto-generated method stub

    }

    @Override
    public void setValue(long value) throws ValueFormatException,
            VersionException, LockException, ConstraintViolationException,
            RepositoryException {
        setValue(getSession().getValueFactory().createValue(value));
    }

    @Override
    public void setValue(Node value) throws ValueFormatException,
            VersionException, LockException, ConstraintViolationException,
            RepositoryException {
        // TODO Auto-generated method stub

    }

    @Override
    public void setValue(String value) throws ValueFormatException,
            VersionException, LockException, ConstraintViolationException,
            RepositoryException {
        setValue(getSession().getValueFactory().createValue(value));
    }

    @Override
    public void setValue(String[] values) throws ValueFormatException,
            VersionException, LockException, ConstraintViolationException,
            RepositoryException {
        // TODO Auto-generated method stub

    }

    @Override
    public void setValue(Value value) throws ValueFormatException,
            VersionException, LockException, ConstraintViolationException,
            RepositoryException {
        getItemManager().updateProperty(path, value);
    }

    @Override
    public void setValue(Value[] values) throws ValueFormatException,
            VersionException, LockException, ConstraintViolationException,
            RepositoryException {
        // TODO Auto-generated method stub

    }

}
