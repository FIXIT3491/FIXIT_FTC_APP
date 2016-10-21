package org.firstinspires.ftc.teamcode.gamecode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by User on 8/25/2015.
 */
public class ServoOp extends OpMode {

    Servo gate;
    double speed = 0.0;
    boolean pressed = false;
    boolean pressed2 = false;
    @Override
    public void init() {
        gate = hardwareMap.servo.get("test");
        gate.setDirection(Servo.Direction.REVERSE);
    }

    @Override
    public void loop() {
        if(gamepad1.a && !pressed){
            speed += 0.1;
            pressed = true;
        } else if (!gamepad1.a) {
            pressed = false;
        }

        if(gamepad1.b && !pressed){
            speed -= 0.1;
            pressed2 = true;
        } else if (!gamepad1.b) {
            pressed2 = false;
        }

        if (speed >= 1.0){
            gate.setDirection(Servo.Direction.FORWARD);
            speed = 0.0;
        }

        if(speed < 0)
        gate.setPosition(speed);
        telemetry.addData("Pos", speed);
        telemetry.addData("d", gate.getPosition());

    }
}
