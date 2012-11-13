package ch.x42.terye.iterator;

import javax.jcr.Node;
import javax.jcr.NodeIterator;

import ch.x42.terye.NodeImpl;

public class NodeIteratorImpl extends RangeIteratorImpl implements NodeIterator {

    public NodeIteratorImpl(Iterable<NodeImpl> nodes) {
        super(nodes);
    }

    @Override
    public Node nextNode() {
        return (Node) next();
    }

}
