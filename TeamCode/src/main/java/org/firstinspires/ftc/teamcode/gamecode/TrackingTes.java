package org.firstinspires.ftc.teamcode.gamecode;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.vuforia.PIXEL_FORMAT;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.R;
import org.firstinspires.ftc.teamcode.RC;
import org.firstinspires.ftc.teamcode.opmodesupport.AutoOpMode;
import org.firstinspires.ftc.teamcode.robots.*;
import org.firstinspires.ftc.teamcode.util.VortexUtils;

/**
 * Created by Windows on 2016-12-28.
 */
@Autonomous
public class TrackingTes extends AutoOpMode {

    @Override
    public void runOp() throws InterruptedException{

        final Fermion lepton = new Fermion(true);

        VuforiaLocalizer.Parameters params = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId);
        params.vuforiaLicenseKey = RC.VUFORIA_LICENSE_KEY;
        params.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        params.cameraMonitorFeedback = VuforiaLocalizer.Parameters.CameraMonitorFeedback.AXES;

        VuforiaLocalizer locale = ClassFactory.createVuforiaLocalizer(params);
        locale.setFrameQueueCapacity(1);
        Vuforia.setFrameFormat(PIXEL_FORMAT.RGB565, true);

        VuforiaTrackables beacons = locale.loadTrackablesFromAsset("FTC_2016-17");
        VuforiaTrackableDefaultListener wheels = (VuforiaTrackableDefaultListener) beacons.get(0).getListener();

        beacons.activate();
        waitForStart();

        lepton.resetTargetAngle();
        lepton.addVeerCheckRunnable();


        while (opModeIsActive() && wheels.getPose() == null) {
            idle();
        }//while

        Log.i(TAG, "runOp: a");

        lepton.stop();

        int config = VortexUtils.waitForBeaconConfig(
                VortexUtils.getImageFromFrame(locale.getFrameQueue().take(), PIXEL_FORMAT.RGB565),
                wheels, locale.getCameraCalibration(), 5000);

        config =1;

        if (config == VortexUtils.BEACON_BLUE_RED) {
            lepton.strafeToBeacon(wheels, 0, 0.5, true, lepton.getIMUAngle()[0], new VectorF(0, 0, 540));
        } else {
            lepton.strafeToBeacon(wheels, 0, 0.5, true, lepton.getIMUAngle()[0], new VectorF(-140, 0, 540));
        }//else

        Log.i(TAG, "runOp: " + config);

    }
}//TrackingTes