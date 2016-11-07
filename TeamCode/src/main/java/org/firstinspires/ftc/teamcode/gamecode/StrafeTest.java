package org.firstinspires.ftc.teamcode.gamecode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RC;
import org.firstinspires.ftc.teamcode.opmodesupport.AutoOpMode;
import org.firstinspires.ftc.teamcode.robots.*;

/**
 * Created by Windows on 2016-11-05.
 */
@Autonomous
public class StrafeTest extends AutoOpMode {

    @Override
    public void runOp() {
        Fermion f = new Fermion(true);
        waitForStart();
        f.strafe(0, 0.2);
        sleep(1000);

    }
}//StrafeTest