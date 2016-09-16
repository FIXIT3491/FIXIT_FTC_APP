package org.firstinspires.ftc.teamcode.newhardware.FXTSensors;

import org.firstinspires.ftc.teamcode.RC;
import com.qualcomm.robotcore.hardware.AccelerationSensor;

/**
 * Created by FIXIT on 15-08-23.
 */
public class FXTAccelerationSensor extends FXTSensor {

    AccelerationSensor accel;

    public FXTAccelerationSensor(String address) {
        accel = RC.h.accelerationSensor.get(address);
        sensorType = FTC_ACCEL;
        sensorName = address;
        oneValue = false;
    }

    @Override
    public String toString() {
        double[] values = getValues();

        return "(X: " + values[0] + ", Y" + values[1] + ", Z" + values[2] + ")";
    }

    public double[] returnValues() {
        AccelerationSensor.Acceleration accelValues = accel.getAcceleration();

        double[] values = {accelValues.x,
                            accelValues.y,
                            accelValues.z};

        for (int i = 0; i < values.length; i++) {
            values[i] = (float) SensorUtils.lowPassFilter(0.25, values[i], super.storedValues[i]);
        }

        return values;
    }

    @Override
    public AccelerationSensor getHardwareSensor() {
        return accel;
    }

}
