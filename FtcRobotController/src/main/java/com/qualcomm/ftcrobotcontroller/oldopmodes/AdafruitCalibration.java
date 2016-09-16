package com.qualcomm.ftcrobotcontroller.oldopmodes;

import com.qualcomm.ftcrobotcontroller.newhardware.FXTSensors.AdafruitIMU;
import com.qualcomm.ftcrobotcontroller.opmodesupport.DoNotRegister;
import com.qualcomm.ftcrobotcontroller.opmodesupport.TeleOpMode;

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
