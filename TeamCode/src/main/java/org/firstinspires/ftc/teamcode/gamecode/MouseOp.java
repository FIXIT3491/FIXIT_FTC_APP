package org.firstinspires.ftc.teamcode.gamecode;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.newhardware.FXTSensors.Mouse;
import org.firstinspires.ftc.teamcode.opmodesupport.TeleOpMode;

/**
 * Created by Windows on 2016-07-06.
 */
@Disabled
public class MouseOp extends TeleOpMode {
    Mouse m;

    @Override
    public void initialize() {

        new Mouse(1133, 49693) {
            @Override
            public void onNewData(byte[] data) {
                Log.i(TAG, "onNewData: " + "wahoooo");
            }
        };

    }

    @Override
    public void loopOpMode() {


    }

    @Override
    public void stop() {
        super.stop();
        m.CloseTheDevice();
    }
}
