package org.firstinspires.ftc.teamcode.gamecode;

import android.util.Log;

import com.qualcomm.hardware.adafruit.AdafruitBNO055IMU;
import com.qualcomm.hardware.adafruit.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;

import org.firstinspires.ftc.teamcode.roboticslibrary.DataWriter;

import java.io.FileNotFoundException;

/**
 * Created by FIXIT on 16-10-08.
 */
@Autonomous
public class IMUDefaultRun extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        DcMotor left = hardwareMap.dcMotor.get("driveL");
        left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        DcMotor right = hardwareMap.dcMotor.get("driveR");
        right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right.setDirection(DcMotorSimple.Direction.REVERSE);

        AdafruitBNO055IMU imu = (AdafruitBNO055IMU) hardwareMap.get(BNO055IMU.class, "adafruit");
        imu.initialize(new BNO055IMU.Parameters());

        OpticalDistanceSensor eopd = hardwareMap.opticalDistanceSensor.get("eopd");

        DataWriter write = null;
        try {
            write = new DataWriter("encoderspeedpid.txt", true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        waitForStart();

        right.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        right.setTargetPosition(3614);
        left.setTargetPosition(3614);
        right.setPower(1);
        left.setPower(1);

        long last = System.nanoTime();
        int lastTikLeft = left.getCurrentPosition();
        int lastTikRight = right.getCurrentPosition();

        while (opModeIsActive() && right.isBusy() && left.isBusy()) {
            write.write("Left Tiks / nanosec: " + ((left.getCurrentPosition() - lastTikLeft) / (System.nanoTime() - last)));
            write.write("Right Tiks / nanosec: " + ((right.getCurrentPosition() - lastTikRight) / (System.nanoTime() - last)));
            last = System.nanoTime();
            Log.i("ANGLE", (imu.getAngularOrientation().firstAngle) + "");
        }//while

        write.write("\n\nTURNING");

        right.setPower(-1);
        left.setPower(1);

        double begin = imu.getAngularOrientation().firstAngle;

        if (begin < -Math.PI) {
            begin += 2 * Math.PI;
        }

        if (begin > Math.PI) {
            begin -= 2 * Math.PI;
        }
        Log.i("TURNING", "ANGLE");
        Log.i("ANGLE", (imu.getAngularOrientation().firstAngle - begin) + "");

        double angleToTurn = 0;

        while (opModeIsActive() && Math.abs(angleToTurn - begin) < 1.57) {
            Log.i("ANGLE", (imu.getAngularOrientation().firstAngle - begin) + "");
            write.write("Left Tiks / nanosec: " + ((left.getCurrentPosition() - lastTikLeft) / (System.nanoTime() - last)));
            write.write("Right Tiks / nanosec: " + ((right.getCurrentPosition() - lastTikRight) / (System.nanoTime() - last)));
            write.write("EOPD Values: " + "Light: " + eopd.getLightDetected() + ", Raw Light: " + eopd.getRawLightDetected());
            last = System.nanoTime();

            angleToTurn = imu.getAngularOrientation().firstAngle;

            if (angleToTurn < -Math.PI) {
                angleToTurn += 2 * Math.PI;
            }

            if (angleToTurn > Math.PI) {
                angleToTurn -= 2 * Math.PI;
            }


        }//while

        right.setPower(0);
        left.setPower(0);
        write.closeWriter();
    }

}
