package org.firstinspires.ftc.teamcode.resqopmodes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RC;
import org.firstinspires.ftc.teamcode.opmodesupport.TeleOpMode;
import org.firstinspires.ftc.teamcode.roboticslibrary.DataWriter;
import org.firstinspires.ftc.teamcode.robots.Lily;
import org.firstinspires.ftc.teamcode.robots.Robot;

import java.io.FileNotFoundException;

/**
 * Created by FIXIT on 8/29/2015
 */
@TeleOp
public class LilyOp extends TeleOpMode {

    Lily lily;
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

    DataWriter writer;

    @Override
    public void initialize() {
        lily = new Lily(true);
        clearTimer(2);
        clearTimer(3);
        try {
            writer = new DataWriter("teleopdatalog" + RC.runNum + ".txt", true);
            RC.runNum++;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
//        RC.t.setDataLogFile("volts.txt", true);
    }//initialize

    @Override
    public void start() {
        lily.tapeAdjust.setPosition(0.01);
        lily.tapeMeasure.setPower(0);
        clearTimer(4);
    }//start


    @Override
    public void loopOpMode() {

        double left = joy1.y1();
        double right = joy1.y2();

        if (collecting) {
            left = -right;
            right = -joy1.y1();
        }

        if (joy1.rightBumper()) {
            left /= 5;
            right /= 5;
        }

        lily.driveL(right);
        lily.driveR(left);


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

//
//        if (joy1.buttonX() && getMilliSeconds() > 500) {
//            if (isHookUp) {
//                lily.hook.goToPos("down");
//                isHookUp = false;
//            } else {
//                lily.hook.goToPos("up");
//                isHookUp = true;
//            }//else
//            clearTimer();
//        }//if

        if (joy1.buttonY() && lily.frontguard.getPosition() < 0.5 && getMilliSeconds(2) > 300) {
            lily.frontguard.goToPos("up");
            lily.backguard.goToPos("up");
            clearTimer(2);
        } else if (joy1.buttonY() && lily.frontguard.getPosition() > 0.5 && getMilliSeconds(2) > 300) {
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

        switch(brushState){
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

        //dataLogData(hardwareMap.voltageSensor.get("Motor Controller 1").getVoltage() + "\n");
        if (!currentlySwitching) {
            if (joy2.buttonA() && armStage == COLLECT) {
                lily.elbow.setTarget(12 + lily.elbow.getBaseCurrentPosition());
                lily.elbow.setPower(0.4);
                armStage = MANUAL;
            } else if (joy2.buttonY() && armStage == COLLECT) {
                lily.elbow.setTarget(-12 + lily.elbow.getBaseCurrentPosition());
                lily.elbow.setPower(0.4);
                armStage = MANUAL;
            } else if (joy2.buttonA()) {
                lily.elbow.setTarget(-25 + lily.elbow.getBaseCurrentPosition());
                lily.elbow.setPower(0.6);
                armStage = MANUAL;
            } else if (joy2.buttonY()) {
                lily.elbow.setTarget(25 + lily.elbow.getBaseCurrentPosition());
                lily.elbow.setPower(0.6);
                armStage = MANUAL;
            } else

            if (joy2.buttonB()) {
                lily.turnTable.setTarget(25 + lily.turnTable.getBaseCurrentPosition());
                lily.turnTable.setPower(0.6);
                armStage = MANUAL;
            } else if (joy2.buttonX()) {
                lily.turnTable.setTarget(-25 + lily.turnTable.getBaseCurrentPosition());
                lily.turnTable.setPower(0.6);
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


//        Log.i("Elbow", lily.elbow.getBaseCurrentPosition() + "----" + lily.elbow.getM().getTargetPosition() + "");
//        Log.i("TurnTable", lily.turnTable.getBaseCurrentPosition() + "----" + lily.turnTable.getM().getTargetPosition() + "");

        lily.checkAllSystems(); //checkTimer all motor savedPositions
        if (getMilliSeconds(4) > 119000) {
            lily.brush.stop();
        }

        if (joy2.buttonStart()) {
            lily.turnTable.stop();
            lily.elbow.stop();
        }//if

//        if (lily.elbow.reachedTarget()) {
//            lily.elbow.stop();
//        }//if
//
//        if (lily.turnTable.reachedTarget()) {
//            lily.elbow.stop();
//        }//if

        writer.write("driveL: " + lily.motorL.returnCurrentState() + "\n");
        writer.write("driveR: " + lily.motorR.returnCurrentState() + "\n");
        writer.write("elbow: " + lily.elbow.returnCurrentState() + "\n");
        writer.write("turnTable: " + lily.turnTable.returnCurrentState() + "\n");
        writer.write("TurnTable/TapeMeasure MC: " + RC.h.voltageSensor.get("Motor Controller 3").getVoltage() + "\n");
        writer.write("Drive MC: " + RC.h.voltageSensor.get("Motor Controller 2").getVoltage() + "\n");
        writer.write("Elbow MC: " + RC.h.voltageSensor.get("Motor Controller 1").getVoltage() + "\n");

//        RC.t.dataLogData("driveL", lily.motorL.returnCurrentState() + "\n");
//        RC.t.dataLogData("driveR", lily.motorR.returnCurrentState() + "\n");
//        RC.t.dataLogData("MotorC1", RC.h.voltageSensor.get("Motor Controller 1").getVoltage() + "\n");
//        RC.t.dataLogData("MotorC2", RC.h.voltageSensor.get("Motor Controller 2").getVoltage() + "\n");
//        RC.t.dataLogData("MotorC3", RC.h.voltageSensor.get("Motor Controller 3").getVoltage() + "\n");

    }//loopOpMode


    public void stop() {
        super.stop();
        writer.closeWriter();
    }

}//class