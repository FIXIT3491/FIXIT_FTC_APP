package org.firstinspires.ftc.teamcode.gamecode;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.opmodesupport.AbstractTest;
import org.firstinspires.ftc.teamcode.opmodesupport.TeleOpMode;
import org.firstinspires.ftc.teamcode.robots.Fermion;
import org.firstinspires.ftc.teamcode.robots.Robot;
import org.firstinspires.ftc.teamcode.util.MathUtils;

/**
 * Created by FIXIT on 2016-12-23.
 */
@TeleOp
public class AbsoluteStrafeVeerOp extends TeleOpMode {

    Fermion top;
    int collectorState = Robot.STOP;

    @Override
    public void initialize() {
        top = new Fermion(true);
    }

    @Override
    public void loopOpMode() {

        double theta = Math.atan2(joy1.x1(), -joy1.y1());

        theta = MathUtils.cvtAngleToNewDomain(theta - Math.toRadians(top.getIMUAngle()[0]));

        double speed = (joy1.rightBumper())? 0.3 : Math.hypot(joy1.y1(), joy1.x1());

        top.strafe(Math.toDegrees(theta), speed, false);

        top.veer(joy1.x2() / 2.0, false, false);

        top.usePlannedSpeeds();

        if(collectorState == Robot.IN && (joy2.rightBumper())) {
            collectorState = Robot.STOP;
        } else if (joy2.rightBumper()) {
            collectorState = Robot.IN;
        }//else

        top.setCollectorState(collectorState);

        if (joy2.rightTrigger()) {
            top.setCollectorState(Robot.OUT);
        }//if
        
        Log.i("thetaPlanned", theta + "");
        Log.i("PlannedSpeeds", top.leftFore.getPlannedSpeed() + ", " + top.leftBack.getPlannedSpeed() + ", " + top.rightBack.getPlannedSpeed() + ", " + top.rightFore.getPlannedSpeed());
        Log.i("Speeds", top.leftBack.getPower() + ", " + top.leftFore.getPower() + ", " + top.rightBack.getPower() + ", " + top.rightFore.getPower());
    }//loopOpMode
}//AbsoluteStrafeOp
