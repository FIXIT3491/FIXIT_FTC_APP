package org.firstinspires.ftc.teamcode.gamecode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.opmodesupport.TeleOpMode;
import org.firstinspires.ftc.teamcode.robots.Robot;

/**
 * Created by Windows on 2016-07-07.
 */
@Autonomous
public class BasicTeleOp extends TeleOpMode {

    Robot robot;

    @Override
    public void initialize() {
        robot = new Robot();
    }

    @Override
    public void loopOpMode() {
        robot.driveL(joy1.y1());
        robot.driveR(joy1.y2());
    }
}
