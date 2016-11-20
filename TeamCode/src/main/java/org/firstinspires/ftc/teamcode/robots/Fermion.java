package org.firstinspires.ftc.teamcode.robots;

import android.graphics.Bitmap;
import android.util.Log;

import com.qualcomm.hardware.adafruit.AdafruitBNO055IMU;
import com.qualcomm.hardware.adafruit.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.vuforia.CameraCalibration;
import com.vuforia.Image;
import com.vuforia.Matrix34F;
import com.vuforia.Tool;
import com.vuforia.Vec2F;
import com.vuforia.Vec3F;

import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.teamcode.RC;
import org.firstinspires.ftc.teamcode.newhardware.FXTSensors.TrackBall;
import org.firstinspires.ftc.teamcode.newhardware.Motor;
import org.firstinspires.ftc.teamcode.opmodesupport.AutoOpMode;
import org.firstinspires.ftc.teamcode.util.MathUtils;
import org.firstinspires.ftc.teamcode.util.OCVUtils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

import java.util.Arrays;

/**
 * Created by FIXIT on 16-10-07.
 */
public class Fermion {

    String TAG = "FERMION";

    public AdafruitBNO055IMU imu;
    public TrackBall mouse;

    public Motor leftFore;
    public Motor rightFore;
    public Motor leftBack;
    public Motor rightBack;

    private double leftForeRightBackStrafeSpeed = 0;
    private double rightForeLeftBackStrafeSpeed = 0;

    public double targetAngle = 0;

    //hsv blue beacon range colours
    public static Scalar blueLow = new Scalar(108, 0, 220);
    public static Scalar blueHigh = new Scalar(178, 255, 255);

    public final static int BEACON_NOT_VISIBLE = 0;
    public final static int BEACON_BLUE_RED = 1;
    public final static int BEACON_RED_BLUE = 2;

    public final static double TURNING_ACCURACY_DEG = 2;

    public final static double DEFAULT_VEER_STRENGTH = 0.7 / 90;
    public static double VEER_TURNING_STRENGTH = DEFAULT_VEER_STRENGTH;

