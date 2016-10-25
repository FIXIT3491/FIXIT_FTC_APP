package org.firstinspires.ftc.teamcode.robots;

import android.graphics.Bitmap;
import android.util.Log;

import com.qualcomm.hardware.adafruit.AdafruitBNO055IMU;
import com.qualcomm.hardware.adafruit.BNO055IMU;
import com.vuforia.CameraCalibration;
import com.vuforia.Image;
import com.vuforia.Matrix34F;
import com.vuforia.Tool;
import com.vuforia.Vec2F;
import com.vuforia.Vec3F;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.internal.AppUtil;
import org.firstinspires.ftc.teamcode.RC;
import org.firstinspires.ftc.teamcode.newhardware.FXTSensors.TrackBall;
import org.firstinspires.ftc.teamcode.newhardware.Motor;
import org.firstinspires.ftc.teamcode.opmodesupport.FXTLinearOpMode;
import org.firstinspires.ftc.teamcode.roboticslibrary.OCVUtils;
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

    public final static double TURNING_ACCURACY_DEG = 10;

    public Fermion(boolean auto) {
        leftFore = new Motor("leftFore");
        rightFore = new Motor("rightFore");
        leftBack = new Motor("leftBack");
        rightBack = new Motor("rightBack");

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
        leftFore.setPower(-speed);
        leftBack.setPower(-speed);
        rightFore.setPower(speed);
        rightBack.setPower(speed);
    }//turnL

    public void turnR(double speed) {
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

        degrees += 45;

        double leftForeRightBack = Math.sin(Math.toRadians(degrees));
        double rightForeLeftBack = Math.cos(Math.toRadians(degrees));

        double multi = speed / Math.max(leftForeRightBack, rightForeLeftBack);
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

        while (true) {

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

        while (true) {

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

        while (true) {

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

        while (true) {

            double current = begin - mouse.getXY().x;

            right(speed * (1 - current / mm));

            if (current > mm) {
                break;
            }//if
        }//while

        stop();

    }//strafeRight

    public void imuTurnL(double degrees, double speed) {

        turnL(speed);
        double beginAngle = imu.getAngularOrientation().firstAngle;

        if (beginAngle > 180) {
            beginAngle -= 360;
        }//if

        double targetAngle = beginAngle - degrees;

        while (true) {

            double currentAngle = imu.getAngularOrientation().firstAngle;

            if (currentAngle > 180) {
                currentAngle -= 360;
            }//if

            double angleToTurn = currentAngle - targetAngle;

            turnL(angleToTurn * (speed / degrees));

            if (Math.abs(angleToTurn) < TURNING_ACCURACY_DEG) {
                break;
            }//if
        }//while

        stop();

        targetAngle = imu.getAngularOrientation().firstAngle;

        if (targetAngle > 180) {
            targetAngle -= 360;
        }//if

    }

    public void imuTurnR(double degrees, double speed) {

        turnR(speed);
        double beginAngle = imu.getAngularOrientation().firstAngle;

        if (beginAngle > 180) {
            beginAngle -= 360;
        }//if

        double targetAngle = beginAngle + degrees;

        while (true) {

            double currentAngle = imu.getAngularOrientation().firstAngle;

            if (currentAngle > 180) {
                currentAngle -= 360;
            }//if

            double angleToTurn = targetAngle - currentAngle;

            turnR(angleToTurn * (speed / degrees));

            if (Math.abs(angleToTurn) < TURNING_ACCURACY_DEG) {
                break;
            }//if
        }//while

        stop();

        targetAngle = imu.getAngularOrientation().firstAngle;

        if (targetAngle > 180) {
            targetAngle -= 360;
        }//if

    }//imuTurnR

    public void absoluteIMUTurn(double degrees, double speed) {
        double currentAngle = imu.getAngularOrientation().firstAngle;

        if (currentAngle > 180) {
            currentAngle -= 360;
        }//if

        double toTurn = degrees - currentAngle;

        if (toTurn < 0) {
            imuTurnL(Math.abs(toTurn), speed);
        } else {
            imuTurnR(Math.abs(toTurn), speed);
        }//if
    }//absoluteIMUTurn

    //absolute degree system
    //only works if veerCheck is being looped
    public void veerTo(double degrees) {
        this.targetAngle = degrees;
    }//veerTo

    //to be used via TaskHandler
    //therefore, it's not a loop
    //no idea if this works...
    //simultaneously turning and strafing with mecanum wheels seems extremely complicated
    public void veerCheck() {

        final double TURNING_CONSTANT = 0.1 / 90.0; //random guess

        double currentAngle = imu.getAngularOrientation().firstAngle;

        if (currentAngle > 180) {
            currentAngle -= 360;
        }//if

        double angleError = targetAngle - currentAngle;

        double leftForePower = leftForeRightBackStrafeSpeed;
        double leftBackPower = rightForeLeftBackStrafeSpeed;
        double rightForePower = rightForeLeftBackStrafeSpeed;
        double rightBackPower = leftForeRightBackStrafeSpeed;

        leftForePower += TURNING_CONSTANT * angleError;
        rightBackPower -= TURNING_CONSTANT * angleError;

        if (Math.abs(leftForePower) > 1) {
            rightBackPower -= leftForePower - Math.signum(leftForePower) * 1;
            leftForePower = Math.signum(leftForePower) * 1;
        } else if (Math.abs(rightBackPower) > 1) {
            leftForePower -= rightBackPower - Math.signum(rightBackPower) * 1;
            rightBackPower = Math.signum(rightBackPower) * 1;
        }//else

        leftBackPower += TURNING_CONSTANT * angleError;
        rightForePower -= TURNING_CONSTANT * angleError;

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

    }//veerCheck

    public void pushBeaconButton(int beaconConfig) {
        if (beaconConfig == Fermion.BEACON_BLUE_RED) {
            strafe(-90, 0.1);
            FXTLinearOpMode.delay(500);
            stop();
        } else {
            strafe(90, 0.1);
            FXTLinearOpMode.delay(500);
            stop();
        }//else

        strafe(0, 0.1);
        FXTLinearOpMode.delay(400);
        strafe(180, 0.1);
        FXTLinearOpMode.delay(400);
        stop();
    }

    public static int waitForBeaconConfig(Image img, VuforiaTrackableDefaultListener beacon, CameraCalibration camCal) {

        int config = BEACON_NOT_VISIBLE;
        while (config == BEACON_NOT_VISIBLE) {
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
                    return BEACON_BLUE_RED;
                } else {
                    return BEACON_RED_BLUE;
                }//else

            }//if

            return BEACON_NOT_VISIBLE;

        }//if

        return BEACON_NOT_VISIBLE;
    }//getBeaconConfig

    public void strafeToBeacon(VuforiaTrackableDefaultListener beacon, double targetDistance) {

        VectorF trans = beacon.getPose().getTranslation();

        strafe(Math.atan2(trans.get(2), trans.get(3)), 0.5);

        while (trans.magnitude() > targetDistance) {

            trans = beacon.getPose().getTranslation();
            strafe(Math.atan2(trans.get(2), trans.get(3)), 0.5);

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
