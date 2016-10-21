package org.firstinspires.ftc.teamcode.resqopmodes;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.opmodesupport.TeleOpMode;
import org.firstinspires.ftc.teamcode.robots.Lily;

/**
 * Created by FIXIT on 16-10-07.
 */
@TeleOp
public class ArmTester extends LinearOpMode {

    DcMotor motor;

    @Override
    public void runOpMode() throws InterruptedException {

        motor = hardwareMap.dcMotor.get("elbow");
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        idle();
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        waitForStart();

        motor.setTargetPosition(500);
        motor.setPower(0.3);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        while (opModeIsActive() && motor.isBusy()) {

            idle();

        }
    }
}
