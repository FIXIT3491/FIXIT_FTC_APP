package com.qualcomm.ftcrobotcontroller.gamecode;

import com.qualcomm.ftcrobotcontroller.newhardware.FXTSensors.AdafruitIMU;
import com.qualcomm.ftcrobotcontroller.opmodesupport.TeleOpMode;
import com.qualcomm.ftcrobotcontroller.robots.Robot;

/**
 * Created by Windows on 2016-02-18.
 */
public class AdafruitOp extends TeleOpMode {

    AdafruitIMU adafruit;
    Robot robot;

    double speed = 0;
    double lastTime = System.currentTimeMillis();
    double distance = 0;
    int stage = 0;

    @Override
    public void initialize() {
        robot = new Robot();
        adafruit = new AdafruitIMU("adafruit", AdafruitIMU.OPERATION_MODE_IMU);
    }//initialize

    @Override
    public void loopOpMode() {

        speed += (System.currentTimeMillis() - lastTime) / 1000.0 * adafruit.getLinearAccelData()[0];
        distance += speed * (System.currentTimeMillis() - lastTime) / 1000.0;
        lastTime = System.currentTimeMillis();

        if (stage == 0 && getMilliSeconds() > 1000) {
            robot.forward(0.5);
            clearTimer();
            stage++;
        } else if (getMilliSeconds() > 1000 && stage == 1) {
            robot.halt();
            clearTimer();
            stage++;
        } else if (getMilliSeconds() > 1000 && stage == 2) {
            robot.backward(0.5);
            clearTimer();
            stage++;
        } else if (getMilliSeconds() > 1000 && stage == 3) {
            robot.halt();
        }//elseif

        if (robot.motorL.getPower() == 0 && robot.motorR.getPower() == 0) {
            adafruit.updateAccelOffsets(adafruit.getAccelData());
        }//if

    }//loopOpMode

}//AdafruitOp
