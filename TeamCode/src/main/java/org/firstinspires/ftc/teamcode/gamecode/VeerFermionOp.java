package org.firstinspires.ftc.teamcode.gamecode;

import android.util.Log;

import org.firstinspires.ftc.teamcode.opmodesupport.TeleOpMode;
import org.firstinspires.ftc.teamcode.robots.Fermion;
import org.firstinspires.ftc.teamcode.util.MathUtils;

/**
 * Created by Windows on 2016-12-04.
 */
public class VeerFermionOp extends TeleOpMode {

    Fermion tau;

    @Override
    public void initialize() {
        tau = new Fermion(false);
    }

    @Override
    public void loopOpMode() {

        double theta = Math.abs(Math.atan2(joy1.x1(), -joy1.y1()));

        if(joy1.x1() < 0) theta *= -1;

        double speed = (joy1.rightBumper())?0.3:Math.hypot(joy1.y1() , joy1.x1());

        tau.strafe(Math.toDegrees(MathUtils.roundToNearest(theta, Math.PI / 4, -Math.PI, Math.PI)), speed);

        tau.veer(-Math.round(joy1.x2()) / 3.0, false);

        Log.i("Speeds", tau.leftBack.getPower() + ", " + tau.leftFore.getPower() + ", " + tau.rightBack.getPower() + ", " + tau.rightFore.getPower());

    }
}
