package org.firstinspires.ftc.teamcode.gamecode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.RC;
import org.firstinspires.ftc.teamcode.robots.Robot;

/**
 * Created by FIXIT on 16-09-19.
 */
public class Pathing extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        VuforiaLocalizer.Parameters params = new VuforiaLocalizer.Parameters();
        params.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        params.vuforiaLicenseKey = RC.VUFORIA_LICENSE_KEY;
        VuforiaLocalizer locale = ClassFactory.createVuforiaLocalizer(params);

        VuforiaTrackables beacons = locale.loadTrackablesFromAsset("FTC_2016-17");

        Robot robot = new Robot();

        waitForStart();


        /*
         Initialize pathfinder

         Initialize robot

         waitForStart();

         runRunnableOnSideThread(analyzeBeaconTrackable("Gears");
         pather.navigateTo(VuforiaTrackable beacon, VuforiaLocalizer locale);

         pressBeaconButton(BeaconStatus gears);

         pather.navigateTo(Point behindRobot);
         pather.navigateTo(Point fiveMetersToRight);

         */
    }
}
