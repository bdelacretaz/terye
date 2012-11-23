package ch.x42.terye.iterator;

import java.util.Collection;
import java.util.Iterator;

import javax.jcr.RangeIterator;
import javax.jcr.RepositoryException;

import ch.x42.terye.ItemManager;
import ch.x42.terye.persistence.id.ItemId;

public abstract class RangeIteratorImpl implements RangeIterator {

    private ItemManager itemManager;
    private Iterator<? extends ItemId> iterator;
    private long size = -1L;
    private long position = 0L;

    public RangeIteratorImpl(ItemManager itemManager,
            Iterable<? extends ItemId> itemIds) {
        this.itemManager = itemManager;
        this.iterator = itemIds.iterator();
        if (itemIds instanceof Collection) {
            size = ((Collection<? extends ItemId>) itemIds).size();
        }
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public Object next() {
        position++;
        ItemId id = iterator.next();
        try {
            return itemManager.getItem(id);
        } catch (RepositoryException e) {
            // XXX: better handling?
            throw new RuntimeException("Stale reference to child node " + id
                    + ". Node has been removed in this session");
        }
    }

    @Override
    public void remove() {
        iterator.remove();
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
        while (skipNum > 0 && iterator.hasNext()) {
            iterator.next();
            position++;
            skipNum--;
        }
    }

}
