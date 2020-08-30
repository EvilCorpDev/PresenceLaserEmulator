package com.home;

import java.util.HashMap;
import java.util.Map;
import java.util.function.IntUnaryOperator;

public class AnalogSignalRepository implements IntUnaryOperator {
    private Map<Integer, Integer> analogSignalMap = new HashMap<>();

    @Override
    public int applyAsInt(int pin) {
        return analogSignalMap.get(pin);
    }

    public void setAnalogValue(int pin, int value) {
        analogSignalMap.put(pin, value);
    }
}
