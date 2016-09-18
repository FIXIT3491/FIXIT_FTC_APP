package org.firstinspires.ftc.teamcode.gamecode;

import org.firstinspires.ftc.teamcode.RC;
import org.firstinspires.ftc.teamcode.newhardware.LinearServo;
import org.firstinspires.ftc.teamcode.opmodesupport.TeleOpMode;

/**
 * Created by Windows on 2016-07-08.
 */
public class LinearTester extends TeleOpMode {
    LinearServo l;
    int stage = 0;

    @Override
    public void initialize() {
        l = new LinearServo("line");
        RC.t.addData(TAG, l.getPosition());
    }

    @Override
    public void loopOpMode() {
        if (joy1.buttonA() && getMilliSeconds() > 1000) {
            l.in(0.05);
            clearTimer();
        }
        if (joy1.buttonY() && getMilliSeconds() > 1000) {
            l.out(0.05);
            clearTimer();
        }
        RC.t.addData("timer", getMilliSeconds());
        RC.t.addData(TAG, l.getPosition());
        if (stage == 0) {
            l.setPosition(1);
            stage++;
        } else if (stage == 1) {
            l.setPosition(0.5);
            stage++;
        } else if (stage == 2) {
            l.setPosition(0.75);
            stage++;
        }

    }
}
