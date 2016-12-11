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

        VectorF trans = wheels.getPose().getTranslation();


        int config = VortexUtils.waitForBeaconConfig(
                VortexUtils.getImageFromFrame(locale.getFrameQueue().take(), PIXEL_FORMAT.RGB565),
                wheels, locale.getCameraCalibration(), 5000);

        Log.i(TAG, "runOp: b");

        trans = VortexUtils.navOffWall(trans, lepton.getIMUAngle()[0], new VectorF(540f, 0, 0));

        double targetAngle = Math.atan2(trans.get(0), -trans.get(2));

        lepton.track(Math.toDegrees(targetAngle), trans.magnitude(), 0.5);

        lepton.absoluteIMUTurn(85, 0.5);

        if (config == VortexUtils.BEACON_BLUE_RED) {
            lepton.track(90, 120, 0.35);
        }//if

        Log.i(TAG, "runOp: " + trans.toString());
        Log.i(TAG, "runOp: " + config);

        lepton.forward(0.5);
        sleep(1500);
        lepton.stop();

        lepton.backward(0.5);
        sleep(1500);
        lepton.stop();

        Log.i(TAG, "runOp: " + config);

        if (config == VortexUtils.BEACON_BLUE_RED) {
            lepton.track(-90, 609.6, 0.5);
        } else {
            lepton.track(-90, 509.6, 0.5);
        }//else

        while (opModeIsActive() && legos.getPose() == null) {
            idle();
        }//while


////
//        Log.i(TAG, "runOp: g" );
////        lepton.stop();
//
//        config = VortexUtils.waitForBeaconConfig(
//                    VortexUtils.getImageFromFrame(locale.getFrameQueue().take(), PIXEL_FORMAT.RGB565),
//                    legos, locale.getCameraCalibration(), 5000);
//
//        Log.i(TAG, "runOp: " + config);


        lepton.stop();
//
//        trans = legos.getPose().getTranslation();
//
//        if (config == VortexUtils.BEACON_BLUE_RED) {
//            trans = VortexUtils.navOffWall(trans, -lepton.imu.getAngularOrientation().firstAngle, new VectorF(-69.85f, 0, 500f));
//        } else {
//            trans = VortexUtils.navOffWall(trans, -lepton.imu.getAngularOrientation().firstAngle, new VectorF(69.85f, 0, 500f));
//        }//else
//
//        targetAngle = Math.atan2(trans.get(0), -trans.get(2));
//
//        if (targetAngle < 0) {
//            lepton.imuTurnL(-targetAngle, 0.5);
//        } else {
//            lepton.imuTurnR(targetAngle, 0.5);
//        }//else
//
//        lepton.track(0, Math.hypot(trans.get(0), trans.get(2)), 0.5);
//
//        lepton.absoluteIMUTurn(90, 0.5);
//
//        lepton.track(0, 200, 0.2);
//
//        lepton.track(180, 200, 0.2);
//
//        lepton.imuTurnL(45, 0.5);
//
//        lepton.track(180, 1000, 0.5);
//
//        RC.setGlobalDouble("TeleBeginAngle", -lepton.imu.getAngularOrientation().firstAngle);

    }//runOp
}
