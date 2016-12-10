package org.firstinspires.ftc.teamcode.gamecode;

import android.util.Log;

import org.firstinspires.ftc.teamcode.RC;
import org.firstinspires.ftc.teamcode.newhardware.FXTSensors.FXTVoltageSensor;
import org.firstinspires.ftc.teamcode.newhardware.Motor;
import org.firstinspires.ftc.teamcode.opmodesupport.AutoOpMode;
import org.firstinspires.ftc.teamcode.opmodesupport.TeleOpMode;
import org.firstinspires.ftc.teamcode.robots.Fermion;
import org.firstinspires.ftc.teamcode.robots.Lily;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by FIXIT on 8/19/2015.
 */
@Autonomous
public class EncoderTest extends AutoOpMode {

    Motor rFore;
    Motor lFore;
    Motor rBack;
    Motor lBack;
    Fermion top;

    @Override
    public void runOp() throws InterruptedException {

        Motor forward = new Motor("rightBack");
        Motor lateral = new Motor("rightFore");
        top = new Fermion(true);

        top.targetAngle = 0;
        waitForStart();

        top.addVeerCheckRunnable();

        double mm = RC.globalDouble("EncoderDistance");

        mm *= (1440 / (4 * Math.PI * 25.4));

        Log.i("MM", mm + "");

        int beginX = lateral.getCurrentPosition();
        int beginY = forward.getCurrentPosition();
        int currentX = beginX;
        int currentY = beginY;

        double remaining = mm;
//
//        top.strafe(90, 0.5);
//        while (opModeIsActive() && remaining > 0) {
//            currentX = lateral.getCurrentPosition();
//            currentY = forward.getCurrentPosition();
//            remaining = mm - Math.hypot((currentX - beginX), (currentY - beginY));
//
//            top.strafe(90, (0.45 * (remaining) + 0.05));
//            Log.i("INFO", "Begin: " + beginX + ", MM: " + mm + ", CURRENT: " + remaining);
//        }//while

        top.track(28, 1000, 0.5);

    }

    public void moveForward(int mm, double speed){

    }
}
