package ch.x42.terye;

import java.util.Iterator;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;

public class NodeIteratorImpl implements NodeIterator {

    private Iterator<NodeImpl> iterator;
    private int size;
    private int position;

    public NodeIteratorImpl(List<NodeImpl> nodes) {
        iterator = nodes.iterator();
        size = nodes.size();
        position = 0;
    }

    @Override
    public long getPosition() {
        return position;
    }

    @Override
    public long getSize() {
        return size;
    }

    @Override
    public void skip(long skipNum) {
        if (skipNum < 0) {
            throw new IllegalArgumentException("Parameter must be non-negative");
        }
        while (skipNum > 0) {
            next();
            skipNum--;
        }
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public Object next() {
        position++;
        return iterator.next();
    }

    @Override
    public void remove() {
        iterator.remove();
    }

    @Override
    public Node nextNode() {
        return (Node) next();
    }

}
