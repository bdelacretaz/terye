package ch.x42.terye.persistence;

import java.util.LinkedList;
import java.util.List;

import ch.x42.terye.ItemImpl;
import ch.x42.terye.NodeImpl;
import ch.x42.terye.PropertyImpl;

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

        public ModifyOperation(NodeImpl node) {
            super(node);
        }

    }

    public static class RemoveOperation extends ChangeLog.Operation {

        public RemoveOperation(NodeImpl node) {
            super(node);
        }

    }

    private List<Operation> operations = new LinkedList<Operation>();

    public void nodeAdded(NodeImpl node) {
        operations.add(new AddOperation(node));
    }

    public void propertyAdded(PropertyImpl property) {
        operations.add(new AddOperation(property));
    }

    public void nodeModified(NodeImpl node) {
        operations.add(new ModifyOperation(node));
    }

    public void nodeRemoved(NodeImpl node) {
        operations.add(new RemoveOperation(node));
    }

    public Iterable<Operation> getOperations() {
        return operations;
    }

}
