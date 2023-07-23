package com.oxande.commons.oxutils;

import java.util.ArrayList;
import java.util.List;

public class Chrono {

    private long started;
    private long stopped;

    private List<Long> laps = null;

    /**
     * Create a new chrono and starts it immediately.
     *
     * @return a new created chrono already started.
     */
    public static Chrono started(){
        Chrono chrono = new Chrono();
        chrono.start();
        return chrono;
    }

    /**
     * Check if the chrono has been started.
     *
     * @return true if the chrono is working.
     */
    public boolean isStarted(){
        return started > 0 && stopped == 0;
    }

    /**
     * Starts or restars the chrono. Do not generate an exception if the
     * chronograph was already started.
     */
    public void restart(){
        started = System.currentTimeMillis();
        stopped = 0;
    }

    public void start(){
        if(!isStarted()){
            restart();
        }
        else {
            throw new IllegalStateException("The chrono is already started.");
        }
    }

    /**
     * Returns the laps created.
     * @return the laps as an array of long
     */
    public synchronized long[] getLaps(){
        if(laps == null){
            return new long[0];
        }
        long[] array = new long[this.laps.size()];
        int i = 0;
        for(Long v : this.laps){
            array[i++] = v;
        }
        return array;
    }

    public synchronized double getAverageTime(){
        if(this.laps == null){
            return -1;
        }
        long total = this.laps.stream().reduce(0L, Long::sum);
        return (double)total / this.laps.size();
    }

    /**
     * Get the lap. The chronograph permits to get laps. You
     * just have to get one and it is saved in the laps for
     * this chronograph.
     *
     * @return the last lap (the created one).
     */
    public synchronized long getLap(){
        if(isStarted()){
            long before = started;
            long now = System.currentTimeMillis();
            if(laps == null){
                laps = new ArrayList<>();
            }
            else {
                before = laps.get(laps.size() -1);
            }
            long currentLap = now - before;
            laps.add(currentLap);
            return currentLap;
        }
        throw new IllegalStateException("The chrono was not started!");
    }

    public void stop(){
        if(isStarted()) {
            stopped = System.currentTimeMillis();
            return;
        }
        throw new IllegalStateException("The chrono was not started!");
    }

    /**
     * Resets the chroopraph. Basically provides a new chronograph.
     */
    public void reset(){
        started = 0L;
        stopped = 0L;
        this.laps = null;
    }

    /**
     * Get the duration. The current duration since the start.
     *
     * @return the duration in milliseconds or 0 if never started.
     */
    public long getDuration(){
        if(started == 0L){
            return 0L;
        }
        long end = (isStarted() ? System.currentTimeMillis() : stopped);
        return end - started;
    }

    public String getHumanDuration(){
        long duration = getDuration();
        return DateUtil.humanDuration(duration);
    }
}
