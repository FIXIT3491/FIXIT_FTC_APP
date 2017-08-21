package org.firstinspires.ftc.teamcode.gamecode;

import android.graphics.Bitmap;
import android.util.Log;

import org.firstinspires.ftc.teamcode.RC;
import org.firstinspires.ftc.teamcode.newhardware.FXTSensors.TrackBall;
import org.firstinspires.ftc.teamcode.opmodesupport.AutoOpMode;
import org.firstinspires.ftc.teamcode.roboticslibrary.FXTCamera;
import org.firstinspires.ftc.teamcode.roboticslibrary.TaskHandler;
import org.firstinspires.ftc.teamcode.robots.Fermion;
import org.firstinspires.ftc.teamcode.robots.Robot;
import org.firstinspires.ftc.teamcode.util.CircleDetector;
import org.firstinspires.ftc.teamcode.util.MathUtils;

import java.util.Arrays;

/**
 * Created by Windows on 2017-04-14.
 */

public class AutoScoreBlue extends AutoOpMode {

    FXTCamera cam;

    @Override
    public void runOp() throws InterruptedException {
        Fermion f = new Fermion(true);

        cam = new FXTCamera(FXTCamera.FACING_BACKWARD, true);

        waitForStart();
        f.startShooterControl();
        f.addVeerCheckRunnable();

        f.capRelease.goToPos("start");

        f.mouse.addAbsoluteCoordinateRunnable(f.imu);

        sleep((int) RC.globalDouble("WaitTime"));
        RC.t.addData("Hello", "Hello");

        f.absoluteTrack(new TrackBall.Point(0, 100), 0.3, false);

        f.absoluteTrack(new TrackBall.Point(60*25.4, 34*25.4), 0.6, true);
        Log.i("AbsoluteInfo", f.mouse.getAbsoluteCoord() + "");
        f.absoluteIMUTurn(-145, 0.7);
        Log.i("AngleEnd", f.getIMUAngle()[0] + "");

        f.shoot();
        f.waitForShooterState(Fermion.LOADED);

        f.shoot();
        f.waitForShooterState(Fermion.LOADING);

        f.absoluteTrack(new TrackBall.Point(56*25.4, 15*25.4), 0.6, true);

        f.absoluteTrack(new TrackBall.Point(8 * 25.4, 24 * 25.4), 0.6, true);

        f.absoluteIMUTurn(-90, 0.7);
        Log.i("AbsoluteInfo", "" + f.mouse.getAbsoluteCoord());

        int angleToFace = -90;

        TaskHandler.pauseTask("Fermion.VEERCHECK");

        clearTimer();
        int i = 1;
        double[] circle = null;
        while (opModeIsActive()) {
            circle = CircleDetector.findBestCircle(cam.photo(), true);

            if (circle != null && circle[0] != -1) {
                break;
            }//if
            if(getMilliSeconds() > 3000){
                if(i < 3){
                    f.imuTurnL(45, 0.5);
                    i++;
                } else {
                    f.imuTurnR(135, 0.5);
                    i = 0;
                }
            }
        }//while

        double angleH = 36 * (circle[1] / circle[3] - 0.5);
//        angleH += Math.signum(angleH) * (10);

        double vHeight = (circle[0] / circle[4]) * 100;

        Log.i("TurningXA", angleH + "");
        Log.i("DistanceXA", vHeight + "");
        Log.i("XACircle", Arrays.toString(circle) + "");

        if (angleH < 0) {
            f.imuTurnL(-angleH, 0.5);
        } else {
            f.imuTurnR(angleH, 0.5);
        }//else

        f.forward(0.4);
        TaskHandler.resumeTask("Fermion.VEERCHECK");

        while (opModeIsActive()) {
            Bitmap bm = cam.photo();
            circle = CircleDetector.findBestCircle2(bm, true);

            angleH = 36 * (circle[1] / circle[3] - 0.5);

            vHeight = (circle[0] / circle[4]) * 100;

            Log.i("TurningXA", angleH + "");
            Log.i("DistanceXA", vHeight + "");
            Log.i("XACircle", Arrays.toString(circle) + "");

            f.setTargetAngle(MathUtils.cvtAngleToNewDomain(f.getTargetAngle() + 0.18 * angleH));

            if (vHeight < 17) {
                break ;
            }//if

        }//while


        f.stop();
//
        TaskHandler.pauseTask("Fermion.VEERCHECK");

        f.imuTurnL(180, 0.5);

        f.backward(0.3);
        f.setCollectorState(Robot.IN);

        clearTimer();
        while (opModeIsActive() && !f.seesBall() && getMilliSeconds() < 4000) {
            idle();
        }//while

        f.stop();
        f.setCollectorState(Robot.STOP);
        f.collector.setPower(1);
        f.door.goToPos("open");

        f.absoluteTrack(new TrackBall.Point(36*25.4, 24*25.4), 0.6, true);
        f.setCollectorState(Robot.STOP);
        Log.i("AbsoluteInfo", f.mouse.getAbsoluteCoord() + "");
        f.absoluteIMUTurn(-90, 0.7);
        Log.i("AngleEnd", f.getIMUAngle()[0] + "");

        f.shoot();
        f.waitForShooterState(Fermion.LOADED);
/*
        double[] circle = null;

        newPathLoop: while (opModeIsActive()) {
            f.absoluteIMUTurn(angleToFace, 0.7);

            circle = f.lookForCircleWithTimeout(cam, 3000);

            Log.i("LOGGING", Arrays.toString(circle));


            if (circle == null || circle[0] == -1) {
                switch(angleToFace) {
                    case -90: angleToFace = -180; continue;
                    case -180: angleToFace = -45; continue;
                    case -45: angleToFace = -90; continue;
                }//switch
            } else {
                double angleH = 36 * (circle[1] / circle[3] - 0.5);

                double vHeight = (circle[0] / circle[4]) * 100;

                Log.i("TurningXA", angleH + "");
                Log.i("DistanceXA", vHeight + "");
                Log.i("XACircle", Arrays.toString(circle) + "");

                if (angleH < 0) {
                    f.imuTurnL(-angleH, 0.5);
                } else {
                    f.imuTurnR(angleH, 0.5);
                }//else

                f.forward(0.4);
                TaskHandler.pauseTask("Fermion.VEERCHECK");




                while (opModeIsActive()) {
                    circle = CircleDetector.findBestCircle2(cam.photo(), true);

                    angleH = 36 * (circle[1] / circle[3] - 0.5);

                    vHeight = (circle[0] / circle[4]) * 100;

                    Log.i("TurningXA", angleH + "");
                    Log.i("DistanceXA", vHeight + "");
                    Log.i("XACircle", Arrays.toString(circle) + "");

                    f.setTargetAngle(MathUtils.cvtAngleToNewDomain(f.getTargetAngle() + 0.18 * angleH));

                    if (vHeight < 20) {
                        break;
                    }//if

                }//while

                Log.i("HELLO!", "hello...");

                f.stop();

                f.imuTurnL(180, 0.5);

                f.backward(0.3);
                f.setCollectorState(Robot.IN);

                long start = System.currentTimeMillis();

                while (opModeIsActive() && !f.seesBall()) {

                    if (System.currentTimeMillis() - start > 1500) {
                        f.absoluteTrack(new TrackBall.Point(0 * 25.4, 24 * 25.4), 0.6, true);

                        switch(angleToFace) {
                            case -90: angleToFace = -180; break;
                            case -180: angleToFace = -45; break;
                            case -45: angleToFace = -90; break;
                        }//switch

                        continue newPathLoop;
                    }//if
                }//while

                f.stop();

                break;
            }//else
        }//while
*/


    }

    public void stopOpMode() {
        cam.destroy();
    }
}
