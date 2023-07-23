package com.oxande.commons.oxutils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;


/**
 * The progress worker is just a help to basically display
 * messages at regular interval.
 *
 * <p>A typical use is:</p>
 * <pre>
 *     // Log every 1 second
 *     ProgressWorker worker = new ProgressWorker(1000);
 *     for(...){
 *         ... // Work to do
 *
 *         // And a regular display through a LOG (or anything)
 *         worker.progress(nb -> LOG.debug("{} rows processed until now...", nb));
 *     }
 * </pre>
 */
public class ProgressWorker {
    private static final Logger LOG = LoggerFactory.getLogger(ProgressWorker.class);

    private final long startedAt;
    private long lastDisplay;
    private long lastRun;
    private final long delay;
    private long sleepingTime;
    private final AtomicInteger count = new AtomicInteger();

    /**
     * Create a new progress worker with a delay of 15 seconds.
     */
    public ProgressWorker() {
        this(DateUtil.ONE_SECOND * 15);
    }

    /**
     * Create a new progress worker with a delay.
     *
     * @param delay the delay in milliseconds.
     */
    public ProgressWorker(long delay) {
        this.startedAt = System.currentTimeMillis();
        this.lastRun = this.lastDisplay = this.startedAt;
        this.delay = delay;
        this.sleepingTime = 0;
    }

    /**
     * wait for an amount of time. This can be used to limit the
     * number of rows processed. The waiting time depends on the
     * running time.
     *
     * @param time the sleep time in millisecond
     * @throws InterruptedException if the wait is interrupted.
     */
    public long sleepFor(long time) throws InterruptedException {
        long waitFor = 0;
        if (this.count.get() > 0) {
            long until = this.startedAt + this.count.get() * time;
            if (until > this.lastRun && this.lastRun >= this.startedAt) {
                while (waitFor == 0) {
                    waitFor = until - this.lastRun;
                    if (waitFor > 0) {
                        synchronized (this) {
                            LOG.debug("Waiting for {} ms...", waitFor);
                            this.wait(waitFor);
                            this.sleepingTime += waitFor;
                        }
                    }
                }
            }
        }
        return waitFor;
    }

    public static boolean sleep(long duration) {
        if(duration > 0){
            try {
                Thread.sleep(duration);
            } catch(InterruptedException ex){
                Thread.currentThread().interrupt();
                return true;
            }
        }
        return false;
    }

    public void sleepAndProgress(long time, IntConsumer consumer) {
        try {
            sleepFor(time);
            progress(consumer);
        } catch (InterruptedException e) {
            LOG.warn("The method has been interrupted.");
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Returns the total waiting time.
     *
     * @return the total sleeping time.
     */
    public long getSleepingTime() {
        return sleepingTime;
    }

    /**
     * This method should be called at each round. If the elapsed time
     * is greater than the delay specified, then the consumer method is
     * run.
     * <p>
     * The consumer receives a counter incremented at each call of the
     * progress method (i.e. the number of rows processed).
     *
     * @param consumer the consumer
     */
    public void progress(IntConsumer consumer) {
        this.lastRun = System.currentTimeMillis();
        this.count.incrementAndGet();
        if (this.lastRun - lastDisplay > delay) {
            consumer.accept(this.count.get());
            lastDisplay = System.currentTimeMillis();
        }
    }

    /**
     * Get the total duration.
     *
     * @return the total duration since the object has been created.
     */
    public long getDuration() {
        return System.currentTimeMillis() - this.startedAt;
    }

    /**
     * Get the total number of calls to the {@link #progress(IntConsumer)} method.
     *
     * @return the number of record processed.
     */
    public long getCount() {
        return this.count.get();
    }

    public double getAverageSleepingTime() {
        if (this.getCount() > 0) {
            return (double) this.getSleepingTime() / this.getCount();
        }
        return 0.0;
    }

    /**
     * The consumer is run only the the total duration has exceeded the
     * time parameter.
     *
     * @param time the time barrier
     * @param consumer the code to run.
     */
    public void doIfLongerThan(long time, LongConsumer consumer) {
        long duration = getDuration();
        if (duration > time) {
            consumer.accept(duration);
        }
    }
}
