package org.firstinspires.ftc.teamcode.velocityvortexopmodes;

import android.util.Log;

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
public class FermionPositionRed extends AutoOpMode {

    @Override
    public void runOp() throws InterruptedException {
        final Fermion tau = new Fermion(true);

        VuforiaLocalizer.Parameters params = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId);
        params.vuforiaLicenseKey = RC.VUFORIA_LICENSE_KEY;
        params.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;

        VuforiaLocalizer locale = ClassFactory.createVuforiaLocalizer(params);
        locale.setFrameQueueCapacity(1);
        Vuforia.setFrameFormat(PIXEL_FORMAT.RGB565, true);

        VuforiaTrackables beacons = locale.loadTrackablesFromAsset("FTC_2016-17");
        VuforiaTrackableDefaultListener gears = (VuforiaTrackableDefaultListener) beacons.get(3).getListener();
        VuforiaTrackableDefaultListener tools = (VuforiaTrackableDefaultListener) beacons.get(1).getListener();

        waitForStart();

        beacons.activate();
        tau.resetTargetAngle();
        tau.addVeerCheckRunnable();

        tau.forward(0.5);

        while (opModeIsActive() && gears.getPose() == null) {
            idle();
        }//while

        Log.i(TAG, "runOp: a");

        tau.stop();

        int config = VortexUtils.waitForBeaconConfig(
                VortexUtils.getImageFromFrame(locale.getFrameQueue().take(), PIXEL_FORMAT.RGB565),
                gears, locale.getCameraCalibration(), 5000);

        if (config == VortexUtils.BEACON_RED_BLUE) {
            tau.strafeToBeacon(gears, 0, 0.5, true, tau.getIMUAngle()[0], new VectorF(-70, 0, 540));
        } else {
            tau.strafeToBeacon(gears, 0, 0.5, true, tau.getIMUAngle()[0], new VectorF(70, 0, 540));
        }//else

        tau.absoluteIMUTurn(-90, 0.5);

        tau.forward(0.3);
        sleep(2100);
        tau.stop();

        tau.backward(0.5);
        sleep(1500);
        tau.stop();

        Log.i(TAG, "runOp: " + config);

        if (config == VortexUtils.BEACON_RED_BLUE) {
            tau.track(90, 539.6, 0.5);
        } else {
            tau.track(90, 679.6, 0.5);
        }//else

        while (opModeIsActive() && tools.getPose() == null) {
            idle();
        }//while

        config = VortexUtils.waitForBeaconConfig(
                VortexUtils.getImageFromFrame(locale.getFrameQueue().take(), PIXEL_FORMAT.RGB565),
                tools, locale.getCameraCalibration(), 5000);

        if (config == VortexUtils.BEACON_RED_BLUE) {
            tau.strafeToBeacon(tools, 0, 0.5, true, tau.getIMUAngle()[0], new VectorF(-70, 0, 540));
        } else {
            tau.strafeToBeacon(tools, 0, 0.5, true, tau.getIMUAngle()[0], new VectorF(70, 0, 540));
        }//else

        tau.forward(0.3);
        sleep(2100);
        tau.stop();

        tau.track(180, 500, 0.5);
        tau.imuTurnL(45, 0.5);
        tau.backward(0.5);
        sleep(2000);
        tau.stop();

    }//runOp

}
