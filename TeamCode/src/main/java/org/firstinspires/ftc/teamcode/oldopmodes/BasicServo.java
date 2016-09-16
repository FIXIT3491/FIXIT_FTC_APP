package org.firstinspires.ftc.teamcode.oldopmodes;

import org.firstinspires.ftc.teamcode.opmodesupport.DoNotRegister;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by FIXIT on 16-01-26.
 */
@DoNotRegister
public class BasicServo extends OpMode {

    DcMotor left;
    DcMotor right;
    int stage = 0;

    @Override
    public void init() {

        left = hardwareMap.dcMotor.get("driveL");
        right = hardwareMap.dcMotor.get("driveR");

    }

    @Override
    public void loop() {

        if (stage == 0) {
            left.setPower(-0.4);
            right.setPower(0.4);
        }

    }
}
