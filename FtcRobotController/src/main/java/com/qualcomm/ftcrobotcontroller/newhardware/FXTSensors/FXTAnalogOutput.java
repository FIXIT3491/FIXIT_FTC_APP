package com.qualcomm.ftcrobotcontroller.newhardware.FXTSensors;

import com.qualcomm.ftcrobotcontroller.RC;
import com.qualcomm.robotcore.hardware.AnalogOutput;

/**
 * Created by Windows on 2016-07-07.
 */
public class FXTAnalogOutput extends FXTSensor {

    private AnalogOutput aout;
    public static final byte VOLTAGE = 0;
    public static final byte SINE = 1;
    public static final byte SQUARE = 2;
    public static final byte TRIANGLE = 3;

    public FXTAnalogOutput(String name) {
        aout = RC.h.analogOutput.get(name);

    }

    public void setVoltage(double voltage) {
        aout.setAnalogOutputMode(VOLTAGE);
        voltage += 4;
        voltage *= 127.875;
        aout.setAnalogOutputVoltage((int) Math.round(voltage));
    }

}
