package ch.x42.terye.observation;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ObservationDispatcher {

    private Set<EventConsumer> consumers;

    protected ObservationDispatcher() {
        this.consumers = new HashSet<EventConsumer>();
    }

    protected void addConsumer(EventConsumer consumer) {
        consumers.add(consumer);
    }

    protected void removeConsumer(EventConsumer consumer) {
        consumers.remove(consumer);
    }

    protected void dispatchEvents(EventCollection events) {
        for (EventConsumer consumer : consumers) {
            consumer.consume(events);
        }
    }

    protected Set<EventConsumer> getConsumers() {
        return Collections.unmodifiableSet(consumers);
    }

}
