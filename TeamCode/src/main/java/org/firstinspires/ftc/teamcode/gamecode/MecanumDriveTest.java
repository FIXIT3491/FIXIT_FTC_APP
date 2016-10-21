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
        double degree = Math.toDegrees(Math.atan2(joy1.y1(), joy1.x1()));

        if (joy1.x1() < 0) {
            degree += 180;
        }//if

        degree = (360 + degree) % 360;

        degree *= -1;
        degree += 360 + 90;
        degree %= 360;

        if (degree > 180) {
            degree -= 360;
        }//if

        rbt.strafe(degree, deadzone(0.09, Math.hypot(joy1.y1(), joy1.x1())));
    }

    public double deadzone(double dead, double val) {
        return (Math.abs(val) > dead)? val : 0;
    }
}
