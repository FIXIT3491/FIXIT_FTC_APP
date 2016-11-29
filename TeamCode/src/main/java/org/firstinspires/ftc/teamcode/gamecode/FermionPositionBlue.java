package org.firstinspires.ftc.teamcode.gamecode;

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
        VuforiaTrackableDefaultListener wheels = (VuforiaTrackableDefaultListener) beacons.get(3).getListener();
        VuforiaTrackableDefaultListener legos = (VuforiaTrackableDefaultListener) beacons.get(1).getListener();

        waitForStart();
        beacons.activate();
        lepton.addVeerCheckRunnable();

        lepton.trackForward(500, 0.5);

        lepton.imuTurnR(45, 0.5);

        lepton.forward(0.5);

        while (opModeIsActive() && wheels.getPose() == null) {
            idle();
        }//while

        lepton.stop();

        int config = VortexUtils.waitForBeaconConfig(
                VortexUtils.getImageFromFrame(locale.getFrameQueue().take(), PIXEL_FORMAT.RGB565),
                wheels, locale.getCameraCalibration(), 5000);

        if (config == VortexUtils.BEACON_BLUE_RED) {
            lepton.strafeToBeacon(wheels, 0, 0.5, lepton.getIMUAngle()[0], new VectorF(-69.85f, 0, 500f));
        } else {
            lepton.strafeToBeacon(wheels, 0, 0.5, lepton.getIMUAngle()[0], new VectorF(69.85f, 0, 500f));
        }//else

        lepton.absoluteIMUTurn(90, 0.5);

        lepton.trackForward(200, 0.2);

        lepton.trackBackward(200, 0.5);

        lepton.turnL(0.2);

        while (opModeIsActive() && legos.getPose() == null) {
            idle();
        }//while

        lepton.stop();

        config = VortexUtils.waitForBeaconConfig(
                VortexUtils.getImageFromFrame(locale.getFrameQueue().take(), PIXEL_FORMAT.RGB565),
                legos, locale.getCameraCalibration(), 5000);

        if (config == VortexUtils.BEACON_BLUE_RED) {
            lepton.strafeToBeacon(legos, 0, 0.5, lepton.getIMUAngle()[0], new VectorF(-69.85f, 0, 500f));
        } else {
            lepton.strafeToBeacon(legos, 0, 0.5, lepton.getIMUAngle()[0], new VectorF(69.85f, 0, 500f));
        }//else

        lepton.absoluteIMUTurn(90, 0.5);

        lepton.trackForward(200, 0.2);

        lepton.trackBackward(200, 0.2);

        //vaguely attempt to knock out the cap ball in autonomous...
        lepton.imuTurnL(45, 0.5);

        lepton.trackBackward(1000, 0.5);

        RC.setGlobalDouble("TeleBeginAngle", lepton.getIMUAngle()[0]);

    }//runOp

}
