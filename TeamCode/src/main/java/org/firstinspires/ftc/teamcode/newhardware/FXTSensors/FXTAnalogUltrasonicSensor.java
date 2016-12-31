package org.firstinspires.ftc.teamcode.newhardware.FXTSensors;

import com.qualcomm.robotcore.hardware.AnalogInput;

import org.firstinspires.ftc.teamcode.RC;
import org.firstinspires.ftc.teamcode.robots.Fermion;

/**
 * Created by Windows on 2016-12-30.
 */

public class FXTAnalogUltrasonicSensor extends FXTSensor {
    AnalogInput ultra;

    public FXTAnalogUltrasonicSensor(String name){
        ultra = RC.h.analogInput.get(name);
    }

    public double getDistance(){
        return ultra.getVoltage() * 578;
    }

    @Override
    public double getValue() {
        return getDistance();
    }
}
