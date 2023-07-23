package com.oxande.commons.oxutils;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Stream;

public class PowerStreamTest {
    public static Logger LOG = LoggerFactory.getLogger(PowerStreamTest.class);

    @Test
    public void sleepTest() {
        Chrono chrono = Chrono.started();
        Stream<Integer> stream = PowerStream.stream(p -> {
            for (int i = 0; i < 100; i++) {
                p.put(i);
                if (i % 13 == 0) {
                    p.sleep(1000);
                }
            }
        }, 10);

        Assert.assertEquals(100, stream.distinct().count());
        Assert.assertThat(7000L, Matchers.lessThanOrEqualTo(chrono.getDuration()));
    }

    @Test
    public void fastGenerationTest() throws InterruptedException {
        int nbElements = 1_000_000;
        Stream<String> stream2 = PowerStream.stream(p -> {
            for (int i = 0; i < nbElements; i++) {
                if (i % 97 == 0) LOG.debug("Storing element {}...", i);
                p.put("Joe");
            }
        }, 9);
        Thread.sleep(1000); // Wait the queue is full
        Assert.assertEquals(nbElements, stream2.count());
    }

}