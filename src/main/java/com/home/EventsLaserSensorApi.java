package com.home;

import static com.home.GlobalConstant.BACK_FREE;
import static com.home.GlobalConstant.BACK_LASER_PIN;
import static com.home.GlobalConstant.BACK_OCCUPIED;
import static com.home.GlobalConstant.CROSSED;
import static com.home.GlobalConstant.FRONT_FREE;
import static com.home.GlobalConstant.FRONT_LASER_PIN;
import static com.home.GlobalConstant.FRONT_OCCUPIED;
import static com.home.GlobalConstant.PERSON_COME_INSIDE;
import static com.home.GlobalConstant.PERSON_COME_OUTSIDE;

import java.util.function.DoubleConsumer;
import java.util.function.IntUnaryOperator;
import java.util.function.LongSupplier;

class LaserSensorListener {
    private final AbstractSensorApi api;

    private final int pin;
    private boolean lastValue = false;
    private final int threshold;
    private final char sensorOccupied;
    private final char sensorFree;

    public LaserSensorListener(AbstractSensorApi api, int pin, int threshold, char sensorFree, char sensorOccupied) {
        this.api = api;
        this.pin = pin;
        this.threshold = threshold;
        this.sensorFree = sensorFree;
        this.sensorOccupied = sensorOccupied;
    }

    void checkState() {
        if (api.analogRead(pin) > threshold && !lastValue) {
            lastValue = true;
            api.addEvent(sensorOccupied);
        } else if (api.analogRead(pin) <= threshold && lastValue) {
            lastValue = false;
            api.addEvent(sensorFree);
        }
    }
}


public class EventsLaserSensorApi extends AbstractSensorApi {

    private final LaserSensorListener frontLaser;
    private final LaserSensorListener backLaser;
    private int peopleCount = 0;

    public EventsLaserSensorApi(DoubleConsumer stateConsumer, LongSupplier timeSupplier,
                                IntUnaryOperator analogSignalProvider) {
        super(stateConsumer, timeSupplier, analogSignalProvider);
        this.frontLaser = new LaserSensorListener(this, FRONT_LASER_PIN, CROSSED - 1, FRONT_FREE, FRONT_OCCUPIED);
        this.backLaser = new LaserSensorListener(this, BACK_LASER_PIN, CROSSED - 1, BACK_FREE, BACK_OCCUPIED);
    }

    @Override
    public void setup() {
        publish_state(0.0f);
    }

    @Override
    public void loop() {
        frontLaser.checkState();
        backLaser.checkState();

        if (compareStates(this.getEventsBuffer(), PERSON_COME_INSIDE)) {
            setPeopleCount(peopleCount + 1);
            clearBuffer(); //Need to clear buffer, so next loop iteration won't think that there was a new person
        } else if (compareStates(this.getEventsBuffer(), PERSON_COME_OUTSIDE)) {
            setPeopleCount(peopleCount - 1);
            clearBuffer();
        }
    }

    @Override
    public void setPeopleCount(int peopleCount) {
        this.peopleCount = peopleCount;
        publish_state(peopleCount);
    }

    private boolean compareStates(char[] currentState, char[] desiredState) {
        if (currentState.length != desiredState.length) {
            throw new RuntimeException("Incompatible states");
        }

        //Looking for start of desiredState
        int currentStateIndexIterator = findStartIndex(currentState, desiredState[0]);

        if (currentStateIndexIterator == -1) {
            return false;
        }

        for (int i = 0; i < currentState.length; i++) {
            if (currentState[currentStateIndexIterator] != desiredState[i]) {
                return false;
            }
            currentStateIndexIterator = getNextBufferIndex(currentStateIndexIterator);
        }
        return true;
    }

    private int findStartIndex(char[] currentState, char startSymbol) {
        for (int i = 0; i < currentState.length; i++) {
            if (currentState[i] == startSymbol) {
                return i;
            }
        }
        return -1;
    }
}
