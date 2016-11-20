package org.firstinspires.ftc.teamcode.gamecode;

import android.support.annotation.Nullable;
import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
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
import org.firstinspires.ftc.teamcode.util.MathUtils;
import org.firstinspires.ftc.teamcode.robots.Fermion;

/**
 * Created by FIXIT on 16-10-21.
 */
@Autonomous
public class FermionBlue extends AutoOpMode {

    @Override
    public void runOp() throws InterruptedException {
        final Fermion muon = new Fermion(true);

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

        muon.forward(0.3);
        sleep(300);
        muon.stop();

        muon.imuTurnR(50, 0.5);

        muon.forward(0.3);

        while (wheels.getPose() == null && opModeIsActive()) {
            idle();
        }//while


        VectorF trans = wheels.getPose().getTranslation();

        Log.i("ANGLE", "HELLO" + Math.toDegrees(Math.atan2(trans.get(0), -trans.get(2))));

        double deg = Math.toDegrees(Math.atan2(trans.get(0), -trans.get(2)));
        if(deg < 0){
            muon.imuTurnL(-deg, 0.3);
        } else {
            muon.imuTurnR(deg, 0.3);
        }

        int config = Fermion.BEACON_NOT_VISIBLE;
        try{
            config = Fermion.waitForBeaconConfig(
                    getImageFromFrame(locale.getFrameQueue().take(), PIXEL_FORMAT.RGB565),
                    wheels, locale.getCameraCalibration(), 5000);
            telemetry.addData("Beacon", config);
            Log.i(TAG, "runOp: " + config);
        } catch (Exception e){
            telemetry.addData("Beacon", "could not not be found");
        }


        muon.strafeToBeacon(wheels, 600, 0.3);

        muon.absoluteIMUTurn(90, 0.5);

        muon.left(0.13);

        while (opModeIsActive() && (wheels.getPose() == null || wheels.getPose().getTranslation().get(0) < ((config == Fermion.BEACON_BLUE_RED)? -80 : 45))) {
            if(wheels.getPose() == null){
                Log.i(TAG, "HELLO null");
            } else {
                Log.i(TAG, "HELLO" + wheels.getPose().getTranslation().get(0));
            }
            idle();
        }//while


        Log.i(TAG, "HELLO FNIale" + wheels.getPose().getTranslation().get(0));

        muon.forward(0.15);
        sleep(1000);
        muon.stop();

        sleep(100);

        muon.backward(0.2);
        sleep(800);
        muon.stop();

        muon.absoluteIMUTurn(90, 0.5);

        muon.left(1);
        sleep(1800);

        muon.absoluteIMUTurn(90, 0.5);
        muon.stop();

        clearTimer();

        long timeBack = 0;
        while (legos.getPose() == null && opModeIsActive()) {
            if(getMilliSeconds() > 1500){
                Log.i(TAG, "runOp: " + "can't see");
                muon.backward(0.3);
                sleep(300);
                timeBack += 300;
                muon.stop();
                clearTimer();
            }
            idle();
        }//while


        Log.i(TAG, "runOp: " + "jolly good");

        muon.left(0.13);

        while (opModeIsActive() && (legos.getPose() == null || legos.getPose().getTranslation().get(0) < -200)) {
            if(legos.getPose() == null){
                Log.i(TAG, "HELLO null");
            } else {
                Log.i(TAG, "HELLO" + legos.getPose().getTranslation().get(0));
            }
            idle();
        }//while

        muon.stop();

        muon.backward(0.3);
        sleep(600);
        muon.stop();

        muon.absoluteIMUTurn(90, 0.5);

        config = Fermion.BEACON_NOT_VISIBLE;
        try{
            config = Fermion.waitForBeaconConfig(
                    getImageFromFrame(locale.getFrameQueue().take(), PIXEL_FORMAT.RGB565),
                    legos, locale.getCameraCalibration(), 5000);
            telemetry.addData("Beacon", config);
            Log.i(TAG, "runOp: " + config);
        } catch (Exception e){
            telemetry.addData("Beacon", "could not not be found");
        }

        sleep(1000);

        while (opModeIsActive() && (legos.getPose() == null || !MathUtils.inRange(legos.getPose().getTranslation().get(0), ((config == Fermion.BEACON_BLUE_RED)? -80 : 45), ((config == Fermion.BEACON_BLUE_RED)? -60 : 65)))) {

            if(legos.getPose() != null && legos.getPose().getTranslation().get(0) > ((config == Fermion.BEACON_BLUE_RED)? -60 : 65)){
                muon.right(0.13);
            } else {
                muon.left(0.13);
            }

            if(legos.getPose() == null){
                Log.i(TAG, "HELLO null");
            } else {
                Log.i(TAG, "HELLO" + legos.getPose().getTranslation().get(0));
            }
            idle();
        }//while

        Log.i(TAG, "HELLO FNIale" + legos.getPose().getTranslation().get(0));

        muon.forward(0.2);
        sleep(1800 + timeBack);
        muon.stop();

        muon.backward(0.2);
        sleep(1400);
        muon.stop();

        RC.setGlobalDouble("TeleBeginAngle", -muon.imu.getAngularOrientation().firstAngle);

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


    public VectorF navOffWall(VectorF trans, double robotAngle, VectorF offWall){
        return new VectorF((float) (trans.get(0) - offWall.get(0) * Math.sin(Math.toRadians(robotAngle)) - offWall.get(2) * Math.cos(Math.toRadians(robotAngle))), trans.get(1), (float) (trans.get(2) + offWall.get(0) * Math.cos(Math.toRadians(robotAngle)) - offWall.get(2) * Math.sin(Math.toRadians(robotAngle))));
    }
}
