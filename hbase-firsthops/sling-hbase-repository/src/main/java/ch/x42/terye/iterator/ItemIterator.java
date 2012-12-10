package ch.x42.terye.iterator;

import java.util.Collection;

import javax.jcr.RepositoryException;

import ch.x42.terye.ItemManager;
import ch.x42.terye.path.Path;

public abstract class ItemIterator extends RangeIteratorImpl<String> {

    private ItemManager itemManager;
    private Path basePath;

    public ItemIterator(ItemManager itemManager, Path basePath,
            Iterable<String> itemNames) {
        super(itemNames);
        this.itemManager = itemManager;
        this.basePath = basePath;
        if (itemNames instanceof Collection) {
            size = ((Collection<String>) itemNames).size();
        }
    }

    @Override
    public Object next() {
        String name = (String) super.next();
        Path path = basePath.resolve(name);
        try {
            // XXX: node iterator might return a property or other way round...
            return itemManager.getItem(path);
        } catch (RepositoryException e) {
            // XXX: better handling?
            throw new RuntimeException("Stale reference to child node " + path);
        }
    }

    public void remove() {
        throw new UnsupportedOperationException("remove");
    }

}
