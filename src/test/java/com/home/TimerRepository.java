package com.home;

import java.util.function.LongSupplier;

public class TimerRepository implements LongSupplier {
    private long time = 0;

    @Override
    public long getAsLong() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void incrementTime(long number) {
        this.time += number;
    }

    public void incrementTime() {
        this.time += 100;
    }
}
