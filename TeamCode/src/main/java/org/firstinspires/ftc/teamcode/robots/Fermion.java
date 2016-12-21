package org.firstinspires.ftc.teamcode.robots;

import android.util.Log;

import com.qualcomm.hardware.adafruit.AdafruitBNO055IMU;
import com.qualcomm.hardware.adafruit.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.teamcode.RC;
import org.firstinspires.ftc.teamcode.newhardware.FXTSensors.TrackBall;
import org.firstinspires.ftc.teamcode.newhardware.Motor;
import org.firstinspires.ftc.teamcode.roboticslibrary.FXTCamera;
import org.firstinspires.ftc.teamcode.roboticslibrary.TaskHandler;
import org.firstinspires.ftc.teamcode.util.MathUtils;
import org.firstinspires.ftc.teamcode.util.PID;
import org.firstinspires.ftc.teamcode.util.VortexUtils;
import org.opencv.core.Point;

/**
 * Created by FIXIT on 16-10-07.
 */
public class Fermion {

    String TAG = "FERMION";

    //position/orientation sensors
    public AdafruitBNO055IMU imu;
    public TrackBall mouse;

    //drive motors
    public Motor leftFore;
    public Motor rightFore;
    public Motor leftBack;
    public Motor rightBack;

    public Motor catapult;

    private double leftForeRightBackStrafeSpeed = 0;
    private double rightForeLeftBackStrafeSpeed = 0;

    public double targetAngle = 0;
    public final static double TURNING_ACCURACY_DEG = 2;

    public boolean useVeerCheck = true;
    public boolean preservingStrafeSpeed = false;
    public double minimumTrackingSpeed = 0.2;
    public PID veerAlgorithm = new PID(PID.Type.PID, RC.globalDouble("VeerProportional"), RC.globalDouble("VeerDerivative"), RC.globalDouble("VeerIntegral"));

    public Fermion(boolean auto) {
        leftFore = new Motor("leftFore");
        leftFore.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftFore.minSpeed = 0;

        rightFore = new Motor("rightFore");
        rightFore.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightFore.minSpeed = 0;

        leftBack = new Motor("leftBack");
        leftBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftBack.minSpeed = 0;

        rightBack = new Motor("rightBack");
        rightBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightBack.minSpeed = 0;

        catapult = new Motor("catapult");

        leftFore.setReverse(true);
        leftBack.setReverse(true);

        mouse = new TrackBall("rightFore", "rightBack"); //doesn't work...
        if (auto) {
            BNO055IMU.Parameters params = new BNO055IMU.Parameters();
            params.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
            params.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;

            imu = (AdafruitBNO055IMU) RC.h.get(BNO055IMU.class, "adafruit");
            imu.initialize(params);

        }//if
    }//Alpha

    //methods for just usual mecanum wheel driving
    public void forward(double speed) {

        strafe(0, speed);

    }//forward

    public void backward(double speed) {

        strafe(180, speed);

    }//forward

    public void left(double speed) {

        strafe(-90, speed);

    }//left

    public void right(double speed) {

        strafe(90, speed);

    }//right

    public void turnL(double speed) {
        useVeerCheck = false;

        leftFore.setPower(-speed);
        leftBack.setPower(-speed);
        rightFore.setPower(speed);
        rightBack.setPower(speed);
    }//turnL

    public void turnR(double speed) {
        useVeerCheck = false;

        leftFore.setPower(speed);
        leftBack.setPower(speed);
        rightFore.setPower(-speed);
        rightBack.setPower(-speed);
    }//turnL

    public void stop() {
        leftFore.stop();
        rightFore.stop();
        leftBack.stop();
        rightBack.stop();
    }//stop

    public void resetTargetAngle() {
        this.targetAngle = MathUtils.cvtAngleToNewDomain(getIMUAngle()[0]);
    }

    /*
    Allows robot to strafe in any direction, with 0° being the front of robot
                         0°
                         |
                 -90° –     – 90°
                         |
                       ±180°
     */
    public void strafe(double degrees, double speed) {
        useVeerCheck = true;

        degrees += 45;

        double leftForeRightBack = Math.sin(Math.toRadians(degrees));
        double rightForeLeftBack = Math.cos(Math.toRadians(degrees));

        double multi = speed / Math.max(Math.abs(leftForeRightBack), Math.abs(rightForeLeftBack));
        leftForeRightBack *= multi;
        rightForeLeftBack *= multi;

        leftForeRightBackStrafeSpeed = leftForeRightBack;
        rightForeLeftBackStrafeSpeed = rightForeLeftBack;

        leftFore.setPower(leftForeRightBack);
        leftBack.setPower(rightForeLeftBack);
        rightFore.setPower(rightForeLeftBack);
        rightBack.setPower(leftForeRightBack);

    }//strafe

