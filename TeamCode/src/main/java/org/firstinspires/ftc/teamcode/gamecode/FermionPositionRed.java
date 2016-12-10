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
public class FermionPositionRed extends AutoOpMode {

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
        VuforiaTrackableDefaultListener gears = (VuforiaTrackableDefaultListener) beacons.get(3).getListener();
        VuforiaTrackableDefaultListener tools = (VuforiaTrackableDefaultListener) beacons.get(1).getListener();

        lepton.stop();

        waitForStart();

        beacons.activate();
        lepton.resetTargetAngle();
        mainTasks.addRunnable(new Runnable() {
            @Override
            public void run() {
                lepton.veerCheck();
            }
        });

        lepton.forward(0.5);

        while (opModeIsActive() && gears.getPose() == null) {
            idle();
        }//while

        Log.i(TAG, "runOp: a");

        lepton.stop();

        VectorF trans = gears.getPose().getTranslation();


        int config = VortexUtils.waitForBeaconConfig(
                VortexUtils.getImageFromFrame(locale.getFrameQueue().take(), PIXEL_FORMAT.RGB565),
                gears, locale.getCameraCalibration(), 5000);

        Log.i(TAG, "runOp: b");

        trans = VortexUtils.navOffWall(trans, lepton.getIMUAngle()[0], new VectorF(540f, 0, 0));

        double targetAngle = Math.atan2(trans.get(0), -trans.get(2));

        lepton.track(Math.toDegrees(targetAngle), trans.magnitude(), 0.5);

        lepton.absoluteIMUTurn(-85, 0.5);

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


        lepton.absoluteIMUTurn(-55, 0.5);

        Log.i(TAG, "runOp: " + config);


        while (opModeIsActive() && tools.getPose() == null) {
            idle();
        }//while

    }//runOp

    //this assumes the horizontal axis is the y-axis since the phone is vertical
    //robot angle is relative to "parallel with the beacon wall"
    public VectorF navOffWall(VectorF trans, double robotAngle, VectorF offWall){
        return new VectorF((float) (trans.get(0) - offWall.get(0) * Math.sin(Math.toRadians(robotAngle)) - offWall.get(2) * Math.cos(Math.toRadians(robotAngle))), trans.get(1), (float) (trans.get(2) + offWall.get(0) * Math.cos(Math.toRadians(robotAngle)) - offWall.get(2) * Math.sin(Math.toRadians(robotAngle))));
    }

    @Nullable
    public static Image getImageFromFrame(VuforiaLocalizer.CloseableFrame frame, int format) {

        long numImgs = frame.getNumImages();
        for (int i = 0; i < numImgs; i++) {
            if (frame.getImage(i).getFormat() == format) {
                return frame.getImage(i);
            }//if
        }//for

        return null;
    }
}
