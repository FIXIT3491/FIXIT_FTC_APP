package org.firstinspires.ftc.teamcode.newhardware;

import org.firstinspires.ftc.teamcode.RC;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by User on 11/8/2015.
 */
public class LinearServo extends FXTServo {

    private double position = 1;
    public double min = 0;
    public double max = 1;
    private double length = 2.54;

    public LinearServo(HardwareMap hardware, String address) {
        super(hardware, address);
    }

    public LinearServo(String address) {
        super(RC.h, address);
    }

    public void out(double speed) {
        if (position + speed > max) {
            position = max;
        } else {
            position += speed;
        }//else
        setPosition(position);
    }//out

    public void in(double speed) {
        if (position - speed < min) {
            position = min;
        } else {
            position -= speed;
        }//else
        setPosition(position);
    }//out

    public void goToPosition(double mm) {
        if (mm < length && mm >= 0) {
            setPosition(mm / length);
        }
    }

    public void setRange(double min, double max) {
        this.min = min;
        this.max = max;
    }

    public void setLength(double length) {
        this.length = length;
    }
}
