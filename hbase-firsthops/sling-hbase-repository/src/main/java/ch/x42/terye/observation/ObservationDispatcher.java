package ch.x42.terye.observation;

import java.util.HashSet;
import java.util.Set;

public class ObservationDispatcher {

    private Set<EventConsumer> consumers;

    protected ObservationDispatcher() {
        this.consumers = new HashSet<EventConsumer>();
    }

    protected synchronized void addConsumer(EventConsumer consumer) {
        consumers.add(consumer);
    }

    protected synchronized void removeConsumer(EventConsumer consumer) {
        consumers.remove(consumer);
    }

}
