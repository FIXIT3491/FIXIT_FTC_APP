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
 * Created by FIXIT on 16-10-18.
 */
public class FermionRed extends AutoOpMode {

    @Override
    public void runOp() throws InterruptedException {
        final Fermion electron = new Fermion(true);

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
                electron.veerCheck();
            }
        });

        //electron.trackForward(609.6, 0.5);
        electron.forward(0.5); //uses time instead of trackball
        delay((int) RC.globalDouble("DriveForwardTime"));
        electron.stop();

        electron.imuTurnL(45, 0.5);
        electron.forward(0.5);

        while(!gears.isVisible()) {
            idle();
        }//while

        VectorF translation = gears.getPose().getTranslation();

        electron.imuTurnL(Math.atan2(translation.get(2), translation.get(3)), 0.5);

        electron.strafeToBeacon(gears, RC.globalDouble("FirstGearsBufferDistance"));

        electron.absoluteIMUTurn(-90, 0.5);

        electron.strafeToBeacon(gears, RC.globalDouble("SecondGearsBufferDistance"));

        electron.pushBeaconButton(Fermion.waitForBeaconConfig(
                getImageFromFrame(locale.getFrameQueue().take(), PIXEL_FORMAT.RGB565),
                gears, locale.getCameraCalibration()));

        electron.right(0.5);

        while (!tools.isVisible()) {
            idle();
        }//while

        electron.strafeToBeacon(tools, RC.globalDouble("ToolsBufferDistance"));

        electron.pushBeaconButton(Fermion.waitForBeaconConfig(
                getImageFromFrame(locale.getFrameQueue().take(), PIXEL_FORMAT.RGB565),
                tools, locale.getCameraCalibration()));

    }//runOp

    //this assumes the horizontal axis is the y-axis since the phone is vertical
    //robot angle is relative to "parallel with the beacon wall"
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
