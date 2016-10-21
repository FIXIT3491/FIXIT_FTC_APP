package org.firstinspires.ftc.teamcode.newhardware;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.CRServoImpl;

import org.firstinspires.ftc.teamcode.RC;
import org.firstinspires.ftc.teamcode.resqopmodes.ServoDebug;

/**
 * Created by FIX IT on 8/19/2015
 * This class allows continuous rotation servos to be used like a motor rather than a servo.
 * @see ServoDebug
 * To find the zero position
 */
public class FXTCRServo extends CRServoImpl implements FXTDevice, Timeable {

    long targetTime = -1;

    public FXTCRServo(CRServo cs) {
        super(cs.getController(), cs.getPortNumber());
    }

    public FXTCRServo(String addr) {
        this(RC.h.crservo.get(addr));
    }

    public void stop() {
        setPower(0);
    }

    public void setTimer(long newTime, double speed) {
        setPower(speed);
        targetTime = System.currentTimeMillis() + newTime;
    }//setTimer

    @Override
    public boolean timeFin() {
        return targetTime == -1;
    }

    @Override
    public void check() {
        if (System.currentTimeMillis() > targetTime && targetTime != -1) {
            setPower(0);
            targetTime = -1;
        }//if
    }

}
