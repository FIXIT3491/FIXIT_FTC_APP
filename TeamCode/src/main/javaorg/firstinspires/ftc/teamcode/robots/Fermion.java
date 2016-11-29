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
import org.firstinspires.ftc.teamcode.opmodesupport.TaskHandler;
import org.firstinspires.ftc.teamcode.util.MathUtils;
import org.firstinspires.ftc.teamcode.util.PID;
import org.firstinspires.ftc.teamcode.util.VortexUtils;

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
    public PID veerAlgorithm = new PID(PID.Type.PID, 2.0 / 90, 0.5 / 90, 0.1 / 90);

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

        rightFore.setReverse(true);
        rightBack.setReverse(true);

        if (auto) {
            BNO055IMU.Parameters params = new BNO055IMU.Parameters();
            params.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
            params.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;

            imu = (AdafruitBNO055IMU) RC.h.get(BNO055IMU.class, "adafruit");
            imu.initialize(params);

            mouse = new TrackBall("leftFore", "rightFore"); //doesn't work...
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

    public void trackForward(double mm, double speed) {

        forward(speed);

        double begin = mouse.getXY().y;

        while (RC.l.opModeIsActive()) {

            double current = mouse.getXY().y - begin;

            forward(speed * (1 - current / mm));

            if (current > mm) {
                break;
            }//if
        }//while

        stop();

    }//trackForward

    public void trackBackward(double mm, double speed) {

        backward(speed);

        double begin = mouse.getXY().y;

        while (RC.l.opModeIsActive()) {

            double current = begin - mouse.getXY().y;

            backward(speed * (1 - current / mm));

            if (current > mm) {
                break;
            }//if
        }//while

        stop();

    }//trackBackward

    public void trackLeft(double mm, double speed) {

        left(speed);

        double begin = mouse.getXY().x;

        while (RC.l.opModeIsActive()) {

            double current = mouse.getXY().x - begin;

            left(speed * (1 - current / mm));

            if (current > mm) {
                break;
            }//if
        }//while

        stop();

    }//strafeLeft

    public void trackRight(double mm, double speed) {
        right(speed);

        double begin = mouse.getXY().x;

        while (RC.l.opModeIsActive()) {

            double current = begin - mouse.getXY().x;

            right(speed * (1 - current / mm));

            if (current > mm) {
                break;
            }//if
        }//while

        stop();

    }//strafeRight

    public void imuTurnL(double degrees, double speed) {

        this.targetAngle = MathUtils.cvtAngleToNewDomain(targetAngle - degrees);

        if(degrees < TURNING_ACCURACY_DEG) return;
        turnL(speed);
        double beginAngle = MathUtils.cvtAngleToNewDomain(getIMUAngle()[0]);
        double targetAngle = MathUtils.cvtAngleToNewDomain(beginAngle - degrees);

        while (RC.l.opModeIsActive()) {

            double currentAngle = MathUtils.cvtAngleToNewDomain(getIMUAngle()[0]);
            double angleToTurn = MathUtils.cvtAngleJumpToNewDomain(currentAngle - targetAngle);

            turnL(angleToTurn / 180 * speed);

            if (angleToTurn < TURNING_ACCURACY_DEG) {
                break;
            }//if
        }//while

        stop();
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

            turnR(angleToTurn / 180 * speed);

            if (angleToTurn < TURNING_ACCURACY_DEG) {
                break;
            }//if
        }//while

        stop();
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

            double angleError = MathUtils.cvtAngleJumpToNewDomain(targetAngle - currentAngle);

            double strength = Math.signum(angleError) * Math.abs(veerAlgorithm.update(angleError));

            Log.i("PID Val", "" + strength);

            veer(strength);
        }//if
    }//veerCheck

    //Changes the robot while letting it keep strafing
    //veerCheck() and veer() have been separated
    //because veer() is used in Tele-Op and Autonomous
    //if speed < 0, robot will veer left
    //if speed > 0, robot will veer right
    public void veer(double speed) {
        double leftForePower = leftForeRightBackStrafeSpeed;
        double leftBackPower = rightForeLeftBackStrafeSpeed;
        double rightForePower = rightForeLeftBackStrafeSpeed;
        double rightBackPower = leftForeRightBackStrafeSpeed;

        if (preservingStrafeSpeed) {
            leftForePower += speed;
            rightBackPower -= speed;

            if (Math.abs(leftForePower) > 1) {
                rightBackPower -= leftForePower - Math.signum(leftForePower) * 1;
                leftForePower = Math.signum(leftForePower) * 1;
            } else if (Math.abs(rightBackPower) > 1) {
                leftForePower -= rightBackPower - Math.signum(rightBackPower) * 1;
                rightBackPower = Math.signum(rightBackPower) * 1;
            }//else

            leftBackPower += speed;
            rightForePower -= speed;

            if (Math.abs(leftBackPower) > 1) {
                rightForePower -= leftBackPower - Math.signum(leftBackPower) * 1;
                leftBackPower = Math.signum(leftBackPower) * 1;
            } else if (Math.abs(rightForePower) > 1) {
                leftBackPower -= rightForePower - Math.signum(rightForePower) * 1;
                rightForePower = Math.signum(rightForePower) * 1;
            }//else
        } else {

            if (Math.abs(leftBackPower + speed) > 1 || Math.abs(leftForePower + speed) > 1
                    || Math.abs(rightBackPower - speed) > 1 || Math.abs(rightForePower - speed) > 1) {

                double multi = (1 - speed) / Math.max(Math.max(Math.abs(leftBackPower), Math.abs(leftForePower)),
                        Math.max(Math.abs(rightBackPower), Math.abs(rightForePower)));
                leftForePower *= multi;
                leftBackPower *= multi;
                rightForePower *= multi;
                rightBackPower *= multi;

                leftForePower += speed;
                leftBackPower += speed;
                rightForePower -= speed;
                rightBackPower -= speed;
            }//if

        }//else

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

        trackForward(Math.hypot(trans.get(0), trans.get(2)) - targetDistance, speed);

        stop();
    }//strafeToBeacon

    public void fireParticle() {

        catapult.runToPosition(1120, 0.5); //spin one full revolution

    }

}//Fermion
