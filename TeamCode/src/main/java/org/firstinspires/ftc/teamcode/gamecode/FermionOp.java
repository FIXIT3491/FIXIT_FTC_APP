package org.firstinspires.ftc.teamcode.gamecode;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.opmodesupport.LinearTeleOpMode;
import org.firstinspires.ftc.teamcode.roboticslibrary.TaskHandler;
import org.firstinspires.ftc.teamcode.util.MathUtils;
import org.firstinspires.ftc.teamcode.robots.*;

/**
 * Created by Windows on 2016-11-04.
 */
@TeleOp
public class FermionOp extends LinearTeleOpMode {
    Fermion charm;
    VuforiaTrackables beacons;
    VuforiaTrackableDefaultListener gears;
    VuforiaTrackableDefaultListener tools;

    @Override
    public void initialize() {
        charm = new Fermion(false);
        TaskHandler.removeAllTasks();
    }

    @Override
    public void loopOpMode() {
        double theta = 0;

        if(joy1.y1() == 0 && joy1.x1() == 0){
            charm.rightFore.setPower(Math.round(-joy1.x2()) / 3.0);
            charm.rightBack.setPower(Math.round(-joy1.x2()) / 3.0);
            charm.leftFore.setPower(Math.round(joy1.x2()) / 3.0);
            charm.leftBack.setPower(Math.round(joy1.x2()) / 3.0);

            Log.i("Speeds", charm.leftBack.getPower() + ", " + charm.leftFore.getPower() + ", " + charm.rightBack.getPower() + ", " + charm.rightFore.getPower());
        } else {
            theta = Math.abs(Math.atan2(joy1.x1(), -joy1.y1()));
            Log.i(TAG, "loopOpMode: " + theta);

            if(joy1.x1() < 0) theta *= -1;

            double speed = (joy1.rightBumper())?0.3:1;
            charm.strafe(Math.toDegrees(MathUtils.roundToNearest(theta, Math.PI / 4, -Math.PI, Math.PI)), speed, true);
        }




        telemetry.addData("FL", charm.leftFore.returnCurrentState());
        telemetry.addData("FR", charm.rightFore.returnCurrentState());
        telemetry.addData("BL", charm.leftBack.returnCurrentState());
        telemetry.addData("BR", charm.rightBack.returnCurrentState());
    }

    @Override
    public void stopOpMode() {

    }
}//FermionOp