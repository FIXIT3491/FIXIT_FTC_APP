package org.firstinspires.ftc.teamcode.gamecode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.RC;
import org.firstinspires.ftc.teamcode.newhardware.FXTSensors.FXTLightSensor;
import org.firstinspires.ftc.teamcode.newhardware.FXTServo;
import org.firstinspires.ftc.teamcode.newhardware.LinearServo;
import org.firstinspires.ftc.teamcode.opmodesupport.LinearTeleOpMode;
import org.firstinspires.ftc.teamcode.opmodesupport.TeleOpMode;

/**
 * Created by User on 8/25/2015.
 */
@TeleOp
public class ServoOp extends LinearTeleOpMode {
    FXTServo s;
    FXTServo s2;
    boolean manualMode = false;
    
    double pos = 0;
    double pos2 = 0;

    @Override
    public void initialize() {
        s = new FXTServo("linear");
        s2 = new FXTServo("linear2");
        s.setPosition(0);
        s2.setPosition(0);
        clearTimer();
    }

    @Override
    public void loopOpMode() {
        if(manualMode) {
            if (joy1.leftBumper() && getMilliSeconds() > 300) {
                pos += 0.01;
                clearTimer();
            } else if (joy1.leftTrigger() && getMilliSeconds() > 300) {
                pos -= 0.01;
                clearTimer();
            }
            RC.t.addData("Pos", pos);


            if (joy1.rightBumper() && getMilliSeconds() > 300) {
                pos2 += 0.01;
                clearTimer();
            } else if (joy1.rightTrigger() && getMilliSeconds() > 300) {
                pos2 -= 0.01;
                clearTimer();
            }
            s2.setPosition(pos2);
            s.setPosition(pos);
        }else{
            if (joy1.buttonA()) {
                s.setPosition(0.7);
                s2.setPosition(0.7);
                sleep(1200);
                s.setPosition(0.2);
                s2.setPosition(0.2);
                sleep(3400);
                s.setPosition(0.65);
                s2.setPosition(0.65);
            }
            if (joy1.buttonB()) {
                s.setPosition(0.2);
                s2.setPosition(0.2);
            } else if (joy1.buttonX()) {
                s.setPosition(0.75);
                s2.setPosition(0.75);
            }


        }

        if(joy1.buttonY()) manualMode = !manualMode;

        RC.t.addData("pos1", s.getPosition() );
        RC.t.addData("pos2", s2.getPosition());
        RC.t.addData("Manual", manualMode );

        
    }
}
