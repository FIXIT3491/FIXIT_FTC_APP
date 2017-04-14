package org.firstinspires.ftc.teamcode.gamecode;

import org.firstinspires.ftc.teamcode.RC;
import org.firstinspires.ftc.teamcode.newhardware.Motor;
import org.firstinspires.ftc.teamcode.opmodesupport.AutoOpMode;
import org.firstinspires.ftc.teamcode.robots.Fermion;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by FIXIT on 8/19/2015.
 */
@Autonomous
public class EncoderTest extends AutoOpMode {



    @Override
    public void runOp() throws InterruptedException {

        Fermion f = new Fermion(true);

        waitForStart();
        f.track(90, 24 * 25.4, 0.6);

    }

    public void moveForward(int mm, double speed){

    }
}
