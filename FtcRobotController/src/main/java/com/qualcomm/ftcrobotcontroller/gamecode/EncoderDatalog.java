package com.qualcomm.ftcrobotcontroller.gamecode;

import android.util.Log;

import com.qualcomm.ftcrobotcontroller.RC;
import com.qualcomm.ftcrobotcontroller.newhardware.Motor;
import com.qualcomm.ftcrobotcontroller.opmodesupport.TeleOpMode;

/**
 * Created by Windows on 2016-03-25.
 */
public class EncoderDatalog extends TeleOpMode {

    Motor test;
    int stage = 0;

    @Override
    public void initialize() {
        test = new Motor("motor");
        test.toggleTargetChecking(true);
        RC.t.setDataLogFile("encoderdata", true);
    }

    @Override
    public void loopOpMode() {
        if (stage == 0) {
            test.setTarget(500, 0.4);
            stage++;
        } else if (stage == 1 && test.reachedTarget) {
            test.setTarget(-500, 0.4);

        }

        test.checkTarget();
        Log.i("Data", "" + test.getCurrentPosition() + ", " + test.target + ", " + test.getPower() + "\n");
        RC.t.dataLogData(test.getCurrentPosition() + ", " + test.target + ", " + test.getPower() + "\n");
    }
}
