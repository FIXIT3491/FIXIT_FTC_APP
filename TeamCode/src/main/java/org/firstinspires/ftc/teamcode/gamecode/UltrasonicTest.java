package org.firstinspires.ftc.teamcode.gamecode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.RC;
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
        while (opModeIsActive()){
            RC.t.addData("Ultraside", top.getUltrasonicDistance(1));
            sleep(10);
        }
    }
}
