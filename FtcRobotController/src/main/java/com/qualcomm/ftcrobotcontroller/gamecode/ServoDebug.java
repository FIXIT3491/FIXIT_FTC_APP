package com.qualcomm.ftcrobotcontroller.gamecode;

import com.qualcomm.ftcrobotcontroller.opmodesupport.DoNotRegister;
import com.qualcomm.ftcrobotcontroller.opmodesupport.Name;
import com.qualcomm.ftcrobotcontroller.opmodesupport.TeleOpMode;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by FTC on 23/10/2015.
 */
@DoNotRegister
public class ServoDebug extends TeleOpMode {

    ArrayList<Servo> servos = new ArrayList<Servo>();
    ArrayList<String> names = new ArrayList<String>();
    int testingNum = 0;
    long timer = 0;

    double position = 0;

    @Override
    public void initialize() {

        int counter = 0;

        for (Map.Entry<String, Servo> servo : hardwareMap.servo.entrySet()) {
            names.add(servo.getKey());
            servos.add(servo.getValue());
        }
        position = servos.get(testingNum).getPosition();
    }

    @Override
    public void loopOpMode() {

        telemetry.addData("Instructions", "To switch servo press x or b.");
        telemetry.addData("More Instructions", "To change that servo position press left bumper or trigger");
        telemetry.addData("Changing", names.get(testingNum));
        telemetry.addData("Position", position);

        if(joy1.buttonX() && getMilliSeconds() < 100){
            testingNum--;
            position = servos.get(testingNum).getPosition();
            clearTimer();
        } else if (joy1.buttonB() && getMilliSeconds() > 100){
            testingNum++;
            position = servos.get(testingNum).getPosition();
            clearTimer();
        }//elseif

        testingNum += 1;
        testingNum -= 1;

        testingNum %= servos.size();

        if(joy1.leftTrigger() && position > 0){

            position -= 0.01;
            servos.get(testingNum).setPosition(position);

        } else if(joy1.leftBumper() && position < 1) {

            position += 0.01;
            servos.get(testingNum).setPosition(position);

        }//else if
    }

}
