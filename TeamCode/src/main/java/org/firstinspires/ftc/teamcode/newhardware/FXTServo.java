package org.firstinspires.ftc.teamcode.newhardware;

import org.firstinspires.ftc.teamcode.RC;

import com.qualcomm.robotcore.hardware.Servo;

import java.util.HashMap;

/**
 * Created by Nirzvi on 15-08-21.
 */
public class FXTServo implements FXTDevice {

    private Servo s;
    public double currentPos = 0.0;

    protected HashMap<String, Double> pos = new HashMap<String, Double>();

    public FXTServo(Servo s) {
        this.s = s;
    }
    public FXTServo(String address) {
        this((Servo) RC.h.get(address));
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

    public void goTo(double position) {
        setPosition(position);
    }//goTo

    private double translate (int angle) {
        return angle / 180.0;
    }

    public void go(){
        setPosition(currentPos);
    }

    public double getPosition() {
        synchronized (s) {
            return s.getPosition();
        }//synchronized
    }

    public void setPosition(double pos) {

        if(pos > 1) pos = 1;
        if(pos < 0) pos = 0;

        currentPos = pos;
        synchronized (s) {
            s.setPosition(pos);
        }//synchronized

    }//setPosition

    public void add(double increment) {
        setPosition(currentPos + increment);
    }

    public void subtract(double decrement) {
        setPosition(currentPos - decrement);
    }

}//FXTServo
