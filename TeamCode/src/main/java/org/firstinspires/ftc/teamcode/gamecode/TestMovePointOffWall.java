package org.firstinspires.ftc.teamcode.gamecode;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.vuforia.Matrix44F;
import com.vuforia.PIXEL_FORMAT;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.R;
import org.firstinspires.ftc.teamcode.RC;
import org.firstinspires.ftc.teamcode.opmodesupport.AutoOpMode;
import org.firstinspires.ftc.teamcode.robots.Fermion;

/**
 * Created by Windows on 2016-11-05.
 */
@Autonomous
public class TestMovePointOffWall extends AutoOpMode {
    @Override
    public void runOp() throws InterruptedException {
        Fermion strange = new Fermion(true);

        VuforiaLocalizer.Parameters params = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId);
        params.vuforiaLicenseKey = RC.VUFORIA_LICENSE_KEY;
        params.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;

        VuforiaLocalizer locale = ClassFactory.createVuforiaLocalizer(params);
        locale.setFrameQueueCapacity(1);
        Vuforia.setFrameFormat(PIXEL_FORMAT.RGB565, true);

        VuforiaTrackables beacons = locale.loadTrackablesFromAsset("FTC_2016-17");
        VuforiaTrackableDefaultListener wheels = (VuforiaTrackableDefaultListener) beacons.get(0).getListener();

        telemetry.update();
        waitForStart();

        beacons.activate();
        OpenGLMatrix pose = wheels.getPose();

        while(opModeIsActive() && pose == null) {
            pose = wheels.getPose();
            idle();
        }//while

//        strange.strafeToBeacon(gears, 500);

        VectorF target = new VectorF(-380, 0, 280);

        VectorF trans = navOffWall(pose.getTranslation(), strange.getIMUAngle()[0], target);

        Log.i("ANGLE", "HELLO" + Math.toDegrees(Math.atan2(trans.get(0), -trans.get(2))));

        double deg = Math.toDegrees(Math.atan2(trans.get(0), -trans.get(2)));
        if(deg < 0){
            strange.imuTurnL(-deg, 0.3);
        } else {
            strange.imuTurnR(deg, 0.3);
        }

        strange.forward(0.2);

        do {
            trans = navOffWall(wheels.getPose().getTranslation(), strange.getIMUAngle()[0], target);
            idle();
            Log.i(TAG, "HELLOIP: " + Math.hypot(trans.get(0), trans.get(2)));
        } while (wheels.getPose() != null && Math.hypot(trans.get(0), trans.get(2)) > 100 && opModeIsActive());

        if(wheels.getPose() == null){
            Log.i(TAG, "wheels " + "null");
        }

        strange.stop();
    }

    public VectorF movePointOffWall(VectorF trans, double robotAngle, double offWall) {

        double alpha = Math.atan2(trans.get(2), trans.get(0));
        double hypot = Math.hypot(trans.get(2), trans.get(0));

        double beta = alpha - robotAngle;

        double zPrime = hypot * Math.sin(beta);
        double xPrime = hypot * Math.cos(beta);

        double epsilon = robotAngle + Math.atan2(zPrime, xPrime - offWall);
        double hypot2 = Math.hypot(zPrime, (xPrime - offWall));

        return new VectorF((float) (hypot2 * Math.cos(epsilon)), trans.get(1), (float) (hypot2 * Math.sin(epsilon)));
    }

    public VectorF navOffWall(VectorF trans, double robotAngle, VectorF offWall){
        return new VectorF((float) (trans.get(0) - offWall.get(0) * Math.sin(Math.toRadians(robotAngle)) - offWall.get(2) * Math.cos(Math.toRadians(robotAngle))), trans.get(1), (float) (trans.get(2) + offWall.get(0) * Math.cos(Math.toRadians(robotAngle)) - offWall.get(2) * Math.sin(Math.toRadians(robotAngle))));
    }

}
