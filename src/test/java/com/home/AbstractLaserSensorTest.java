package com.home;

import static org.junit.Assert.assertEquals;
import static com.home.GlobalConstant.BACK_LASER_PIN;
import static com.home.GlobalConstant.CROSSED;
import static com.home.GlobalConstant.FRONT_LASER_PIN;
import static com.home.GlobalConstant.NO_CROSSED;

import org.junit.Test;

public abstract class AbstractLaserSensorTest {
    protected StateRepository stateRepository;
    protected TimerRepository timerRepository;
    protected AnalogSignalRepository analogSignalRepository;
    protected AbstractSensorApi sensorApi;

    abstract void setSensorApi();

    @Test
    public void shouldIncrementPeopleWhenPeopleWillComeInsideNotFromFirstTime() {
        sensorApi.setPeopleCount(0);

        timerRepository.incrementTime(100);
        analogSignalRepository.setAnalogValue(FRONT_LASER_PIN, CROSSED);
        sensorApi.loop();

        timerRepository.incrementTime(100);
        analogSignalRepository.setAnalogValue(FRONT_LASER_PIN, NO_CROSSED);
        sensorApi.loop();

        timerRepository.incrementTime(1_000_000);

        comeInside();

        assertEquals(Double.valueOf(1), stateRepository.getState());
    }

    @Test
    public void shouldReturnSameValueWhenPeopleSomeTimeTriggerOnlyOneLine() {
        sensorApi.setPeopleCount(0);

        timerRepository.incrementTime(100);
        analogSignalRepository.setAnalogValue(FRONT_LASER_PIN, CROSSED);
        sensorApi.loop();

        timerRepository.incrementTime(100);
        analogSignalRepository.setAnalogValue(FRONT_LASER_PIN, NO_CROSSED);
        sensorApi.loop();

        timerRepository.incrementTime(2_000_000);

        timerRepository.incrementTime(100);
        analogSignalRepository.setAnalogValue(BACK_LASER_PIN, CROSSED);
        sensorApi.loop();

        timerRepository.incrementTime(100);
        analogSignalRepository.setAnalogValue(BACK_LASER_PIN, NO_CROSSED);
        sensorApi.loop();

        assertEquals(Double.valueOf(0), stateRepository.getState());
    }

    @Test
    public void shouldReturnZeroPeopleAfterSetup() {
        sensorApi.setup();

        assertEquals(Double.valueOf(0), stateRepository.getState());
    }

    @Test
    public void shouldIncrementPeopleCountWhenCrossedTwoLaser() {
        sensorApi.setPeopleCount(1);

        comeInside();

        assertEquals(Double.valueOf(2), stateRepository.getState());
    }

    @Test
    public void shouldDecrementPeopleWhenCrossedTwoLaser() {
        sensorApi.setPeopleCount(1);

        comeOut();

        assertEquals(Double.valueOf(0), stateRepository.getState());
    }


    @Test
    public void shouldReturnSameCountOfPeopleWhenPeopleRollbackInside() {
        sensorApi.setPeopleCount(1);

        timerRepository.incrementTime(100);
        analogSignalRepository.setAnalogValue(BACK_LASER_PIN, CROSSED);
        sensorApi.loop();

        timerRepository.incrementTime(100);
        analogSignalRepository.setAnalogValue(FRONT_LASER_PIN, CROSSED);
        sensorApi.loop();

        timerRepository.incrementTime(100);
        analogSignalRepository.setAnalogValue(FRONT_LASER_PIN, NO_CROSSED);
        sensorApi.loop();

        timerRepository.incrementTime(100_000);
        analogSignalRepository.setAnalogValue(BACK_LASER_PIN, NO_CROSSED);
        sensorApi.loop();

        assertEquals(Double.valueOf(1), stateRepository.getState());
    }

