package ch.x42.terye.iterator;

import java.util.Collection;

import javax.jcr.RepositoryException;

import ch.x42.terye.ItemManager;
import ch.x42.terye.persistence.id.ItemId;

public abstract class ItemIteratorImpl extends RangeIteratorImpl<ItemId> {

    private ItemManager itemManager;

    @SuppressWarnings("unchecked")
    public ItemIteratorImpl(ItemManager itemManager,
            Iterable<? extends ItemId> itemIds) {
        super((Iterable<ItemId>) itemIds);
        this.itemManager = itemManager;
        if (itemIds instanceof Collection) {
            size = ((Collection<? extends ItemId>) itemIds).size();
        }
    }

    @Override
    public Object next() {
        ItemId id = (ItemId) super.next();
        try {
            return itemManager.getItem(id);
        } catch (RepositoryException e) {
            // XXX: better handling?
            throw new RuntimeException("Stale reference to child node " + id);
        }
    }

}
