package com.home;

import org.junit.Before;
import org.junit.Test;

import static com.home.GlobalConstant.*;
import static org.junit.Assert.assertEquals;

public class PresenceLaserSensorTest {
    private StateRepository stateRepository;
    private TimerRepository timerRepository;
    private AnalogSignalRepository analogSignalRepository;
    private AbstractSensorApi sensorApi;

    @Before
    public void setUp() {
        stateRepository = new StateRepository();
        timerRepository = new TimerRepository();
        analogSignalRepository = new AnalogSignalRepository();

        analogSignalRepository.setAnalogValue(FRONT_LASER_PIN, NO_CROSSED);
        analogSignalRepository.setAnalogValue(BACK_LASER_PIN, NO_CROSSED);

        sensorApi = new PresenceLaserSensor(stateRepository, timerRepository, analogSignalRepository);
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

    @Test
    public void shouldDecrementPeopleWhenCrossedTwoLaser() {
        sensorApi.setPeopleCount(1);

        comeOut();

        assertEquals(Double.valueOf(0), stateRepository.getState());
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
}
