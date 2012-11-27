package ch.x42.terye.observation;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ObservationDispatcher implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Thread thread;
    private BlockingQueue<EventCollection> queue;
    private Set<EventConsumer> consumers;
    private Object lock;

    public ObservationDispatcher() {
        thread = new Thread(this, "ObservationManager");
        thread.setDaemon(true);
        queue = new LinkedBlockingQueue<EventCollection>();
        consumers = new HashSet<EventConsumer>();
        lock = new Object();
        thread.start();
    }

    protected void addConsumer(EventConsumer consumer) {
        synchronized (lock) {
            consumers.add(consumer);
        }
    }

    protected void removeConsumer(EventConsumer consumer) {
        synchronized (lock) {
            consumers.remove(consumer);
        }
    }

    protected void dispatchEvents(EventCollection events) {
        try {
            queue.put(events);
        } catch (InterruptedException e) {
            logger.warn("Interrupted while putting to event queue");
        }
    }

    protected Set<EventConsumer> getConsumers() {
        return Collections.unmodifiableSet(consumers);
    }

    @Override
    public void run() {
        while (true) {
            try {
                EventCollection events = queue.take();
                // XXX: don't lock during whole notification process?
                synchronized (lock) {
                    for (EventConsumer consumer : consumers) {
                        try {
                            consumer.consume(events);
                        } catch (Throwable e) {
                            logger.warn("Consumer threw an exception: {}", e);
                        }
                    }
                }
            } catch (InterruptedException e) {
                logger.warn("Interrupted while taking from event queue");
            }
        }
    }
}
