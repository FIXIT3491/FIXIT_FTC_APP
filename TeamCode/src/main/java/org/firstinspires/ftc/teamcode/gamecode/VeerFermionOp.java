package org.firstinspires.ftc.teamcode.gamecode;

import org.firstinspires.ftc.teamcode.RC;
import org.firstinspires.ftc.teamcode.opmodesupport.TeleOpMode;
import org.firstinspires.ftc.teamcode.robots.Fermion;
import org.firstinspires.ftc.teamcode.util.MathUtils;

/**
 * Created by FIXIT on 16-11-28.
 */
public class VeerFermionOp extends TeleOpMode {

    /**
     * VeerFermionOp has one purpose:
     * to test if the robot can turn and strafe at the same time
     * if so, driving could be much easier (we'll have to see)
     */

    Fermion charm;

    @Override
    public void initialize() {
        charm = new Fermion(false);

        //since our drivers tend to drive at FULL SPEED
        //we have to prioritize veering over strafing
        //or else the robot will never turn!
        charm.preservingStrafeSpeed = false;

        telemetry.addData("Red Alliance: ", RC.globalBool("RedAlliance"));
    }

    @Override
    public void loopOpMode() {

        double theta = Math.abs(Math.atan2(joy1.x1(), -joy1.y1()));
        theta *= (joy1.x1() < 0)? -1 : 1;

        //I have some questions about roundToNearest...
        charm.strafe(Math.toDegrees(MathUtils.roundToNearest(theta, Math.PI / 4, -Math.PI)), joy1.rightBumper()? 0.3 : 1);
        charm.veer(joy1.x2());
    }
}
