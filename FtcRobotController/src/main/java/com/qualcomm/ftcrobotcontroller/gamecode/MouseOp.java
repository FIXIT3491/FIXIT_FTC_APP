package com.qualcomm.ftcrobotcontroller.gamecode;

import android.util.Log;

import com.qualcomm.ftcrobotcontroller.newhardware.FXTSensors.Mouse;
import com.qualcomm.ftcrobotcontroller.opmodesupport.TeleOpMode;

/**
 * Created by Windows on 2016-07-06.
 */
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
