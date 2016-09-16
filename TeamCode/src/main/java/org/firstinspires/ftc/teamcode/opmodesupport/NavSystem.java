package org.firstinspires.ftc.teamcode.opmodesupport;

import android.graphics.Bitmap;
import android.util.Log;

import org.firstinspires.ftc.teamcode.RC;
import org.firstinspires.ftc.teamcode.newhardware.FXTSensors.AdafruitIMU;
import org.firstinspires.ftc.teamcode.newhardware.Motor;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.Arrays;

/**
 * Created by FIXIT on 16-04-10.
 */
public class NavSystem {

    AdafruitIMU adafruit;
    double ySpeed = 0;
    long lastCalculationTime = 0;

    double targetAngle = 0;

    double angleError = 0;
    double linearError = 0;

    double distanceTraveled = 0;

    static boolean training = true;
    static double learnRate = 0.05;
    static double oldDistMapScore = 0;
    static double[] lastAction = {-1, 0};
    static double[] weights;

    //values to tune
    double linearDivisor = 0.05;
    double angleDivisor = 120;

    /**
     * Pathfinding Values
     */

    public void calculateError() {

        angleError = targetAngle - adafruit.getEulerAngles()[0];

        ySpeed += (adafruit.getAccelData()[1] * (System.currentTimeMillis() - lastCalculationTime));

        double totalDist = ySpeed * (System.currentTimeMillis() - lastCalculationTime);
        lastCalculationTime = System.currentTimeMillis();

        double yDist = (totalDist * Math.cos(Math.toRadians(angleError)));
        double xDist = (totalDist * Math.cos(Math.toRadians(angleError)));

        linearError += xDist;

        distanceTraveled += yDist;
    }//calculateError

    public void minimizeError(Motor motorL, Motor motorR) {

        double motorSpeed = (motorL.getPower() + motorR.getPower()) / 2;

        if (angleError > 180) {
            angleError -= 360;
        } else if (angleError < -180) {
            angleError += 360;
        }//elseif

        Log.i("AngleToTurn", "veerCheck: " + angleError);

        //calculates to what degree to alter the motor speeds to fix the angle
        double alter = (Math.abs(angleError) / angleDivisor);
        alter += (linearError / linearDivisor);

        //approximate (very approximate) calculations show that '120' has the robot
        //turning at 2.78 degrees per second when angleToTurn = 2 degrees, show this seems good

        Log.i("Altering", "" + alter);

        if (angleError < 0) {
            motorR.setPowerSuper(motorSpeed + alter);
            motorL.setPowerSuper(motorSpeed - alter);
        } else if (angleError > 0) {
            motorR.setPowerSuper(motorSpeed - alter);
            motorL.setPowerSuper(motorSpeed + alter);
        }

        Log.i("MotorSpeeds", "R: " + motorR.getPower() + ", L: " + motorL.getPower());

    }

    public void updateAngle(double distanceTraveled) {

        targetAngle = 0;

    }

    public static double pathFind (Mat current, Mat field) {

        int pixelLeniency = 70;
        int sampleFreq = 1;

        double camFOV = 56.5;

        Mat img = new Mat();

        Core.absdiff(current, field, img);
        Imgproc.threshold(img, img, pixelLeniency, 255, Imgproc.THRESH_BINARY);
        Imgproc.cvtColor(img, img, Imgproc.COLOR_BGR2GRAY);

        if (weights == null) {
            weights = new double[img.cols() / sampleFreq];
        }//if

        Core.absdiff(current, field, img);
        Imgproc.threshold(img, img, pixelLeniency, 255, Imgproc.THRESH_BINARY);
        Imgproc.cvtColor(img, img, Imgproc.COLOR_BGR2GRAY);

        int failedRowCount = 0;

        int[] heights = new int[img.cols() / sampleFreq];

        for (int i = 0; i < img.cols(); i += sampleFreq) {

            for (int j = img.rows() - 1; j > 0; j--) {

                if (img.get(j, i)[0] != 0) {
                    failedRowCount++;

                    if (failedRowCount > 4) {
                        heights[i / sampleFreq] = j + failedRowCount;

                        failedRowCount = 0;
                        break;
                    }//if
                }//else
            }//for
        }//for


//        alterWeights(calculateDistMapScore(heights));
//
//        double angle = 0;
//
//        for (int i = 0; i < heights.length; i++) {
//            angle += weights[i] * heights[i];
//        }//for

        Bitmap bit = Bitmap.createBitmap(img.cols(), img.rows(), Bitmap.Config.ARGB_8888);

        Utils.matToBitmap(createDistMap(smooth(heights), img.size()), bit);

        try {
            RC.a.display.setImageBitmap(bit);
        } catch (Exception e) {

        }//catch

        double angle = combineHeightsToAngle(heights, img.size());

        return angle;
    }//pathFind

