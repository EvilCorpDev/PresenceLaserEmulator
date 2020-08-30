package com.home;

import java.util.function.DoubleConsumer;

public class StateRepository implements DoubleConsumer {
    private Double state;

    @Override
    public void accept(double value) {
        state = value;
    }

    public Double getState() {
        return state;
    }
}
