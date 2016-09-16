package com.qualcomm.ftcrobotcontroller.gamecode;

import android.graphics.Bitmap;
import android.util.Log;

import com.qualcomm.ftcrobotcontroller.RC;
import com.qualcomm.ftcrobotcontroller.opmodesupport.NavSystem;
import com.qualcomm.ftcrobotcontroller.opmodesupport.TeleOpMode;
import com.qualcomm.ftcrobotcontroller.roboticslibrary.OCVUtils;
import com.qualcomm.ftcrobotcontroller.robots.Robot;
import com.qualcomm.robotcore.hardware.DcMotorController;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

/**
 * Created by FIXIT on 16-07-04.
 */
public class PathFindingOp extends TeleOpMode {

    Mat field;
    int stage = 0;
    double baseSpeed = 0.09;

    Robot robot;
    //    AdafruitIMU adafruit;
    double beginAngl = 0;

    @Override
    public void initialize() {
        RC.t.setDataLogFile("weights", true);

        RC.cam.start();
        robot = new Robot();
//        adafruit = new AdafruitIMU("adafruit", AdafruitIMU.OPERATION_MODE_IMU);

        robot.motorL.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        robot.motorR.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);

        robot.motorL.minSpeed = 0.04;
        robot.motorR.minSpeed = 0.04;

        field = OCVUtils.getMatFromFile("field.jpg");
        Log.i("Gap Info", "Finished Init");
//        beginAngl = adafruit.getEulerAngles()[0];
    }

    double target = 0;

    @Override
    public void loopOpMode() {

        if (stage == 0) {
//            beginAngl = adafruit.getEulerAngles()[0];
        }

        if (stage > 0) {
            Bitmap newImg = Bitmap.createScaledBitmap(RC.cam.image, 300, 400, false);

            Mat current = new Mat(newImg.getHeight(), newImg.getWidth(), CvType.CV_8UC3);
            Utils.bitmapToMat(newImg, current);

//            double newAngle = adafruit.getEulerAngles()[0] - beginAngl;

//            if (newAngle > 180) {
//                newAngle -= 360;
//            }//if

            double angleToTurn = NavSystem.pathFind(current, field);

            Log.i("AngleToTurn", angleToTurn + "");

            if (angleToTurn == -181) {
                robot.backward(baseSpeed);
            } else if (Math.abs(angleToTurn) < 10) {
                Log.i("Gap", "Driving Forward");
                robot.forward(baseSpeed);
            } else {
                Log.i("Gap", "Driving Left");

                double speed = 0.15 * (angleToTurn / Math.abs(angleToTurn));
                robot.turnR(speed);
            }

            Log.i("sdfsd", "sdfsd");
        }
        stage++;
    }

    @Override
    public void stop() {
        super.stop();
    }
}
