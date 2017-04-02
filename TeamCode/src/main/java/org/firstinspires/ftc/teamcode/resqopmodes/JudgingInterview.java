package org.firstinspires.ftc.teamcode.resqopmodes;

import android.speech.tts.TextToSpeech;

import org.firstinspires.ftc.teamcode.RC;
import org.firstinspires.ftc.teamcode.opmodesupport.TeleOpMode;
import org.firstinspires.ftc.teamcode.robots.Lily;
import org.firstinspires.ftc.teamcode.robots.Robot;

/**
 * Created by Windows on 2016-03-05.
 */
public class JudgingInterview extends TeleOpMode implements TextToSpeech.OnInitListener {

    Lily lily;
    TextToSpeech text;
    int brushState = Robot.FORWARD;
    boolean collecting = true;
    final int BEGIN = 0;
    final int DEFAULT = 1;
    final int COLLECT = 2;
    final int MANUAL = 3;
    final int RAMP = 4;

    int armStage = BEGIN;
    int switchingFrom = BEGIN;
    boolean currentlySwitching = false;
    boolean isHookUp = true;
    boolean zipout = false;
    boolean hookMoving = false;

    @Override
    public void initialize() {
        lily = new Lily(true);
        text = new TextToSpeech(RC.c(), this);
    }

