package ch.x42.terye.iterator;

import javax.jcr.Node;
import javax.jcr.NodeIterator;

import ch.x42.terye.ItemManager;
import ch.x42.terye.persistence.id.NodeId;

public class NodeIteratorImpl extends RangeIteratorImpl implements NodeIterator {

    public NodeIteratorImpl(ItemManager itemManager, Iterable<NodeId> nodeIds) {
        super(itemManager, nodeIds);
    }

    @Override
    public Node nextNode() {
        return (Node) next();
    }

}
