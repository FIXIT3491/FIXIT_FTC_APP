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
import org.firstinspires.ftc.teamcode.opmodesupport.TaskHandler;
import org.firstinspires.ftc.teamcode.roboticslibrary.MathUtils;
import org.firstinspires.ftc.teamcode.robots.Fermion;

/**
 * Created by FIXIT on 16-10-21.
 */
@Autonomous
public class FermionRed extends AutoOpMode {

    Fermion muon;

    @Override
    public void runOp() throws InterruptedException {
        muon = new Fermion(true);

        VuforiaLocalizer.Parameters params = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId);
        params.vuforiaLicenseKey = RC.VUFORIA_LICENSE_KEY;
        params.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;

        VuforiaLocalizer locale = ClassFactory.createVuforiaLocalizer(params);
        locale.setFrameQueueCapacity(1);
        Vuforia.setFrameFormat(PIXEL_FORMAT.RGB565, true);

        VuforiaTrackables beacons = locale.loadTrackablesFromAsset("FTC_2016-17");
        VuforiaTrackableDefaultListener gears = (VuforiaTrackableDefaultListener) beacons.get(3).getListener();
        VuforiaTrackableDefaultListener tools = (VuforiaTrackableDefaultListener) beacons.get(1).getListener();

        waitForStart();
//        TaskHandler.addLoopedTask("veerCheck", new Runnable() {
//            @Override
//            public void run() {
//                muon.veerCheck();
//            }
//        }, 5);

        beacons.activate();

        muon.forward(0.3);
        sleep(300);
        muon.stop();

        muon.imuTurnL(50, 0.5);

        muon.forward(0.3);

        while (gears.getPose() == null && opModeIsActive()) {
            idle();
        }//while

        VectorF trans = gears.getPose().getTranslation();

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
                    gears, locale.getCameraCalibration(), 5000);
            telemetry.addData("Beacon", config);
            Log.i(TAG, "runOp: " + config);
        } catch (Exception e){
            telemetry.addData("Beacon", "could not not be found");
        }//catch


        muon.strafeToBeacon(gears, 600, 0.3);

        muon.absoluteIMUTurn(-90, 0.5);

        muon.stop();
        sleep(1000);

        muon.right(0.13);

        boolean saidNull = false;
        while (opModeIsActive() && (gears.getPose() == null || !MathUtils.inRange(gears.getPose().getTranslation().get(0), ((config == Fermion.BEACON_RED_BLUE)? -80 : 45), ((config == Fermion.BEACON_RED_BLUE)? -15 : 10)))) {

            if(gears.getPose().getTranslation().get(0) < ((config == Fermion.BEACON_RED_BLUE)? -80 : 45)){
                muon.left(0.13);
            } else {
                muon.right(0.13);
            }//else

            if(gears.getPose() == null){
                Log.i(TAG, "HELLO null");
            } else {
                Log.i(TAG, "Gears: " + gears.getPose().getTranslation().get(0));
            }
            idle();
        }//while


        Log.i(TAG, "Gears Final: " + gears.getPose().getTranslation().get(0));


        muon.forward(0.15);
        sleep(1000);
        muon.stop();

        sleep(100);

        muon.backward(0.2);
        sleep(800);
        muon.stop();

        muon.absoluteIMUTurn(-90, 0.5);

        muon.right(1);
        sleep(1800);

        muon.absoluteIMUTurn(-90, 0.5);
        muon.stop();

        clearTimer();

        int timeBack = 0;
        while (tools.getPose() == null && opModeIsActive()) {
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


        muon.backward(0.3);
        sleep(600);
        muon.stop();

        config = Fermion.BEACON_NOT_VISIBLE;
        try{
            config = Fermion.waitForBeaconConfig(
                    getImageFromFrame(locale.getFrameQueue().take(), PIXEL_FORMAT.RGB565),
                    tools, locale.getCameraCalibration(), 5000);
            telemetry.addData("Beacon", config);
            Log.i(TAG, "runOp: " + config);
        } catch (Exception e){
            telemetry.addData("Beacon", "could not not be found");
        }//catch

        sleep(1000);

        while (opModeIsActive() && (tools.getPose() == null || !MathUtils.inRange(tools.getPose().getTranslation().get(0), ((config == Fermion.BEACON_RED_BLUE)? -80 : 45), ((config == Fermion.BEACON_RED_BLUE)? -15 : 10)))) {

            if(tools.getPose().getTranslation().get(0) < ((config == Fermion.BEACON_RED_BLUE)? -80 : 45)){
                muon.left(0.13);
            } else {
                muon.right(0.13);
            }//else

            if(tools.getPose() == null){
                Log.i(TAG, "HELLO null");
            } else {
                Log.i(TAG, "Tools: " + tools.getPose().getTranslation().get(0));
            }
            idle();
        }//while

        Log.i(TAG, "Tools Final: " + tools.getPose().getTranslation().get(0));

        muon.forward(0.2);
        sleep(1400 + timeBack);
        muon.stop();

        sleep(1000);

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

    public void stopOpMode(){
        muon.stop();
    }
}
