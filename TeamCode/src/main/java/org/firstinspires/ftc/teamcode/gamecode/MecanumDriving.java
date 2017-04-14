package org.firstinspires.ftc.teamcode.gamecode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.opmodesupport.AutoOpMode;
import org.firstinspires.ftc.teamcode.robots.Fermion;

/**
 * Created by FIXIT on 2017-04-13.
 */
@Autonomous
public class MecanumDriving extends AutoOpMode {
    @Override
    public void runOp() throws InterruptedException {
        Fermion f = new Fermion(true);

        waitForStart();

        f.capRelease.goToPos("start");

        f.forward(0.5);

        sleep(1000);

        f.right(0.5);

        sleep(1000);

        f.backward(0.5);

        sleep(1000);

        f.left(0.5);

        sleep(1000);
    }
}
