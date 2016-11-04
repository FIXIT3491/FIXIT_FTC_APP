package org.firstinspires.ftc.teamcode.gamecode;

import org.firstinspires.ftc.teamcode.RC;
import org.firstinspires.ftc.teamcode.newhardware.FXTSensors.FXTVoltageSensor;
import org.firstinspires.ftc.teamcode.newhardware.Motor;
import org.firstinspires.ftc.teamcode.opmodesupport.TeleOpMode;
import org.firstinspires.ftc.teamcode.robots.Lily;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by FIXIT on 8/19/2015.
 */
@Autonomous
@Disabled
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
        testing.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODERS);
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

        dataLogData(testing.getCurrentPosition() + "\n");
        RC.t.addData("Speed", (int) rpm + "RPM");
        RC.t.addData("Voltage", volts.returnValue() + "V");
        lastEnc = testing.getCurrentPosition();
        lastTime = System.currentTimeMillis();
    }
}
