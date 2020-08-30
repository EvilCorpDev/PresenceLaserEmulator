package com.home;

import java.util.function.DoubleConsumer;
import java.util.function.IntUnaryOperator;
import java.util.function.LongSupplier;

class LaserSensor {
    private static final int ONE_SECOND_DELAY = 1000000;
    private AbstractSensorApi api;
    //----------------------------
    private int pin;
    private boolean lastValue = false;
    private long triggeredTime;
    private int threshold;


    public LaserSensor(AbstractSensorApi api, int pin, int threshold) {
        this.api = api;
        this.pin = pin;
        this.threshold = threshold;
    }

    void setup() {
//        adcAttachPin(pin);
//        analogSetPinAttenuation(pin, ADC_6db);
    }

    boolean isTriggered() {
        if(lastValue && (api.micros() - triggeredTime < ONE_SECOND_DELAY)) {
            return lastValue;
        }

        if(api.analogRead(pin) > threshold) {
            lastValue = true;
            triggeredTime = api.micros();
            return true;
        } {
            lastValue = false;
            return false;
        }
    }

    public long getTriggeredTime() {
        return triggeredTime;
    }
}

public class PresenceLaserSensor extends AbstractSensorApi {
    private LaserSensor frontSensor;
    private LaserSensor backSensor;
    private int peopleCount;

    public PresenceLaserSensor(DoubleConsumer stateConsumer, LongSupplier timeSupplier, IntUnaryOperator analogSignalProvider) {
        super(stateConsumer, timeSupplier, analogSignalProvider);

        frontSensor = new LaserSensor(this, GlobalConstant.FRONT_LASER_PIN, GlobalConstant.CROSSED - 1);
        backSensor = new LaserSensor(this, GlobalConstant.BACK_LASER_PIN, GlobalConstant.CROSSED - 1);
    }

    @Override
    public void setPeopleCount(int peopleCount) {
        this.peopleCount = peopleCount;
        publish_state(peopleCount);
    }

    @Override
    public void setup() {
        publish_state(0.0f);

        frontSensor.setup();
        backSensor.setup();
    }

    @Override
    public void loop() {
        boolean frontResult = frontSensor.isTriggered();
        boolean backResult = backSensor.isTriggered();

        if(frontResult && backResult) {
            if(frontSensor.getTriggeredTime() < backSensor.getTriggeredTime()) {
                peopleCount++;
            } else {
                peopleCount--;
            }
            publish_state(peopleCount);
        }
    }
}
