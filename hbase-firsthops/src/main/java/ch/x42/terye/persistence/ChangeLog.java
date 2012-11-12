package ch.x42.terye.persistence;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import ch.x42.terye.ItemImpl;

public class ChangeLog {

    public static abstract class Operation {

        private ItemState state;

        private Operation(ItemState state) {
            this.state = state;
        }

        private Operation(ItemImpl item) {
            this(item.getState());
        }

        public ItemState getState() {
            return state;
        }

        public boolean isAddOperation() {
            return false;
        }

        public boolean isModifyOperation() {
            return false;
        }

    }

    public static class AddOperation extends ChangeLog.Operation {

        private AddOperation(ItemImpl item) {
            super(item);
        }

        public boolean isAddOperation() {
            return true;
        }

    }

    public static class ModifyOperation extends ChangeLog.Operation {

        private ModifyOperation(ItemImpl item) {
            super(item);
        }

        public boolean isModifyOperation() {
            return true;
        }

    }

    private List<Operation> operations = new LinkedList<Operation>();

    public void itemAdded(ItemImpl item) {
        operations.add(new AddOperation(item));
    }

    public void itemModified(ItemImpl item) {
        operations.add(new ModifyOperation(item));
    }

    public void itemRemoved(ItemImpl item) {
    }

    public Iterable<Operation> getOperations() {
        return operations;
    }

    public boolean isEmpty() {
        return operations.isEmpty();
    }

    public void purge() {
        operations = new LinkedList<Operation>();
    }

    public int size() {
        return operations.size();
    }

    public Iterator<Operation> iterator() {
        return operations.iterator();
    }

}
