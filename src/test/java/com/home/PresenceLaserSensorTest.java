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

        sensorApi.setup();
    }

    @Test
    public void shouldReturnZeroPeopleAfterSetup() {
        sensorApi.setup();

        assertEquals(Double.valueOf(0), stateRepository.getState());
    }

    @Test
    public void shouldIncrementPeopleCountWhenCrossedTwoLaser() {
        Double peopleCount = stateRepository.getState();

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

        assertEquals(Double.valueOf(peopleCount + 1), stateRepository.getState());
    }

    @Test
    public void shouldReturnZeroPeopleWhenCrossedOneLaser() {
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


}
