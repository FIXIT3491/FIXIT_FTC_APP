package org.firstinspires.ftc.teamcode.robots;


import org.firstinspires.ftc.teamcode.newhardware.Motor;
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
