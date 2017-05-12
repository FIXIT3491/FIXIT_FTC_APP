package org.firstinspires.ftc.teamcode.gamecode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.opmodesupport.AutoOpMode;
import org.firstinspires.ftc.teamcode.robots.Fermion;

/**
 * Created by FIXIT on 8/19/2015.
 */
@Autonomous
public class EncoderTest extends AutoOpMode {



    @Override
    public void runOp() throws InterruptedException {

        Fermion f = new Fermion(true);

        waitForStart();

        f.track(26.56, 1363.107, 0.6);
    }

    public void moveForward(int mm, double speed){

    }
}
