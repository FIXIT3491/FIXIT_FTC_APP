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
import org.firstinspires.ftc.teamcode.robots.Robot;
import org.firstinspires.ftc.teamcode.util.MathUtils;
import org.firstinspires.ftc.teamcode.robots.Fermion;
import org.firstinspires.ftc.teamcode.util.VortexUtils;

import static org.firstinspires.ftc.teamcode.util.VortexUtils.getImageFromFrame;

/**
 * Created by FIXIT on 16-10-21.
 */
@Autonomous
public class FermionBlueNoVuforia extends AutoOpMode {

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
        muon.addVeerCheckRunnable();

        muon.forward(0.3);
        sleep(300);
        muon.stop();

        muon.imuTurnR(50, 0.5);

        muon.forward(0.3);

        while (opModeIsActive() && muon.getLight(Robot.LEFT) < 0.2 && muon.getLight(Robot.RIGHT) < 0.2) {
            idle();
        }//while

        muon.absoluteIMUTurn(90, 0.5);

        //300 is a guess
        if (muon.ultra.getDistance() < 300) {
            muon.backward(0.3);
            while (muon.ultra.getDistance() < 300) {
                idle();
            }//while
            muon.stop();
        }//if

        int config = VortexUtils.NOT_VISIBLE;
        try{
            config = VortexUtils.waitForBeaconConfig(
                    getImageFromFrame(locale.getFrameQueue().take(), PIXEL_FORMAT.RGB565),
                    wheels, locale.getCameraCalibration(), 5000);
            telemetry.addData("Beacon", config);
            Log.i(TAG, "runOp: " + config);
        } catch (Exception e){
            telemetry.addData("Beacon", "could not not be found");
        }//catch


        muon.left(0.2);

        int sensor = (config == VortexUtils.BEACON_BLUE_RED)? Robot.LEFT : Robot.RIGHT;
        while (opModeIsActive() && muon.getLight(sensor) < 0.2){
            Log.i("light", "" + muon.getLight(sensor));
        }
        muon.stop();
        sleep(100);
        muon.forward(0.5);
        sleep(700);
        muon.stop();
        muon.backward(0.5);
        sleep(500);


        //------------------------------Beacon 2--------------

        muon.absoluteIMUTurn(90, 0.5);

        muon.left(1);
        sleep(1000);
        muon.left(0.2);

        sensor = Robot.LEFT;
        while (opModeIsActive() && muon.getLight(sensor) < 0.2){
            Log.i("light", "" + muon.getLight(sensor));
        }
        muon.stop();
        muon.absoluteIMUTurn(90, 0.5);
        muon.stop();

        //300 is a guess
        if (muon.ultra.getDistance() < 300) {
            muon.backward(0.3);
            while (muon.ultra.getDistance() < 300) {
                idle();
            }//while
            muon.stop();
        }//if

        config = VortexUtils.NOT_VISIBLE;
        try{
            config = VortexUtils.waitForBeaconConfig(
                    getImageFromFrame(locale.getFrameQueue().take(), PIXEL_FORMAT.RGB565),
                    legos, locale.getCameraCalibration(), 5000);
            telemetry.addData("Beacon", config);
            Log.i(TAG, "runOp: " + config);
        } catch (Exception e){
            telemetry.addData("Beacon", "could not not be found");
        }

        while(opModeIsActive() && muon.ultra.getDistance() > 300){
            muon.forward(0.2);
        }

        muon.left(0.2);

        sensor = (config == VortexUtils.BEACON_BLUE_RED)? Robot.LEFT : Robot.RIGHT;
        while (opModeIsActive() && muon.getLight(sensor) < 0.2){
            Log.i("light", "" + muon.getLight(sensor));
        }//while

        muon.stop();
        sleep(100);
        muon.forward(0.5);
        sleep(700);
        muon.stop();
        muon.backward(0.5);
        sleep(500);

        muon.forward(0.3);
        sleep(1000);
        muon.stop();
        muon.backward(0.5);
        sleep(300);
        muon.stop();

    }//runOp

}

