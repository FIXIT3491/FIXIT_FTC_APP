package com.qualcomm.ftcrobotcontroller.newhardware.FXTSensors;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by FIXIT on 15-11-08.
 */
public class FXTColourSensor extends FXTSensor {

    ColorSensor colour;

    public FXTColourSensor (HardwareMap hardware, String name) {

        colour = hardware.colorSensor.get(name);
        sensorType = FTC_COLOUR;
        colour.enableLed(true);

    }

    public String toString() {
        return "R:" + colour.red() + "B:" + colour.blue();
    }

    public double returnValue() {
        return colour.red();
    }

    public ColorSensor getHardwareSensor() {
        return colour;
    }

}
