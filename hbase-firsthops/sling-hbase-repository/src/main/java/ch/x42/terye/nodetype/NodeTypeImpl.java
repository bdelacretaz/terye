package ch.x42.terye.nodetype;

import javax.jcr.Value;
import javax.jcr.nodetype.NodeDefinition;
import javax.jcr.nodetype.NodeType;
import javax.jcr.nodetype.NodeTypeIterator;
import javax.jcr.nodetype.PropertyDefinition;


public class NodeTypeImpl implements NodeType {

    private String name;
    
    public NodeTypeImpl(String name) {
        this.name = name;
    }
    
    @Override
    public String getName() {
        return name;
    }

    @Override
    public String[] getDeclaredSupertypeNames() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isAbstract() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isMixin() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean hasOrderableChildNodes() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isQueryable() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String getPrimaryItemName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PropertyDefinition[] getDeclaredPropertyDefinitions() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public NodeDefinition[] getDeclaredChildNodeDefinitions() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public NodeType[] getSupertypes() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public NodeType[] getDeclaredSupertypes() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public NodeTypeIterator getSubtypes() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public NodeTypeIterator getDeclaredSubtypes() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isNodeType(String nodeTypeName) {
        return name.equals(nodeTypeName);
    }

    @Override
    public PropertyDefinition[] getPropertyDefinitions() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public NodeDefinition[] getChildNodeDefinitions() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean canSetProperty(String propertyName, Value value) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean canSetProperty(String propertyName, Value[] values) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean canAddChildNode(String childNodeName) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean canAddChildNode(String childNodeName, String nodeTypeName) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean canRemoveItem(String itemName) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean canRemoveNode(String nodeName) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean canRemoveProperty(String propertyName) {
        // TODO Auto-generated method stub
        return false;
    }

}
