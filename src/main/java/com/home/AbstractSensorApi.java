package com.home;

import java.util.function.DoubleConsumer;
import java.util.function.IntUnaryOperator;
import java.util.function.LongSupplier;

public abstract class AbstractSensorApi {
    private final DoubleConsumer stateConsumer;
    private final LongSupplier timeSupplier;
    private final IntUnaryOperator analogSignalProvider;

    public AbstractSensorApi(DoubleConsumer stateConsumer, LongSupplier timeSupplier, IntUnaryOperator analogSignalProvider) {
        this.stateConsumer = stateConsumer;
        this.timeSupplier = timeSupplier;
        this.analogSignalProvider = analogSignalProvider;
    }

    public abstract void setup();
    public abstract void loop();

    protected void publish_state(float state) {
        stateConsumer.accept(state);
    }

    protected long micros() {
        return timeSupplier.getAsLong();
    }

    protected int analogRead(int analogPin) {
        return analogSignalProvider.applyAsInt(analogPin);
    }
}
