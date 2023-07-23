package com.oxande.commons.oxutils;

public final class AtomicDouble {
    private double value;

    public synchronized double addAndGet(double inc){
        value += inc;
        return value;
    }

    public synchronized double doubleValue() {
        return value;
    }

    public synchronized void set(double val) {
        this.value = val;
    }

    public synchronized double get() {
        return doubleValue();
    }
}
