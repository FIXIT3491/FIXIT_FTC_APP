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
public class FermionPositionBlue extends AutoOpMode {

    @Override
    public void runOp() throws InterruptedException {
        final Fermion lepton = new Fermion(true);

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
                lepton.veerCheck();
            }
        });

        lepton.trackForward(609.6, 0.5);
        lepton.imuTurnL(45, 0.5);
        lepton.forward(0.5);

        while(!gears.isVisible()) {
            idle();
        }//while

        lepton.strafeToBeacon(gears, 100, 0.5);

        lepton.absoluteIMUTurn(-90, 0.5);

        lepton.strafeToBeacon(gears, 40, 0.5);

        lepton.pushBeaconButton(Fermion.waitForBeaconConfig(
                getImageFromFrame(locale.getFrameQueue().take(), PIXEL_FORMAT.RGB565),
                gears, locale.getCameraCalibration(), 5000));

        lepton.trackLeft(1219.2, 0.5);

        lepton.pushBeaconButton(Fermion.waitForBeaconConfig(
                getImageFromFrame(locale.getFrameQueue().take(), PIXEL_FORMAT.RGB565),
                tools, locale.getCameraCalibration(), 5000));

    }//runOp

    //this assumes the horizontal axis is the y-axis since the phone is vertical
    //robot angle is relative to "parallel with the beacon wall"
    public VectorF movePointOffWall(VectorF trans, double robotAngle, double offWall) {

        double angle = Math.abs(robotAngle) + Math.atan2(trans.get(3), trans.get(2));
        double hypot = Math.hypot(trans.get(3), trans.get(2));

        double xDist = hypot * Math.sin(angle) - offWall;
        double zDist = hypot * Math.cos(angle);

        hypot = Math.hypot(xDist, zDist);
        angle -= robotAngle;

        xDist = -hypot * Math.sin(angle);
        zDist = hypot * Math.cos(angle);

        return new VectorF(trans.get(1), (float) xDist, (float) zDist);
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
