package org.firstinspires.ftc.teamcode.newhardware.FXTSensors;

import org.firstinspires.ftc.teamcode.RC;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

/**
 * Created by FIXIT on 15-08-23.
 */
public class FXTUltraSonicSensor extends FXTSensor {

    UltrasonicSensor ultrasonic;

    public FXTUltraSonicSensor(String address) {
        ultrasonic = RC.h.ultrasonicSensor.get(address);
        sensorType = FTC_ULTRASONIC;
    }

    @Override
    public String toString() {
        return "" + getValue();
    }

    public double returnValue() {

        return ultrasonic.getUltrasonicLevel();

    }

    @Override
    public UltrasonicSensor getHardwareSensor() {
        return ultrasonic;
    }

}
