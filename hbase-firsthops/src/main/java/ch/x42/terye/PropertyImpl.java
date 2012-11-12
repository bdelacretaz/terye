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
import ch.x42.terye.value.ValueFactoryImpl;
import ch.x42.terye.value.ValueImpl;

public class PropertyImpl extends ItemImpl implements Property {

    /**
     * Value representation of the property state
     */
    private ValueImpl value;

    /**
     * 'value' must correspond to 'state' represented as a value object
     */
    public PropertyImpl(SessionImpl session, PropertyState state,
            ValueImpl value) {
        // TODO: validate name
        super(session, state);
        this.value = value;
    }

    public PropertyImpl(SessionImpl session, PropertyState state)
            throws RepositoryException {
        // TODO: validate name
        super(session, state);
        this.value = ((ValueFactoryImpl) session.getValueFactory())
                .createValue(state.getType(), state.getBytes());
    }

    public void setValueInternal(ValueImpl value) throws RepositoryException {
        this.value = value;
        this.state = new PropertyState(getState().getId(), value);
        getItemManager().propertyUpdated(this);
    }

    @Override
    public Binary getBinary() throws ValueFormatException, RepositoryException {
        return value.getBinary();
    }

    @Override
    public boolean getBoolean() throws ValueFormatException,
            RepositoryException {
        return value.getBoolean();
    }

    @Override
    public Calendar getDate() throws ValueFormatException, RepositoryException {
        return value.getDate();
    }

    @Override
    public BigDecimal getDecimal() throws ValueFormatException,
            RepositoryException {
        return value.getDecimal();
    }

    @Override
    public PropertyDefinition getDefinition() throws RepositoryException {
        return new PropertyDefinitionImpl();
    }

    @Override
    public double getDouble() throws ValueFormatException, RepositoryException {
        return value.getDouble();
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
        return value.getLong();
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
    public InputStream getStream() throws ValueFormatException,
            RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getString() throws ValueFormatException, RepositoryException {
        return value.getString();
    }

    @Override
    public int getType() throws RepositoryException {
        return value.getType();
    }

    @Override
    public Value getValue() throws ValueFormatException, RepositoryException {
        return value;
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
        setValueInternal((ValueImpl) getSession().getValueFactory()
                .createValue(value));
    }

    @Override
    public void setValue(Binary value) throws ValueFormatException,
            VersionException, LockException, ConstraintViolationException,
            RepositoryException {
        setValueInternal((ValueImpl) getSession().getValueFactory()
                .createValue(value));
    }

    @Override
    public void setValue(boolean value) throws ValueFormatException,
            VersionException, LockException, ConstraintViolationException,
            RepositoryException {
        setValueInternal((ValueImpl) getSession().getValueFactory()
                .createValue(value));
    }

    @Override
    public void setValue(Calendar value) throws ValueFormatException,
            VersionException, LockException, ConstraintViolationException,
            RepositoryException {
        setValueInternal((ValueImpl) getSession().getValueFactory()
                .createValue(value));
    }

    @Override
    public void setValue(double value) throws ValueFormatException,
            VersionException, LockException, ConstraintViolationException,
            RepositoryException {
        setValueInternal((ValueImpl) getSession().getValueFactory()
                .createValue(value));
    }

    @SuppressWarnings("deprecation")
    @Override
    public void setValue(InputStream value) throws ValueFormatException,
            VersionException, LockException, ConstraintViolationException,
            RepositoryException {
        setValueInternal((ValueImpl) getSession().getValueFactory()
                .createValue(value));
    }

    @Override
    public void setValue(long value) throws ValueFormatException,
            VersionException, LockException, ConstraintViolationException,
            RepositoryException {
        setValueInternal((ValueImpl) getSession().getValueFactory()
                .createValue(value));
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
        setValueInternal((ValueImpl) getSession().getValueFactory()
                .createValue(value));
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
        // XXX: cast might not be valid
        setValueInternal((ValueImpl) value);
    }

    @Override
    public void setValue(Value[] values) throws ValueFormatException,
            VersionException, LockException, ConstraintViolationException,
            RepositoryException {
        // TODO Auto-generated method stub

    }

}