    @Test
    public void shouldReturnSameCountOfPeopleWhenPeopleRollbackOutside() {
        sensorApi.setPeopleCount(1);

        timerRepository.incrementTime(100);
        analogSignalRepository.setAnalogValue(FRONT_LASER_PIN, CROSSED);
        sensorApi.loop();

        timerRepository.incrementTime(100);
        analogSignalRepository.setAnalogValue(BACK_LASER_PIN, CROSSED);
        sensorApi.loop();

        timerRepository.incrementTime(100_000);
        analogSignalRepository.setAnalogValue(BACK_LASER_PIN, NO_CROSSED);
        sensorApi.loop();

        timerRepository.incrementTime(100);
        analogSignalRepository.setAnalogValue(FRONT_LASER_PIN, NO_CROSSED);
        sensorApi.loop();

        assertEquals(Double.valueOf(1), stateRepository.getState());
    }

    @Test
    public void shouldReturnZeroPeopleWhenCrossedOneFrontLaser() {
        sensorApi.setPeopleCount(0);

        for (int i = 0; i < 100; i++) {
            timerRepository.incrementTime(100);
            analogSignalRepository.setAnalogValue(FRONT_LASER_PIN, CROSSED);
            sensorApi.loop();

            timerRepository.incrementTime(100);
            analogSignalRepository.setAnalogValue(FRONT_LASER_PIN, NO_CROSSED);
            sensorApi.loop();
        }

        assertEquals(Double.valueOf(0), stateRepository.getState());
    }

    @Test
    public void shouldReturnZeroPeopleWhenCrossedOneBackLaser() {
        sensorApi.setPeopleCount(0);

        for (int i = 0; i < 100; i++) {
            timerRepository.incrementTime(100);
            analogSignalRepository.setAnalogValue(BACK_LASER_PIN, CROSSED);
            sensorApi.loop();

            timerRepository.incrementTime(100);
            analogSignalRepository.setAnalogValue(BACK_LASER_PIN, NO_CROSSED);
            sensorApi.loop();
        }

        assertEquals(Double.valueOf(0), stateRepository.getState());
    }

    @Test
    public void shouldReturnNumberOfPeopleComeInside() {
        sensorApi.setPeopleCount(0);

        for (int i = 0; i < 100; i++) {
            comeInside();
            timerRepository.incrementTime(1_000_000);
        }

        assertEquals(Double.valueOf(100), stateRepository.getState());
    }

    @Test
    public void shouldReturnZeroWhenAllPeopleComeOut() {
        sensorApi.setPeopleCount(100);

        for (int i = 0; i < 100; i++) {
            comeOut();
            timerRepository.incrementTime(1_000_000);
        }

        assertEquals(Double.valueOf(0), stateRepository.getState());
    }

    private void comeInside() {
        timerRepository.incrementTime(100);
        analogSignalRepository.setAnalogValue(FRONT_LASER_PIN, CROSSED);
        sensorApi.loop();

        timerRepository.incrementTime(999_999);
        analogSignalRepository.setAnalogValue(BACK_LASER_PIN, CROSSED);
        sensorApi.loop();

        timerRepository.incrementTime(100);
        analogSignalRepository.setAnalogValue(FRONT_LASER_PIN, NO_CROSSED);
        sensorApi.loop();

        timerRepository.incrementTime(100_000);
        analogSignalRepository.setAnalogValue(BACK_LASER_PIN, NO_CROSSED);
        sensorApi.loop();
    }

    private void comeOut() {
        timerRepository.incrementTime(100);
        analogSignalRepository.setAnalogValue(BACK_LASER_PIN, CROSSED);
        sensorApi.loop();

        timerRepository.incrementTime(999_999);
        analogSignalRepository.setAnalogValue(FRONT_LASER_PIN, CROSSED);
        sensorApi.loop();

        timerRepository.incrementTime(100_000);
        analogSignalRepository.setAnalogValue(BACK_LASER_PIN, NO_CROSSED);
        sensorApi.loop();

        timerRepository.incrementTime(100);
        analogSignalRepository.setAnalogValue(FRONT_LASER_PIN, NO_CROSSED);
        sensorApi.loop();
    }
}

