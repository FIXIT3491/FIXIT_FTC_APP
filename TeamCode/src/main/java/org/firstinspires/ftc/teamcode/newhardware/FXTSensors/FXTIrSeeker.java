package org.firstinspires.ftc.teamcode.newhardware.FXTSensors;

import org.firstinspires.ftc.teamcode.RC;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;

/**
 * Created by FIXIT on 15-08-23.
 */
public class FXTIrSeeker extends FXTSensor {

    IrSeekerSensor irSeeker;

    public FXTIrSeeker(String address) {
        irSeeker = RC.h.irSeekerSensor.get(address);
        super.sensorType = FTC_IR;
        sensorName = address;
        storedValues = new double[2];
        oneValue = false;
    }

    @Override
    public String toString() {
        return "" + getValues()[0];
    }

    @Override
    public String getName() {
        return "IR";
    }

    public double[] returnValues() {

        return new double[] {irSeeker.getAngle(), irSeeker.getStrength()};
    }

    @Override
    public IrSeekerSensor getHardwareSensor() {
        return irSeeker;
    }

}
