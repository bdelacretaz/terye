package ch.x42.terye.nodetype;

import javax.jcr.RepositoryException;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.nodetype.InvalidNodeTypeDefinitionException;
import javax.jcr.nodetype.NoSuchNodeTypeException;
import javax.jcr.nodetype.NodeDefinitionTemplate;
import javax.jcr.nodetype.NodeType;
import javax.jcr.nodetype.NodeTypeDefinition;
import javax.jcr.nodetype.NodeTypeExistsException;
import javax.jcr.nodetype.NodeTypeIterator;
import javax.jcr.nodetype.NodeTypeManager;
import javax.jcr.nodetype.NodeTypeTemplate;
import javax.jcr.nodetype.PropertyDefinitionTemplate;

public class NodeTypeManagerImpl implements NodeTypeManager {

    @Override
    public NodeType getNodeType(String nodeTypeName)
            throws NoSuchNodeTypeException, RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean hasNodeType(String name) throws RepositoryException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public NodeTypeIterator getAllNodeTypes() throws RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public NodeTypeIterator getPrimaryNodeTypes() throws RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public NodeTypeIterator getMixinNodeTypes() throws RepositoryException {
        return new NodeTypeIteratorImpl();
    }

    @Override
    public NodeTypeTemplate createNodeTypeTemplate()
            throws UnsupportedRepositoryOperationException, RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public NodeTypeTemplate createNodeTypeTemplate(NodeTypeDefinition ntd)
            throws UnsupportedRepositoryOperationException, RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public NodeDefinitionTemplate createNodeDefinitionTemplate()
            throws UnsupportedRepositoryOperationException, RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PropertyDefinitionTemplate createPropertyDefinitionTemplate()
            throws UnsupportedRepositoryOperationException, RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public NodeType registerNodeType(NodeTypeDefinition ntd, boolean allowUpdate)
            throws InvalidNodeTypeDefinitionException, NodeTypeExistsException,
            UnsupportedRepositoryOperationException, RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public NodeTypeIterator registerNodeTypes(NodeTypeDefinition[] ntds,
            boolean allowUpdate) throws InvalidNodeTypeDefinitionException,
            NodeTypeExistsException, UnsupportedRepositoryOperationException,
            RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void unregisterNodeType(String name)
            throws UnsupportedRepositoryOperationException,
            NoSuchNodeTypeException, RepositoryException {
        // TODO Auto-generated method stub

    }

    @Override
    public void unregisterNodeTypes(String[] names)
            throws UnsupportedRepositoryOperationException,
            NoSuchNodeTypeException, RepositoryException {
        // TODO Auto-generated method stub

    }

}
