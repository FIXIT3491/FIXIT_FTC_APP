package com.qualcomm.ftcrobotcontroller.robots;


import com.qualcomm.ftcrobotcontroller.newhardware.Motor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by FIXIT on 8/19/2015.
 */
public class Rover extends Robot {

    public Rover() {
        super();
    }

    public Rover(Motor driveL, Motor driveR) {
        super(driveL, driveR);
    }

}
