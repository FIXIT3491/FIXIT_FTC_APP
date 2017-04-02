package org.firstinspires.ftc.teamcode.gamecode;

import android.util.Log;

import org.firstinspires.ftc.teamcode.RC;
import org.firstinspires.ftc.teamcode.newhardware.FXTSensors.FXTVoltageSensor;
import org.firstinspires.ftc.teamcode.newhardware.Motor;
import org.firstinspires.ftc.teamcode.opmodesupport.AutoOpMode;
import org.firstinspires.ftc.teamcode.opmodesupport.TeleOpMode;
import org.firstinspires.ftc.teamcode.robots.Fermion;
import org.firstinspires.ftc.teamcode.robots.Lily;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by FIXIT on 8/19/2015.
 */
@Autonomous
public class EncoderTest extends AutoOpMode {



    @Override
    public void runOp() throws InterruptedException {

        Motor forward = new Motor("yTrack");
        Motor lateral = new Motor("xTrack");

        waitForStart();

        while(opModeIsActive()){
            RC.t.addData("X", lateral.getCurrentPosition());
            RC.t.addData("Y", forward.getCurrentPosition());
            telemetry.update();
            sleep(10);
        }

    }

    public void moveForward(int mm, double speed){

    }
}
