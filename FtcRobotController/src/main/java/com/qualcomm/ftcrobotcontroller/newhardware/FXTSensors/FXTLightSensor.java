package com.qualcomm.ftcrobotcontroller.newhardware.FXTSensors;

import com.qualcomm.ftcrobotcontroller.RC;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.LightSensor;

/**
 * Created by FIXIT on 15-11-08.
 */
public class FXTLightSensor extends FXTSensor {

    LightSensor light;

    public FXTLightSensor (String name) {

        light = RC.h.lightSensor.get(name);
        light.enableLed(true);
        sensorType = FTC_LIGHT;

    }

    public String toString() {
        double[] values = getValues();
        return "Val: " + values[0] + ", Raw: " + values[1];
    }

    protected double returnValue() {
        return light.getLightDetected();
    }

    protected double[] returnValues() {
        return new double[] {light.getLightDetected(), light.getLightDetectedRaw()};
    }

    public void enableLed (boolean b) {
        light.enableLed(b);
    }

    public LightSensor getHardwareSensor() {
        return light;
    }

}
