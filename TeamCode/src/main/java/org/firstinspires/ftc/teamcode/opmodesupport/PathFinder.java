package org.firstinspires.ftc.teamcode.opmodesupport;

import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.teamcode.robots.Robot;

/**
 * Created by FIXIT on 16-09-19.
 */
public class PathFinder {

    public static void navigateTo(Point start, Point dst, Robot robot, PathType type) {

        if (type.equals(PathType.DIRECT)) {
            double angleToTurn = Math.atan2(dst.x - start.x, dst.y - start.y);

            angleToTurn = (angleToTurn > 0)? angleToTurn : angleToTurn + 2*Math.PI;
            angleToTurn = Math.toDegrees(angleToTurn);

            //robot controls need to be updated
            robot.turnL(angleToTurn); //completely incorrect use of this method
            robot.forwardDistance((int) Math.abs(start.hypot() - dst.hypot()), 0.5);
        }

    }

    public static void navigateTo(VuforiaTrackable target, Robot robot, PathType type, boolean useXAxis) {

        if (type.equals(PathType.DIRECT)) {
            double angleToTurn;

            VuforiaTrackableDefaultListener listener = (VuforiaTrackableDefaultListener) target.getListener();

            while (!listener.isVisible()) {
                try {
                    Thread.sleep(5);
                } catch (Exception e) {
                    e.printStackTrace();
                }//catch
            }//while

            VectorF translation = listener.getPose().getTranslation();

            if (useXAxis) {
                angleToTurn = Math.atan2(translation.get(0), translation.get(2));
            } else {
                angleToTurn = Math.atan2(translation.get(1), translation.get(2));
            }//else

            //robot controls need to be updated
            robot.turnL(angleToTurn); //completely incorrect use of this method

            if (useXAxis) {
                robot.forwardDistance((int) Math.hypot(translation.get(0), translation.get(2)), 0.5);
            } else {
                robot.forwardDistance((int) Math.hypot(translation.get(1), translation.get(2)), 0.5);
            }//else
        }

    }

    public enum PathType {
        DIRECT,
        CURVE //with mecanum wheels, we can't really implement this yet
    }

    public class Point {

        double x = 0;
        double y = 0;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }//Point

        public double hypot() {
            return Math.hypot(x, y);
        }

        @Override
        public String toString() {
            return "{" + x + ", " + y + "}";
        }

    }
}
