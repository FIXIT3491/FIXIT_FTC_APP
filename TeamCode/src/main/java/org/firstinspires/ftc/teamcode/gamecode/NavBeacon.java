package org.firstinspires.ftc.teamcode.gamecode;

import com.qualcomm.hardware.adafruit.AdafruitBNO055IMU;
import com.qualcomm.hardware.adafruit.BNO055IMU;
import com.qualcomm.hardware.adafruit.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.R;
import org.firstinspires.ftc.teamcode.opmodesupport.TeleOpMode;
import org.firstinspires.ftc.teamcode.robots.Robot;

/**
 * Created by FIXIT on 16-09-18.
 */
public class NavBeacon extends LinearOpMode {

    double requestedRadians = 0;

    //if robot is driving in the correct direction
    //the motor power would be "normalSpeed"
    //intuitively, I think that higher speeds call for
    //a more severe "turnConstant"
    final double normalSpeed = 0.5;

    //dictates how severely veer checking occurs
    //higher value would lead to less severity
    final double turnConstant = 1.0;

    Robot bot;

    @Override
    public void runOpMode() throws InterruptedException {

        VuforiaLocalizer.Parameters params = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId);
        params.vuforiaLicenseKey = "Ad0I0ir/////AAAAAfR3NIO1HkxSqM8NPhlEftFXtFAm6DC5w4Cjcy30WUdGozklFlAkxeHpjfWc4moeL2ZTPvZ+wAoyOnlZxyB6Wr1BRE9154j6K1/8tPvu21y5ke1MIbyoJ/5BAQuiwoAadjptZ8fpS7A0QGPrMe0VauJIM1mW3UU2ezYFSOcPghCOCvQ8zid1Bb8A92IkbLcBUcv3DEC6ia4SEkbRMY7TpOh2gzsXdsue4tqj9g7vj7zBU5Hu4WhkMDJRsThn+5QoHXqvavDsCElwmDHG3hlEYo7qN/vV9VcQUX9XnVLuDeZhkp885BHK5vAe8T9W3Vxj2H/R4oijQso6hEBaXsOpCHIWGcuphpoe9yoQlmNRRZ97";
        params.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;

        VuforiaLocalizer vuforia = ClassFactory.createVuforiaLocalizer(params);

        VuforiaTrackables beacons = vuforia.loadTrackablesFromAsset("FTC_2016-17");

        AdafruitBNO055IMU imu = hardwareMap.get(AdafruitBNO055IMU.class, "adafruit");

        BNO055IMU.Parameters imuParams = new BNO055IMU.Parameters();
        imuParams.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        imuParams.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        imuParams.loggingEnabled      = true;
        imuParams.loggingTag          = "IMU";
        imuParams.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        bot = new Robot();

        imu.initialize(imuParams);
        beacons.activate();

        waitForStart();

        VectorF translation = ((VuforiaTrackableDefaultListener) beacons.get(0)).getPose().getTranslation();

        double ellipseX = translation.get(0);
        double ellipseZ = translation.get(2);

        double xScale = ellipseX / ellipseZ;

        double radiansDiff = 0;

        while (opModeIsActive()) {

            radiansDiff = imu.getAngularOrientation().firstAngle;

            translation = ((VuforiaTrackableDefaultListener) beacons.get(0)).getPose().getTranslation();

            double actualRadians = Math.atan2(translation.get(0), translation.get(2));

            //get distance to beacon
            double distance = Math.hypot(translation.get(0), translation.get(2));

            double absoluteX = distance * Math.cos(actualRadians - radiansDiff);
            double absoluteZ = distance * Math.sin(actualRadians - radiansDiff);

            requestedRadians = getEllipsePrime(absoluteX, absoluteZ, xScale);

            double error = requestedRadians - imu.getAngularOrientation().firstAngle;

            bot.motorL.setPower(normalSpeed + (error / turnConstant));

            bot.motorR.setPower(normalSpeed - (error / turnConstant));

        }//while


    }

    public static double getEllipsePrime(double x, double y, double ellipseProp) {
        double slope = (-ellipseProp * ellipseProp * x) / y;

        return Math.atan(slope);
    }

}
