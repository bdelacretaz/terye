package ch.x42.terye.iterator;

import java.util.Iterator;

import javax.jcr.RangeIterator;

public abstract class RangeIteratorImpl<T> implements RangeIterator {

    protected final Iterator<T> iterator;
    protected long position = 0L;
    protected long size = -1L;

    public RangeIteratorImpl(Iterable<T> iterable) {
        this.iterator = iterable.iterator();
        this.position = 0;
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

    @Override
    public long getSize() {
        return size;
    }

    @Override
    public long getPosition() {
        return position;
    }

}
