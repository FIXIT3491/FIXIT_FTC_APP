package com.qualcomm.ftcrobotcontroller.newhardware.FXTSensors;

import com.qualcomm.ftcrobotcontroller.RC;
import com.qualcomm.robotcore.hardware.TouchSensor;

/**
 * Created by FIXIT on 15-08-23.
 * A touch sensor that implements the standard FixedSensor methods
 */
public class FXTTouchSensor extends FXTSensor {

    /**
     * The original touch sensor
     */
    TouchSensor touch;

    /**
     * Creates a new touch sensor impletenting standard Fixed sensor methods
     * @param address The name in the XML config file.
     */
    public FXTTouchSensor(String address) {
        touch = RC.h.touchSensor.get(address);
        sensorType = FTC_TOUCH;
        sensorName = address;
    }//FixedTouchSensor

    /**
     * The first sensor value. To be called by the datalogger
     * @return the first value within the array of value.
     */
    @Override
    public String toString() {
        return "" + getValue();
    }//toString

    /**
     * Gets the name of the sensor
     * @return the name "Touch"
     */
    @Override
    public String getName() {
        return sensorName;
    }//getName

    /**
     * Returns the data from the sensor
     * @return the value of the touch sensor
     */
    public double returnValue() {
        storedValue = touch.getValue();

        return touch.getValue();
    }//getValue

    public boolean isPressed() {
        return touch.isPressed();
    }

    /**
     * Gets the sensor
     * @return the touch sensor
     */
    @Override
    public TouchSensor getHardwareSensor() {
        return touch;
    }//getHardwareSensor

}//FixedTouchSensor
