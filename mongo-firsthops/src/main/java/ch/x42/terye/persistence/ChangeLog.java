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

    private List<Operation> operations = new LinkedList<Operation>();

    public void nodeAdded(NodeImpl node) {
        operations.add(new AddOperation(node));
    }

}
