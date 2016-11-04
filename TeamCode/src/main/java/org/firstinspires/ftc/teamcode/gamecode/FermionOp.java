package org.firstinspires.ftc.teamcode.gamecode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RC;
import org.firstinspires.ftc.teamcode.opmodesupport.AutoOpMode;
import org.firstinspires.ftc.teamcode.opmodesupport.TeleOpMode;
import org.firstinspires.ftc.teamcode.roboticslibrary.MathUtils;
import org.firstinspires.ftc.teamcode.robots.*;

/**
 * Created by Windows on 2016-11-04.
 */
@TeleOp
public class FermionOp extends TeleOpMode {
    Fermion fermi;


    @Override
    public void initialize() {
        fermi = new Fermion(true);
    }

    @Override
    public void loopOpMode() {
        double theta = 0;
        if(joy1.rightBumper()){
            theta = Math.abs(Math.atan2(joy1.y1(), joy1.x1()));

            switch (MathUtils.getQuadrant(joy1.x1(), joy1.y1())){
                case 2: theta *= -1;
                    break;
                case 3: theta = -theta - Math.PI / 2;
                    break;
                case 4: theta += Math.PI / 2;
                    break;
            }
            fermi.strafe(Math.toDegrees(MathUtils.roundToNearest(theta, Math.PI / 4, - Math.PI / 2, Math.PI / 2)), 1);
        } else {
            fermi.rightFore.setPower((joy1.x2() - joy1.y1() - joy1.x1()) / 3.0);
            fermi.rightBack.setPower((joy1.x2() - joy1.y1() + joy1.x1()) / 3.0);
            fermi.leftFore.setPower((-joy1.x2() - joy1.y1() + joy1.x1()) / 3.0);
            fermi.leftBack.setPower((-joy1.x2() - joy1.y1() - joy1.x1())/ 3.0);
        }


        if(joy1.buttonA()){
            fermi.absoluteIMUTurn(90, 0.5);
        }
    }
}//FermionOp