    public void track(double degrees, double mm, double speed) {

        strafe(degrees, speed);

        mm *= 1440 / (4 * Math.PI * 25.4);

        double distanceRemaining = 0;
        Point begin = mouse.getEncTiks();
        Point end = new Point(begin.x + mm * Math.sin(Math.toRadians(degrees)), begin.y + mm * Math.cos(Math.toRadians(degrees)));

        Log.i("DISTPID", "X: " + (end.x - begin.x) + ", Y: " + (end.y - begin.y));

        while (RC.l.opModeIsActive()) {
            Point current = mouse.getEncTiks();
            distanceRemaining = Math.hypot(end.x - current.x, end.y - current.y);

            Log.i("Distance", distanceRemaining + "");

            strafe(Math.toDegrees(Math.atan2(end.x - current.x, end.y - current.y)), ((speed - minimumTrackingSpeed) * (distanceRemaining / mm)) + minimumTrackingSpeed);

            Log.i("ANGLEPID", Math.atan2(end.x - current.x, end.y - current.y) + "");

            if (Math.abs(distanceRemaining) < 100 * speed) {
                break;
            }//if
        }//while

        stop();
    }

    public void imuTurnL(double degrees, double speed) {

        if(degrees < TURNING_ACCURACY_DEG) return;
        turnL(speed);

        this.targetAngle = MathUtils.cvtAngleToNewDomain(targetAngle - degrees);

        double beginAngle = MathUtils.cvtAngleToNewDomain(getIMUAngle()[0]);
        double targetAngle = MathUtils.cvtAngleToNewDomain(beginAngle - degrees);

        while (RC.l.opModeIsActive()) {

            double currentAngle = MathUtils.cvtAngleToNewDomain(getIMUAngle()[0]);
            double angleToTurn = MathUtils.cvtAngleJumpToNewDomain(currentAngle - targetAngle);

            turnL(angleToTurn / 180 * (speed - 0.1) + 0.1);

            if (angleToTurn < TURNING_ACCURACY_DEG) {
                break;
            }//if
        }//while


        stop();
        useVeerCheck = true;
    }//imuTurnL

    public void imuTurnR(double degrees, double speed) {

        this.targetAngle = MathUtils.cvtAngleToNewDomain(targetAngle + degrees);
        if(degrees < TURNING_ACCURACY_DEG) return;

        turnR(speed);

        double beginAngle = MathUtils.cvtAngleToNewDomain(getIMUAngle()[0]);
        double targetAngle = MathUtils.cvtAngleToNewDomain(beginAngle + degrees);

        while (RC.l.opModeIsActive()) {

            double currentAngle = MathUtils.cvtAngleToNewDomain(getIMUAngle()[0]);
            double angleToTurn = MathUtils.cvtAngleJumpToNewDomain(targetAngle - currentAngle);

            turnR(angleToTurn / 180 * (speed - 0.1) + 0.1);

            if (angleToTurn < TURNING_ACCURACY_DEG) {
                break;
            }//if
        }//while

        stop();
        useVeerCheck = true;
    }//imuTurnR

    public void absoluteIMUTurn(double degrees, double speed) {
        double currentAngle = MathUtils.cvtAngleToNewDomain(getIMUAngle()[0]);

        double toTurn = MathUtils.cvtAngleJumpToNewDomain(degrees - currentAngle);

        if (toTurn < 0) {
            imuTurnL(Math.abs(toTurn), speed);
        } else {
            imuTurnR(Math.abs(toTurn), speed);
        }//if
    }//absoluteIMUTurn

    public double[] getIMUAngle() {
        Orientation orient = imu.getAngularOrientation();

        return new double[] {-orient.firstAngle, -orient.secondAngle, -orient.thirdAngle};
    }//getIMUAngle

