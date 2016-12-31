package org.firstinspires.ftc.teamcode.gamecode;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RC;
import org.firstinspires.ftc.teamcode.newhardware.FXTSensors.FXTLightSensor;
import org.firstinspires.ftc.teamcode.newhardware.FXTSensors.FXTOpticalDistanceSensor;
import org.firstinspires.ftc.teamcode.opmodesupport.TeleOpMode;
import org.firstinspires.ftc.teamcode.robots.Fermion;
import org.firstinspires.ftc.teamcode.robots.Robot;
import org.firstinspires.ftc.teamcode.util.MathUtils;

/**
 * Created by Windows on 2016-12-04.
 */
@TeleOp
public class VeerFermionOp extends TeleOpMode {

    Fermion tau;
    int collectorState = Robot.STOP;

    double lF, lB, rF, rB = 0;

    @Override
    public void initialize() {
//        top = new Fermion(true);
        tau = new Fermion(false);
//        top.mouse.addAbsoluteCoordinateRunnable(top.imu);
//        setDataLogFile("path.txt", true);
        tau.startShooterControl();
        tau.prime();
    }

    @Override
    public void loopOpMode() {
//        dataLogData(top.mouse.absoluteFieldCoord.toString() + "\n");

        double theta = Math.atan2(-joy1.x1(), joy1.y1());

        Log.i("Speeds=!", "" + Math.toDegrees(theta));
        double speed = (joy1.rightBumper())? 0.3 : Math.hypot(joy1.y1(), joy1.x1());

        tau.strafe(Math.toDegrees(theta), speed, false);

        tau.veer(Math.round(joy1.x2()) / 2.0, false, false);

        if(collectorState == Robot.IN && (joy2.rightBumper())) {
            collectorState = Robot.STOP;
        } else if (joy2.rightBumper()) {
            collectorState = Robot.IN;
        }//else

        tau.setCollectorState(collectorState);

        if (joy2.rightTrigger()) {
            tau.setCollectorState(Robot.OUT);
        }//if

        if(joy2.buttonA()){
            tau.shoot();
        } else if(joy2.buttonB()){
            tau.prime();
        }

        if(joy2.leftBumper()){
            tau.door.goToPos("open");
        } else {
            tau.door.goToPos("close");
        }


        tau.usePlannedSpeeds();

    }//loopOpMode


    public void veer(double speed, boolean preservingStrafeSpeed) {

        speed = Math.signum(speed) * Math.min(1, Math.abs(speed));

        double leftForePower = (lF + rB) / 2.0;
        double leftBackPower = (lB + rF) / 2.0;
        double rightForePower = (lB + rF) / 2.0;
        double rightBackPower = (lF + rB) / 2.0;

        if (preservingStrafeSpeed) {

            double maxCutOff = Math.max(Math.max(Math.abs(leftBackPower + speed), Math.abs(leftForePower + speed)), Math.max(Math.abs(rightBackPower - speed), Math.abs(rightForePower - speed)));
            maxCutOff -= 1;

            if (maxCutOff > 0) {
                speed -= maxCutOff;
            }//if

            leftForePower += speed;
            leftBackPower += speed;
            rightForePower -= speed;
            rightBackPower -= speed;
        } else {

            double max = MathUtils.max(Math.abs(leftBackPower + speed), Math.abs(leftForePower + speed), Math.abs(rightBackPower - speed), Math.abs(rightForePower - speed));
            double maxOriginal = MathUtils.max(Math.abs(leftBackPower), Math.abs(leftForePower), Math.abs(rightBackPower), Math.abs(rightForePower));

            if (max > 1) {
                double maxAllowed = 1 - Math.abs(speed);

                leftForePower *= maxAllowed / maxOriginal;
                leftBackPower *= maxAllowed / maxOriginal;
                rightForePower *= maxAllowed / maxOriginal;
                rightBackPower *= maxAllowed / maxOriginal;
            }//if

            leftForePower += speed;
            leftBackPower += speed;
            rightForePower -= speed;
            rightBackPower -= speed;

        }//else

        Log.i("Resulting Speeds", "LF: " + leftForePower + ", LB: " + leftBackPower + ", RF: " + rightForePower + ", RB: " + rightBackPower);

        lF = leftForePower;
        lB = leftBackPower;
        rF = rightForePower;
        rB = rightBackPower;
    }//veer


    public void strafe(double degrees, double speed) {

        degrees += 45;

        double leftForeRightBack = Math.sin(Math.toRadians(degrees));
        double rightForeLeftBack = Math.cos(Math.toRadians(degrees));

        double multi = speed / Math.max(Math.abs(leftForeRightBack), Math.abs(rightForeLeftBack));
        leftForeRightBack *= multi;
        rightForeLeftBack *= multi;

        lF = leftForeRightBack;
        rB = leftForeRightBack;

        rF = rightForeLeftBack;
        lB = rightForeLeftBack;

    }//strafe

}//VeerFermionOp