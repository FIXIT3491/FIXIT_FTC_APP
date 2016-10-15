package org.firstinspires.ftc.teamcode.gamecode;

import com.qualcomm.hardware.adafruit.AdafruitBNO055IMU;
import com.qualcomm.hardware.adafruit.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;

import org.firstinspires.ftc.teamcode.RC;
import org.firstinspires.ftc.teamcode.opmodesupport.FXTLinearOpMode;
import org.firstinspires.ftc.teamcode.robots.Lily;

/**
 * Created by FIXIT on 16-10-08.
 */
@Autonomous
public class IMUDefaultRun extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        DcMotor left = hardwareMap.dcMotor.get("driveL");
        left.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        DcMotor right = hardwareMap.dcMotor.get("driveR");
        right.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        right.setDirection(DcMotorSimple.Direction.REVERSE);

        AdafruitBNO055IMU imu = (AdafruitBNO055IMU) hardwareMap.get(BNO055IMU.class, "adafruit");
        imu.initialize(new BNO055IMU.Parameters());

        OpticalDistanceSensor eopd = hardwareMap.opticalDistanceSensor.get("eopd");

        RC.t.setDataLogFile("imuauto.txt", true);

        waitForStart();

        right.setTargetPosition(5614);
        left.setTargetPosition(5614);
        right.setPower(1);
        left.setPower(1);

        long last = System.currentTimeMillis();
        int lastTikLeft = left.getCurrentPosition();
        int lastTikRight = right.getCurrentPosition();

        while (opModeIsActive() && !right.isBusy() && !left.isBusy()) {
            RC.t.dataLogData("Left Tiks / sec", ((left.getCurrentPosition() - lastTikLeft) / (System.currentTimeMillis() - last)));
            RC.t.dataLogData("Right Tiks / sec", ((right.getCurrentPosition() - lastTikRight) / (System.currentTimeMillis() - last)));
            last = System.currentTimeMillis();
        }//while

        RC.t.dataLogData("\n\nTURNING");

        right.setPower(-1);
        left.setPower(1);

        double begin = imu.getAngularOrientation().firstAngle;

        while (opModeIsActive() && imu.getAngularOrientation().firstAngle - begin < 90) {
            RC.t.dataLogData("Left Tiks / sec", ((left.getCurrentPosition() - lastTikLeft) / (System.currentTimeMillis() - last)));
            RC.t.dataLogData("Right Tiks / sec", ((right.getCurrentPosition() - lastTikRight) / (System.currentTimeMillis() - last)));
            RC.t.dataLogData("EOPD Values", "Light: " + eopd.getLightDetected() + ", Raw Light: " + eopd.getRawLightDetected());
            last = System.currentTimeMillis();
        }//while

        right.setPower(0);
        left.setPower(0);
    }

}
