package org.firstinspires.ftc.teamcode.gamecode;

import com.qualcomm.hardware.adafruit.AdafruitBNO055IMU;
import com.qualcomm.hardware.adafruit.BNO055IMU;
import com.qualcomm.hardware.adafruit.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.RC;
import org.firstinspires.ftc.teamcode.newhardware.FXTSensors.AdafruitIMU;
import org.firstinspires.ftc.teamcode.opmodesupport.TeleOpMode;

/**
 * Created by Owner on 8/31/2015.
 */
@Autonomous
public class AdafruitCheck extends TeleOpMode {

    AdafruitBNO055IMU imu;

    @Override
    public void initialize() {
        BNO055IMU.Parameters params = new BNO055IMU.Parameters();
        params.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        params.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        params.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        imu = (AdafruitBNO055IMU)RC.h.i2cDeviceSynch.get("adafruit");
        imu.initialize(params);
    }

    @Override
    public void loopOpMode() {
        Orientation o = imu.getAngularOrientation();
        RC.t.addData("Angle-Heading", o.firstAngle);
        RC.t.addData("Angle-Roll", o.secondAngle);
        RC.t.addData("Angle-Pitch", o.thirdAngle);

        Acceleration a = imu.getLinearAcceleration();
        RC.t.addData("Lin-Accel-X", a.xAccel);
        RC.t.addData("Lin-Accel-Y", a.yAccel);
        RC.t.addData("Lin-Accel-Z", a.zAccel);

        AngularVelocity v = imu.getAngularVelocity();
        RC.t.addData("Gyro-X", v.firstAngleRate);
        RC.t.addData("Gyro-Y", v.secondAngleRate);
        RC.t.addData("Gyro-Z", v.thirdAngleRate);
    }

}