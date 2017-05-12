package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by FIXIT on 2017-02-16.
 */

public class SonarUtils {

    public final static int TURN_LEFT = 0;
    public final static int TURN_RIGHT = 1;
    public final static int MOVE_CLOSER = 2;
    public final static int MOVE_FURTHER = 3;

    private final static int SONAR_MATCH_THRESHOLD = 10;

    public static double[] compareTwoSonarStates(double[] reference, double[] current) {

        double referenceMean = mean(reference);
        double currentMean = mean(current);

        //if distOffset > 0, move further
        //if distOffset < 0, move closer
        double distOffset = referenceMean - currentMean;

        add(reference, -referenceMean);
        add(current, -currentMean);

        int bestMatchedCount = 0;
        int bestOffset = 0;

        for (int i = 0; i < current.length; i++) {
            int currentMatchedCount = 0;
            int currentOffset = 0;

            for (int j = 0; j < reference.length; j++) {

                if (Math.abs(reference[j] - current[i]) < SONAR_MATCH_THRESHOLD) {
                    currentMatchedCount++;

                    //if currentOffset > 0, current is more left than ref
                    //if currentOffset < 0, current is more right than ref
                    currentOffset = j - i;

                } else {
                    if ((j == reference.length - 1 || i == current.length - 1)
                            && bestMatchedCount < currentMatchedCount) {
                        bestMatchedCount = currentMatchedCount;
                        bestOffset = currentOffset;
                    }//if

                    currentMatchedCount = 0;
                    currentOffset = 0;
                }//else
            }//for
        }//for

        int directionY = (distOffset > 0)? MOVE_FURTHER : MOVE_CLOSER;
        int directionX = (bestOffset > 0)? TURN_RIGHT : TURN_LEFT;

        return new double[]{directionY, Math.abs(distOffset), directionX, Math.abs(bestOffset)};
    }//compareSensorStates

    public static double mean(double[] array) {
        double sum = 0;
        for (double add : array) {
            sum += add;
        }//for

        return sum / array.length;
    }

    public static void add(double[] array, double val) {
        for (int i = 0; i < array.length; i++) {
            array[i] += val;
        }//for
    }//subtract

}
