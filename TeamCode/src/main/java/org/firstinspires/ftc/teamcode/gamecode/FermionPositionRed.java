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
import org.firstinspires.ftc.teamcode.util.VortexUtils;

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
        tau.addVeerCheckRunnable();

        tau.trackForward(500, 0.5);

        tau.imuTurnL(45, 0.5);

        tau.forward(0.5);

        while (opModeIsActive() && gears.getPose() == null) {
            idle();
        }//while

        tau.stop();

        int config = VortexUtils.waitForBeaconConfig(
                getImageFromFrame(locale.getFrameQueue().take(), PIXEL_FORMAT.RGB565),
                gears, locale.getCameraCalibration(), 5000);

        //since the beacon config can also be "ALL_BLUE" or "NO_BLUE"
        //we technically are assuming the beacon config is "RED_BLUE" by default
        if (config == VortexUtils.BEACON_BLUE_RED) {
            tau.strafeToBeacon(gears, 0, 0.5, tau.getIMUAngle()[0], new VectorF(-69.85f, 0, 500f));
        } else {
            tau.strafeToBeacon(gears, 0, 0.5, tau.getIMUAngle()[0], new VectorF(69.85f, 0, 500f));
        }//else

        tau.absoluteIMUTurn(-90, 0.5);

        tau.trackForward(40, 0.2);

        tau.trackBackward(150, 0.5);

        tau.turnR(0.2);

        while (opModeIsActive() && tools.getPose() == null) {
            idle();
        }//while

        tau.stop();

        config = VortexUtils.waitForBeaconConfig(
                getImageFromFrame(locale.getFrameQueue().take(), PIXEL_FORMAT.RGB565),
                tools, locale.getCameraCalibration(), 5000);

        if (config == VortexUtils.BEACON_BLUE_RED) {
            tau.strafeToBeacon(tools, 0, 0.5, tau.getIMUAngle()[0], new VectorF(-69.85f, 0, 500f));
        } else {
            tau.strafeToBeacon(tools, 0, 0.5, tau.getIMUAngle()[0], new VectorF(69.85f, 0, 500f));
        }//else

        tau.absoluteIMUTurn(-90, 0.5);

        tau.trackForward(200, 0.2);

        tau.trackBackward(200, 0.2);

        tau.imuTurnR(45, 0.5);

        tau.trackBackward(1000, 0.5);

        RC.setGlobalDouble("TeleBeginAngle", tau.getIMUAngle()[0]);

    }//runOp


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
