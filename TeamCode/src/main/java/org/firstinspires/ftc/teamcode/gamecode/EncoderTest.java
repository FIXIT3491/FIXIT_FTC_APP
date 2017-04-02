package org.firstinspires.ftc.teamcode.gamecode;

import org.firstinspires.ftc.teamcode.RC;
import org.firstinspires.ftc.teamcode.newhardware.Motor;
import org.firstinspires.ftc.teamcode.opmodesupport.AutoOpMode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

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
            RC.t.addData("X", lateral.getBaseCurrentPosition());
            RC.t.addData("Y", forward.getBaseCurrentPosition());
            telemetry.update();
            sleep(10);
        }

    }

    public void moveForward(int mm, double speed){

    }
}
