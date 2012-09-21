package ch.x42.terye.iterator;

import java.util.Collection;
import java.util.Iterator;

import javax.jcr.RangeIterator;

public class RangeIteratorImpl<T> implements RangeIterator {

    private Iterator<T> iterator;
    private long size = -1L;
    private long position = 0L;

    public RangeIteratorImpl(Iterable<T> iterable) {
        iterator = iterable.iterator();
        if (iterable instanceof Collection) {
            size = ((Collection<T>) iterable).size();
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
        while (skipNum > 0) {
            next();
            skipNum--;
        }
    }

}
