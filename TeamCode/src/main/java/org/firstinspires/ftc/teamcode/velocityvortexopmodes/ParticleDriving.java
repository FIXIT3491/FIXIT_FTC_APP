package org.firstinspires.ftc.teamcode.velocityvortexopmodes;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.opmodesupport.AutoOpMode;
import org.firstinspires.ftc.teamcode.roboticslibrary.FXTCamera;
import org.firstinspires.ftc.teamcode.robots.Fermion;
import org.firstinspires.ftc.teamcode.robots.Robot;
import org.firstinspires.ftc.teamcode.util.CircleDetector;
import org.firstinspires.ftc.teamcode.util.MathUtils;

import java.util.Arrays;

/**
 * Created by FIXIT on 2017-04-15.
 */
@Autonomous
public class ParticleDriving extends AutoOpMode {

    Fermion f;
    FXTCamera cam;

    @Override
    public void runOp() throws InterruptedException {

        f = new Fermion(true);
        cam = new FXTCamera(FXTCamera.FACING_BACKWARD, true);

        waitForStart();

        int angleToFace = 0;

        double[] circle = null;

        while (opModeIsActive()) {
            f.absoluteIMUTurn(angleToFace, 0.7);

            circle = f.lookForCircleWithTimeout(cam, 3000);

            Log.i("LOGGING", Arrays.toString(circle));


            if (circle == null || circle[0] == -1) {
                switch(angleToFace) {
                    case 0: angleToFace = -45; continue;
                    case -45: angleToFace = 45; continue;
                    case 45: angleToFace = 0; continue;
                }//switch
            }//if

            break;
        }//while

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

        f.forward(0.2);

        long start = System.currentTimeMillis();

        while (opModeIsActive()) {
            circle = CircleDetector.findBestCircle(cam.photo(), true);

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

        while (opModeIsActive() && !f.seesBall()) {
            idle();
        }//while

        f.stop();
    }

    public void stopOpMode(){
//        f.stop();
//        cam.destroy();
    }
}
