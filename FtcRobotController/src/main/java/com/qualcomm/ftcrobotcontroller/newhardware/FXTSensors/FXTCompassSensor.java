package com.qualcomm.ftcrobotcontroller.newhardware.FXTSensors;

import com.qualcomm.ftcrobotcontroller.RC;
import com.qualcomm.robotcore.hardware.CompassSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by FIXIT on 15-08-23.
 */
public class FXTCompassSensor extends FXTSensor {

    CompassSensor compass;

    public FXTCompassSensor(String address) {
        compass = RC.h.compassSensor.get(address);
        sensorType = FTC_COMPASS;
        sensorName = address;
    }

    @Override
    public String toString() {
        return "" + getValue();
    }

    @Override
    public String getName() {
        return "Compass";
    }


    public double returnValue() {
        return compass.getDirection();

    }

    @Override
    public CompassSensor getHardwareSensor() {
        return compass;
    }

}
