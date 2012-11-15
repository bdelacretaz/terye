package ch.x42.terye.persistence;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import ch.x42.terye.ItemImpl;

// XXX: make log more intelligent
public class ChangeLog {

    public static abstract class Operation {

        private ItemState state;

        private Operation() {

        }

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
            return (this instanceof ChangeLog.AddOperation);
        }

        public boolean isModifyOperation() {
            return (this instanceof ChangeLog.ModifyOperation);
        }

        public boolean isRemoveOperation() {
            return (this instanceof ChangeLog.RemoveOperation);
        }

        public boolean isRemoveRangeOperation() {
            return (this instanceof ChangeLog.RemoveRangeOperation);
        }

    }

    public static class AddOperation extends ChangeLog.Operation {

        private AddOperation(ItemImpl item) {
            super(item);
        }

    }

    public static class ModifyOperation extends ChangeLog.Operation {

        private ModifyOperation(ItemImpl item) {
            super(item);
        }

    }

    public static class RemoveOperation extends ChangeLog.Operation {

        private RemoveOperation(ItemImpl item) {
            super(item);
        }

    }

    public static class RemoveRangeOperation extends ChangeLog.Operation {

        private String partialKey;

        private RemoveRangeOperation(String partialKey) {
            this.partialKey = partialKey;
        }

        public String getPartialKey() {
            return partialKey;
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
        operations.add(new RemoveOperation(item));
    }

    public void rangeRemoved(String partialKey) {
        operations.add(new RemoveRangeOperation(partialKey));
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
