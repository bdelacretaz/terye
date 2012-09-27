package iterator;

import javax.jcr.Node;
import javax.jcr.NodeIterator;

import ch.x42.terye.ItemManager;

public class NodeIteratorImpl extends RangeIteratorImpl implements NodeIterator {

    public NodeIteratorImpl(ItemManager itemManager, Iterable<String> nodes) {
        super(itemManager, nodes);
    }

    @Override
    public Node nextNode() {
        return (Node) next();
    }

}
