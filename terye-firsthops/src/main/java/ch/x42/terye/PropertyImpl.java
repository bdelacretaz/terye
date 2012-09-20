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


public class PropertyImpl extends ItemImpl implements Property {
    
    private Object value;

    public PropertyImpl(Path path, Object value) {
        // TODO: validate name
        super(path);
        this.value = value;
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
    public InputStream getStream() throws ValueFormatException,
            RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getString() throws ValueFormatException, RepositoryException {
        if(!(value instanceof String)) {
            throw new ValueFormatException();
        }
        return (String) value;
    }

    @Override
    public int getType() throws RepositoryException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Value getValue() throws ValueFormatException, RepositoryException {
        // TODO Auto-generated method stub
        return null;
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
    public void setValue(Value arg0) throws ValueFormatException,
            VersionException, LockException, ConstraintViolationException,
            RepositoryException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setValue(Value[] arg0) throws ValueFormatException,
            VersionException, LockException, ConstraintViolationException,
            RepositoryException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setValue(String arg0) throws ValueFormatException,
            VersionException, LockException, ConstraintViolationException,
            RepositoryException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setValue(String[] arg0) throws ValueFormatException,
            VersionException, LockException, ConstraintViolationException,
            RepositoryException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setValue(InputStream arg0) throws ValueFormatException,
            VersionException, LockException, ConstraintViolationException,
            RepositoryException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setValue(Binary arg0) throws ValueFormatException,
            VersionException, LockException, ConstraintViolationException,
            RepositoryException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setValue(long arg0) throws ValueFormatException,
            VersionException, LockException, ConstraintViolationException,
            RepositoryException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setValue(double arg0) throws ValueFormatException,
            VersionException, LockException, ConstraintViolationException,
            RepositoryException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setValue(BigDecimal arg0) throws ValueFormatException,
            VersionException, LockException, ConstraintViolationException,
            RepositoryException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setValue(Calendar arg0) throws ValueFormatException,
            VersionException, LockException, ConstraintViolationException,
            RepositoryException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setValue(boolean arg0) throws ValueFormatException,
            VersionException, LockException, ConstraintViolationException,
            RepositoryException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setValue(Node arg0) throws ValueFormatException,
            VersionException, LockException, ConstraintViolationException,
            RepositoryException {
        // TODO Auto-generated method stub
        
    }

}