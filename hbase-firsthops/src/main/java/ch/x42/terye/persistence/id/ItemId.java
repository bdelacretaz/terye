package ch.x42.terye.persistence.id;

import java.io.Serializable;

public abstract class ItemId implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String uuid;

    public ItemId(String uuid) {
        this.uuid = uuid;
    }

    public abstract boolean denotesNode();

    @Override
    public String toString() {
        return uuid;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ItemId other = (ItemId) obj;
        if (uuid == null) {
            if (other.uuid != null) {
                return false;
            }
        } else if (!uuid.equals(other.uuid)) {
            return false;
        }
        return true;
    }

}