    public static double combineHeightsToAngle(int[] heights, Size size) {
        double avgLeft = 0;
        double avgRight = 0;
        double totalAvg = 0;

        for (int i = 0; i < heights.length / 2; i++) {
            avgLeft += heights[i];
        }//for

        avgLeft /= (heights.length / 2);

        for (int i = heights.length / 2; i < heights.length; i++) {
            avgRight += heights[i];
        }//for

        avgRight /= (heights.length / 2);

        double actualAvg = (avgLeft + avgRight) / 2.0;
        int count = 0;

        for (int i = 0; i < heights.length; i++) {

            if (heights[i] >= actualAvg) {
                totalAvg += heights[i];
                count++;
            }//if
        }//for

        totalAvg /= count;

        double angle = 0;

        System.out.println("Avg Left: " + avgLeft);
        System.out.println("Avg Right: " + avgRight);

        if (avgLeft < avgRight) {
            angle = totalAvg * -45 / size.height;
        } else {
            angle = totalAvg * 45 / size.height;
        }//else

        if (totalAvg < 200) {
            return 0;
        }
        if (totalAvg > 380) {
            return -181;
        }

        System.out.println(angle);
        return angle;
    }

    public static Mat createDistMap(int[] heights, Size size) {
        Mat distMap = new Mat(size, CvType.CV_8UC3);

        byte[] fill = new byte[(int) (size.area() * distMap.channels())];

        Arrays.fill(fill, (byte) 127);

        distMap.put(0, 0, fill);

        for (int i = 0; i < heights.length; i++) {
            if (heights[i] - 1 >= 0
                    && heights[i] - 1 < size.height) {
                distMap.put(heights[i] - 1, i, new byte[]{-128, 0, 0});
            }//if

            if (heights[i] >= 0
                    && heights[i] < size.height) {
                distMap.put(heights[i], i, new byte[]{-128, 0, 0});
            }//if

            if (heights[i] + 1 >= 0
                    && heights[i] + 1 < size.height) {
                distMap.put(heights[i] + 1, i, new byte[]{-128, 0, 0});
            }//if
        }//for

        return distMap;
    }

    //MACHINE LEARNING APPROACH - Meh?

    public static int calculateDistMapScore (int[] heights) {

        int sum = 0;

        for (int i = 0; i < heights.length; i++) {
            sum += heights[i];
        }//for

        return sum;
    }

    public static void alterWeights(int newScore) {

        if (newScore <= oldDistMapScore) {
            int newIndex = (int) lastAction[0];

            newIndex++;
            newIndex %= weights.length;

            weights[newIndex] += learnRate;
            lastAction[0] = newIndex;
            lastAction[1] = learnRate;

        } else if (newScore > oldDistMapScore) {

            weights[(int) lastAction[0]] -= lastAction[1];
            lastAction[1] = -lastAction[1];

        }//elseif

        oldDistMapScore = newScore;

    }//alterWeights

    public static int[] smooth (int[] values) {

        for (int i = 1; i < values.length - 1; i++) {
            values[i] = (values[i - 1] + values[i + 1]) / 2;
        }//for

        return values;
    }

    public static void onStop() {
        RC.t.out.write(weights);
    }

}
