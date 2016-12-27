package org.firstinspires.ftc.teamcode.gamecode;

import android.support.annotation.Nullable;
import android.util.Log;

import com.vuforia.Image;
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
import org.firstinspires.ftc.teamcode.robots.Fermion;
import org.firstinspires.ftc.teamcode.util.VortexUtils;

/**
 * Created by FIXIT on 16-10-21.
 */
public class FermionPositionBlue extends AutoOpMode {

    @Override
    public void runOp() throws InterruptedException {
        final Fermion lepton = new Fermion(true);

        VuforiaLocalizer.Parameters params = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId);
        params.vuforiaLicenseKey = RC.VUFORIA_LICENSE_KEY;
        params.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;

        VuforiaLocalizer locale = ClassFactory.createVuforiaLocalizer(params);
        locale.setFrameQueueCapacity(1);
        Vuforia.setFrameFormat(PIXEL_FORMAT.RGB565, true);

        VuforiaTrackables beacons = locale.loadTrackablesFromAsset("FTC_2016-17");
        VuforiaTrackableDefaultListener wheels = (VuforiaTrackableDefaultListener) beacons.get(0).getListener();
        VuforiaTrackableDefaultListener legos = (VuforiaTrackableDefaultListener) beacons.get(2).getListener();

        waitForStart();

        beacons.activate();
        lepton.resetTargetAngle();
        lepton.addVeerCheckRunnable();

        lepton.forward(0.5);

        while (opModeIsActive() && wheels.getPose() == null) {
            idle();
        }//while

        Log.i(TAG, "runOp: a");

        lepton.stop();

        int config = VortexUtils.waitForBeaconConfig(
                VortexUtils.getImageFromFrame(locale.getFrameQueue().take(), PIXEL_FORMAT.RGB565),
                wheels, locale.getCameraCalibration(), 5000);

        if (config == VortexUtils.BEACON_BLUE_RED) {
            lepton.strafeToBeacon(wheels, 0, 0.5, true, lepton.getIMUAngle()[0], new VectorF(-70, 0, 540));
        } else {
            lepton.strafeToBeacon(wheels, 0, 0.5, true, lepton.getIMUAngle()[0], new VectorF(70, 0, 540));
        }//else

        lepton.absoluteIMUTurn(90, 0.5);

        lepton.forward(0.3);
        sleep(2100);
        lepton.stop();

        lepton.backward(0.5);
        sleep(1500);
        lepton.stop();

        Log.i(TAG, "runOp: " + config);

        if (config == VortexUtils.BEACON_BLUE_RED) {
            lepton.track(-90, 539.6, 0.5);
        } else {
            lepton.track(-90, 679.6, 0.5);
        }//else

        while (opModeIsActive() && legos.getPose() == null) {
            idle();
        }//while

        config = VortexUtils.waitForBeaconConfig(
                VortexUtils.getImageFromFrame(locale.getFrameQueue().take(), PIXEL_FORMAT.RGB565),
                legos, locale.getCameraCalibration(), 5000);

        if (config == VortexUtils.BEACON_BLUE_RED) {
            lepton.strafeToBeacon(legos, 0, 0.5, true, lepton.getIMUAngle()[0], new VectorF(-70, 0, 540));
        } else {
            lepton.strafeToBeacon(legos, 0, 0.5, true, lepton.getIMUAngle()[0], new VectorF(70, 0, 540));
        }//else

        lepton.forward(0.3);
        sleep(2100);
        lepton.stop();

        lepton.track(180, 500, 0.5);
        lepton.imuTurnL(45, 0.5);
        lepton.backward(0.5);
        sleep(2000);
        lepton.stop();

    }//runOp
}//FermionPositionBlue
