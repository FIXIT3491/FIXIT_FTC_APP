package org.firstinspires.ftc.teamcode.roboticslibrary;

import com.qualcomm.hardware.adafruit.AdafruitBNO055IMU;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
import org.opencv.core.Point;

/**
 * Created by FIXIT on 16-10-02.
 */
public class NavUtils {

    public static Point getRobotPosFromIMU(Acceleration accel, Velocity vel, Point oldPt, int numMilliseconds) {

        vel.xVeloc += (accel.xAccel * numMilliseconds) / 1000.0;
        vel.yVeloc += (accel.yAccel * numMilliseconds) / 1000.0;
        vel.zVeloc += (accel.zAccel * numMilliseconds) / 1000.0;

        oldPt.x += (vel.xVeloc * numMilliseconds) / 1000.0;
        oldPt.y += (vel.yVeloc * numMilliseconds) / 1000.0;

        return oldPt;
    }


}
