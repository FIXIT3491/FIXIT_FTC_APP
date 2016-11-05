package org.firstinspires.ftc.teamcode.gamecode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RC;
import org.firstinspires.ftc.teamcode.opmodesupport.TeleOpMode;
import org.firstinspires.ftc.teamcode.robots.Fermion;

/**
 * Created by FIXIT on 16-10-14.
 */
@TeleOp
public class MecanumDriveTest extends TeleOpMode {

    Fermion rbt;

    @Override
    public void initialize() {
        rbt = new Fermion(false);
    }

    @Override
    public void loopOpMode() {

//        double degrees = 90 - Math.toDegrees(Math.asin(-joy1.y1()));
//
//        degrees *= Math.signum(joy1.x1());
//
//        rbt.strafe(degrees, Math.hypot(joy1.y1(), joy1.x1()));

        rbt.rightFore.setPower((joy1.x2() + joy1.y1() - joy1.x1()) / 3.0);
        rbt.rightBack.setPower((joy1.x2() + joy1.y1() + joy1.x1()) / 3.0);
        rbt.leftFore.setPower((-joy1.x2() + joy1.y1() + joy1.x1()) / 3.0);
        rbt.leftBack.setPower((-joy1.x2() + joy1.y1() - joy1.x1())/ 3.0);

//        RC.t.addData("h", degrees);
        RC.t.addData("leftFore", rbt.leftFore.returnCurrentState());
        RC.t.addData("leftBack", rbt.leftBack.returnCurrentState());
        RC.t.addData("rightFore", rbt.rightFore.returnCurrentState());
        RC.t.addData("rightBack", rbt.rightBack.returnCurrentState());

    }


}
