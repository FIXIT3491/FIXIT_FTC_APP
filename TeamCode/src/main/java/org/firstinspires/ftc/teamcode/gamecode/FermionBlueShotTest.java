package org.firstinspires.ftc.teamcode.gamecode;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.RC;
import org.firstinspires.ftc.teamcode.opmodesupport.AutoOpMode;
import org.firstinspires.ftc.teamcode.roboticslibrary.FXTCamera;
import org.firstinspires.ftc.teamcode.robots.Robot;
import org.firstinspires.ftc.teamcode.robots.Fermion;
import org.firstinspires.ftc.teamcode.util.VortexUtils;

/**
 * Created by FIXIT on 16-10-21.
 */
@Autonomous
public class FermionBlueShotTest extends AutoOpMode {

    @Override
    public void runOp() throws InterruptedException {
        final Fermion muon = new Fermion(true);
        FXTCamera cam = new FXTCamera(FXTCamera.FACING_BACKWARD, true);


        double voltage = muon.getBatteryVoltage();
        muon.startShooterControl();
        waitForStart();
        muon.capRelease.goToPos("start");
        muon.addVeerCheckRunnable();
        muon.resetTargetAngle();

        muon.right(1);
        if(voltage > 13.5){
            sleep(900);
        } else {
            sleep(1100);
        }
        muon.stop();
        muon.shoot();
        Log.i("Shooter", muon.getShooterState() + "");
        muon.waitForShooterState(Fermion.LOADED);
        muon.shoot();
        muon.waitForShooterState(Fermion.LOADING);

        muon.absoluteIMUTurn(65, 0.6);

        muon.forward(1);
        sleep(800);
        muon.stop();
        sleep(200);

        muon.absoluteIMUTurn(90, 0.6);

        muon.startWallFollowing(0, -90, 0.5, 400);

        int sensor = Robot.LEFT;

        if (muon.getLight(sensor) < muon.LIGHT_THRESHOLD) {
            while (opModeIsActive() && muon.getLight(sensor) < muon.LIGHT_THRESHOLD) {
                Log.i("light", "" + muon.getLight(sensor));
            }//while
        }//if
        muon.stop();
        muon.endWallFollowing();
        muon.absoluteIMUTurn(90, 0.5);
        muon.stop();

        int config = VortexUtils.NOT_VISIBLE;
        try{
            config = VortexUtils.getBeaconConfig(cam.getImage());
            telemetry.addData("Beacon", config);
            Log.i(TAG, "runOp: " + config);
        } catch (Exception e){
            telemetry.addData("Beacon", "could not not be found");
        }


        while(opModeIsActive() && muon.getUltrasonicDistance(0) > 300){
            muon.forward(0.2);
        }

        muon.clearBall();

        if(config == VortexUtils.BEACON_RED_BLUE){
            muon.stop();
            muon.left(0.4);

            sensor = Robot.RIGHT;
            if (muon.getLight(sensor) < Fermion.LIGHT_THRESHOLD) {
                while (opModeIsActive() && muon.getLight(sensor) < muon.LIGHT_THRESHOLD) {
                    Log.i("light", "" + muon.getLight(sensor));
                }
            }//if

            muon.stop();
            sleep(100);
        } else {
            muon.stop();

            if (muon.getLight(Robot.LEFT) < Fermion.LIGHT_THRESHOLD) {

                muon.right(0.4);

                sensor = Robot.LEFT;
                while (opModeIsActive() && muon.getLight(sensor) < muon.LIGHT_THRESHOLD) {
                    Log.i("light", "" + muon.getLight(sensor));
                }
            }//if

            muon.stop();
            sleep(100);
        }

        muon.forward(0.3);
        sleep(1000);
        muon.stop();
        muon.backward(0.5);
        muon.resetBallClearing();
        sleep(500);
        muon.stop();

        muon.absoluteIMUTurn(90, 0.5);

        int lastConfig = config;

        config = VortexUtils.doubleCheckBeaconConfig(cam.photo());
        if (config != VortexUtils.BEACON_ALL_BLUE) {

            muon.clearBall();

            if (config == VortexUtils.BEACON_ALL_RED) {
                sleep(5000);
            } else if (config == VortexUtils.BEACON_RED_BLUE ) {
                if (muon.getLight(Robot.RIGHT) < Fermion.LIGHT_THRESHOLD && lastConfig != config) {
                    muon.stop();
                    muon.left(0.4);

                    sensor = Robot.RIGHT;
                    while (opModeIsActive() && muon.getLight(sensor) < muon.LIGHT_THRESHOLD) {
                        Log.i("light", "" + muon.getLight(sensor));
                    }
                    muon.stop();
                    sleep(100);
                }//if
            } else {
                if (muon.getLight(Robot.LEFT) < Fermion.LIGHT_THRESHOLD) {
                    muon.stop();
                    muon.right(0.4);

                    sensor = Robot.LEFT;
                    while (opModeIsActive() && muon.getLight(sensor) < muon.LIGHT_THRESHOLD) {
                        Log.i("light", "" + muon.getLight(sensor));
                    }
                    muon.stop();
                    sleep(100);
                }//if
            }//else

            muon.forward(0.3);
            sleep(1000);
            muon.stop();
            muon.backward(0.5);
            muon.resetBallClearing();
            sleep(500);
            muon.stop();

        }//if


        //------------------------------Beacon 2--------------
        muon.startWallFollowing(0, -90, 1, 400);
        sleep(1000);
        muon.setTargetSpeed(0.5);

        sensor = Robot.LEFT;
        while (opModeIsActive() && muon.getLight(sensor) < Fermion.LIGHT_THRESHOLD){
            Log.i("light", "" + muon.getLight(sensor));
        }
        muon.endWallFollowing();
        muon.stop();
        muon.absoluteIMUTurn(90, 0.5);
        muon.stop();



        config = VortexUtils.NOT_VISIBLE;
        try{
            config = VortexUtils.getBeaconConfig(cam.getImage());
            telemetry.addData("Beacon", config);
            Log.i(TAG, "runOp: " + config);
        } catch (Exception e){
            telemetry.addData("Beacon", "could not not be found");
        }


        while(opModeIsActive() && muon.getUltrasonicDistance(0) > 300){
            muon.forward(0.2);
        }

        muon.clearBall();

        if(config == VortexUtils.BEACON_RED_BLUE){
            muon.stop();
            muon.left(0.4);

            sensor = Robot.RIGHT;
            while (opModeIsActive() && muon.getLight(sensor) < muon.LIGHT_THRESHOLD){
                Log.i("light", "" + muon.getLight(sensor));
            }
            muon.stop();
            sleep(100);
        }

        muon.forward(0.3);
        sleep(1000);
        muon.stop();
        muon.backward(0.5);
        muon.resetBallClearing();
        sleep(300);
        muon.stop();

        muon.absoluteIMUTurn(90, 0.5);

        lastConfig = config;
        config = VortexUtils.doubleCheckBeaconConfig(cam.photo());
        if (config != VortexUtils.BEACON_ALL_BLUE) {
            muon.clearBall();

            if (config == VortexUtils.BEACON_ALL_RED) {
                sleep(5000);
            } else if (config == VortexUtils.BEACON_RED_BLUE ) {
                if (muon.getLight(Robot.RIGHT) < Fermion.LIGHT_THRESHOLD && lastConfig != config) {
                    muon.stop();
                    muon.left(0.4);

                    sensor = Robot.RIGHT;
                    while (opModeIsActive() && muon.getLight(sensor) < muon.LIGHT_THRESHOLD) {
                        Log.i("light", "" + muon.getLight(sensor));
                    }
                    muon.stop();
                    sleep(100);
                }//if
            } else {
                if (muon.getLight(Robot.LEFT) < Fermion.LIGHT_THRESHOLD) {
                    muon.stop();
                    muon.right(0.4);

                    sensor = Robot.LEFT;
                    while (opModeIsActive() && muon.getLight(sensor) < muon.LIGHT_THRESHOLD) {
                        Log.i("light", "" + muon.getLight(sensor));
                    }
                    muon.stop();
                    sleep(100);
                }//if
            }//else

            muon.forward(0.3);
            sleep(1000);
            muon.stop();
            muon.backward(0.5);
            muon.resetBallClearing();
            sleep(500);
            muon.stop();

        }//if



        if(RC.globalBool("Cap-ball")){
            muon.imuTurnL(50, 0.7);
            muon.backward(1);
            sleep(1800);
            muon.stop();
        }
        cam.destroy();
    }//runOp

}
