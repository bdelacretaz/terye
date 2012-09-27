package ch.x42.terye.persistence;

public enum ItemType {

    NODE(NodeState.class), PROPERTY(PropertyState.class);

    private Class<? extends ItemState> stateClass;

    private ItemType(Class<? extends ItemState> stateClass) {
        this.stateClass = stateClass;
    }

    public Class<? extends ItemState> getStateClass() {
        return stateClass;
    }

}