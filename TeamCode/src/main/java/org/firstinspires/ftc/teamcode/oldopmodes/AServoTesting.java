package org.firstinspires.ftc.teamcode.oldopmodes;

import org.firstinspires.ftc.teamcode.opmodesupport.DoNotRegister;
import org.firstinspires.ftc.teamcode.opmodesupport.TeleOpMode;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by FIXIT on 9/1/2015
 */
@DoNotRegister
public class AServoTesting extends TeleOpMode {

    Servo test2;
    double pos = 0;
    long time = 0;

    @Override
    public void initialize() {
        test2 = hardwareMap.servo.get("bluezip");
    }

    @Override
    public void loopOpMode() {
//
//        if (joy2.buttonA()) {
//            test2.setPosition(0.1);
//            RC.t.addData("Pos", "0");
//        } else {
//            test2.setPosition(0.76);
//            RC.t.addData("Pos", "1");
//        }

        if (joy1.buttonA() && System.currentTimeMillis() - time > 500) {
            pos += 0.01;
            time = System.currentTimeMillis();
        } else if (joy1.buttonB() && System.currentTimeMillis() - time > 500) {
            pos -= 0.01;
            time = System.currentTimeMillis();
        }

        if (pos > 0 && pos < 1) {
            test2.setPosition(pos);
        }

        telemetry.addData("hu", pos);

    }

}
