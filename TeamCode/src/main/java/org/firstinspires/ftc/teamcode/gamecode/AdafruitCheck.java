package org.firstinspires.ftc.teamcode.gamecode;

import org.firstinspires.ftc.teamcode.RC;
import org.firstinspires.ftc.teamcode.newhardware.FXTSensors.AdafruitIMU;
import org.firstinspires.ftc.teamcode.opmodesupport.DoNotRegister;
import org.firstinspires.ftc.teamcode.opmodesupport.TeleOpMode;

/**
 * Created by Owner on 8/31/2015.
 */
@DoNotRegister
public class AdafruitCheck extends TeleOpMode {

    AdafruitIMU boschBNO055;

    @Override
    public void initialize() {
        boschBNO055 = new AdafruitIMU("adafruit", (byte) AdafruitIMU.OPERATION_MODE_IMU);
    }

    @Override
    public void loopOpMode() {
        RC.t.addData("Angle-Heading", boschBNO055.getEulerAngles()[0]);
        RC.t.addData("Angle-Roll", boschBNO055.getEulerAngles()[1]);
        RC.t.addData("Angle-Pitch", boschBNO055.getEulerAngles()[2]);

        RC.t.addData("Accel-X", boschBNO055.getAccelData()[0]);
        RC.t.addData("Accel-Y", boschBNO055.getAccelData()[1]);
        RC.t.addData("Accel-Z", boschBNO055.getAccelData()[2]);

        RC.t.addData("Gyro-X", boschBNO055.getGyroAngles()[0]);
        RC.t.addData("Gyro-Y", boschBNO055.getGyroAngles()[1]);
        RC.t.addData("Gyro-Z", boschBNO055.getGyroAngles()[2]);
    }

}