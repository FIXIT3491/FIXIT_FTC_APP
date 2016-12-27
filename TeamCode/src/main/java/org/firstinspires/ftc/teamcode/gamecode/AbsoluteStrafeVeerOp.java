package org.firstinspires.ftc.teamcode.gamecode;

import android.util.Log;

import org.firstinspires.ftc.teamcode.opmodesupport.TeleOpMode;
import org.firstinspires.ftc.teamcode.robots.Fermion;
import org.firstinspires.ftc.teamcode.util.MathUtils;

/**
 * Created by FIXIT on 2016-12-23.
 */

public class AbsoluteStrafeVeerOp extends TeleOpMode {

    Fermion top;

    @Override
    public void initialize() {
        top = new Fermion(true);
    }

    @Override
    public void loopOpMode() {

        double theta = Math.atan2(joy1.x1(), joy1.y1());

        theta = MathUtils.cvtAngleToNewDomain(theta - top.getIMUAngle()[0]);

        double speed = (joy1.rightBumper())? 0.3 : Math.hypot(joy1.y1(), joy1.x1());

        top.strafe(Math.toDegrees(MathUtils.roundToNearest(theta, Math.PI / 4, -Math.PI, Math.PI)), speed);

        top.veer(Math.round(joy1.x2()) / 3.0, false);

        Log.i("Speeds", top.leftBack.getPower() + ", " + top.leftFore.getPower() + ", " + top.rightBack.getPower() + ", " + top.rightFore.getPower());
    }//loopOpMode
}//AbsoluteStrafeOp
