package com.qualcomm.ftcrobotcontroller.roboticslibrary;

import android.util.Log;

import com.qualcomm.ftcrobotcontroller.RC;
import com.qualcomm.ftcrobotcontroller.newhardware.FXTSensors.FXTSensor;
import com.qualcomm.robotcore.robocol.Telemetry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by FIXIT on 15-08-23.
 */
public class FXTTelemetry {

    private Telemetry telemetry;
    public DataWriter out;
    private List<FXTSensor> sensors = new ArrayList<FXTSensor>();

    private Thread dataLogging;

    public void setTelemetry (Telemetry telem) {
        this.telemetry = telem;
    }

    public void setDataLogFile(String fileName, boolean overWrite) {
        try {
            out = new DataWriter(fileName, overWrite);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDataLogFile(String fileName) {
        setDataLogFile(fileName, true);
    }

    public void close() {
        if (out != null)
            out.closeWriter();
    }

    //methods to quickly telemetry something
    public void addData(String data) {
        telemetry.addData("Data", data);
    }

    public void addData(int data) {
        telemetry.addData("Data", data);
    }

    public void addData(float data) {
        telemetry.addData("Data", data);
    }

    public void addData(double data) {
        telemetry.addData("Data", data);
    }

    public void addData(byte data) {
        telemetry.addData("Data", data);
    }

    //methods to normally telemetry something
    //will only overwrite any uses if a key is used multiple times

    public void addData(String key, String data) {
        telemetry.addData(key, data);
    }

    public void addData(String key, int data) {
        telemetry.addData(key, data);
    }

    public void addData(String key, float data) {
        telemetry.addData(key, data);
    }

    public void addData(String key, double data) {
        telemetry.addData(key, data);
    }

    public void addData(String key, byte data) {
        telemetry.addData(key, data);
    }


    //One-time data logging primitives

    public void dataLogData(String key, String data) {
        telemetry.addData(key, data);

        if (out != null)
            out.write(key + ": " + data);
    }

    public void dataLogData(String data) {
        if (out != null)
            out.write(data);
    }

    public void dataLogData(String key, double data) {
        telemetry.addData(key, data);

        if (out != null)
            out.write(key + ": " + data);
    }

    public void dataLogData(double data) {
        if (out != null)
            out.write(data);
    }

    public void dataLogData(String key, int data) {
        telemetry.addData(key, data);

        if (out != null)
            out.write(key + ": " + data);
    }

    public void dataLogData(int data) {
        if (out != null)
            out.write(data);
    }


    //DataLogging sensors

    public void dataLogSensor (FXTSensor sensor) {
        if (out != null)
            out.write(sensor.getName() + ": " + sensor); //FXTSensor.toString() is overridden
    }

    public void addSensorToDataLog (FXTSensor sensor) {
        if (sensors.contains(sensor)) {
            Log.e("DataLog", "Sensor is already being datalogged!");
        } else {
            sensors.add(sensor);
        }
    }

    public void removeSensorFromDataLog(FXTSensor sensor) {
        if (!sensors.contains(sensor)) {
            Log.e("DataLog", "Sensor isn't being datalogged!");
        } else {
            sensors.remove(sensor);
        }
    }

    public void dataLogSensorList () {

        if (out != null && !sensors.isEmpty()) {
            for (int i = 0; i < sensors.size(); i++) {
                out.write(sensors.get(i).getName() + ": " + sensors.get(i));
            }
        }
    }

    public void beginDataLogging() {
        dataLogging = new Thread() {
            public void run() {
                dataLog();
            }
        };
        dataLogging.start();
    }

    public void stopDataLogging() {
        out.closeWriter();
    }

    public void dataLog() {
        //User-defined method
    }
}
