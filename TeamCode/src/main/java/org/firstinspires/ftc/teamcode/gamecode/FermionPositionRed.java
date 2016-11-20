package org.firstinspires.ftc.teamcode.gamecode;

import android.support.annotation.Nullable;

import com.vuforia.Image;
import com.vuforia.PIXEL_FORMAT;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.RC;
import org.firstinspires.ftc.teamcode.opmodesupport.AutoOpMode;
import org.firstinspires.ftc.teamcode.robots.Fermion;

/**
 * Created by FIXIT on 16-10-21.
 */
public class FermionPositionRed extends AutoOpMode {

    @Override
    public void runOp() throws InterruptedException {
        final Fermion tau = new Fermion(true);

        VuforiaLocalizer.Parameters params = new VuforiaLocalizer.Parameters();
        params.vuforiaLicenseKey = RC.VUFORIA_LICENSE_KEY;
        params.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;

        VuforiaLocalizer locale = ClassFactory.createVuforiaLocalizer(params);
        locale.setFrameQueueCapacity(1);
        Vuforia.setFrameFormat(PIXEL_FORMAT.RGB565, true);

        VuforiaTrackables beacons = locale.loadTrackablesFromAsset("FTC_2016-17");
        VuforiaTrackableDefaultListener gears = (VuforiaTrackableDefaultListener) beacons.get(3).getListener();
        VuforiaTrackableDefaultListener tools = (VuforiaTrackableDefaultListener) beacons.get(1).getListener();

        waitForStart();
        beacons.activate();

        mainTasks.addRunnable(new Runnable() {
            @Override
            public void run() {
                tau.veerCheck();
            }
        });

        tau.trackForward(609.6, 0.5);
        tau.imuTurnL(45, 0.5);
        tau.forward(0.5);

        while(!gears.isVisible()) {
            idle();
        }//while

        tau.strafeToBeacon(gears, 100, 0.5);

        tau.absoluteIMUTurn(-90, 0.5);

        tau.strafeToBeacon(gears, 40, 0.5);

        tau.pushBeaconButton(Fermion.waitForBeaconConfig(
                getImageFromFrame(locale.getFrameQueue().take(), PIXEL_FORMAT.RGB565),
                gears, locale.getCameraCalibration(), 5000));

        tau.trackRight(1219.2, 0.5);

        tau.pushBeaconButton(Fermion.waitForBeaconConfig(
                getImageFromFrame(locale.getFrameQueue().take(), PIXEL_FORMAT.RGB565),
                tools, locale.getCameraCalibration(), 5000));

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
