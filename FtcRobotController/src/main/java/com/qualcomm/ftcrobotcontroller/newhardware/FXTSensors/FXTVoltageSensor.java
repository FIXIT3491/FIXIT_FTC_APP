package com.qualcomm.ftcrobotcontroller.newhardware.FXTSensors;

import com.qualcomm.ftcrobotcontroller.RC;
import com.qualcomm.robotcore.hardware.VoltageSensor;

/**
 * Created by FIXIT on 15-08-23.
 */
public class FXTVoltageSensor extends FXTSensor {

    VoltageSensor volt;

    public FXTVoltageSensor(String address) {
        volt = RC.h.voltageSensor.get(address);
        sensorType = FTC_VOLTAGE;
        sensorName = address;
    }

    @Override
    public String toString() {
        return "" + getValue();
    }

    @Override
    public String getName() {
        return "Voltage";
    }

    public double returnValue() {
        storedValue = volt.getVoltage();

        return storedValue;
    }

    @Override
    public VoltageSensor getHardwareSensor() {
        return volt;
    }
}
