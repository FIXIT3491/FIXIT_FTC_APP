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
public class FermionRedShot extends AutoOpMode {

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
        VuforiaTrackableDefaultListener gears = (VuforiaTrackableDefaultListener) beacons.get(3).getListener();
        VuforiaTrackableDefaultListener tools = (VuforiaTrackableDefaultListener) beacons.get(1).getListener();

        RC.t.addData("OpMode", "initialized");
        muon.startShooterControl();
        muon.prime();
        waitForStart();
        beacons.activate();
        muon.addVeerCheckRunnable();
        muon.resetTargetAngle();

        muon.right(1);
        sleep(1100);
        muon.stop();
        muon.shoot();
        muon.waitForState(Fermion.FIRE);

        muon.imuTurnR(25, 0.5);

        muon.forward(0.2);

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

        int config = VortexUtils.NOT_VISIBLE;
        try{
            config = VortexUtils.waitForBeaconConfig(
                    getImageFromFrame(locale.getFrameQueue().take(), PIXEL_FORMAT.RGB565),
                    gears, locale.getCameraCalibration(), 5000);
            telemetry.addData("Beacon", config);
            Log.i(TAG, "runOp: " + config);
        } catch (Exception e){
            telemetry.addData("Beacon", "could not not be found");
        }

        Log.i(TAG, "runOp: before");
        muon.forward(1);
        sleep(600);
        Log.i(TAG, "runOp: after");

        muon.absoluteIMUTurn(-90, 0.5);

        while (opModeIsActive() && muon.ultra.getDistance() < 50){
            muon.backward(0.2);
        }
        while(opModeIsActive() && muon.ultra.getDistance() > 457){
            muon.forward(0.2);
        }

        muon.stop();

        muon.right(0.3);

        int sensor = (config == VortexUtils.BEACON_RED_BLUE)? Robot.LEFT : Robot.RIGHT;
        while (opModeIsActive() && muon.getLight(sensor) < 0.2){
            Log.i("light", "" + muon.getLight(sensor));
        }
        muon.stop();
        sleep(100);
        muon.forward(0.5);
        sleep(700);
        muon.stop();
        muon.backward(0.5);
        sleep(300);



        //------------------------------Beacon 2--------------

        muon.right(1);
        sleep(1200);

        while (opModeIsActive() && muon.ultra.getDistance() < 100){
            muon.backward(0.2);
        }
        while(opModeIsActive() && muon.ultra.getDistance() > 457){
            muon.forward(0.2);
        }
        muon.stop();

        muon.right(0.3);

        sensor = Robot.LEFT;
        while (opModeIsActive() && muon.getLight(sensor) < 0.2){
            Log.i("light", "" + muon.getLight(sensor));
        }
        muon.stop();
        sleep(100);

        muon.absoluteIMUTurn(-90, 0.6);

        muon.stop();
        RC.t.addData("Hi", "ya");

        while (opModeIsActive() && muon.ultra.getDistance() < 500) {
            muon.backward(1);
        }//while
        muon.stop();

        while (tools.getPose() == null && opModeIsActive()){
            idle();
        }

        config = VortexUtils.NOT_VISIBLE;
        try{
            config = VortexUtils.waitForBeaconConfig(
                    getImageFromFrame(locale.getFrameQueue().take(), PIXEL_FORMAT.RGB565),
                    tools, locale.getCameraCalibration(), 5000);
            telemetry.addData("Beacon", config);
            Log.i(TAG, "runOp: " + config);
        } catch (Exception e){
            telemetry.addData("Beacon", "could not not be found");
        }

        while(opModeIsActive() && muon.ultra.getDistance() > 300){
            muon.forward(0.2);
        }


        if(config == VortexUtils.BEACON_BLUE_RED){
            muon.stop();
            muon.left(0.3);

            sensor = Robot.RIGHT;
            while (opModeIsActive() && muon.getLight(sensor) < 0.2){
                Log.i("light", "" + muon.getLight(sensor));
            }
            muon.stop();
            sleep(100);
        }

        muon.forward(0.3);
        sleep(1000);
        muon.stop();
        muon.backward(0.5);
        sleep(300);
        muon.stop();

        if(RC.globalBool("Cap-ball")){
            muon.imuTurnR(45, 0.7);
            muon.backward(1);
            sleep(2000);
            muon.stop();
        }
    }//runOp

}
