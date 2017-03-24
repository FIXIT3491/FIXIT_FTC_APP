package org.firstinspires.ftc.teamcode.gamecode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.opmodesupport.AutoOpMode;
import org.firstinspires.ftc.teamcode.robots.Fermion;

/**
 * Created by Windows on 2017-03-04.
 */
@Autonomous
public class UltrasonicTest extends AutoOpMode {

    @Override
    public void runOp() throws InterruptedException {

        Fermion top = new Fermion(true);

        waitForStart();
        top.resetTargetAngle();

        top.setWallDistance(400);
        top.strafe(-90, 1, true);
        top.startWallFollowing(top.ultra);
        sleep(3000);
        top.stop();

    }
}
