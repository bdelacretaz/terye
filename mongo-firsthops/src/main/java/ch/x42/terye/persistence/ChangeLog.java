package ch.x42.terye.persistence;

import java.util.LinkedList;
import java.util.List;

import ch.x42.terye.ItemImpl;

public class ChangeLog {

    public static abstract class Operation {

        private ItemImpl item;

        public Operation(ItemImpl item) {
            this.item = item;
        }

        public ItemImpl getItem() {
            return item;
        }

    }

    public static class AddOperation extends ChangeLog.Operation {

        public AddOperation(ItemImpl item) {
            super(item);
        }

    }

    public static class ModifyOperation extends ChangeLog.Operation {

        public ModifyOperation(ItemImpl item) {
            super(item);
        }

    }

    public static class RemoveOperation extends ChangeLog.Operation {

        public RemoveOperation(ItemImpl item) {
            super(item);
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

    public Iterable<Operation> getOperations() {
        return operations;
    }

    public boolean isEmpty() {
        return operations.isEmpty();
    }

    public void purge() {
        operations = new LinkedList<Operation>();
    }

}
