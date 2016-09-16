package com.qualcomm.ftcrobotcontroller.gamecode;

import com.qualcomm.ftcrobotcontroller.RC;
import com.qualcomm.ftcrobotcontroller.newhardware.FXTSensors.FXTVoltageSensor;
import com.qualcomm.ftcrobotcontroller.newhardware.Motor;
import com.qualcomm.ftcrobotcontroller.opmodesupport.TeleOpMode;
import com.qualcomm.ftcrobotcontroller.robots.Lily;
import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Created by FIXIT on 8/19/2015.
 */
public class EncoderTest extends TeleOpMode {

    Motor testing;
    FXTVoltageSensor volts;
    int stage = 0;
    Lily lily;
    long lastTime = 0;
    int lastEnc = 0;
    double rpm = 0;

    @Override
    public void initialize() {
        testing = new Motor("tester");
        volts = new FXTVoltageSensor("Motor Controller 1");
        setDataLogFile("motor.txt", true);
        testing.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
    }

    @Override
    public void loopOpMode() {

        if (getMilliSeconds() < 1000) {
            testing.setPower(1);
            rpm = (testing.getCurrentPosition() - lastEnc) / 1120.0 / ((System.currentTimeMillis() - lastTime) / 60000.0);
        } else if (getMilliSeconds() < 4000) {
            testing.setPower(0.1);
            rpm = (testing.getCurrentPosition() - lastEnc) / 1120.0 / ((System.currentTimeMillis() - lastTime) / 60000.0);
        } else if (getMilliSeconds() < 8000) {
            testing.setPower(-0.1);
            rpm = (testing.getCurrentPosition() - lastEnc) / 1120.0 / ((System.currentTimeMillis() - lastTime) / 60000.0);
        } else if (getMilliSeconds() < 12000) {
            testing.setPower(-1);
            rpm = (testing.getCurrentPosition() - lastEnc) / 1120.0 / ((System.currentTimeMillis() - lastTime) / 60000.0);
        }
        RC.t.dataLogData(testing.getCurrentPosition() + "\n");
        RC.t.addData("Speed", (int) rpm + "RPM");
        RC.t.addData("Voltage", volts.returnValue() + "V");
        lastEnc = testing.getCurrentPosition();
        lastTime = System.currentTimeMillis();
    }
}
