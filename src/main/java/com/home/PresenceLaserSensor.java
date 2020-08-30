package com.home;

import java.util.function.DoubleConsumer;
import java.util.function.IntUnaryOperator;
import java.util.function.LongSupplier;

public class PresenceLaserSensor extends AbstractSensorApi {
    public PresenceLaserSensor(DoubleConsumer stateConsumer, LongSupplier timeSupplier, IntUnaryOperator analogSignalProvider) {
        super(stateConsumer, timeSupplier, analogSignalProvider);
    }

    @Override
    public void setup() {
        publish_state(0.0f);
    }

    @Override
    public void loop() {
        publish_state();
    }
}
