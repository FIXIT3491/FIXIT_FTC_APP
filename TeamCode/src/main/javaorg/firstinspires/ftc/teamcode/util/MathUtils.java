package org.firstinspires.ftc.teamcode.util;

/**
 * Created by Windows on 2016-11-04.
 */
public class MathUtils {

    public static int getQuadrant(double x, double y){
        if(x >= 0){
            if(y >= 0) return 1;
            else return 4;
        } else {
            if(y >= 0) return 2;
            else return 3;
        }
    }

    public static double roundToNearest(double num, double increment, double lowBound, double upBound){
        for (int i = 0; lowBound + i * increment <= upBound; ) {
            if(num < lowBound + (i + 0.5) * increment){
                return lowBound + i * increment;
            }
            i++;
        }
        throw new IllegalArgumentException("num " + num + " not in range! Range is " + lowBound +"-" + upBound);
    }

    public static double roundToNearest(double num, double increment, double lowBound){

        num -= lowBound;
        num = increment * Math.round(num / increment);

        return num;
    }

    public static boolean inRange(double num, double bound1, double bound2){
        return (num > bound1 && num < bound2) || (num > bound2 && num < bound1);
    }

    public static double cvtAngleToNewDomain(double angle) {
        if (angle < -180) {
            angle += 360;
        } else if (angle > 180) {
            angle -= 360;
        }//elseif

        return angle;
    }

    public static double cvtAngleJumpToNewDomain(double delta) {
        if (Math.abs(delta) > 180) {
            delta = Math.signum(delta) * 360 - delta;
        }//if

        return delta;
    }
}
