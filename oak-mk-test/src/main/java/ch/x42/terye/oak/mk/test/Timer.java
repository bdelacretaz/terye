package ch.x42.terye.oak.mk.test;

import java.util.LinkedList;
import java.util.List;

/**
 * Simple timer class.
 */
public class Timer {

    private long start;
    private List<Long> durations;
    private long last;
    private Double avg;
    private Long min;
    private Long max;

    public Timer() {
        durations = new LinkedList<Long>();
    }

    public void start() {
        start = System.currentTimeMillis();
    }

    public void stop() {
        last = System.currentTimeMillis() - start;
        durations.add(last);
    }

    public long getLastDuration() {
        return last;
    }

    public int getCount() {
        return durations.size();
    }

    public double getAverageDuration() {
        if (avg == null) {
            long sum = 0L;
            for (Long duration : durations) {
                sum += duration;
            }
            avg = sum / (double) getCount();
        }
        return avg;
    }

    public long getMinDuration() {
        if (min == null) {
            min = Long.MAX_VALUE;
            for (Long duration : durations) {
                if (duration < min) {
                    min = duration;
                }
            }
        }
        return min;
    }

    public long getMaxDuration() {
        if (max == null) {
            max = Long.MIN_VALUE;
            for (Long duration : durations) {
                if (duration > max) {
                    max = duration;
                }
            }
        }
        return max;
    }

}
