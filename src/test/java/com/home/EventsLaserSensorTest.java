package com.home;

import static com.home.GlobalConstant.BACK_LASER_PIN;
import static com.home.GlobalConstant.FRONT_LASER_PIN;
import static com.home.GlobalConstant.NO_CROSSED;

import org.junit.Before;

public class EventsLaserSensorTest extends AbstractLaserSensorTest{
    @Override
    void setSensorApi() {
        sensorApi = new EventsLaserSensorApi(stateRepository, timerRepository, analogSignalRepository);
    }

    @Before
    public void setUp() {
        stateRepository = new StateRepository();
        timerRepository = new TimerRepository();
        analogSignalRepository = new AnalogSignalRepository();

        analogSignalRepository.setAnalogValue(FRONT_LASER_PIN, NO_CROSSED);
        analogSignalRepository.setAnalogValue(BACK_LASER_PIN, NO_CROSSED);

        setSensorApi();
    }
}
