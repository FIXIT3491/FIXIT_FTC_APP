package org.firstinspires.ftc.teamcode.newhardware;

import org.firstinspires.ftc.teamcode.gamecode.ServoDebug;

import java.util.HashMap;

/**
 * Created by FIX IT on 8/19/2015
 * This class allows continuous rotation servos to be used like a motor rather than a servo.
 * @see ServoDebug
 * To find the zero position
 */
public class ContinuousServo extends FXTServo implements Timeable {

    /** Local Fields*/

    /**
     * Stores the zero (stopped) position of the servo. Changes servo to servo but
     * many are 0.55
     */
    public double zeroPosition = 0.53;

    /**
     * Allow the user to have predefined motions to make calling the servo simpler
     */
    private HashMap<String, Integer> motions = new HashMap<String, Integer>();


    private long targetTime = -1;

    /**Constructors*/

    /**
     * Simplest constructor accepts the Tele-Op mode hardwaremap and the name in the config file
     * @param address The name of the servo in the config file
     */
    public ContinuousServo (String address) {
        super(address);
    }//ContinuousServo

    /**
     * This adds a pre-set motion for the servo.
     * @param name How you will access the motion
     * @param time Time you want it to run. If time is negative the servo will spin backwards
     */
    @Deprecated
    public void addMotion(String name, int time) {
        motions.put(name, time);
    }//addMotion

    /**
     * This removes a pre-set motion for the servo.
     *
     * @param name How you will access the motion
     */
    @Deprecated
    public void removeMotion(String name) {
        motions.remove(name);
    }//removeMotion

    public void useMotion (String name) {
        int time = motions.get(name);
        int direction = (time < 0)? -1 : 1;

        goForThread(direction * 0.1f, Math.abs(time));
    }

    public void goForThread(final double power, final int time) {

        new Thread() {
            public void run() {
                setPower(power);
                long startTime = System.currentTimeMillis();
                while (System.currentTimeMillis() - startTime < time) {}
                ContinuousServo.this.stop();
            }
        }.start();
    }

    /**
     * Gets the current zero of the servo
     *
     * @return the zero position of the servo
     */
    public double getZeroPosition() {
        return zeroPosition;
    }//getZeroPosition

    /**
     * Set the zero position position. The zero position is the value at which a continuous servo does not spin.
     * @param zeroPosition
     */
    public void setZeroPosition(double zeroPosition) {
        this.zeroPosition = zeroPosition;
        stop();
    }//setZeroPosition

    /**
     * Stops the servo's motion
     */
    public void stop(){
        setPosition(zeroPosition);
    }//stop

    public void setPower (double power, int time) {
        setPower(power);
        setTargetTime(time);
    }

    public double getPower(){
        double power = getPosition();
        if(power > zeroPosition){
            power -= zeroPosition;
            power = power / zeroPosition;
        } else if( power < zeroPosition){
            power -= zeroPosition;
            power = power / zeroPosition;
        } else {
            power = 0;
        }
        return power;
    }

    public void setPower(double power) {
        if (power >= 0.05) {
            power *= (1 - zeroPosition);
            power += zeroPosition;
        } else if (power <= -0.05) {
            power = (1 + power) * zeroPosition;
        } else {
            power = zeroPosition;
        }
        setPosition(power);
    }

    @Override
    public void setTargetTime(long newTime) {
        targetTime = System.currentTimeMillis() + newTime;
    }

    @Override
    public boolean timeFin() {
        return targetTime == -1;
    }

    @Override
    public void check() {
        if (System.currentTimeMillis() > targetTime && targetTime != -1) {
            stop();
            targetTime = -1;
        }//if
    }//check
}
