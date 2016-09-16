package com.qualcomm.ftcrobotcontroller.newhardware.FXTSensors;

/**
 * Created by FIXIT on 15-09-10.
 */
public class OnBoardSensor extends FXTSensor {

    private OnBoardSensorManager osm;

    /**
     * Creates a new MySensor and registers the listener to be the sensor manager
     * @param sensorType the type of sensor. Must be standard sensor types
     * @param sensorManager the sensor manager that updates the storedValues
     */
    public OnBoardSensor (int sensorType, OnBoardSensorManager sensorManager) {
        super.sensorType = sensorType;
        sensorManager.addSensor(sensorType);
        this.osm = sensorManager;
    }//MySensor

    /**
     * Creates a new MySensor and registers the listener to be the sensor manager
     * @param sensorType the type of sensor. Must be standard sensor types
     * @param name the name of the sensor
     * @param sensorManager the sensor manager that updates the storedValues
     */
    public OnBoardSensor (int sensorType, String name, OnBoardSensorManager sensorManager) {
        super.sensorType = sensorType;
        sensorManager.addSensor(sensorType);
        this.osm = sensorManager;
        sensorName = name;
    }//MySensor

    /**
     * method to get the type of the sensor in string form
     * @return The type of the sensor in a string
     */
    public String getTypeString() {
        switch (sensorType){
            case ACCEL: return "Accelerometer";
            case MAG: return "Magnetometer";
            case ORIENT: return "Orientation";
            case GRAV: return "Gravity";
            case LIGHT: return "Light";
            case TEMP: return "Temperature";
            case PROX: return "Proximity";
            case LINEAR_ACC: return "Linear Acceleration";
            case ROTATION_VECTOR: return "Rotation Vector";
            default: return "Gyroscope";
        }//switch
    }//getName

    /**
     * Get the type of the sensor
     * @return the sensor type.
     */
    public int type() {
        return sensorType;
    }//type

    /**
     * Get the current storedValues of the sensor
     * @return the current storedValues
     */
    public double[] returnValues() {
        return osm.getValues(super.sensorType);
    }//getValues

}