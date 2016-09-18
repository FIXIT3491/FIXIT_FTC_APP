package org.firstinspires.ftc.teamcode.gamecode;

import android.util.Log;

import org.firstinspires.ftc.teamcode.roboticslibrary.FTCVuforia;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.util.HashMap;

/**
 * Created by Windows on 2016-06-28.
 */
public class VuforiaOp extends OpMode {

    FTCVuforia vuf;
    int stage = 1;

    HashMap<String, double[]> vuforiaData = new HashMap<String, double[]>();

    @Override
    public void init() {
    }

    @Override
    public void loop() {
        vuforiaData = vuf.getVuforiaData();

        if (vuforiaData.size() > 0) {
            if (vuforiaData.containsKey("block")) {
                telemetry.addData("Calc", vuforiaData.get("block")[0]);
            }
        }
    }

    public void stop() {
        super.stop();
        try {
            vuf.destroy();
        } catch (Exception e) {
            Log.i("Vuforia", e.getMessage());
        }
    }
}
