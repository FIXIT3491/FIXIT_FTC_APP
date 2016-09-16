package org.firstinspires.ftc.teamcode.newhardware.FXTSensors;

import android.hardware.Sensor;

import org.firstinspires.ftc.teamcode.newhardware.FXTDevice;
import com.qualcomm.robotcore.hardware.HardwareDevice;

/**
 * Created by FIXIT on 15-08-23.
 * Standard sensor class that combines internal and external sensors
 */
public abstract class FXTSensor implements FXTDevice {

    /**
     * Onboard Accelerometer
     */
    public final static int ACCEL = Sensor.TYPE_ACCELEROMETER;

    /**
     * Onboard Magnetometer
     */
    public final static int MAG = Sensor.TYPE_MAGNETIC_FIELD;
    /**
     * Onboard orientation sensor
     * @deprecated
     */
    @Deprecated
    public final static int ORIENT = Sensor.TYPE_ORIENTATION;

    /**
     * Onboard Gyro sensor
     */
    public final static int GYRO = Sensor.TYPE_GYROSCOPE;

    /**
     * Onboard Gravity sensor.
     */
    public final static int GRAV = Sensor.TYPE_GRAVITY;

    /**
     * Onboard Light sensor
     */
    public final static int LIGHT = Sensor.TYPE_LIGHT;

    /**
     * Onboard Thermometer
     */
    public final static int TEMP = Sensor.TYPE_TEMPERATURE;

    /**
     * Onboard Proximity Sensor
     */
    public final static int PROX = Sensor.TYPE_PROXIMITY;

    /**
     * Onboard Linear Accelerometer
     */
    public final static int LINEAR_ACC = Sensor.TYPE_LINEAR_ACCELERATION;

    /**
     * Onboard Rotation Vector Sensor
     */
    public final static int ROTATION_VECTOR = Sensor.TYPE_ROTATION_VECTOR;

    /**
     * External HiTechnic Accelerometer sensor
     */
    public final static int FTC_ACCEL = 1011;


    /**
     * External HiTechnic Compass sensor
     */
    public final static int FTC_COMPASS = 1012;


    /**
     * External HiTechnic Gyro sensor
     */
    public final static int FTC_GYRO = 1013;


    /**
     * External HiTechnic or Modern Robotics IR sensor
     */
    public final static int FTC_IR = 1014;


    /**
     * External HiTechnic or Modern Robotics EOPD
     */
    public final static int FTC_OPTIC_DIST = 1015;


    /**
     * External LEGO or Modern Robotics Touch sensor
     */
    public final static int FTC_TOUCH = 1016;


    /**
     * External LEGO Ultrasonic (Sonar) sensor
     */
    public final static int FTC_ULTRASONIC = 1017;


    /**
     * External Voltmeter for Tetrix battery
     */
    public final static int FTC_VOLTAGE = 1018;

    /**
     * Hi-technic colour sensor
     */
    public final static int FTC_COLOUR = 1019;

    /**
     *
     * Hi-technic light sensor
     */
    public final static int FTC_LIGHT = 1020;

    public enum SensorFilter {
        LOW_PASS,
        AVG,
        NONE
    }

    /**
     * The type of sensor
     */
    public int sensorType;

    /**
     * Name of sensor
     */
    protected String sensorName;

    /**
     * Current stored value of sensor if the sensor returns a double
     */
    public double storedValue = 0;

    /**
     * Current stored storedValues of sensor if the sensor returns an array
     */
    public double[] storedValues = null;

    /**
     * Boolean stating whether this sensor returns a single value or an array of storedValues
     */
    boolean oneValue = true;

    double filterGain = 0.25;

    SensorFilter filter = SensorFilter.NONE;

    public void setFilterGain (double newGain) {
        filterGain = newGain;
    }

    /**
     * An array of float with different storedValues. Used by both FTC and on board sensors
     * @return zero unless overridden
     */
    public double[] getValues(){

        double[] newValues = returnValues();

        if (storedValues != null) {
            switch (filter) {
                case LOW_PASS: {
                    for (int i = 0; i < newValues.length; i++) {
                        newValues[i] = SensorUtils.lowPassFilter(0.25, newValues[i], storedValues[i]);
                    }
                }
                break;
                case AVG: {
                    for (int i = 0; i < newValues.length; i++) {
                        newValues[i] = SensorUtils.avg(newValues[i], storedValues[i]);
                    }
                }
                break;
            }
        }

        storedValues = newValues;

        return newValues;
    }//getValues

    /**
     * Method for returning single FTC storedValues
     * @return value of sensor with filter
     */
    public double getValue() {

        double newValue = returnValue();

        if (storedValue != 0) {

            switch (filter) {
                case LOW_PASS:
                    newValue = SensorUtils.lowPassFilter(0.25, newValue, storedValue);
                    break;
                case AVG:
                    newValue = SensorUtils.avg(newValue, storedValue);
                    break;
            }

        }

        storedValue = newValue;

        return newValue;

    }//getValue

    protected double[] returnValues() {

        return null;
    }

    protected double returnValue() {
        return 0;
    }

    /**
     * Get the sensor name
     * @return The name of the sensor
     */
    public String getName() {
        return sensorName;
    }

    /**
     * Gets the standard class that all FTC sensors extend
     * @return the superclass of an FTC sensor, or null if undefined
     */
    public HardwareDevice getHardwareSensor() {
        return null;
    }

    /**
     * Sets the filter type of this sensor
     * @param filter the new filter type that this sensor should use
     */
    public void setFilter (SensorFilter filter) {
        this.filter = filter;
    }


    public void check() {
        getValues();
        getValue();
    }//check

}//FixedSensor
