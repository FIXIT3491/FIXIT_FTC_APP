package org.firstinspires.ftc.teamcode.robots;

import android.util.Log;

import com.qualcomm.ftcrobotcontroller.R;
import org.firstinspires.ftc.teamcode.RC;
import org.firstinspires.ftc.teamcode.newhardware.FXTCRServo;
import org.firstinspires.ftc.teamcode.newhardware.FXTDevice;
import org.firstinspires.ftc.teamcode.newhardware.FXTSensors.AdafruitIMU;
import org.firstinspires.ftc.teamcode.newhardware.FXTServo;
import org.firstinspires.ftc.teamcode.newhardware.LinearServo;
import org.firstinspires.ftc.teamcode.newhardware.Motor;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;

import java.util.HashMap;

/**
 * Created by FIX IT on 12/28/2015.
 */
public class Lily extends Robot {

    public Motor tapeMeasure;
    public Motor turnTable;
    public Motor elbow;

    public FXTServo frontguard;
    public FXTServo backguard;
    public FXTServo doorL;
    public FXTServo doorR;
    public FXTServo door;
    public FXTServo wrist;
    public FXTServo redZipliner;
    public FXTServo blueZipliner;

    public FXTCRServo people;
    public FXTCRServo brush;
    public FXTCRServo hook;

    public LinearServo tapeAdjust;
    public LinearServo button;

    public OpticalDistanceSensor EOPD;
    public AdafruitIMU adafruit;

    public int targetAngle = 0;
    public boolean angleReached = true;
    public double commandedTurningSpeed = 0;

    int motionStage = 0;

    public Lily() {
        this(false);
    }

    public Lily(int stpd) {

        super();

        HashMap<String, FXTDevice> devs = parseRobotXML(R.xml.lily);

        tapeMeasure = (Motor) devs.get("tapeMeasure");
        turnTable = (Motor) devs.get("turnTable");
        elbow = (Motor) devs.get("elbow");

        redZipliner = (FXTServo) devs.get("redzip");
        blueZipliner = (FXTServo) devs.get("bluezip");
        doorL = (FXTServo) devs.get("doorL");
        doorR = (FXTServo) devs.get("doorR");
        frontguard = (FXTServo) devs.get("frontguard");
        backguard = (FXTServo) devs.get("backguard");

        people = (FXTCRServo) devs.get("people");
        brush = (FXTCRServo) devs.get("brush");
        hook = (FXTCRServo) devs.get("hook");

        tapeAdjust = (LinearServo) devs.get("tapeAdjust");
        button = (LinearServo) devs.get("button");

    }

    public Lily(boolean teleOp) {
        super();

        if (!teleOp) {
            motorL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            motorR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            adafruit = new AdafruitIMU("adafruit", (byte) AdafruitIMU.OPERATION_MODE_IMU);
        }//if

        tapeAdjust = new LinearServo("tapeAdjust");
        tapeAdjust.setRange(0, 0.5);

        motorL.setReverse(true);
        motorR.setReverse(false);

        tapeMeasure = new Motor("tapeMeasure");
        tapeMeasure.stop();

        hook = new FXTCRServo("hook");
//        hook.setZeroPosition(0.5);

        frontguard = new FXTServo("frontguard");
        frontguard.addPos("down", 0.2);
        frontguard.addPos("up", 0.8);
        frontguard.goToPos("down");

        backguard = new FXTServo("backguard");
        backguard.addPos("down", 0.2);
        backguard.addPos("up", 0.9);
        backguard.goToPos("down");

        wrist = new FXTServo("wrist");
        wrist.setPosition(0.5);

        doorL = new FXTServo("doorL");
        doorL.addPos("closed", 0.65);
        doorL.addPos("open", 0);
        doorL.goToPos("closed");

        doorR = new FXTServo("doorR");
        doorR.addPos("closed", 0.35);
        doorR.addPos("open", 0.7);
        doorR.goToPos("closed");

        brush = new FXTCRServo("brush");

        redZipliner = new FXTServo("redzip");
        redZipliner.addPos("open", 0.2);
        redZipliner.addPos("close", 0);
        redZipliner.goToPos("close");

        blueZipliner = new FXTServo("bluezip");
        blueZipliner.addPos("open", 0);
        blueZipliner.addPos("close", 0.2);
        blueZipliner.goToPos("close");

        people = new FXTCRServo("people");
//        people.setZeroPosition(0.53);

        turnTable = new Motor("turnTable");
        turnTable.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        turnTable.toggleTargetFixing(true);
//        turnTable.positioningAccuracy = 11;

        elbow = new Motor("elbow");
        elbow.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        elbow.toggleTargetFixing(true);

        EOPD = RC.h.opticalDistanceSensor.get("eopd");
        wheelDiameter = 5;
    }

    public void turnL(double speed) {
        super.turnL(speed);
        commandedTurningSpeed = speed;
    }

    public void turnR(double speed) {
        super.turnR(speed);
        commandedTurningSpeed = speed;
    }

    public boolean allReady() {
        return angleReached && super.allReady();
    }

    public boolean armReady() {
        return !elbow.isBusy() && !turnTable.isBusy();
    }

    public void checkAllSystems() {

        super.checkAllSystems();

//        elbow.checkTimer();
//        turnTable.checkTimer();

        if (adafruit != null) {
            angleCheck();
        }
    }

