package org.firstinspires.ftc.teamcode.robots;

import android.support.annotation.IntDef;
import android.util.Log;

import com.qualcomm.hardware.adafruit.AdafruitBNO055IMU;
import com.qualcomm.hardware.adafruit.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.teamcode.RC;
import org.firstinspires.ftc.teamcode.newhardware.FXTSensors.FXTOpticalDistanceSensor;
import org.firstinspires.ftc.teamcode.newhardware.FXTSensors.TrackBall;
import org.firstinspires.ftc.teamcode.newhardware.FXTServo;
import org.firstinspires.ftc.teamcode.newhardware.LinearServo;
import org.firstinspires.ftc.teamcode.newhardware.Motor;
import org.firstinspires.ftc.teamcode.roboticslibrary.TaskHandler;
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
    public Motor collector;

    public LinearServo shooter1;
    public LinearServo shooter2;

    public FXTServo door;

    public FXTOpticalDistanceSensor leftBeacon;
    public FXTOpticalDistanceSensor rightBeacon;

    public double targetAngle = 0;
    private final static double TURNING_ACCURACY_DEG = 2;
    private final static double MINIMUM_TURNING_SPEED = 0.1;

    public boolean preservingStrafeSpeed = false;
    private boolean useVeerCheck = true;
    private PID veerAlgorithm = new PID(PID.Type.PID, RC.globalDouble("VeerProportional"), RC.globalDouble("VeerDerivative"), RC.globalDouble("VeerIntegral"));
    private final static double MINIMUM_TRACKING_SPEED = 0.19;
    private final static double TRACKING_ACCURACY_TIKS = 30;

    public double commandedStrafeSpeedRightForeLeftBack = 0;
    public double commandedStrafeSpeedRightBackLeftFore = 0;

    public static int LOADED = 0;
    public static int FIRING = 1;
    public static int PRIMED = 2;
    public static int RELOADING = 3;
    public static int PRIMING = 4;
    public static int FIRE = 5;
    private long fireTime = 0;

    public int shooterState = LOADED;
    public int requestedShooterState = LOADED;

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

        leftFore.setReverse(true);
        leftBack.setReverse(true);

        collector = new Motor("collector");

        shooter1 = new LinearServo("shoot1");
        shooter1.setPosition(0.2);
        shooter2 = new LinearServo("shoot2");
        shooter2.setPosition(0.2);


        door = new FXTServo("door");
        door.addPos("open", 0.53);
        door.addPos("close", 0);
        door.goToPos("close");



        mouse = new TrackBall("rightFore", "rightBack");
        if (auto) {
            BNO055IMU.Parameters params = new BNO055IMU.Parameters();
            params.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
            params.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;

            imu = (AdafruitBNO055IMU) RC.h.get(BNO055IMU.class, "adafruit");
            imu.initialize(params);
        }//if

        leftBeacon = new FXTOpticalDistanceSensor("leftBeacon");
        rightBeacon = new FXTOpticalDistanceSensor("rightBeacon");

    }//Fermion

    /*
    MECANUM DRIVING METHODS
     */

    public void usePlannedSpeeds() {
        leftFore.usePlannedSpeed();
        leftBack.usePlannedSpeed();
        rightFore.usePlannedSpeed();
        rightBack.usePlannedSpeed();
    }//usePlannedSpeeds

    public void forward(double speed) {

        strafe(0, speed, true);

    }//forward

    public void backward(double speed) {

        strafe(180, speed, true);

    }//forward

    public void left(double speed) {

        strafe(-90, speed, true);

    }//left

    public void right(double speed) {

        strafe(90, speed, true);

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
        commandedStrafeSpeedRightBackLeftFore = 0;
        commandedStrafeSpeedRightForeLeftBack = 0;
        leftFore.stop();
        rightFore.stop();
        leftBack.stop();
        rightBack.stop();
    }//stop

    /*
    Allows robot to strafe in any direction, with 0° being the front of robot
                         0°
                         |
                 -90° –    – 90°
                         |
                       ±180°
     */
    public void strafe(double degrees, double speed, boolean setImmediately) {
        useVeerCheck = true;

        degrees += 45;

        double leftForeRightBack = Math.sin(Math.toRadians(degrees));
        double rightForeLeftBack = Math.cos(Math.toRadians(degrees));

        double multi = speed / Math.max(Math.abs(leftForeRightBack), Math.abs(rightForeLeftBack));
        leftForeRightBack *= multi;
        rightForeLeftBack *= multi;

        if (!setImmediately) {
            Log.i("!!!PlannedSpeeds", leftForeRightBack + ", " + rightForeLeftBack);
            leftFore.setPlannedSpeed(leftForeRightBack);
            leftBack.setPlannedSpeed(rightForeLeftBack);
            rightFore.setPlannedSpeed(rightForeLeftBack);
            rightBack.setPlannedSpeed(leftForeRightBack);
        } else {
            leftFore.setPower(leftForeRightBack);
            leftBack.setPower(rightForeLeftBack);
            rightFore.setPower(rightForeLeftBack);
            rightBack.setPower(leftForeRightBack);
        }//else

        commandedStrafeSpeedRightBackLeftFore = leftForeRightBack;
        commandedStrafeSpeedRightForeLeftBack = rightForeLeftBack;

    }//strafe

    public void track(double degrees, double mm, double speed) {

        strafe(degrees, speed, true);
        mm *= 1440 / (4 * Math.PI * 25.4);

        double distanceRemaining;
        TrackBall.Point begin = mouse.getEncTiks();
        TrackBall.Point end = new TrackBall.Point(begin.x + mm * Math.sin(Math.toRadians(degrees)), begin.y + mm * Math.cos(Math.toRadians(degrees)));

        Log.i("DISTPID", "X: " + (end.x - begin.x) + ", Y: " + (end.y - begin.y));

        while (RC.l.opModeIsActive()) {
            TrackBall.Point current = mouse.getEncTiks();
            distanceRemaining = Math.hypot(end.x - current.x, end.y - current.y);

            Log.i("Distance", distanceRemaining + "; " + current.toString() + "; " + (((speed - MINIMUM_TRACKING_SPEED) * Math.pow(distanceRemaining / mm, 2)) + MINIMUM_TRACKING_SPEED));

            strafe(Math.toDegrees(Math.atan2(end.x - current.x, end.y - current.y)), (((speed - MINIMUM_TRACKING_SPEED) * Math.pow(distanceRemaining / mm, 2)) + MINIMUM_TRACKING_SPEED), true);

            Log.i("ANGLEPID", Math.atan2(end.x - current.x, end.y - current.y) + "");

            if (Math.abs(distanceRemaining) < TRACKING_ACCURACY_TIKS) {
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

            turnL(angleToTurn / 180 * (speed - MINIMUM_TURNING_SPEED) + MINIMUM_TURNING_SPEED);

            if (angleToTurn < TURNING_ACCURACY_DEG) {
                break;
            }//if
        }//while

        stop();
        useVeerCheck = true;
    }//imuTurnL

    public void imuTurnR(double degrees, double speed) {

        if(degrees < TURNING_ACCURACY_DEG) return;
        turnR(speed);

        this.targetAngle = MathUtils.cvtAngleToNewDomain(targetAngle + degrees);

        double beginAngle = MathUtils.cvtAngleToNewDomain(getIMUAngle()[0]);
        double targetAngle = MathUtils.cvtAngleToNewDomain(beginAngle + degrees);

        while (RC.l.opModeIsActive()) {

            double currentAngle = MathUtils.cvtAngleToNewDomain(getIMUAngle()[0]);
            double angleToTurn = MathUtils.cvtAngleJumpToNewDomain(targetAngle - currentAngle);

            turnR(angleToTurn / 180 * (speed - MINIMUM_TURNING_SPEED) + MINIMUM_TURNING_SPEED);

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
            imuTurnL(-toTurn, speed);
        } else {
            imuTurnR(toTurn, speed);
        }//else
        setTargetAngle(degrees);
    }//absoluteIMUTurn

    public double[] getIMUAngle() {
        Orientation orient = imu.getAngularOrientation();

        return new double[] {-orient.firstAngle, -orient.secondAngle, -orient.thirdAngle};
    }//getIMUAngle

    public void resetTargetAngle() {
        this.targetAngle = MathUtils.cvtAngleToNewDomain(getIMUAngle()[0]);
    }//resetTargetAngle

    public void setTargetAngle(double targetAngle){
        this.targetAngle = targetAngle;
    }

    public void addVeerCheckRunnable() {
        TaskHandler.addLoopedTask("veerCheck", new Runnable() {
            @Override
            public void run() {
                veerCheck();
            }
        }, 5);
    }//addVeerCheckRunnable

    //to be used via TaskHandler
    //essentially, we need to turn and strafe at the same time
    //uses PID
    public void veerCheck() {
        if (useVeerCheck) {
            double currentAngle = MathUtils.cvtAngleToNewDomain(getIMUAngle()[0]);

            Log.i("PIDAngle", currentAngle + ", " + targetAngle);

            double angleError = MathUtils.cvtAngleJumpToNewDomain(targetAngle - currentAngle);

            double strength = Math.signum(angleError) * Math.abs(veerAlgorithm.update(angleError));

            Log.i("PID Val", "" + strength);

            veer(strength, preservingStrafeSpeed, true);
        }//if
    }//veerCheck

    //Changes the robot's angle while letting it keep strafing
    //if speed < 0, robot will veer left
    //if speed > 0, robot will veer right
    //if preservingStrafeSpeed is true, then the robot's strafing speed won't be affected
    //if preservingStrafeSpeed is false, then the robot might slow down to accomodate veering
    public void veer(double speed, boolean preservingStrafeSpeed, boolean setImmediately) {

        speed = Math.signum(speed) * Math.min(1, Math.abs(speed));
//
//        double leftForePower = (leftFore.getPower() + rightBack.getPower()) / 2.0;
//        double leftBackPower = (leftBack.getPower() + rightFore.getPower()) / 2.0;
//        double rightForePower = (leftBack.getPower() + rightFore.getPower()) / 2.0;
//        double rightBackPower = (leftFore.getPower() + rightBack.getPower()) / 2.0;

        double leftForePower = commandedStrafeSpeedRightBackLeftFore;
        double leftBackPower = commandedStrafeSpeedRightForeLeftBack;
        double rightForePower = commandedStrafeSpeedRightForeLeftBack;
        double rightBackPower = commandedStrafeSpeedRightBackLeftFore;

        if (!setImmediately) {
            leftForePower = (leftFore.plannedSpeed + rightBack.plannedSpeed) / 2.0;
            leftBackPower = (rightFore.plannedSpeed + leftBack.plannedSpeed) / 2.0;
            rightForePower = (rightFore.plannedSpeed + leftBack.plannedSpeed) / 2.0;
            rightBackPower = (leftFore.plannedSpeed + rightBack.plannedSpeed) / 2.0;
        }

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
            double maxOriginal = MathUtils.max(Math.abs(leftBackPower), Math.abs(leftForePower), Math.abs(rightBackPower), Math.abs(rightForePower));

            if (max > 1) {
                double maxAllowed = 1 - Math.abs(speed);

                leftForePower *= maxAllowed / maxOriginal;
                leftBackPower *= maxAllowed / maxOriginal;
                rightForePower *= maxAllowed / maxOriginal;
                rightBackPower *= maxAllowed / maxOriginal;
            }//if

            leftForePower += speed;
            leftBackPower += speed;
            rightForePower -= speed;
            rightBackPower -= speed;

        }//else

        Log.i("Resulting Speeds", "LF: " + leftForePower + ", LB: " + leftBackPower + ", RF: " + rightForePower + ", RB: " + rightBackPower);

        if (!setImmediately) {
            leftFore.setPlannedSpeed(leftForePower);
            leftBack.setPlannedSpeed(leftBackPower);
            rightFore.setPlannedSpeed(rightForePower);
            rightBack.setPlannedSpeed(rightBackPower);
        } else {
            leftFore.setPower(leftForePower);
            leftBack.setPower(leftBackPower);
            rightFore.setPower(rightForePower);
            rightBack.setPower(rightBackPower);
        }//else

    }//veer

    public void strafeToBeacon(VuforiaTrackableDefaultListener beacon, double bufferDistance, double speed, boolean strafe) {

        if (beacon.getPose() != null) {
            VectorF trans = beacon.getPose().getTranslation();

            double angle = Math.toDegrees(Math.atan2(trans.get(0), -trans.get(2)));

            if (strafe) {
                track(angle, Math.hypot(trans.get(0), trans.get(2) - bufferDistance), speed);
            } else {
                if (angle < 0) {
                    imuTurnL(-angle, speed);
                } else {
                    imuTurnR(angle, speed);
                }//else

                track(0, Math.hypot(trans.get(0), trans.get(2)) - bufferDistance, speed);
            }//else

        } else {
            RC.t.addData("FERMION", "Strafe To Beacon failed: Beacon not visible");
        }//else

    }//strafeToBeacon

    public void strafeToBeacon(VuforiaTrackableDefaultListener beacon, double bufferDistance, double speed, boolean strafe,
                               double robotAngle, VectorF coordinate) {

        if (beacon.getPose() != null) {
            VectorF trans = beacon.getPose().getTranslation();

            Log.i(TAG, "strafeToBeacon: " + trans);

            trans = VortexUtils.navOffWall(trans, robotAngle, coordinate);

            Log.i(TAG, "strafeToBeacon: " + trans);

            double angle = Math.toDegrees(Math.atan2(trans.get(0), trans.get(2)));

            Log.i(TAG, "strafeToBeacon: " + angle);


            if (strafe) {
                track(angle, Math.hypot(trans.get(0), trans.get(2)) - bufferDistance, speed);
            } else {
                if (angle < 0) {
                    imuTurnL(-angle, speed);
                } else {
                    imuTurnR(angle, speed);
                }//else

                track(0, Math.hypot(trans.get(0), trans.get(2) - bufferDistance), speed);
            }//else

        } else {
            RC.t.addData("FERMION", "Strafe To Beacon failed: Beacon not visible");
        }//else
    }//strafeToBeacon

    public void setCollectorState(@CollectorStates int state){
        switch (state){
            case Robot.IN: collector.setPower(-1);
                break;
            case Robot.OUT: collector.setPower(1);
                break;
            case Robot.STOP: collector.stop();
        }
    }
    @IntDef({Robot.IN, Robot.OUT, Robot.STOP})
    public @interface CollectorStates{};

    public double getLight(@LightSensors int config){
        if(config == Robot.LEFT){
            return leftBeacon.getValue();
        } else {
            return rightBeacon.getValue();
        }
    }

    @IntDef({Robot.LEFT, Robot.RIGHT})
    public @interface LightSensors{}


    public void shoot(){
        requestedShooterState = FIRE;
    }

    public void reload(){
        requestedShooterState = LOADED;
    }

    public void prime(){
        requestedShooterState = PRIMED;
    }

    protected void updateShooter(){
        if(fireTime < System.currentTimeMillis()) {
            if(shooterState == FIRING){
                shooterState = FIRE;
            } else if(shooterState == PRIMING){
                shooterState = PRIMED;
            } else if(shooterState == RELOADING){
                shooterState = LOADED;
            }

            if (shooterState == LOADED) {
                if (requestedShooterState == PRIMED) {
                    shooter1.setPosition(0.45);
                    shooter2.setPosition(0.45);
                    fireTime = System.currentTimeMillis() + 1910;
                    requestedShooterState = -1;
                    shooterState = PRIMING;
                } else if(requestedShooterState == FIRE){
                    shooter1.setPosition(0.55);
                    shooter2.setPosition(0.55);
                    fireTime = System.currentTimeMillis() + 3900;
                    shooterState = FIRING;
                }
            } else if(shooterState == PRIMED){
                if(requestedShooterState == FIRE){
                    shooter1.setPosition(0.7);
                    shooter2.setPosition(0.7);
                    fireTime = System.currentTimeMillis() + 1800;
                    requestedShooterState = -1;
                    shooterState = FIRING;
                }
            } else if(shooterState == FIRE){
                shooter1.setPosition(0.2);
                shooter2.setPosition(0.2);
                fireTime = System.currentTimeMillis() + 3700;
                requestedShooterState = -1;
                shooterState = RELOADING;
            } else if(requestedShooterState == LOADED){
                shooter1.setPosition(0.2);
                shooter2.setPosition(0.2);
            }
        }
    }

    public void startShooterControl(){
        TaskHandler.addLoopedTask("Shooter", new Runnable(){
            @Override
            public void run() {
                updateShooter();
            }
        }, 5);
    }


}//Fermion
