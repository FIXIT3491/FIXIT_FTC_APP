package org.firstinspires.ftc.teamcode.gamecode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.opmodesupport.AutoOpMode;
import org.firstinspires.ftc.teamcode.robots.Fermion;

/**
 * Created by Aila on 16-12-10.
 */
@Autonomous
public class CapBallEncoders extends AutoOpMode{

    @Override
    public void runOp() throws InterruptedException {

        Fermion fermion = new Fermion(false);

        fermion.track(0,2000,0.7);

    }
}
