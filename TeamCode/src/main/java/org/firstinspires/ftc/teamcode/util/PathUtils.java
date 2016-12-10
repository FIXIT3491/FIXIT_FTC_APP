package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.hardware.adafruit.AdafruitBNO055IMU;

import org.firstinspires.ftc.teamcode.newhardware.FXTSensors.TrackBall;

/**
 * Created by Windows on 2016-11-19.
 */
public class PathUtils {

    public static int COMPLETE_MOTION = 0;
    public static int STAGED_MOTION = 1;

//    public static void driveOnFunction(ParameteredRunnable deriv, AdafruitBNO055IMU imu, TrackBall mouse,
//                                        boolean turnWithDeriv, double startingPosition) {
//
//        double beginAngle = cvtAngleToNewDomain(-imu.getAngularOrientation().firstAngle);
//        double x = 0;
//
//        while (true) {
//            double currentAngle = cvtAngleToNewDomain(-imu.getAngularOrientation().firstAngle);
//            x += mouse.getXYIncrement().y * Math.cos(currentAngle - beginAngle);
//            x += mouse.getXYIncrement().x * Math.sin(currentAngle - beginAngle);
//
//            double degree = Math.atan((Double) deriv.run(new Double(x))); //replace with f'(x)
//
//            if (turnWithDeriv) {
//                //veerTo(currentTargetAngle + degree)
//            } else {
//                //strafe(degree, speed)
//            }//else
//        }//while
//
//    }

    public static void driveCircle(final double radius, double beginRad, AdafruitBNO055IMU imu, TrackBall mouse) {
        ParameteredRunnable circleDeriv = new ParameteredRunnable() {
            @Override
            public Object run(Object param) {
                return ((Double) param).doubleValue() / Math.sqrt(radius*radius - ((Double) param).doubleValue()*((Double) param).doubleValue());
            }
        };

        //driveOnFunction(circleDeriv, imu, mouse, true, radius * Math.cos(beginRad));
    }

    public static double cvtAngleToNewDomain(double angle) {
        if (angle < -180) {
            angle += 360;
        } else if (angle > 180) {
            angle -= 360;
        }//elseif

        return angle;
    }

    public static interface ParameteredRunnable {

        public Object run(Object param);

    }


}
