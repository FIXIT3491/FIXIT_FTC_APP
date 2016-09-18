package org.firstinspires.ftc.teamcode.gamecode;

import org.firstinspires.ftc.teamcode.RC;
import org.firstinspires.ftc.teamcode.newhardware.FXTServo;
import org.firstinspires.ftc.teamcode.opmodesupport.TeleOpMode;

import java.util.HashMap;

/**
 * Created by Windows on 2016-07-07.
 */
public class HeadFollower extends TeleOpMode {

    FXTServo pan;
    FXTServo tilt;

    @Override
    public void initialize() {
        pan = new FXTServo("pan");
        pan.setPosition(0.5);
        tilt = new FXTServo("tilt");
        tilt.setPosition(0.5);

    }

    @Override
    public void loopOpMode() {
        HashMap<String, double[]> datas = null;

        if (datas.containsKey("block")) {
            double x = datas.get("block")[3];
            double y = datas.get("block")[4];
            RC.t.addData("block", x + "," + y);

            if (x < -20) {
                pan.subtract(0.05);
            } else if (x > 20) {
                pan.add(0.05);
            }

            if (y < -20) {
                tilt.subtract(0.05);
            } else if (y > 20) {
                tilt.add(0.05);
            }

        }
    }

    public void stop() {

    }
}
