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
        VuforiaTrackableDefaultListener gears = (VuforiaTrackableDefaultListener) beacons.get(3).getListener();

        telemetry.update();
        waitForStart();

        beacons.activate();
        OpenGLMatrix pose = gears.getPose();

        while(opModeIsActive() && pose == null) {
            pose = gears.getPose();
            idle();
        }//while

        VectorF translation = movePointOffWall(pose.getTranslation(), -strange.imu.getAngularOrientation().firstAngle, 375);

        Log.i(TAG, translation.toString());
        strange.imuTurnR(Math.atan2(translation.get(0), translation.get(2)), 0.2);

        strange.forward(0.1);

        while (opModeIsActive() && Math.hypot(translation.get(0), translation.get(2)) > 10) {
            translation = movePointOffWall(gears.getPose().getTranslation(), -strange.imu.getAngularOrientation().firstAngle, 375);
        }//while

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

}
