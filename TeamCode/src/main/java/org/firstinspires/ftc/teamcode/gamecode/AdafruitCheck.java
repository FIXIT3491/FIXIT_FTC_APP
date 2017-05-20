package org.firstinspires.ftc.teamcode.gamecode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.opmodesupport.AutoOpMode;
import org.firstinspires.ftc.teamcode.robots.Fermion;

/**
 * Created by Owner on 8/31/2015.
 */
@Autonomous
public class AdafruitCheck extends AutoOpMode{


    @Override
    public void runOp() throws InterruptedException {
        Fermion f = new Fermion(true);

        waitForStart();

        f.imuTurnL(90, 0.5);

        sleep(500);
    }
}