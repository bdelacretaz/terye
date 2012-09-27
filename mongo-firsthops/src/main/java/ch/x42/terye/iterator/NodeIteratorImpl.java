package ch.x42.terye.iterator;

import javax.jcr.Node;
import javax.jcr.NodeIterator;

import ch.x42.terye.ItemManager;
import ch.x42.terye.persistence.ItemType;

public class NodeIteratorImpl extends RangeIteratorImpl implements NodeIterator {

    public NodeIteratorImpl(ItemManager itemManager, Iterable<String> nodes) {
        super(itemManager, nodes, ItemType.NODE);
    }

    @Override
    public Node nextNode() {
        return (Node) next();
    }

}
