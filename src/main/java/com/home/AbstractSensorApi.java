package com.home;

import java.util.function.DoubleConsumer;
import java.util.function.IntUnaryOperator;
import java.util.function.LongSupplier;

public abstract class AbstractSensorApi {

    private static final int EVENT_BUFFER_SIZE = 4;

    private final DoubleConsumer stateConsumer;
    private final LongSupplier timeSupplier;
    private final IntUnaryOperator analogSignalProvider;

    private int eventsBufferPointer = -1;
    private final char[] eventsBuffer = new char[EVENT_BUFFER_SIZE];

    public AbstractSensorApi(DoubleConsumer stateConsumer, LongSupplier timeSupplier,
                             IntUnaryOperator analogSignalProvider) {
        this.stateConsumer = stateConsumer;
        this.timeSupplier = timeSupplier;
        this.analogSignalProvider = analogSignalProvider;
    }

    public abstract void setup();

    public abstract void loop();

    public abstract void setPeopleCount(int peopleCount);

    public void publish_state(float state) {
        stateConsumer.accept(state);
    }

    public long micros() {
        return timeSupplier.getAsLong();
    }

    public int analogRead(int analogPin) {
        return analogSignalProvider.applyAsInt(analogPin);
    }

    public void addEvent(char eventSymbol) {
        eventsBufferPointer = getNextBufferIndex(eventsBufferPointer);
        eventsBuffer[eventsBufferPointer] = eventSymbol;
    }

    public void clearBuffer() {
        for (int i = 0; i < eventsBuffer.length; i++) {
            eventsBuffer[i] = '0';
        }
    }

    public char[] getEventsBuffer() {
        return eventsBuffer;
    }

    public int getNextBufferIndex(int bufferPointer) {
        if (bufferPointer == EVENT_BUFFER_SIZE - 1) {
            return 0;
        }

        return bufferPointer + 1;
    }
}
