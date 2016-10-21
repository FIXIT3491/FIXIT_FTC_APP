package org.firstinspires.ftc.teamcode.gamecode;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.RC;
import org.firstinspires.ftc.teamcode.newhardware.Motor;
import org.firstinspires.ftc.teamcode.opmodesupport.TeleOpMode;

/**
 * Created by Windows on 2016-03-25.
 */
@Disabled
public class EncoderDatalog extends TeleOpMode {

    Motor test;
    int stage = 0;

    @Override
    public void initialize() {
        test = new Motor("motor");
        test.toggleChecking(true);
        RC.t.setDataLogFile("encoderdata", true);
    }

    @Override
    public void loopOpMode() {
        if (stage == 0) {
            test.setTarget(500, 0.4);
            stage++;
        } else if (stage == 1 && test.isFin()) {
            test.setTarget(-500, 0.4);

        }

//        test.checkTarget();
//        Log.i("Data", "" + test.getCurrentPosition() + ", " + test.target + ", " + test.getPower() + "\n");
//        RC.t.dataLogData(test.getCurrentPosition() + ", " + test.target + ", " + test.getPower() + "\n");
    }
}