    public void imuTurnL(double degrees, double speed) {
        turnL(speed);
        motorL.toggleChecking(true);
        motorR.toggleChecking(true);
        setTargetAngle((int) -degrees);
    }

    public void imuTurnR(double degrees, double speed) {
        turnR(speed);
        motorL.toggleChecking(true);
        motorR.toggleChecking(true);
        setTargetAngle((int) degrees);
    }

    public void setTargetAngle (double newTargetAngle) {

        //adds on the angle to turn
        this.targetAngle = (int) (newTargetAngle + this.targetAngle);

        if (this.targetAngle > 360) {
            this.targetAngle -= 360;
        } else if (this.targetAngle < 0) {
            this.targetAngle += 360;
        }

        this.angleReached = false;
    }//setTargetAngle

    public void turnLToAngle(int angle) {
        imuTurnL(angle - targetAngle, 0.5);
    }//turnLToAngle

    public void turnRToAngle(int angle) {
        imuTurnR(angle - targetAngle, 0.5);
    }//turnRToAngle

    public boolean moveArm(int newElbowPosition, int newTurnTablePosition, double newWristPosition, boolean elbowFirst) {

        if (elbowFirst) {
            if (motionStage == 0) {
                elbow.setTarget(elbow.getBeginningPosition() + newElbowPosition);
                elbow.setPower(0.3);
                motionStage++;
            } else if (motionStage == 1 && armReady()) {
                elbow.stop();
                turnTable.setTarget(turnTable.getBeginningPosition() + newTurnTablePosition);
                turnTable.setPower(0.3);
                motionStage++;
            } else if (motionStage == 2 && armReady()) {
                turnTable.stop();
                wrist.setPosition(newWristPosition);
                motionStage = 0;
                return true;
            }//elseif
        } else {
            if (motionStage == 0) {
                turnTable.setTarget(turnTable.getBeginningPosition() + newTurnTablePosition);
                turnTable.setPower(0.3);
                motionStage++;
            } else if (motionStage == 1 && armReady()) {
                turnTable.stop();
                elbow.setTarget(elbow.getBeginningPosition() + newElbowPosition);
                elbow.setPower(0.3);
                motionStage++;
            } else if (motionStage == 2 && armReady()) {
                elbow.stop();
                wrist.setPosition(newWristPosition);
                motionStage = 0;
                return true;
            }//elseif
        }//else

        return false;
    }//moveArm

    //fixes the robot's angle (for general turning)
    public void angleCheck() {

        if (!angleReached) {

            double currentAngle = adafruit.getEulerAngles()[0];

            double angleToTurn = targetAngle - currentAngle;

            if (angleToTurn > 180) {
                angleToTurn -= 360;
            } else if (angleToTurn < -180) {
                angleToTurn += 360;
            }//elseif

            //if the angle to turn is less than 0.5 degree, we're done.
            if (Math.abs(angleToTurn) < 0.5) {
                angleReached = true;
//                motorL.target = motorL.getBaseCurrentPosition();
//                motorR.target = motorR.getBaseCurrentPosition();
                halt();
                return;
            }//if

            //calculates to what degree to alter the motor speeds to fix the angle
            double multiplier = Math.abs(angleToTurn) / 90.0;

            //making sure we're not increasing the motor speeds
            if (multiplier > 1) {
                multiplier = 1;
            }//if

            RC.t.addData("AngleToTurn", angleToTurn);

            if (multiplier * commandedTurningSpeed < 8.0) {
                multiplier = 0.8 / commandedTurningSpeed;
            }//if

            RC.t.addData("Altered Speed", (commandedTurningSpeed * multiplier));
            RC.t.addData("Angle Remaining", angleToTurn);
            
            if (angleToTurn > 0) {
                motorR.setPower(-commandedTurningSpeed * multiplier);
                motorL.setPower(commandedTurningSpeed * multiplier);
            } else if (angleToTurn < 0) {
                motorR.setPower(commandedTurningSpeed * multiplier);
                motorL.setPower(-commandedTurningSpeed * multiplier);
            }//elseif
        }//if

    }//angleCheck

    //fixes the robot's angle (for veer prevention)
    //may change to using encoders
    public void veerCheck() {

        //if we currently not trying to turn,
        //then we will activate our veer prevention
        if (angleReached) {

            double motorSpeed = (motorL.getPower() + motorR.getPower()) / 2;

            double angleToTurn = targetAngle - adafruit.getEulerAngles()[0];

            if (angleToTurn > 180) {
                angleToTurn -= 360;
            } else if (angleToTurn < -180) {
                angleToTurn += 360;
            }//elseif

            Log.i("AngleToTurn", "veerCheck: " + angleToTurn);

            //calculates to what degree to alter the motor speeds to fix the angle
            double alter = (Math.abs(angleToTurn) / 120.0);

            //approximate (very approximate) calculations show that '120' has the robot
            //turning at 2.78 degrees per second when angleToTurn = 2 degrees, show this seems good

            Log.i("Altering", "" + alter);

            if (angleToTurn < 0) {
                motorR.setPower(motorSpeed - alter);
                motorL.setPower(motorSpeed + alter);
            } else if (angleToTurn > 0) {
                motorR.setPower(motorSpeed + alter);
                motorL.setPower(motorSpeed - alter);
            }//elseif

            Log.i("MotorSpeeds", "R: " + motorR.getPower() + ", L: " + motorL.getPower());

        }//if

    }//veerCheck


}