    public void addVeerCheckRunnable() {
        TaskHandler.addLoopedTask("veerCheck", new Runnable() {
            @Override
            public void run() {
                veerCheck();
            }
        }, 5);
    }//addVeerCheckRunnable

    //to be used via TaskHandler
    //therefore, it's not a loop
    //essentially, we need to turn and strafe at the same time
    //uses PID (PID hasn't been tested!)
    public void veerCheck() {
        if (useVeerCheck) {
            double currentAngle = MathUtils.cvtAngleToNewDomain(getIMUAngle()[0]);

            Log.i("PIDAngle", currentAngle + ", " + targetAngle);

            double angleError = MathUtils.cvtAngleJumpToNewDomain(targetAngle - currentAngle);

            double strength = Math.signum(angleError) * Math.abs(veerAlgorithm.update(angleError));

            Log.i("PID Val", "" + strength);
            veer(strength, preservingStrafeSpeed);
        }//if
    }//veerCheck

    //Changes the robot while letting it keep strafing
    //veerCheck() and veer() have been separated
    //because veer() is used in Tele-Op and Autonomous
    //if speed < 0, robot will veer left
    //if speed > 0, robot will veer right
    public void veer(double speed, boolean preservingStrafeSpeed) {

        double leftForePower = (leftFore.getPower() + rightBack.getPower()) / 2.0;
        double leftBackPower = (leftBack.getPower() + rightFore.getPower()) / 2.0;
        double rightForePower = (leftBack.getPower() + rightFore.getPower()) / 2.0;
        double rightBackPower = (leftFore.getPower() + rightBack.getPower()) / 2.0;

        if (preservingStrafeSpeed) {

            double maxCutOff = Math.max(Math.max(Math.abs(leftBackPower + speed), Math.abs(leftForePower + speed)), Math.max(Math.abs(rightBackPower - speed), Math.abs(rightForePower - speed)));
            maxCutOff -= 1;

            if (maxCutOff > 0) {
                speed -= maxCutOff;
            }//if

            leftForePower += speed;
            leftBackPower += speed;
            rightForePower -= speed;
            rightBackPower -= speed;
        } else {

            double max = MathUtils.max(Math.abs(leftBackPower + speed), Math.abs(leftForePower + speed), Math.abs(rightBackPower - speed), Math.abs(rightForePower - speed));
            double highestSpeed = MathUtils.max(Math.abs(leftBackPower), Math.abs(leftForePower), Math.abs(rightBackPower), Math.abs(rightForePower));

            if (max > 1) {
                double maxAllowed = 1 - (max - 1);

                leftForePower *= maxAllowed / highestSpeed;
                leftBackPower *= maxAllowed / highestSpeed;
                rightForePower *= maxAllowed / highestSpeed;
                rightBackPower *= maxAllowed / highestSpeed;
            }//if

            leftForePower += speed;
            leftBackPower += speed;
            rightForePower -= speed;
            rightBackPower -= speed;

        }//else

        Log.i("Resulting Speeds", "LF: " + leftForePower + ", LB: " + leftBackPower + ", RF: " + rightForePower + ", RB: " + rightBackPower);

        leftFore.setPower(leftForePower);
        leftBack.setPower(leftBackPower);
        rightFore.setPower(rightForePower);
        rightBack.setPower(rightBackPower);
    }//veer

    public void strafeToBeacon(VuforiaTrackableDefaultListener beacon, double targetDistance, double speed) {

        strafeToBeacon(beacon, targetDistance, speed, -0.1f, new VectorF(0, -0.1f, 0));

    }//strafeToBeacon

    //until we can get a omni-directional distance sensor, the robot must turn towards the beacon first
    public void strafeToBeacon(VuforiaTrackableDefaultListener beacon, double targetDistance, double speed,
                               double robotAngle, VectorF coordinate) {

        VectorF trans = beacon.getPose().getTranslation();

        if (robotAngle != -0.1f) {
            trans = VortexUtils.navOffWall(trans, robotAngle, coordinate);
        }//if

        double angle = Math.toDegrees(Math.atan2(trans.get(0), -trans.get(2)));

        if (angle < 0) {
            imuTurnL(-angle, speed);
        } else {
            imuTurnR(angle, speed);
        }//else

        stop();
    }//strafeToBeacon

    public void fireParticle() {
        catapult.runToPosition(1120, 0.5); //spin one full revolution
    }

}//Fermion
