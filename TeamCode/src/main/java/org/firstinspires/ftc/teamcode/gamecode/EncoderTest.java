package org.firstinspires.ftc.teamcode.gamecode;

import android.util.Log;

import org.firstinspires.ftc.teamcode.RC;
import org.firstinspires.ftc.teamcode.newhardware.FXTSensors.FXTVoltageSensor;
import org.firstinspires.ftc.teamcode.newhardware.Motor;
import org.firstinspires.ftc.teamcode.opmodesupport.AutoOpMode;
import org.firstinspires.ftc.teamcode.opmodesupport.TeleOpMode;
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


    @Override
    public void runOp() throws InterruptedException {

        lFore = new Motor("leftFore");
        rFore = new Motor("rightFore");
        rFore.setReverse(true);
        lBack = new Motor("leftBack");
        rBack = new Motor("rightBack");
        rBack.setReverse(true);

        waitForStart();

        lBack.runToPosition(1000, 0.5);
        lFore.runToPosition(1000, 0.5);
        rBack.runToPosition(1000, 0.5);
        rFore.runToPosition(1000, 0.5);

        while(opModeIsActive() &&( !lBack.isThere() || !lFore.isThere() || !rBack.isThere() || !rFore.isThere())){
            idle();
            Log.i("TOG lBack", lBack.returnCurrentState());
            Log.i("TOG rBack", rBack.returnCurrentState());
            Log.i("TOG lFore", lFore.returnCurrentState());
            Log.i("TOG rFore", rFore.returnCurrentState());
        }

        lBack.stop();
        lFore.stop();
        rBack.stop();
        rFore.stop();

    }
}
