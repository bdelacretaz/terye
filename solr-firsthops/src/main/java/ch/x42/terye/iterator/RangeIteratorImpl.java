package ch.x42.terye.iterator;

import java.util.Collection;
import java.util.Iterator;

import javax.jcr.PathNotFoundException;
import javax.jcr.RangeIterator;

import ch.x42.terye.ItemManager;
import ch.x42.terye.Path;
import ch.x42.terye.persistence.ItemType;

public abstract class RangeIteratorImpl implements RangeIterator {

    private ItemManager itemManager;
    private Iterator<String> iterator;
    private ItemType type;
    private long size = -1L;
    private long position = 0L;

    public RangeIteratorImpl(ItemManager itemManager, Iterable<String> items,
            ItemType type) {
        this.itemManager = itemManager;
        this.iterator = items.iterator();
        this.type = type;
        if (items instanceof Collection) {
            size = ((Collection<String>) items).size();
        }
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public Object next() {
        position++;
        String item = iterator.next();
        try {
            return itemManager.getItem(new Path(item), type);
        } catch (PathNotFoundException e) {
            return null;
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
        while (skipNum > 0) {
            next();
            skipNum--;
        }
    }

}
