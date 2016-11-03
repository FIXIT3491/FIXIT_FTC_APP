package org.firstinspires.ftc.teamcode.gamecode;

import org.firstinspires.ftc.teamcode.opmodesupport.TeleOpMode;
import org.firstinspires.ftc.teamcode.robots.Fermion;

/**
 * Created by FIXIT on 16-10-14.
 */
public class MecanumDriveTest extends TeleOpMode {
    Fermion rbt;

    @Override
    public void initialize() {
        rbt = new Fermion(false);
    }

    @Override
    public void loopOpMode() {
         //TODO: put the actual tele op back
    }

    public double deadzone(double dead, double val) {
        return (Math.abs(val) > dead)? val : 0;
    }
}
