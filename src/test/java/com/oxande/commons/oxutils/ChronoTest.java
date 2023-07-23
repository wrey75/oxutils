package com.oxande.commons.oxutils;

import org.junit.Assert;
import org.junit.Test;

public class ChronoTest {

    void waitFor(int duration) throws InterruptedException {
        Thread.sleep(duration);
    }

    /**
     * This tests includes sleeping time to ensure success for tests.
     *
     * @throws InterruptedException if interrupted.
     */
    @Test
    public void startStopTest() throws InterruptedException {
        Chrono chrono = new Chrono();
        Assert.assertFalse(chrono.isStarted());
        chrono.start();
        Assert.assertTrue(chrono.isStarted());

        waitFor(20);
        long duration0 = chrono.getDuration();
        Assert.assertTrue(duration0 >= 20);

        waitFor(20);
        long duration1 = chrono.getDuration();
        Assert.assertTrue(duration1 >= 40);

        chrono.stop();
        waitFor(20);
        long duration2 = chrono.getDuration();
        Assert.assertTrue(duration2 - duration1 < 20);

        Assert.assertFalse(chrono.isStarted());
    }

    @Test
    public void autostartTest() throws InterruptedException {
        Chrono chrono = Chrono.started();
        Assert.assertTrue(chrono.isStarted());
    }
}
