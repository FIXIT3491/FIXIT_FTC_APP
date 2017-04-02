package org.firstinspires.ftc.teamcode.newhardware;


import org.firstinspires.ftc.teamcode.RC;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Created by FIXIT on 15-08-18.
 * This class take the basic functionality of DcMotor but adds some methods to it
 */
public class Motor implements FXTDevice {

    private DcMotor m;

    public int beginningPosition = 0;
    private long targetTime = -1;
    private int numTiksPerRev = 1120;

    public double minSpeed = 0.09;
    public int accuracy = 20;
    public double plannedSpeed = 0;

    public enum Type {
        AM20,
        AM40,
        AM60,
        Tetrix,
    }


    /**
     * Constructors
     */

    /**
     * Initializes a new Motor based on a pre-existing DcMotor
     * @param motor a pre-existing DcMotor
     * @see DcMotor
     */

    public Motor (DcMotor motor) {
        this.m = motor;
        m.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }//Motor

    public Motor(String address) {
        this(RC.h.dcMotor.get(address));
    }//Motor

    /*
     * Methods
     */

    /**
     * Changes whether a motor spins forward or reverse based on a positive input value.
     * @param reverse If true the motor is reversed
     */
    public void setReverse (boolean reverse) {
        synchronized (m) {
            if (reverse) {
                m.setDirection(DcMotorSimple.Direction.REVERSE);
            } else {
                m.setDirection(DcMotorSimple.Direction.FORWARD);
            }//else
        }//synchronized
    }//setReverse

    /**
     * Stops the motor
     */
    public void stop() {
        setPower(0);
    }//stop

    //PRECISION CONTROL

    public void toggleChecking(boolean check) {
        if (check) {
            setMode(DcMotor.RunMode.RUN_TO_POSITION);
        } else {
            setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }//else
    }//toggleChecking

    public void setMotorType(Type motor){
        switch (motor){
            case AM20: numTiksPerRev = 560;
                break;
            case AM40: numTiksPerRev = 1120;
                break;
            case AM60: numTiksPerRev = 1680;
                break;
            case Tetrix: numTiksPerRev = 1440;
                break;
        }
    }

    public int getNumTiksPerRev() {
        return numTiksPerRev;
    }

    public void setAbsoluteTarget(int target){

        setTarget(getCurrentPosition() + target - getAbsolutePosition());

    }

    public int getAbsolutePosition(){
        return (getCurrentPosition() - beginningPosition) % numTiksPerRev;
    }

    public void resetEncoders(){
        beginningPosition = getCurrentPosition();
    }

    public void setTimer(int millis, double speed) {
        setPower(speed);
        targetTime = System.currentTimeMillis() + millis;
    }//setTimer

    public void setTarget(int target, double speed) {
        setTarget(target);
        setPower(speed);
    }//setTarget

    //SET MOTOR POWER
    public boolean isBusy() {
        synchronized (m) {
            return m.isBusy();
        }//synchronized
    }//isBusy

    public boolean isFin() {
        return Math.abs(getCurrentPosition() - getM().getTargetPosition()) < accuracy;
    }//finished

    public DcMotor getM() {
        synchronized (m) {
            return m;
        }//synchronized
    }//getM

    public void setTarget(int tik) {
        synchronized (m) {
            m.setTargetPosition(tik + m.getCurrentPosition());
        }//synchronized
    }//setTarget

    public void setRelativeTarget(int tik) {
        setTarget(tik - getCurrentPosition());
    }//setAbsTarget


    public int getCurrentPosition() {
        synchronized (m) {
            return m.getCurrentPosition();
        }//synchronized
    }//getCurrentPosition

    public int getTarget(){
        return m.getTargetPosition();
    }

    public double getPower() {
        synchronized (m) {
            return m.getPower();
        }//synchronized
    }//getPower

    public void setMode(DcMotor.RunMode mode) {
        synchronized (m) {
            m.setMode(mode);
        }//synchronized
    }//setMode

    public void setPower(double power) {

        if (Math.abs(power) > 1) {
            power = 1 * Math.signum(power);
        } else if (Math.abs(power) < minSpeed && Math.abs(power) > 1E-10) {
            power = minSpeed * Math.signum(power);
        } else if(Math.abs(power) < 1E-10){
            power = 0; //prevent round off error
        }//else

        synchronized (m) {
            m.setPower(power);
        }//synchronized
    }//setPowerSuper

    //LOGGING

    public String returnCurrentState() {
        return "Current Pos: " + getCurrentPosition() +
                ", Power: " + getPower() +
                ", Target: " + getM().getTargetPosition();
    }//returnCurrentState

    public void runToPosition(int tiks, double speed){

        m.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        m.setTargetPosition(m.getCurrentPosition() + tiks);
        setPower(speed);
    }

    public void completeRunToPosition(int tiks, double speed) {
        runToPosition(tiks, speed);

        while (isFin()) {
            RC.l.idle();
        }//while

        stop();
    }//completeRunToPosition

    //INHERITED METHODS

    public void check() {

        if (!getM().getMode().equals(DcMotor.RunMode.RUN_TO_POSITION)) {
            if (System.currentTimeMillis() > targetTime && targetTime != -1) {
                stop();
                targetTime = -1;
            }//if
        }//if

    }//run



    public void setPlannedSpeed(double plannedSpeed) {
        this.plannedSpeed = plannedSpeed;
    }//setPlannedSpeed

    public void usePlannedSpeed() {
        setPower(plannedSpeed);
    }//usePlannedSpeed

}//Motor
