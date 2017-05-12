package org.firstinspires.ftc.teamcode.robots;

import org.firstinspires.ftc.teamcode.RC;
import org.firstinspires.ftc.teamcode.newhardware.FXTCRServo;
import org.firstinspires.ftc.teamcode.newhardware.FXTSensors.AdafruitIMU;
import org.firstinspires.ftc.teamcode.newhardware.FXTServo;
import org.firstinspires.ftc.teamcode.newhardware.LinearServo;
import org.firstinspires.ftc.teamcode.newhardware.Motor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by User on 10/17/2015.
 */
public class Billy extends Robot {

    public AdafruitIMU adafruit;
    public Motor baseJoint;
    public Motor turnTable;
    public LinearServo elbow;
    public Motor ziplines;
    private Motor tapeMeasure;
    private FXTCRServo brush;
    private LinearServo tapeAdjust;
    private LinearServo wrist;
    private FXTServo door;
    private FXTCRServo hook;

    public Billy() {
        this(false);
    }

    public Billy(boolean teleOp) {
        super();

        brush = new FXTCRServo("brush");

        baseJoint = new Motor("base");
        baseJoint.toggleChecking(true);

        turnTable = new Motor("turnTable");
        turnTable.toggleChecking(true);

        elbow = new LinearServo("elbow");
        wrist = new LinearServo("wrist");
        hook = new FXTCRServo("hook");

        door = new FXTServo("door");
        door.addPos("open", 1);
        door.addPos("close", 0);


        tapeMeasure = new Motor("tapeMeasure");
        tapeMeasure.stop();

        if (!teleOp) {
            adafruit = new AdafruitIMU("adafruit", (byte) AdafruitIMU.OPERATION_MODE_IMU);
        }

        tapeAdjust = new LinearServo("tapeAdjust");
        tapeAdjust.setPosition(0);
        ziplines = new Motor("lights");
        ziplines.setPower(0.1);
    }//constructor

    public void setBrushState(int state){
        switch (state){
            case OUT:
                brush.setPower(-1);
                break;
            case IN:
                brush.setPower(1);
                break;
            case STOP:
                brush.stop();
                break;
        }//switch
    }//setBrushState

    public void setBrushState(int state, int time) {
        switch (state) {
            case OUT:
                brush.setPower(-0.1);
                break;
            case IN:
                brush.setPower(0.1);
                break;
            case STOP: brush.stop();
                break;
        }//switch
        wait(time);
        brush.stop();
    }//setBrushState

    public void turnArm(int direction) {
        switch (direction) {
            case LEFT:
                turnTable.setPower(0.15);
                break;
            case RIGHT:
                turnTable.setPower(-0.15);
                break;
            case STOP:
                turnTable.stop();
                break;
        }//switch
        RC.t.addData("arm", turnTable.getBaseCurrentPosition());
        RC.t.addData("base", baseJoint.getBaseCurrentPosition());
    }

    public void turnArm(int direction, int degrees) {
        int target = turnTable.getBaseCurrentPosition();
        switch (direction) {
            case LEFT:
                turnTable.setPower(0.05);
                target += degrees;
                while (turnTable.getBaseCurrentPosition() < target && RC.l.opModeIsActive()) {
                    RC.l.idle();
                }
                break;
            case RIGHT:
                turnTable.setPower(-0.05);
                target -= degrees;
                while (turnTable.getBaseCurrentPosition() > target && RC.l.opModeIsActive()) {
                    RC.l.idle();
                }
                break;
            case STOP:
                turnTable.stop();
                break;
        }//switch

        turnTable.stop();
    }

    public void setTapeMeasure(int state){
        switch (state){
            case OUT:
                tapeMeasure.setPower(1);
                break;
            case IN:
                tapeMeasure.setPower(-1);
                break;
            case STOP: RC.t.addData("Helo", "hi"); tapeMeasure.setPower(0);
                break;
        }//switch
    }//setTapeMeasure

