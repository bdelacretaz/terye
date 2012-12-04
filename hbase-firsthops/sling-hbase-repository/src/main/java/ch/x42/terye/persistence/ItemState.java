package ch.x42.terye.persistence;

import ch.x42.terye.path.Path;
import ch.x42.terye.path.PathFactory;
import ch.x42.terye.persistence.id.ItemId;
import ch.x42.terye.persistence.id.NodeId;

public abstract class ItemState {

    private final ItemId id;
    private final NodeId parentId;
    private Path path;

    public ItemState(ItemId id, NodeId parentId) {
        this.id = id;
        this.parentId = parentId;
    }

    public ItemId getId() {
        return id;
    }

    public NodeId getParentId() {
        return parentId;
    }

    public String getPath() {
        return id.toString();
    }

    public Path getPathInternal() {
        if (path == null) {
            path = PathFactory.create(getPath());
        }
        return path;
    }

    public abstract boolean isNode();

}
