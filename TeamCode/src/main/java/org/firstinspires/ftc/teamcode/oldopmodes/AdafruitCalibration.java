package org.firstinspires.ftc.teamcode.oldopmodes;

import org.firstinspires.ftc.teamcode.newhardware.FXTSensors.AdafruitIMU;
import org.firstinspires.ftc.teamcode.opmodesupport.DoNotRegister;
import org.firstinspires.ftc.teamcode.opmodesupport.TeleOpMode;

/**
 * Created by User on 8/27/2015.
 */
@DoNotRegister
public class AdafruitCalibration extends TeleOpMode {

    AdafruitIMU adafruit;
    public void initialize() {
        AdafruitIMU.dataLogCalibrationValues("adafruit", AdafruitIMU.OPERATION_MODE_COMPASS);
    }

    @Override
    public void loopOpMode() {

    }
}