    public Fermion(boolean auto) {
        leftFore = new Motor("leftFore");
        leftFore.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightFore = new Motor("rightFore");
        rightFore.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftBack = new Motor("leftBack");
        leftBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightBack = new Motor("rightBack");
        rightBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        rightFore.setReverse(true);
        rightBack.setReverse(true);

        if (auto) {
            BNO055IMU.Parameters params = new BNO055IMU.Parameters();
            params.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
            params.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;

            imu = (AdafruitBNO055IMU) RC.h.get(BNO055IMU.class, "adafruit");
            imu.initialize(params);

            mouse = new TrackBall("leftFore", "rightFore");
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
        VEER_TURNING_STRENGTH = 0;

        leftFore.setPower(-speed);
        leftBack.setPower(-speed);
        rightFore.setPower(speed);
        rightBack.setPower(speed);
    }//turnL

    public void turnR(double speed) {
        VEER_TURNING_STRENGTH = 0;

        leftFore.setPower(speed);
        leftBack.setPower(speed);
        rightFore.setPower(-speed);
        rightBack.setPower(-speed);
    }//turnL

    /*
    Allows robot to strafe in any direction, with 0° being the front of robot
                         0°
                         |
                 -90° –     – 90°
                         |
                        ±180°
     */
    public void strafe(double degrees, double speed) {
        VEER_TURNING_STRENGTH = DEFAULT_VEER_STRENGTH;

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
        double beginAngle = MathUtils.cvtAngleToNewDomain(-imu.getAngularOrientation().firstAngle);
        double targetAngle = MathUtils.cvtAngleToNewDomain(beginAngle - degrees);


        Log.i("deg traget", "imuTurnL: " + targetAngle);
        while (RC.l.opModeIsActive()) {

            double currentAngle = MathUtils.cvtAngleToNewDomain(-imu.getAngularOrientation().firstAngle);

            double angleToTurn = Math.abs(currentAngle - targetAngle);

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
        double beginAngle = MathUtils.cvtAngleToNewDomain(-imu.getAngularOrientation().firstAngle);

        double targetAngle = MathUtils.cvtAngleToNewDomain(beginAngle + degrees);

        Log.i("deg traget", "imuTurnL: " + targetAngle);
        while (RC.l.opModeIsActive()) {

            double currentAngle = MathUtils.cvtAngleToNewDomain(-imu.getAngularOrientation().firstAngle);

            double angleToTurn = Math.abs(currentAngle - targetAngle);

            turnR(angleToTurn / 180 * speed);

            if (angleToTurn < TURNING_ACCURACY_DEG) {
                break;
            }//if
        }//while

        stop();
    }//imuTurnR

    public void absoluteIMUTurn(double degrees, double speed) {
        double currentAngle = -imu.getAngularOrientation().firstAngle;

        if (currentAngle > 180) {
            currentAngle -= 360;
        }//if

        double toTurn = degrees - currentAngle;
        Log.i("deg", toTurn + "");

        if (toTurn < 0) {
            imuTurnL(Math.abs(toTurn), speed);
        } else {
            imuTurnR(Math.abs(toTurn), speed);
        }//if
    }//absoluteIMUTurn


    //to be used via TaskHandler
    //therefore, it's not a loop
    //no idea if this works...
    //simultaneously turning and strafing with mecanum wheels seems extremely complicated
    public void veerCheck() {
        if (VEER_TURNING_STRENGTH > 0) {
            double currentAngle = -imu.getAngularOrientation().firstAngle;

            if (currentAngle > 180) {
                currentAngle -= 360;
            } else if (currentAngle < -180) {
                currentAngle += 360;
            }//elseif

            double angleError = targetAngle - currentAngle;

            if (angleError > 180) {
                angleError -= 360;
            } else if (angleError < -180) {
                angleError += 360;
            }//elseif

            Log.i("A!", targetAngle + ", " + currentAngle);

            double leftForePower = leftForeRightBackStrafeSpeed;
            double leftBackPower = rightForeLeftBackStrafeSpeed;
            double rightForePower = rightForeLeftBackStrafeSpeed;
            double rightBackPower = leftForeRightBackStrafeSpeed;

            leftForePower += VEER_TURNING_STRENGTH * angleError;
            rightBackPower -= VEER_TURNING_STRENGTH * angleError;

            if (Math.abs(leftForePower) > 1) {
                rightBackPower -= leftForePower - Math.signum(leftForePower) * 1;
                leftForePower = Math.signum(leftForePower) * 1;
            } else if (Math.abs(rightBackPower) > 1) {
                leftForePower -= rightBackPower - Math.signum(rightBackPower) * 1;
                rightBackPower = Math.signum(rightBackPower) * 1;
            }//else

            leftBackPower += VEER_TURNING_STRENGTH * angleError;
            rightForePower -= VEER_TURNING_STRENGTH * angleError;

            if (Math.abs(leftBackPower) > 1) {
                rightForePower -= leftBackPower - Math.signum(leftBackPower) * 1;
                leftBackPower = Math.signum(leftBackPower) * 1;
            } else if (Math.abs(rightForePower) > 1) {
                leftBackPower -= rightForePower - Math.signum(rightForePower) * 1;
                rightForePower = Math.signum(rightForePower) * 1;
            }//else

            leftFore.setPower(leftForePower);
            leftBack.setPower(leftBackPower);
            rightFore.setPower(rightForePower);
            rightBack.setPower(rightBackPower);
        }//if
    }//veerCheck

    public void pushBeaconButton(int beaconConfig) {
        if (beaconConfig == Fermion.BEACON_BLUE_RED) {
            strafe(-90, 0.1);
            AutoOpMode.delay(500);
            stop();
        } else {
            strafe(90, 0.1);
            AutoOpMode.delay(500);
            stop();
        }//else

        strafe(0, 0.1);
        AutoOpMode.delay(400);
        strafe(180, 0.1);
        AutoOpMode.delay(400);
        stop();
    }

    public static int waitForBeaconConfig(Image img, VuforiaTrackableDefaultListener beacon, CameraCalibration camCal, long timeOut) {

        int config = BEACON_NOT_VISIBLE;
        long beginTime = System.currentTimeMillis();
        while (config == BEACON_NOT_VISIBLE && System.currentTimeMillis() - beginTime < timeOut && RC.l.opModeIsActive()) {
            config = getBeaconConfig(img, beacon, camCal);
            RC.l.idle();
        }//while

        return config;
    }

    public static int getBeaconConfig(Image img, VuforiaTrackableDefaultListener beacon, CameraCalibration camCal) {

        OpenGLMatrix pose = beacon.getRawPose();

        if (pose != null) {

            Matrix34F rawPose = new Matrix34F();
            float[] poseData = Arrays.copyOfRange(pose.transposed().getData(), 0, 12);

            rawPose.setData(poseData);

            if (img != null && img.getPixels() != null) {

                //calculating pixel coordinates of image target corners
                float[][] corners = new float[4][2];
                Vec2F[] cornerVecs = new Vec2F[4];

                cornerVecs[0] = Tool.projectPoint(camCal, rawPose, new Vec3F(-127, 92, 0)); //upper left
                cornerVecs[1] = Tool.projectPoint(camCal, rawPose, new Vec3F(127, 92, 0)); //upper right
                cornerVecs[2] = Tool.projectPoint(camCal, rawPose, new Vec3F(127, -92, 0)); //lower right
                cornerVecs[3] = Tool.projectPoint(camCal, rawPose, new Vec3F(-127, -92, 0)); //lower left

                for (int i = 0; i < 4; i++) {
                    corners[i] = cornerVecs[i].getData();
                }//for

                //extending bounding box to show any object immediately above the image target
                corners[0][0] -= Math.abs(corners[0][0] - corners[3][0]);
                corners[0][0] = Math.max(corners[0][0], 0);

                corners[0][0] -= Math.abs(corners[0][1] - corners[3][1]);
                corners[0][1] = Math.max(corners[0][1], 0);

                //getting camera image...
                Bitmap bm = Bitmap.createBitmap(img.getWidth(), img.getHeight(), Bitmap.Config.RGB_565);
                bm.copyPixelsFromBuffer(img.getPixels());

                //turning the corner pixel coordinates into a proper bounding box
                Mat crop = OCVUtils.bitmapToMat(bm, CvType.CV_8UC3);
                float x = Math.min(Math.min(corners[1][0], corners[3][0]), Math.min(corners[0][0], corners[2][0]));
                float y = Math.min(Math.min(corners[1][1], corners[3][1]), Math.min(corners[0][1], corners[2][1]));
                float width = Math.max(Math.abs(corners[0][0] - corners[2][0]), Math.abs(corners[1][0] - corners[3][0]));
                float height = Math.max(Math.abs(corners[0][1] - corners[2][1]), Math.abs(corners[1][1] - corners[3][1]));

                //make sure our bounding box doesn't go outside of the image
                //OpenCV doesn't like that...
                x = Math.max(x, 0);
                y = Math.max(y, 0);
                width = (x + width > crop.cols())? crop.cols() - x : width;
                height = (y + height > crop.rows())? crop.rows() - y : height;

                //cropping bounding box out of camera image
                Mat cropped = new Mat(crop, new Rect((int) x, (int) y, (int) width, (int) height));

                //filtering out non-beacon-blue colours in HSV colour space
                Imgproc.cvtColor(cropped, cropped, Imgproc.COLOR_RGB2HSV_FULL);

                //get filtered mask
                //if pixel is within acceptable blue-beacon-colour range, it's changed to white. If not, it's black
                Mat mask = new Mat();
                Core.inRange(cropped, blueLow, blueHigh, mask);
                Moments mmnts = Imgproc.moments(mask, true);

                //calculating centroid of the resulting binary mask via image moments
                Log.i("CentroidX", "" + ((mmnts.get_m10() / mmnts.get_m00())));
                Log.i("CentroidY", "" + ((mmnts.get_m01() / mmnts.get_m00())));

                //Note: for some reason, we end up with a image that is rotated 90 degrees
                //if centroid is in the bottom half of the image, the blue beacon is on the left
                //if the centroid is in the top half, the blue beacon is on the right
                if ((mmnts.get_m01() / mmnts.get_m00()) < cropped.rows() / 2) {
                    return BEACON_RED_BLUE;
                } else {
                    return BEACON_BLUE_RED;
                }//else

            }//if

            return BEACON_NOT_VISIBLE;

        }//if

        return BEACON_NOT_VISIBLE;
    }//getBeaconConfig

    public void strafeToBeacon(VuforiaTrackableDefaultListener beacon, double targetDistance, double speed) {

        strafeToBeacon(beacon, targetDistance, speed, new VectorF(0, -0.1f, 0));

    }//strafeToBeacon


    public void strafeToBeacon(VuforiaTrackableDefaultListener beacon, double targetDistance, double speed, VectorF coordinate) {

        VectorF trans = beacon.getPose().getTranslation();

        for (int i = 0; i < coordinate.length(); i++) {
            if (coordinate.get(i) == -0.1f) {
                coordinate.put(i, trans.get(i));

            }//if
        }//for

        trans.subtract(coordinate);

        Log.i("DIMENS", trans.get(0) + ", " + trans.get(2));
        Log.i(TAG, "DIMENSION " + coordinate.get(0) + ", " + coordinate.get(2));

        Log.i("ANGLE", Math.toDegrees(Math.atan2(trans.get(0), -trans.get(2))) + "");
        strafe(Math.toDegrees(Math.atan2(trans.get(0), -trans.get(2))), speed);

        while (RC.l.opModeIsActive() && trans.magnitude() > targetDistance) {

            trans = beacon.getPose().getTranslation();
            trans.subtract(coordinate);

            strafe(Math.toDegrees(Math.atan2(trans.get(0), -trans.get(2))), speed);

        }//while

        stop();
    }//strafeToBeacon

    public void stop() {
        leftFore.stop();
        rightFore.stop();
        leftBack.stop();
        rightBack.stop();
    }//stop

}//Fermion
