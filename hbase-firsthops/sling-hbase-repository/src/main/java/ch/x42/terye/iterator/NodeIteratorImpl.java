package ch.x42.terye.iterator;

import javax.jcr.Node;
import javax.jcr.NodeIterator;

import ch.x42.terye.ItemManager;
import ch.x42.terye.path.Path;

public class NodeIteratorImpl extends ItemIterator implements NodeIterator {

    public NodeIteratorImpl(ItemManager itemManager, Path basePath,
            Iterable<String> nodeNames) {
        super(itemManager, basePath, nodeNames);
    }

    @Override
    public Node nextNode() {
        return (Node) next();
    }

}
