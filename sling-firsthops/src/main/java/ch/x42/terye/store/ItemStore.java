package ch.x42.terye.store;

import java.util.HashMap;
import java.util.Map;

import ch.x42.terye.ItemImpl;
import ch.x42.terye.NodeImpl;
import ch.x42.terye.Path;

public class ItemStore {

    private static ItemStore instance;

    private Map<String, ItemImpl> items;

    private ItemStore() {
        items = new HashMap<String, ItemImpl>();
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

}
