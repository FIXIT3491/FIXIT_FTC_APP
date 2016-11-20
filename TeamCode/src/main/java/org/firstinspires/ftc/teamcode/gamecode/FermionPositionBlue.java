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
        VuforiaTrackableDefaultListener wheels = (VuforiaTrackableDefaultListener) beacons.get(3).getListener();
        VuforiaTrackableDefaultListener legos = (VuforiaTrackableDefaultListener) beacons.get(1).getListener();

        waitForStart();
        beacons.activate();

        mainTasks.addRunnable(new Runnable() {
            @Override
            public void run() {
                lepton.veerCheck();
            }
        });

        lepton.trackForward(500, 0.5);

        lepton.imuTurnR(45, 0.5);

        lepton.forward(0.5);

        while (opModeIsActive() && wheels.getPose() == null) {
            idle();
        }//while

        lepton.stop();

        VectorF trans = wheels.getPose().getTranslation();

        int config = Fermion.waitForBeaconConfig(
                getImageFromFrame(locale.getFrameQueue().take(), PIXEL_FORMAT.RGB565),
                wheels, locale.getCameraCalibration(), 5000);


        if (config == Fermion.BEACON_BLUE_RED) {
            trans = navOffWall(trans, -lepton.imu.getAngularOrientation().firstAngle, new VectorF(-69.85f, 0, 340f));
        } else {
            trans = navOffWall(trans, -lepton.imu.getAngularOrientation().firstAngle, new VectorF(69.85f, 0, 340f));
        }//else

        double targetAngle = Math.atan2(trans.get(0), -trans.get(2));

        if (targetAngle < 0) {
            lepton.imuTurnL(-targetAngle, 0.5);
        } else {
            lepton.imuTurnR(targetAngle, 0.5);
        }//else

        lepton.trackForward(Math.hypot(trans.get(0), trans.get(2)), 0.5);

        lepton.absoluteIMUTurn(90, 0.5);

        lepton.trackForward(40, 0.2);

        lepton.trackBackward(50, 0.5);

        lepton.turnL(0.2);

        while (opModeIsActive() && legos.getPose() == null) {
            idle();
        }//while

        lepton.stop();

        config = Fermion.waitForBeaconConfig(
                    getImageFromFrame(locale.getFrameQueue().take(), PIXEL_FORMAT.RGB565),
                    legos, locale.getCameraCalibration(), 5000);

        trans = legos.getPose().getTranslation();

        if (config == Fermion.BEACON_BLUE_RED) {
            trans = navOffWall(trans, -lepton.imu.getAngularOrientation().firstAngle, new VectorF(-69.85f, 0, 500f));
        } else {
            trans = navOffWall(trans, -lepton.imu.getAngularOrientation().firstAngle, new VectorF(69.85f, 0, 500f));
        }//else

        targetAngle = Math.atan2(trans.get(0), -trans.get(2));

        if (targetAngle < 0) {
            lepton.imuTurnL(-targetAngle, 0.5);
        } else {
            lepton.imuTurnR(targetAngle, 0.5);
        }//else

        lepton.trackForward(Math.hypot(trans.get(0), trans.get(2)), 0.5);

        lepton.absoluteIMUTurn(90, 0.5);

        lepton.trackForward(200, 0.2);

        lepton.trackBackward(200, 0.2);

        lepton.imuTurnL(45, 0.5);

        lepton.trackBackward(1000, 0.5);

        RC.setGlobalDouble("TeleBeginAngle", -lepton.imu.getAngularOrientation().firstAngle);

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
