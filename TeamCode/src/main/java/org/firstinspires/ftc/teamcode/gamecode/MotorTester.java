package org.firstinspires.ftc.teamcode.gamecode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RC;
import org.firstinspires.ftc.teamcode.opmodesupport.AutoOpMode;
import org.firstinspires.ftc.teamcode.robots.*;

/**
 * Created by Windows on 2016-12-08.
 */
@Autonomous
public class MotorTester extends AutoOpMode {

    @Override
    public void runOp() {
        Fermion up = new Fermion(false);
        waitForStart();
        up.rightBack.setPower(0.3);
        sleep(1000);
        up.rightBack.setPower(-0.3);
        sleep(1000);
        up.rightBack.setPower(0);


        up.rightFore.setPower(0.3);
        sleep(1000);
        up.rightFore.setPower(-0.3);
        sleep(1000);
        up.rightFore.setPower(0);

        up.leftBack.setPower(0.3);
        sleep(1000);
        up.leftBack.setPower(-0.3);
        sleep(1000);
        up.leftBack.setPower(0);

        up.leftFore.setPower(0.3);
        sleep(1000);
        up.leftFore.setPower(-0.3);
        sleep(1000);
        up.leftFore.setPower(0);
        

    }
}//MotorTester