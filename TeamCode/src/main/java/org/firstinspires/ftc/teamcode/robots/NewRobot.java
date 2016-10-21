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
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.internal.AppUtil;
import org.firstinspires.ftc.teamcode.RC;
import org.firstinspires.ftc.teamcode.newhardware.FXTSensors.TrackBall;
import org.firstinspires.ftc.teamcode.newhardware.Motor;
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
public class NewRobot {

    public AdafruitBNO055IMU imu;
    public TrackBall mouse;

    public Motor leftFore;
    public Motor rightFore;
    public Motor leftBack;
    public Motor rightBack;

    public double targetAngle = 0;

    //hsv blue beacon range colours
    public static Scalar blueLow = new Scalar(108, 0, 220);
    public static Scalar blueHigh = new Scalar(178, 255, 255);

    public final static int BEACON_BLUE_RED = 0;
    public final static int BEACON_RED_BLUE = 1;
    public final static int BEACON_NOT_VISIBLE = 2;

    public NewRobot(boolean auto) {
        leftFore = new Motor("leftFore");
        rightFore = new Motor("rightFore");
        leftBack = new Motor("leftBack");
        rightBack = new Motor("rightBack");

        rightFore.setReverse(true);
        rightBack.setReverse(true);

        if (auto) {
            BNO055IMU.Parameters params = new BNO055IMU.Parameters();
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

        while (true) {

            double currentAngle = imu.getAngularOrientation().firstAngle;

            if (currentAngle > 180) {
                currentAngle -= 360;
            }//if

            double angleToTurn = degrees - (currentAngle - beginAngle);

            if (Math.abs(angleToTurn) < 10) {
                break;
            }//if
        }//while

    }

    public void imuTurnR(double degrees, double speed) {

        turnL(speed);
        double beginAngle = imu.getAngularOrientation().firstAngle;

        while (true) {

            double currentAngle = imu.getAngularOrientation().firstAngle;

            if (currentAngle > 180) {
                currentAngle -= 360;
            }//if

            double angleToTurn = degrees - (currentAngle - beginAngle);

            if (Math.abs(angleToTurn) < 10) {
                break;
            }//if
        }//while

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
    public void veerCheck() {

        final double TURNING_CONSTANT = 0.1 / 90.0;

        double angleError = targetAngle - imu.getAngularOrientation().firstAngle;

        double leftForePower = leftFore.getPower();
        double leftBackPower = leftBack.getPower();
        double rightForePower = rightFore.getPower();
        double rightBackPower = rightBack.getPower();

        if (angleError < 0) {
            leftForePower -= TURNING_CONSTANT * Math.abs(angleError);
            rightBackPower += TURNING_CONSTANT * Math.abs(angleError);

            if (leftForePower < -1) {
                rightBackPower += -1 - leftForePower;
                leftForePower = -1;
            }//if

            if (rightBackPower > 1) {
                leftForePower += 1 - rightBackPower;
                rightBackPower = 1;
            }//if

            leftBackPower -= TURNING_CONSTANT * Math.abs(angleError);
            rightForePower += TURNING_CONSTANT * Math.abs(angleError);

            if (leftBackPower < -1) {
                rightForePower += -1 - leftBackPower;
                leftBackPower = -1;
            }//if

            if (rightForePower > 1) {
                leftBackPower += 1 - rightForePower;
                rightForePower = 1;
            }//if
        } else {
            rightForePower -= TURNING_CONSTANT * Math.abs(angleError);
            leftBackPower += TURNING_CONSTANT * Math.abs(angleError);

            if (rightForePower < -1) {
                leftBackPower += -1 - rightForePower;
                rightForePower = -1;
            }//if

            if (leftBackPower > 1) {
                rightForePower += 1 - leftBackPower;
                leftBackPower = 1;
            }//if

            rightBackPower -= TURNING_CONSTANT * Math.abs(angleError);
            leftForePower += TURNING_CONSTANT * Math.abs(angleError);

            if (rightBackPower < -1) {
                leftForePower += -1 - rightBackPower;
                rightBackPower = -1;
            }//if

            if (leftForePower > 1) {
                rightBackPower += 1 - leftForePower;
                leftForePower = 1;
            }//if
        }//else

        leftFore.setPower(leftForePower);
        leftBack.setPower(leftBackPower);
        rightFore.setPower(rightForePower);
        rightBack.setPower(rightBackPower);

    }//veerCheck

    public static int getBeaconConfig(Image img, VuforiaTrackable beacon, CameraCalibration camCal) {

        OpenGLMatrix pose = ((VuforiaTrackableDefaultListener) beacon.getListener()).getRawPose();

        if (pose != null) {

            Matrix34F rawPose = new Matrix34F();
            float[] poseData = Arrays.copyOfRange(pose.transposed().getData(), 0, 12);

            rawPose.setData(poseData);

            if (img != null && img.getPixels() != null) {
                float[][] corners = new float[4][2];
                Vec2F[] cornerVecs = new Vec2F[4];

                cornerVecs[0] = Tool.projectPoint(camCal, rawPose, new Vec3F(-127, 92, 0)); //upper left
                cornerVecs[1] = Tool.projectPoint(camCal, rawPose, new Vec3F(127, 92, 0)); //upper right
                cornerVecs[2] = Tool.projectPoint(camCal, rawPose, new Vec3F(127, -92, 0)); //lower right
                cornerVecs[3] = Tool.projectPoint(camCal, rawPose, new Vec3F(-127, -92, 0)); //lower left

                for (int i = 0; i < 4; i++) {
                    corners[i] = cornerVecs[i].getData();
                }//for

                corners[0][0] -= Math.abs(corners[0][0] - corners[3][0]);
                corners[0][0] = Math.max(corners[0][0], 0);

                corners[0][0] -= Math.abs(corners[0][1] - corners[3][1]);
                corners[0][1] = Math.max(corners[0][1], 0);

                Bitmap bm = Bitmap.createBitmap(img.getWidth(), img.getHeight(), Bitmap.Config.RGB_565);
                bm.copyPixelsFromBuffer(img.getPixels());

                Mat crop = OCVUtils.bitmapToMat(bm, CvType.CV_8UC3);
                float x = Math.min(Math.min(corners[1][0], corners[3][0]), Math.min(corners[0][0], corners[2][0]));
                float y = Math.min(Math.min(corners[1][1], corners[3][1]), Math.min(corners[0][1], corners[2][1]));
                float width = Math.max(Math.abs(corners[0][0] - corners[2][0]), Math.abs(corners[1][0] - corners[3][0]));
                float height = Math.max(Math.abs(corners[0][1] - corners[2][1]), Math.abs(corners[1][1] - corners[3][1]));

                x = Math.max(x, 0);
                y = Math.max(y, 0);
                width = (x + width > crop.cols())? crop.cols() - x : width;
                height = (y + height > crop.rows())? crop.rows() - y : height;

                Mat cropped = new Mat(crop, new Rect((int) x, (int) y, (int) width, (int) height));

                Imgproc.cvtColor(cropped, cropped, Imgproc.COLOR_RGB2HSV_FULL);

                Mat mask = new Mat();
                Core.inRange(cropped, blueLow, blueHigh, mask);
                Moments mmnts = Imgproc.moments(mask, true);

                Imgproc.cvtColor(mask, mask, Imgproc.COLOR_GRAY2RGB);
                Imgproc.cvtColor(cropped, cropped, Imgproc.COLOR_HSV2RGB);

                Core.bitwise_and(mask, cropped, cropped);

                Log.i("CentroidX", "" + ((mmnts.get_m10() / mmnts.get_m00())));
                Log.i("CentroidY", "" + ((mmnts.get_m01() / mmnts.get_m00())));

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

    public void stop() {
        leftFore.stop();
        rightFore.stop();
        leftBack.stop();
        rightBack.stop();
    }
}//Alpha
