package ch.x42.terye.store;

import java.util.Iterator;
import java.util.NavigableMap;
import java.util.TreeMap;

import javax.jcr.RepositoryException;

import ch.x42.terye.ItemImpl;
import ch.x42.terye.NodeImpl;
import ch.x42.terye.Path;
import ch.x42.terye.store.ChangeLog.AddOperation;
import ch.x42.terye.store.ChangeLog.ModifyOperation;
import ch.x42.terye.store.ChangeLog.Operation;
import ch.x42.terye.store.ChangeLog.RemoveOperation;

public class ItemStore {

    private static ItemStore instance;

    private NavigableMap<String, ItemImpl> items;

    private ItemStore() {
        items = new TreeMap<String, ItemImpl>();
        // create root node
        items.put(Path.ROOT, new NodeImpl(null, new Path(Path.ROOT)));
    }

    public static synchronized ItemStore getInstance() {
        if (instance == null) {
            instance = new ItemStore();
        }

        return instance;
    }

    public synchronized ItemImpl load(String id, ItemType type) {
        ItemImpl item = items.get(id);
        if (item != null && (type == null || item.getItemType().equals(type))) {
            return item;
        }

        return null;
    }

    public synchronized void store(ItemImpl item) throws RepositoryException {
        items.put(item.getPath(), item);
    }

    public synchronized void delete(String id) {
        // delete the item
        ItemImpl item = items.remove(id);
        // if it is a node...
        if (item == null || !item.isNode()) {
            return;
        }
        // ...delete its descendants
        Iterator<String> iterator = items.tailMap(id, true).navigableKeySet()
                .iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            if (!key.startsWith(id)) {
                break;
            }
            iterator.remove();
        }
    }

    public synchronized void persist(ChangeLog log) throws RepositoryException {
        Iterator<Operation> iterator = log.iterator();
        while (iterator.hasNext()) {
            Operation op = iterator.next();
            if (op instanceof AddOperation) {
                store(op.getItem());
            } else if (op instanceof ModifyOperation) {
                store(op.getItem());
            } else if (op instanceof RemoveOperation) {
                delete(op.getItem().getPath());
            }
        }
    }

}
