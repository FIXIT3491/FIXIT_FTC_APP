package org.firstinspires.ftc.teamcode.velocityvortexopmodes;

import android.graphics.Bitmap;
import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.opmodesupport.AutoOpMode;
import org.firstinspires.ftc.teamcode.roboticslibrary.FXTCamera;
import org.firstinspires.ftc.teamcode.roboticslibrary.TaskHandler;
import org.firstinspires.ftc.teamcode.robots.Fermion;
import org.firstinspires.ftc.teamcode.robots.Robot;
import org.firstinspires.ftc.teamcode.util.CircleDetector;
import org.firstinspires.ftc.teamcode.util.MathUtils;

import java.util.Arrays;

/**
 * Created by FIXIT on 16-10-07.
 */
@Autonomous
public class AutoBallCollector extends AutoOpMode {

    /*
    RANGE: 285 to 500 mm away from beacon
    TARGET: 370mm
     */
    FXTCamera cam;

    @Override
    public void runOp() throws InterruptedException {

        Fermion f = new Fermion(true);
        cam = new FXTCamera(FXTCamera.FACING_BACKWARD, true);


        waitForStart();
        f.addVeerCheckRunnable();
        TaskHandler.pauseTask("Fermion.VEERCHECK");

        f.capRelease.goToPos("start");

        double[] circle = null;
        while (opModeIsActive()) {
            circle = CircleDetector.findBestCircle(cam.photo(), true);

            if (circle[0] != -1) {
                break;
            }//if
        }//while

        double angleH = 36 * (circle[1] / circle[3] - 0.5);
//        angleH += Math.signum(angleH) * (10);

        double vHeight = (circle[0] / circle[4]) * 100 - 5;

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

            if (vHeight < 15) {
                break;
            }//if

        }//while

        f.stop();
//
        TaskHandler.pauseTask("Fermion.VEERCHECK");

        f.imuTurnL(180, 0.5);

        f.backward(0.3);
        f.setCollectorState(Robot.IN);

        while (opModeIsActive() && !f.seesBall()) {
            idle();
        }//while

        f.stop();

//        f.track(0, distanceToBall, 0.25);
//
//        circle = CircleDetector.findBestCircle2(cam.photo());
//
//        angleH  = 36 * (circle[1] / circle[3] - 0.5);
//
//        Log.i("TurningXA", "" + angleH);
//        Log.i("XACircle", "" + Arrays.toString(circle));
//
//
//        if (angleH < 0) {
//            f.imuTurnL(-angleH, 0.5);
//        } else {
//            f.imuTurnR(angleH, 0.5);
//        }//else

//        if (angleH < 0) {
//            f.imuTurnR(180 + angleH, 0.5);
//        } else {
//            f.imuTurnL(180 - angleH, 0.5);
//        }//else

    }//runOp


    public void stopOpMode() {
        if (cam != null)
            cam.destroy();
    }
}
