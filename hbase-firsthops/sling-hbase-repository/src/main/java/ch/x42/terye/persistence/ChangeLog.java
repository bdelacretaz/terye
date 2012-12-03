package ch.x42.terye.persistence;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.jcr.RepositoryException;

import ch.x42.terye.ItemImpl;
import ch.x42.terye.path.Path;
import ch.x42.terye.path.PathFactory;
import ch.x42.terye.persistence.id.ItemId;

public class ChangeLog {

    private Map<ItemId, ItemState> addedStates = new LinkedHashMap<ItemId, ItemState>();
    private Map<ItemId, ItemState> modifiedStates = new LinkedHashMap<ItemId, ItemState>();
    private Map<ItemId, ItemState> removedStates = new LinkedHashMap<ItemId, ItemState>();

    public void added(ItemImpl item) {
        addedStates.put(item.getId(), item.getState());
    }

    public void modified(ItemImpl item) {
        if (!addedStates.containsKey(item.getId())) {
            modifiedStates.put(item.getId(), item.getState());
        }
    }

    public void removed(ItemImpl item) {
        if (addedStates.remove(item.getId()) == null) {
            modifiedStates.remove(item.getId());
            removedStates.put(item.getId(), item.getState());
        }
    }

    public Collection<ItemState> getAddedStates() {
        return addedStates.values();
    }

    public Collection<ItemState> getModifiedStates() {
        return modifiedStates.values();
    }

    public Collection<ItemState> getRemovedStates() {
        return removedStates.values();
    }

    /**
     * This method returns the minimal subset of the removed states, such that
     * every removed state is either part of the returned set or a descendant of
     * one of the states in the returned set.
     */
    public List<ItemState> getMinimalRemovedStates() throws RepositoryException {
        // states contains all removed states
        List<ItemState> states = new LinkedList<ItemState>(
                removedStates.values());
        // sort states by increasing depth
        Collections.sort(states, new Comparator<ItemState>() {

            @Override
            public int compare(ItemState s1, ItemState s2) {
                Path p1 = PathFactory.create(s1.getPath());
                Path p2 = PathFactory.create(s2.getPath());
                if (p1.getDepth() < p2.getDepth()) {
                    return -1;
                } else if (p1.getDepth() == p2.getDepth()) {
                    return 0;
                }
                return 1;
            }

        });
        // start with an empty list of minimal roots
        List<ItemState> roots = new LinkedList<ItemState>();
        // loop through all states
        for (ItemState state : states) {
            boolean covered = false;
            // loop through all minimal roots
            for (ItemState root : roots) {
                Path statePath = PathFactory.create(state.getPath());
                Path rootPath = PathFactory.create(root.getPath());
                // if this state is a descendant of one of the minimal roots, we
                // can discard it
                if (statePath.isDescendantOf(rootPath)) {
                    covered = true;
                    break;
                }
            }
            // this state is not a descendant of one of the minimal roots, add
            // to minimal roots
            if (!covered) {
                roots.add(state);
            }
        }
        return roots;
    }

    public boolean isEmpty() {
        return (addedStates.isEmpty() && modifiedStates.isEmpty() && removedStates
                .isEmpty());
    }

    public void purge() {
        addedStates.clear();
        modifiedStates.clear();
        removedStates.clear();
    }

}
