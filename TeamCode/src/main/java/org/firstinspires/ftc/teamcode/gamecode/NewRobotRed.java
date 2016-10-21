package org.firstinspires.ftc.teamcode.gamecode;

import com.vuforia.PIXEL_FORMAT;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.R;
import org.firstinspires.ftc.teamcode.RC;
import org.firstinspires.ftc.teamcode.opmodesupport.FXTLinearOpMode;
import org.firstinspires.ftc.teamcode.opmodesupport.TaskHandler;
import org.firstinspires.ftc.teamcode.robots.NewRobot;

/**
 * Created by FIXIT on 16-10-18.
 */
public class NewRobotRed extends FXTLinearOpMode {

    @Override
    public void runOp() throws InterruptedException {
        final NewRobot rbt = new NewRobot(true);

        VuforiaLocalizer.Parameters params = new VuforiaLocalizer.Parameters();
        params.vuforiaLicenseKey = RC.VUFORIA_LICENSE_KEY;
        params.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;

        VuforiaLocalizer locale = ClassFactory.createVuforiaLocalizer(params);
        locale.setFrameQueueCapacity(1);
        Vuforia.setFrameFormat(PIXEL_FORMAT.RGB565, true);

        VuforiaTrackables beacons = locale.loadTrackablesFromAsset("FTC_2016-17");
        VuforiaTrackableDefaultListener gears = (VuforiaTrackableDefaultListener) beacons.get(3).getListener();

        waitForStart();
        beacons.activate();

        mainTasks.addRunnable(new Runnable() {
            @Override
            public void run() {
                rbt.veerCheck();
            }
        });

        rbt.trackForward(609.6, 0.5);
        rbt.imuTurnL(45, 0.5);
        rbt.forward(0.5);

        while(!gears.isVisible()) {
            idle();
        }//while

        VectorF trans = movePointOffWall(gears.getPose().getTranslation(), rbt.imu.getAngularOrientation().firstAngle, 100);

        while (trans.magnitude() < 100) {
            trans = movePointOffWall(gears.getPose().getTranslation(), rbt.imu.getAngularOrientation().firstAngle, 100);

            double targetAngle = Math.atan2(trans.get(3), trans.get(2));

            rbt.absoluteIMUTurn(targetAngle, 0.25);
        }//while

    }

    //this assumes the horizontal axis is the y-axis since the phone is vertical
    //robot angle is relative to "parallel with the beacon wall"
    public VectorF movePointOffWall(VectorF trans, double robotAngle, double offWall) {

        double angle = Math.abs(robotAngle) + Math.atan2(trans.get(3), trans.get(2));
        double hypot = Math.hypot(trans.get(3), trans.get(2));

        double xDist = hypot * Math.sin(angle) - offWall;
        double zDist = hypot * Math.cos(angle);

        hypot = Math.hypot(xDist, zDist);
        angle -= robotAngle;

        xDist = -hypot * Math.sin(angle);
        zDist = hypot * Math.cos(angle);

        return new VectorF(trans.get(1), (float) xDist, (float) zDist);
    }

}
