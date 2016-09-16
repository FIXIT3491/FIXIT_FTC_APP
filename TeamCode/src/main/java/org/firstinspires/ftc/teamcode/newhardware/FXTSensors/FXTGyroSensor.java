package org.firstinspires.ftc.teamcode.newhardware.FXTSensors;

import org.firstinspires.ftc.teamcode.RC;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by FIXIT on 15-08-23.
 */
public class FXTGyroSensor extends FXTSensor {

    GyroSensor gyro;

    public FXTGyroSensor(String address) {
        gyro = RC.h.gyroSensor.get(address);
        sensorType = FTC_GYRO;
        sensorName = address;
    }

    @Override
    public String toString() {
        return "" + getValue();
    }

    @Override
    public String getName() {
        return "Gyro";
    }

    public double returnValue() {
        return gyro.getRotation();

    }

    @Override
    public GyroSensor getHardwareSensor() {
        return gyro;
    }

}
