package com.qualcomm.ftcrobotcontroller.oldopmodes;

import com.qualcomm.ftcrobotcontroller.RC;
import com.qualcomm.ftcrobotcontroller.newhardware.FXTSensors.FXTColourSensor;
import com.qualcomm.ftcrobotcontroller.newhardware.FXTSensors.FXTLightSensor;
import com.qualcomm.ftcrobotcontroller.opmodesupport.DoNotRegister;
import com.qualcomm.ftcrobotcontroller.opmodesupport.TeleOpMode;

/**
 * Created by FIXIT on 15-11-08.
 */
@DoNotRegister
public class ColourTest extends TeleOpMode {

    FXTColourSensor colour;

    @Override
    public void initialize() {
        RC.setOpMode(this);
        colour = new FXTColourSensor(RC.h, "colour");

    }

    @Override
    public void loopOpMode() {
        telemetry.addData("ColourData", colour.toString());

    }

}
