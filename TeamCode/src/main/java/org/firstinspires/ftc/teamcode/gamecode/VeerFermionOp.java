package org.firstinspires.ftc.teamcode.gamecode;

import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RC;
import org.firstinspires.ftc.teamcode.opmodesupport.TeleOpMode;
import org.firstinspires.ftc.teamcode.roboticslibrary.FXTCamera;
import org.firstinspires.ftc.teamcode.robots.Fermion;
import org.firstinspires.ftc.teamcode.robots.Robot;

/**
 * Created by Windows on 2016-12-04.
 */
@TeleOp
public class VeerFermionOp extends TeleOpMode implements TextToSpeech.OnInitListener {

    private Fermion tau;
    private TextToSpeech tts;

    private double driveDirection = 1;

    private long ballCollectId = 28347289352L;

    private boolean capReleased = false;
    private int collectorState = Robot.STOP;

    private FXTCamera cam;

    @Override
    public void initialize() {
        tts = new TextToSpeech(RC.c(), this);
        tau = new Fermion(false);
        cam = new FXTCamera(FXTCamera.FACING_BACKWARD, true);

        tau.startShooterControl();

        clearTimer(3);
        clearTimer(4);
        cam.setExposure(-12);

    }//initialize

    public void init_loop() {
        if (getMilliSeconds(4) > 1000) {
            cam.setExposure(12);
            Log.i("fsdf", "fsdfs");
            Log.i(cam.getBaseCamera().getParameters().getExposureCompensation() + "", cam.getBaseCamera().getParameters().getExposureCompensationStep() + "");
        }
    }
    public void start(){
        tau.capRelease.goToPos("start");
    }

    @Override
    public void loopOpMode() {
        //variable to keep track of which timer is for which function
        int timerIdx = 0;

        /*
        SWITCH DRIVING DIRECTIONS
         */
        if (tap(timerIdx++, joy1.leftBumper())) {
            driveDirection *= -1;
        }//if

        /*
        STRAFING & VEERING
         */
        double theta = Math.atan2(-driveDirection * joy1.x1(), driveDirection * joy1.y1());
        double speed = ((joy1.rightBumper())? 0.3 : 1.0) * Math.hypot(joy1.y1(), joy1.x1());

        tau.strafe(Math.toDegrees(theta), speed, false);
        tau.veer(joy1.x2() / 2.0, false, false);

        tau.usePlannedSpeeds();

        /*
        COLLECTOR CONTROL & BALL DETECTION
         */
        if(collectorState == Robot.IN && tap(timerIdx, joy2.rightBumper())) {
            collectorState = Robot.STOP;
            clearTimer();
        } else if (tap(timerIdx, joy2.rightBumper())) {
            collectorState = Robot.IN;
            clearTimer();
        }//else
        timerIdx++;

        if (joy2.rightTrigger()) {
            tau.setCollectorState(Robot.OUT);
        } else if(tau.getShooterState() != Fermion.LOADING){
            tau.setCollectorState(collectorState);
        }//else

        RC.t.addData("Ball in Collector", tau.seesBall());

        /*
        SHOOTER
         */
        if(joy2.buttonA()){
            tau.shoot();
        }//else

        /*
        DOOR
         */
        if(-joy2.y1() < -0.15){
            tau.door.goToPos("open");
        } else if(tau.getShooterState() != Fermion.LOADING){
            tau.door.goToPos("close");
        }//else

        /*
        FERMION SPEAKING
         */
        if(joy2.buttonY() && !tts.isSpeaking()){
            tts.speak("Hello, my name is Fermion", TextToSpeech.QUEUE_ADD, null);
        }//if

        /*
        PRONG RELEASE
         */
        if (tap(timerIdx++, joy2.buttonX(), 500)){
            Log.i("Prong Release Timer", getMilliSeconds(timerIdx - 1) + "");
            if (!capReleased) {
                tau.capRelease.goToPos("release");
                capReleased = true;
            } else {
                tau.capRelease.goToPos("start");
                capReleased = false;
            }//else
        }//if

        /*
        RAISING CAP BALLS
         */
        if (joy2.leftBumper() && capReleased) {
            tau.liftCapBall();
        } else if (joy2.leftTrigger() && capReleased) {
            tau.lowerCapBall();
        } else {
            tau.lifter.stop();
        }//else


        if (tap(timerIdx++, tau.seesBall())) {
            tau.lights.setPower(0);
        } else {
            tau.lights.setPower(1);
        }//if



    }//loopOpMode

    public void stop(){
        super.stop();
        cam.destroy();
    }


    @Override
    public void onInit(int status) {
        RC.t.addData("Ready to talk", "!");
    }
}//VeerFermionOp