    public void adjustTapeAngle(int state) {
        switch (state) {
            case UP:
                tapeAdjust.out(0.1);
                break;
            case DOWN:
                tapeAdjust.in(0.1);
                break;
        }//switch

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int getBrushState(int state) {
        return (brush.getPower() < 0) ? BACKWARD : (brush.getPower() > 0) ? FORWARD : STOP;
    }//getBrushState

    public void setBaseJointState(int state) {
        switch (state) {
            case UP:
                baseJoint.setPower(0.1);
                break;
            case DOWN:
                baseJoint.setPower(-0.1);
                break;
            case STOP:
                baseJoint.stop();
                break;
        }//switch
        RC.t.addData("arm", turnTable.getBaseCurrentPosition());
        RC.t.addData("base", baseJoint.getBaseCurrentPosition());
    }//setBaseJoint

    public void setBaseJointState(int state, int deg) {
        int target = baseJoint.getBaseCurrentPosition();

        try {
            ((LinearOpMode) (RC.o)).waitOneFullHardwareCycle();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        switch (state) {
            case UP:
                baseJoint.setPower(0.1);
                target += deg;
                while (baseJoint.getBaseCurrentPosition() < target && RC.l.opModeIsActive()) {
                    RC.t.addData("base", baseJoint.getBaseCurrentPosition());
                    RC.l.idle();
                }
                break;
            case DOWN:
                baseJoint.setPower(-0.1);
                target -= deg;
                while (baseJoint.getBaseCurrentPosition() > target && RC.l.opModeIsActive()) {
                    RC.l.idle();
                }
                break;
            case STOP:
                baseJoint.stop();
                break;
        }//switch

        baseJoint.stop();
    }//setBaseJoint

    //ARM MOTIONS

    //LOW
    public void moveToLowBasket() {

        turnTable.setTargetAndPower(770, 0.5);
        elbow.setPosition(0.48);

    }

    public void backFromLowBasket() {

        turnTable.setTargetAndPower(-700, 0.3);
        elbow.setPosition(0.2);

    }

    //CHANGE MED/HIGH VALUES

    //MED
    public void moveToMedBasket() {

        turnTable.setTargetAndPower(770, 0.5);
        elbow.setPosition(0.48);

    }

    public void backFromMedBasket() {

        turnTable.setTargetAndPower(-700, 0.3);
        elbow.setPosition(0.2);

    }

    //HIGH
    public void moveToHighBasket() {

        turnTable.setTargetAndPower(770, 0.5);
        elbow.setPosition(0.48);

    }

    public void backFromHighBasket() {

        turnTable.setTargetAndPower(-700, 0.3);
        elbow.setPosition(0.2);

    }

    public void moveElbow(int state) {

        switch (state) {
            case OUT:
                elbow.out(0.0007);
                break;
            case IN:
                elbow.in(0.0007);
                break;
        }

    }

    public void moveWrist(int state) {
        switch (state) {
            case OUT:
                wrist.out(0.001);
                break;
            case IN:
                wrist.in(0.001);
                break;
        }
    }

    public void moveDoor(int state) {
        switch (state) {
            case OPEN:
                door.goToPos("open");
                break;
            case CLOSE:
                door.goToPos("close");
                break;
        }
    }

    public void imuTurnL(int degrees, double speed) {

        degrees = (int) adafruit.getEulerAngles()[0] - degrees;
        degrees %= 360;

        turnL(speed);

        RC.t.addData("Heading", adafruit.getEulerAngles()[0]);

        while (Math.abs(adafruit.getEulerAngles()[0] - degrees) > 2 && RC.l.opModeIsActive()) {
            try {
                ((LinearOpMode) RC.o).waitOneFullHardwareCycle();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            RC.t.addData("Heading", adafruit.getEulerAngles()[0]);
        }

        halt();
    }

    public void imuTurnR(int degrees, double speed) {

        degrees = (int) adafruit.getEulerAngles()[0] + degrees;
        degrees %= 360;

        turnR(speed);

        RC.t.addData("Heading", adafruit.getEulerAngles()[0]);

        while (Math.abs(adafruit.getEulerAngles()[0] - degrees) > 2 && RC.l.opModeIsActive()) {
            try {
                ((LinearOpMode) RC.o).waitOneFullHardwareCycle();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            RC.t.addData("Heading", adafruit.getEulerAngles()[0]);
        }

        halt();
    }

    public void hookDown() {
        hook.setPower(0.2);
    }

    public void stopHook() {
        hook.setPower(0);
    }

    public void checkMotorTargets() {

//        baseJoint.checkTargetWithFixing();
//        turnTable.checkTargetWithFixing();

    }

}