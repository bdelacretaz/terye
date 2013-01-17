package ch.x42.terye.oak.mk.test;

/**
 * Simple timer class.
 */
public class Timer {

    private long start;
    private long stop;

    public void start() {
        start = System.currentTimeMillis();
    }

    public void stop() {
        stop = System.currentTimeMillis();
    }

    public long getDuration() {
        return stop - start;
    }

}
