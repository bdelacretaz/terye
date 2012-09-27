package ch.x42.terye.persistence;

import java.util.LinkedList;
import java.util.List;

import ch.x42.terye.NodeImpl;

public class ChangeLog {

    public static abstract class Operation {

        private NodeImpl node;

        public Operation(NodeImpl node) {
            this.node = node;
        }

        public NodeImpl getNode() {
            return node;
        }

    }

    public static class AddOperation extends ChangeLog.Operation {

        public AddOperation(NodeImpl node) {
            super(node);
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
