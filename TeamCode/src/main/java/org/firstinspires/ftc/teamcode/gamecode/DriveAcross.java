package org.firstinspires.ftc.teamcode.gamecode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.opmodesupport.AutoOpMode;
import org.firstinspires.ftc.teamcode.opmodesupport.TaskHandler;
import org.firstinspires.ftc.teamcode.opmodesupport.TeleOpMode;
import org.firstinspires.ftc.teamcode.robots.Fermion;
import org.firstinspires.ftc.teamcode.robots.Robot;

/**
 * Created by Windows on 2016-01-30.
 */
@Autonomous
public class DriveAcross extends AutoOpMode {

    @Override
    public void runOp() throws InterruptedException {
        final Fermion up = new Fermion(true);

        waitForStart();

        TaskHandler.addLoopedTask("veerCheck", new Runnable() {
            @Override
            public void run() {
                up.veerCheck();
            }
        }, 5);

        up.forward(0.13);
        sleep(2000);

        up.imuTurnR(90, 0.25);

        up.right(0.13);
        sleep(2000);

        up.stop();
    }
}
