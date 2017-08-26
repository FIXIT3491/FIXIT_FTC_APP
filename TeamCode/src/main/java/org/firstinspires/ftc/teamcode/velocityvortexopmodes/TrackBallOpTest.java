package org.firstinspires.ftc.teamcode.velocityvortexopmodes;

import android.util.Log;

import org.firstinspires.ftc.teamcode.newhardware.FXTSensors.TrackBall;
import org.firstinspires.ftc.teamcode.opmodesupport.LinearTeleOpMode;
import org.firstinspires.ftc.teamcode.robots.Fermion;
import org.firstinspires.ftc.teamcode.util.MathUtils;

/**
 * Created by Windows on 2017-04-14.
 */

public class TrackBallOpTest extends LinearTeleOpMode {

    Fermion proton;

    @Override
    public void initialize() {

        proton = new Fermion(true);

    }

    public void onStart() {
        proton.mouse.addAbsoluteCoordinateRunnable(proton.imu);
        proton.capRelease.goToPos("start");
    }

    @Override
    public void loopOpMode() {

        /*
        STRAFING & VEERING
         */
        double theta = Math.atan2(-1 * joy1.x1(), joy1.y1());
        double speed = ((joy1.rightBumper())? 0.3 : 1.0) * Math.hypot(joy1.y1(), joy1.x1());

        proton.strafe(Math.toDegrees(theta), speed, false);
        proton.veer(joy1.x2() / 2.0, false, false);

        proton.usePlannedSpeeds();

        if (joy1.buttonA()) {
            TrackBall.Point absolute = proton.mouse.getAbsoluteCoord().multiply(3 * Math.PI * 25.4 / 1440);
            double degrees = -Math.toDegrees(Math.atan2(absolute.getY(), absolute.getX())) - 90;

            Log.i("Degrees", degrees + "");
            Log.i("Distance", absolute.hypot() + "");
            degrees += proton.getIMUAngle()[0];

            degrees = MathUtils.cvtAngleToNewDomain(degrees);


            if (degrees < 0) {
                proton.imuTurnL(-degrees, 0.6);
            } else {
                proton.imuTurnR(degrees, 0.6);
            }

            absolute = proton.mouse.getAbsoluteCoord().multiply(3 * Math.PI * 25.4 / 1440);


            proton.track(0, absolute.hypot(), 0.6);
//            proton.absoluteIMUTurn(0, 0.6);
        }//if
    }

    @Override
    public void stopOpMode() {
        
    }
}
