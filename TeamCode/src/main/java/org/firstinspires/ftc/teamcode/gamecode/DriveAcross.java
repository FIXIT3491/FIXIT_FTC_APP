package org.firstinspires.ftc.teamcode.gamecode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.opmodesupport.AutoOpMode;
import org.firstinspires.ftc.teamcode.robots.Fermion;

/**
 * Created by Windows on 2016-01-30.
 */
@Autonomous
public class DriveAcross extends AutoOpMode {

    @Override
    public void runOp() throws InterruptedException {
        final Fermion up = new Fermion(true);

        waitForStart();

        sleep(10000);

        up.track(0,1600,0.7);
    }
}
