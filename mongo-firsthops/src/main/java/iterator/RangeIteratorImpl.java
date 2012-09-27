package iterator;

import java.util.Collection;
import java.util.Iterator;

import javax.jcr.RangeIterator;

import ch.x42.terye.ItemManager;

public class RangeIteratorImpl implements RangeIterator {

    private ItemManager itemManager;
    private Iterator<String> iterator;
    private long size = -1L;
    private long position = 0L;

    public RangeIteratorImpl(ItemManager itemManager, Iterable<String> items) {
        this.itemManager = itemManager;
        iterator = items.iterator();
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
        return itemManager.getNode(item);
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
