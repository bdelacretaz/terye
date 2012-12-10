package ch.x42.terye.persistence;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import ch.x42.terye.persistence.id.ItemId;
import ch.x42.terye.persistence.id.NodeId;

public class NodeState extends ItemState {

    private String nodeTypeName;
    private List<String> childNodes;
    private List<String> properties;

    public NodeState(NodeId id, String nodeTypeName, List<String> childNodes,
            List<String> properties) {
        super(id);
        this.nodeTypeName = nodeTypeName;
        this.childNodes = childNodes;
        this.properties = properties;
    }

    public NodeState(NodeId id, String nodeTypeName) {
        this(id, nodeTypeName, new CopyOnWriteArrayList<String>(),
                new LinkedList<String>());
    }

    @Override
    public NodeId getId() {
        return (NodeId) super.getId();
    }

    public String getNodeTypeName() {
        return nodeTypeName;
    }

    public List<String> getChildNodes() {
        return childNodes;
    }

    public List<String> getProperties() {
        return properties;
    }

    @Override
    public boolean isNode() {
        return true;
    }

    @Override
    public NodeState clone(ItemId newId) {
        return new NodeState((NodeId) newId, getNodeTypeName(),
                new CopyOnWriteArrayList<String>(getChildNodes()),
                new LinkedList<String>(getProperties()));
    }

}
