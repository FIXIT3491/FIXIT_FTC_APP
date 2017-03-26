package org.firstinspires.ftc.teamcode.gamecode;

import android.media.Image;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RC;
import org.firstinspires.ftc.teamcode.newhardware.FXTSensors.FXTLightSensor;
import org.firstinspires.ftc.teamcode.newhardware.FXTSensors.FXTOpticalDistanceSensor;
import org.firstinspires.ftc.teamcode.opmodesupport.TeleOpMode;
import org.firstinspires.ftc.teamcode.roboticslibrary.FXTCamera;
import org.firstinspires.ftc.teamcode.robots.Fermion;
import org.firstinspires.ftc.teamcode.robots.Robot;
import org.firstinspires.ftc.teamcode.util.CircleDetector;
import org.firstinspires.ftc.teamcode.util.MathUtils;

/**
 * Created by Windows on 2016-12-04.
 */
@TeleOp
public class VeerFermionOp extends TeleOpMode implements TextToSpeech.OnInitListener {

    Fermion tau;
    int collectorState = Robot.STOP;
    TextToSpeech tts;

    double driveDirection = 1;

    long ballCollectId = 28347289352L;

    boolean released = false;
    FXTCamera cammy;

    int minExpo = 0;
    int maxExpo = 0;

    @Override
    public void initialize() {
        tts = new TextToSpeech(RC.c(), this);
        tau = new Fermion(false);
        cammy = new FXTCamera(FXTCamera.FACING_BACKWARD, true);
        minExpo = cammy.getBaseCamera().getParameters().getMinExposureCompensation();
        maxExpo = cammy.getBaseCamera().getParameters().getMaxExposureCompensation();

        tau.startShooterControl();
        tau.prime();
        clearTimer(3);
        clearTimer(4);
        cammy.setExposure(-12);
    }

    public void init_loop() {
        if (getMilliSeconds(4) > 1000) {
            cammy.setExposure(12);
            Log.i("fsdf", "fsdfs");
            Log.i(cammy.getBaseCamera().getParameters().getExposureCompensation() + "", cammy.getBaseCamera().getParameters().getExposureCompensationStep() + "");
        }
    }

    @Override
    public void loopOpMode() {

        double theta = Math.atan2(-driveDirection * joy1.x1(), driveDirection * joy1.y1());

        Log.i("Speeds=!", "" + Math.toDegrees(theta));
        double speed = ((joy1.rightBumper())? 0.3 : 1.0) * Math.hypot(joy1.y1(), joy1.x1());

        tau.strafe(Math.toDegrees(theta), speed, false);

        tau.veer(joy1.x2() / 2.0, false, false);

        if(collectorState == Robot.IN && (joy2.rightBumper()) && getMilliSeconds() > 500) {
            collectorState = Robot.STOP;
            clearTimer();
        } else if (joy2.rightBumper() && getMilliSeconds() > 500) {
            collectorState = Robot.IN;
            clearTimer();
        }//else


        if (joy2.rightTrigger()) {
            tau.setCollectorState(Robot.OUT);
        } else {
            tau.setCollectorState(collectorState);
        }

        if(joy2.buttonA()){
            tau.shoot();
        } else if(joy2.buttonX()){
            tau.prime();
        }

        if(-joy2.y1() < -0.15){
            tau.door.goToPos("open");
        } else {
            tau.door.goToPos("close");
        }

        if (joy1.tapLeftBumper()) {
            driveDirection *= -1;
            //tau.switchLights();
        }//if


        if(joy2.buttonY() && !tts.isSpeaking()){
            tts.speak("Hello, my name is Fermion", TextToSpeech.QUEUE_ADD, null);
        }

        tau.usePlannedSpeeds();

        if (tau.seesBall()) {
            RC.t.speakString("Ball Collected!", ballCollectId);
        }//if

        RC.t.addData("Ball in Collector", tau.seesBall());

        if (joy2.buttonB() && !released && getMilliSeconds(2) > 500) {
            tau.capRelease.goToPos("released");
            released = true;
            clearTimer(2);
        }
        else if (joy2.buttonB() && released && getMilliSeconds(2) > 500) {
            tau.capRelease.goToPos("stored");
            released = false;
            clearTimer(2);
        }

        if (joy2.leftBumper() && released) {
            tau.liftCapBall();
        } else if (joy2.leftTrigger() && released) {
            tau.lowerCapBall();
        } else {
            tau.lifter.stop();
        }

        if(getMilliSeconds(3) > 1000){

//            cammy.saveFrame("Image" + RC.runNum++ );
//            double[] info = CircleDetector.findBestCircle(cammy.getImage());


//            double distInFront = 90 - (info[1] * 76 / 1920.833153) * ;
//            double angleH = (info[0] * 76 / 1920.833153);

//            cammy.saveFrame("Image" + RC.runNum++);
//            RC.t.addData("Saved", "Image" + RC.runNum++);
//            clearTimer(3);
        }

    }//loopOpMode

    public void stop(){
        cammy.destroy();
    }


    @Override
    public void onInit(int status) {
        RC.t.addData("Ready to talk", "!");
    }
}//VeerFermionOp