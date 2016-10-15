package org.firstinspires.ftc.teamcode.gamecode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.teamcode.RC;
import org.firstinspires.ftc.teamcode.roboticslibrary.DataWriter;

import java.io.FileNotFoundException;

/**
 * Created by FIXIT on 16-10-07.
 */
@Autonomous
public class EncoderDefaultRun extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        RC.setOpMode(this);



        DcMotor left = hardwareMap.dcMotor.get("driveL");
        left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        DcMotor right = hardwareMap.dcMotor.get("driveR");
        right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right.setDirection(DcMotorSimple.Direction.REVERSE);

        DataWriter write = null;
        try {
            write = new DataWriter("encoderspeedpid.txt", true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        VoltageSensor volt = hardwareMap.voltageSensor.get("Motor Controller 2");

        write.write("Voltage MC1: " + volt.getVoltage() + "\n");

        waitForStart();

        idle();


        right.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        right.setTargetPosition(5614);
        left.setTargetPosition(5614);
        right.setPower(1.0);
        left.setPower(1.0);

        long last = System.nanoTime();
        int lastTikLeft = left.getCurrentPosition();
        int lastTikRight = right.getCurrentPosition();

        while (opModeIsActive()) {
            write.write("Left Tiks / sec:" + ((left.getCurrentPosition() - lastTikLeft) / ((System.nanoTime() - last) / 1000000000.0)) + "\n");
            write.write("Right Tiks / sec: " + ((right.getCurrentPosition() - lastTikRight) / ((System.nanoTime() - last) / 1000000000.0)) + "\n");
            write.write("Voltage MC1: " + volt.getVoltage() + "\n");
            last = System.currentTimeMillis();
        }

        write.closeWriter();

    }

}
