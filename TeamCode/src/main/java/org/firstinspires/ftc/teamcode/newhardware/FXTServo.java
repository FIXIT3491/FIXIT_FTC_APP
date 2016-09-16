package org.firstinspires.ftc.teamcode.newhardware;

import org.firstinspires.ftc.teamcode.RC;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;

import java.util.HashMap;

/**
 * Created by Nirzvi on 15-08-21.
 */
public class FXTServo extends Servo implements FXTDevice {

    public double currentPos = 0.0;

    protected HashMap<String, Double> pos = new HashMap<String, Double>();

    public FXTServo(Servo s) {
        super(s.getController(), s.getPortNumber());
    }

    public FXTServo(ServoController sControl, int portNumber) {
        super(sControl, portNumber);
    }

    public FXTServo(HardwareMap hardware, String address) {
        this(hardware.servo.get(address));
    }

    public FXTServo(String address) {
        this(RC.h, address);
    }

    public void addPos (String name, double position) {
        pos.put(name, position);
    }

    public void removePos(String name) {
        pos.remove(name);
    }

    public void goToPos(String name) {
        setPosition(pos.get(name));
    }

    public void goTo(int position) {
        super.setPosition(position);
    }

    private double translate (int angle) {
        return angle / 180.0;
    }

    public void go(){
        setPosition(currentPos);
    }

    public void setPosition(double pos) {

        if(pos > 1) pos = 1;
        if(pos < 0) pos = 0;

        currentPos = pos;
        super.setPosition(pos);
    }//setPosition

    public void add(double increment) {
        setPosition(currentPos + increment);
    }

    public void subtract(double decrement) {
        setPosition(currentPos - decrement);
    }

    @Override
    public void check() {
        //NOTHING!
    }

}//FXTServo
