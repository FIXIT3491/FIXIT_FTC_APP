package org.firstinspires.ftc.teamcode.newhardware.FXTSensors;

import com.qualcomm.hardware.adafruit.AdafruitBNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.RC;
import org.firstinspires.ftc.teamcode.roboticslibrary.TaskHandler;

/**
 * Created by FIXIT on 16-10-08.
 */
public class TrackBall {

    public DcMotor xEnc;
    public DcMotor yEnc;

    public Point absoluteFieldCoord;

    private Point lastTiks;

    public TrackBall(String xAddr, String yAddr) {
        xEnc = RC.h.dcMotor.get(xAddr);
        yEnc = RC.h.dcMotor.get(yAddr);

        lastTiks = new Point(xEnc.getCurrentPosition(), -yEnc.getCurrentPosition());
    }//TrackBall

    //switch encoder ports
    //and reverse the values
    public Point getEncTiks() {
        return new Point(xEnc.getCurrentPosition(), -yEnc.getCurrentPosition());
    }

    public void setAbsoluteCoord(Point coord) {
        this.absoluteFieldCoord = coord;
    }//setAbsoluteCoord

    public void addAbsoluteCoordinateRunnable (final AdafruitBNO055IMU imu) {

        TaskHandler.addLoopedTask("AbsoluteCoordinate", new Runnable() {
            @Override
            public void run() {
                synchronized (imu) {
                    updateAbsolutePoint(-imu.getAngularOrientation().firstAngle);
                }//synchronized
            }//run
        }, 5);//TaskHandler

    }//addAbsoluteCoordinateRunnable

    public void updateAbsolutePoint(double robotAngle) {
        Point delta = getEncTiks().subtract(lastTiks);
        lastTiks = getEncTiks();

        double yChange = delta.y * Math.cos(Math.toRadians(robotAngle)) - delta.x * Math.sin(Math.toRadians(robotAngle));
        double xChange = delta.y * Math.sin(Math.toRadians(robotAngle)) + delta.x * Math.cos(Math.toRadians(robotAngle));

        absoluteFieldCoord.x += xChange;
        absoluteFieldCoord.y += yChange;

    }

    public static class Point {

        public double x;
        public double y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public Point subtract (Point pt) {
            return new Point(this.x - pt.x, this.y - pt.y);
        }

        public Point add(Point pt) {
            return new Point(this.x + pt.x, this.y + pt.y);
        }

        public String toString() {
            return this.x + ", " + this.y;
        }

    }


}
