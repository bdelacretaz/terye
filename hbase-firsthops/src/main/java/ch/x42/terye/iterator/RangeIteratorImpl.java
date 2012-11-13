package ch.x42.terye.iterator;

import java.util.Collection;
import java.util.Iterator;

import javax.jcr.RangeIterator;

import ch.x42.terye.ItemImpl;

public abstract class RangeIteratorImpl implements RangeIterator {

    private Iterator<? extends ItemImpl> iterator;
    private long size = -1L;
    private long position = 0L;

    public RangeIteratorImpl(Iterable<? extends ItemImpl> items) {
        this.iterator = items.iterator();
        if (items instanceof Collection) {
            size = ((Collection<? extends ItemImpl>) items).size();
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
            next();
            skipNum--;
        }
    }

}