    @Override
    public void loopOpMode() {
        if (joy1.y1() < 0 && !text.isSpeaking()) {
            text.speak("Hello, my name is Lily.", TextToSpeech.QUEUE_ADD, null);
        }

        if (joy1.y2() > 0) {
            lily.people.setPower(-1);
        } else if (joy1.y2() < 0) {
            lily.people.setPower(1);
        } else {
            lily.people.stop();
        }

        if (joy1.buttonX() && getMilliSeconds() > 500) {
            hookMoving = true;
            isHookUp = !isHookUp;
            clearTimer();
        }

        if (hookMoving && getMilliSeconds() > 300) {
            hookMoving = false;
        } else if (hookMoving) {
            if (isHookUp) {
                lily.hook.setPower(1);
            } else {
                lily.hook.setPower(-1);
            }
        } else {
            lily.hook.stop();
        }


        RC.t.addData("motorR", lily.motorR.getBaseCurrentPosition());

        RC.t.addData("motorL", lily.motorL.getBaseCurrentPosition());


        if (joy1.buttonY() && lily.frontguard.currentPos < 0.5 && getMilliSeconds(2) > 300) {
            lily.frontguard.goToPos("up");
            lily.backguard.goToPos("up");
            clearTimer(2);
        } else if (joy1.buttonY() && lily.frontguard.currentPos > 0.5 && getMilliSeconds(2) > 300) {
            lily.frontguard.goToPos("down");
            lily.backguard.goToPos("down");
            clearTimer(2);
        }//elseif

        if (armStage != COLLECT && switchingFrom != COLLECT) {
            lily.wrist.setPosition((joy2.y1() / 3) + 0.5);
        } else {
            lily.wrist.setPosition(0.5);
        }

        if (joy2.leftBumper()) {
            lily.tapeMeasure.setPower(0.15);
        } else if (joy2.leftTrigger()) {
            lily.tapeMeasure.setPower(-1);
        } else {
            lily.tapeMeasure.stop();
        }//else

        if (joy2.rightTrigger()) {
            lily.tapeAdjust.out(0.0005);
        } else if (joy2.rightBumper()) {
            lily.tapeAdjust.in(0.0005);
        }//elseif

        if (joy2.y2() < -0.5) {
            lily.doorR.goToPos("open");
            lily.doorL.goToPos("open");
        } else {
            lily.doorR.goToPos("closed");
            lily.doorL.goToPos("closed");
        }

        if (joy1.buttonB() && getMilliSeconds(3) > 1000) {
            clearTimer(3);
            zipout = !zipout;
        }

        if (getMilliSeconds(3) < 1000 && zipout) {
            lily.blueZipliner.goToPos("open");
            lily.redZipliner.goToPos("open");
        } else if (getMilliSeconds(3) < 1000 && !zipout) {
            lily.blueZipliner.goToPos("close");
            lily.redZipliner.goToPos("close");
        }

        switch (brushState) {
            case Robot.STOP:
                lily.brush.setPower(0);
                break;
            case Robot.FORWARD:
                lily.brush.setPower(-1);
                break;
            case Robot.BACKWARD:
                lily.brush.setPower(1);
                break;
        }//switch

        dataLogData(hardwareMap.voltageSensor.get("Motor Controller 1").getVoltage() + "\n");
        if (!currentlySwitching) {
            if (joy2.buttonA() && armStage == COLLECT) {
                lily.elbow.setMinimumSpeed(0.2);
                lily.elbow.setTargetAndPower(12, 0.9);
                lily.elbow.setPositioningAccuracy(11);
                armStage = MANUAL;
            } else if (joy2.buttonY() && armStage == COLLECT) {
                lily.elbow.setTargetAndPower(-12, 0.9);
                lily.elbow.setMinimumSpeed(0.1);
                lily.elbow.setPositioningAccuracy(11);
                armStage = MANUAL;
            } else if (joy2.buttonA()) {
                lily.elbow.setMinimumSpeed(0.3);
                lily.elbow.setTargetAndPower(25, 0.9);
                lily.elbow.setPositioningAccuracy(24);
                armStage = MANUAL;
            } else if (joy2.buttonY()) {
                lily.elbow.setTargetAndPower(-25, 0.9);
                lily.elbow.setMinimumSpeed(0.3);
                lily.elbow.setPositioningAccuracy(24);
                armStage = MANUAL;
            } else if (joy2.buttonB()) {
                lily.turnTable.setTargetAndPower(25, 1.8);
                armStage = MANUAL;
            } else if (joy2.buttonX()) {
                lily.turnTable.setTargetAndPower(-25, 1.8);
                armStage = MANUAL;
            }//elseif

            if (joy2.buttonUp() && armStage != DEFAULT) {
                switchingFrom = armStage;
                armStage = DEFAULT;
                collecting = false;
                lily.elbow.setMinimumSpeed(0.04);
                currentlySwitching = true;
            } else if (joy2.buttonDown() && armStage == DEFAULT) {
                switchingFrom = armStage;
                armStage = COLLECT;
                currentlySwitching = true;
                lily.elbow.setMinimumSpeed(0.04);
                collecting = true;
            } else if (joy2.buttonRight() && armStage != RAMP) {
                switchingFrom = armStage;
                armStage = RAMP;
                currentlySwitching = true;
                lily.elbow.setMinimumSpeed(0.04);
                collecting = false;
            }

        }//if

        if (currentlySwitching) {

            if (armStage == RAMP) {

                brushState = Robot.STOP;
                if (lily.moveArm(-1650, 843, 0, true)) {
                    switchingFrom = RAMP;
                    currentlySwitching = false;
                }//if

            } else if (armStage == COLLECT) {

                brushState = Robot.FORWARD;
                lily.turnTable.setMinimumSpeed(0.04);
                if (lily.moveArm(-65, 0, 0.5, false)) {
                    switchingFrom = COLLECT;
                    currentlySwitching = false;
                }//if

            } else if (armStage == DEFAULT) {

                brushState = Robot.STOP;
                lily.turnTable.setMinimumSpeed(0.04);
                if (lily.moveArm(-1650, 0, 0.5, false)) {
                    switchingFrom = DEFAULT;
                    currentlySwitching = false;
                }//if
            }

        }//if

        if (joy1.buttonA()) {
            brushState = Robot.BACKWARD;
        }

        if (joy1.leftTrigger()) {
            brushState = Robot.FORWARD;
        } else if (joy1.leftBumper()) {
            brushState = Robot.STOP;
        }

        RC.t.addData("TurnTable Position", lily.turnTable.getBaseCurrentPosition());
        RC.t.addData("Elbow Position", lily.elbow.getBaseCurrentPosition());
        RC.t.addData("Wrist Position", lily.wrist.getPosition());

        lily.checkAllSystems(); //checkTimer all motor positions
        if (getMilliSeconds(4) > 119000) {
            lily.brush.stop();
        }
        if (joy2.buttonStart()) {
            lily.turnTable.stop();
            lily.elbow.stop();
        }
    }//loopOpMode

    @Override
    public void onInit(int status) {
    }
}
