package org.firstinspires.ftc.teamcode.oldopmodes;

import org.firstinspires.ftc.teamcode.RC;
import org.firstinspires.ftc.teamcode.newhardware.FXTSensors.FXTColourSensor;
import org.firstinspires.ftc.teamcode.opmodesupport.DoNotRegister;
import org.firstinspires.ftc.teamcode.opmodesupport.TeleOpMode;

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
