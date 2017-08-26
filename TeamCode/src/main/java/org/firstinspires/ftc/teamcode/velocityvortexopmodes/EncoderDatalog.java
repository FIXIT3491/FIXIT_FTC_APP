package org.firstinspires.ftc.teamcode.velocityvortexopmodes;

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
        RC.t.setDataLogFile("encoderdata", true);
    }

    @Override
    public void loopOpMode() {
        if (stage == 0) {
            test.setTargetAndPower(500, 0.4);
            stage++;
        } else if (stage == 1 && test.isTimeFin()) {
            test.setTargetAndPower(-500, 0.4);

        }

        Log.i("Data", "" + test.getBaseCurrentPosition() + ", " + test.getTarget() + ", " + test.getPower() + "\n");
        RC.t.dataLog(test.getBaseCurrentPosition() + ", " + test.getTarget() + ", " + test.getPower() + "\n");
    }
}
