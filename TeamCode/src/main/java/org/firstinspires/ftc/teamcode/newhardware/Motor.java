package org.firstinspires.ftc.teamcode.newhardware;


import org.firstinspires.ftc.teamcode.RC;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by FIXIT on 15-08-18.
 * This class take the basic functionality of DcMotor but adds some methods to it
 */
public class Motor extends DcMotor implements FXTDevice, Timeable {

    public static final double CHEC_TAR_PROP_CONST = 4000.0;
    public static final double CHEC_TAR_FIX_PROP_CONST = 500.0;
    public static final double TAR_ACC_CONST = 5.0;

    public double commandedSpeed = 0;

    public boolean targetCheck = false;
    public boolean reachedTarget = true;
    public boolean isFixing = false;

    public int target = getCurrentPosition();
    public int beginningPosition = 0;
    private long targetTime = -1;

    public double minSpeed = 0.09;

    public int accuracy = 0;

    /**
     * Constructors
     */

    /**
     * Initializes a new Motor based on a pre-existing DcMotor
     * @param motor a pre-existing DcMotor
     * @see DcMotor
     */

    public Motor (DcMotor motor) {
        super(motor.getController(), motor.getPortNumber());
        this.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        beginningPosition = getCurrentPosition();
    }//Motor

    /**
     * Initializes a new Motor based on the motor controller and port
     *
     * @param dcMotorController a pre-existing DcMotorController
     * @param portNumber the port the motor is in either 1 or 2
     * @see DcMotor
     */

    public Motor (DcMotorController dcMotorController, int portNumber) {
        super(dcMotorController, portNumber);
        this.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        beginningPosition = getCurrentPosition();
    }//Motor

    /**
     * Initializes a new Motor from the OpMode HardwareMap and uses the key determined by the
     * XML config file. Then it uses this DcMotor to call the first constructor
     *
     * @param hardware The OpMode HardwareMap.
     * @param address the port the motor is in either 1 or 2
     * @see com.qualcomm.robotcore.hardware.DcMotor
     */

    public Motor (HardwareMap hardware, String address) {
        this(hardware.dcMotor.get(address));
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
        if (reverse) {
            setDirection(Direction.REVERSE);
        } else {
            setDirection(Direction.FORWARD);
        }//else
    }//setReverse

    /**
     * Stops the motor
     */
    public void stop() {
        setPower(0);
    }//stop

    //PRECISION CONTROL

    public void toggleTargetChecking (boolean check) {
        this.targetCheck = check;

        if (check) {
            this.target = getCurrentPosition();
        }//if
    }//toggleTargetChecking


    public void toggleTargetFixing(boolean fix) {
        isFixing = fix;
        if (fix) {
            toggleTargetChecking(true);
        }//if
    }//toggleTargetFixing

    public boolean isTargetChecking() {
        return this.targetCheck;
    }//isTargetChecking

    public boolean finished() {
        if (isTargetChecking()) {
            return reachedTarget;
        } else {
            return targetTime == -1;
        }//else
    }//finished

    public void setTarget(int target) {
        this.target = target + getCurrentPosition();
        this.reachedTarget = false;
        accuracy = (int) (TAR_ACC_CONST * commandedSpeed);

        toggleTargetChecking(true);
    }//setTarget


    public void setTarget(int target, double speed) {
        setTarget(target);
        setPower(Math.abs(speed));
    }//setTarget


    public void checkTarget() {

        if (isTargetChecking() && !reachedTarget) {

            if ((getPower() > 0 && getCurrentPosition() > target)
                    || (getPower() < 0 && getCurrentPosition() < target)) {
                stop();
                reachedTarget = true;
            } else {

                //proportional control system (further away we are from the target position, faster we go)
                double multi = Math.abs(getCurrentPosition() - this.target) / (CHEC_TAR_PROP_CONST * Math.abs(commandedSpeed));

                //makes sure we're not speeding past the commanded speed
                //and we're going in the right direction
                multi = Math.min(multi, 1) * ((getCurrentPosition() < this.target)? 1 : -1);

                setPowerSuper(commandedSpeed * multi);
            }//else
        }//if
    }//checkTarget

    //suspected battery drainer...
    public void checkTargetWithFixing() {

        if (isTargetChecking()) {

            //if we're within acceptable range of our target, don't move
            if (getCurrentPosition() < this.target + accuracy && getCurrentPosition() > this.target - accuracy) {

                setPowerSuper(0);
                reachedTarget = true;

            } else {

                double multi = Math.abs(getCurrentPosition() - this.target) / CHEC_TAR_FIX_PROP_CONST;

                if (getCurrentPosition() > this.target + accuracy) {
                    multi *= -1;
                }//if

                setPowerSuper(commandedSpeed * multi);
            }//else
        }//if
    }//checkTargetWithFixing


    //SET MOTOR POWER

    @Override
    public void setPower(double power) {
        setPowerSuper(power);
        this.commandedSpeed = power;
    }//setPower

    //calls the default DcMotor method (doesn't affect commandedSpeed)
    public void setPowerSuper(double power) {

        if (power > 1) {
            power = 1;
        } else if (power < -1) {
            power = -1;
        } else if (power > -minSpeed && power < 0) {
            power = -minSpeed;
        } else if (power < -minSpeed && power > 0) {
            power = minSpeed;
        }//elseif

        super.setPower(power);
    }//setPowerSuper


    //LOGGING

    public String returnCurrentState() {
        return "Current Pos: " + getCurrentPosition() +
                ", Speed: " + getPower() +
                ", Commanded Speed: " + commandedSpeed +
                ", Power: " + getPower() +
                ", Target: " + target;
    }//returnCurrentState


    //INHERITED METHODS

    @Override
    public void check() {
        if (isTargetChecking()) {

            if (!isFixing) {
                checkTarget();
            } else {
                checkTargetWithFixing();
            }//else

        } else {

            if (System.currentTimeMillis() > targetTime && targetTime != -1) {
                stop();
                targetTime = -1;
            }//if

        }//else
    }//run

    @Override
    public void setTargetTime(long newTime) {
        targetTime = System.currentTimeMillis() + newTime;
    }//setTargetTime

    @Override
    public boolean timeFin() {
        return targetTime == -1;
    }

}//Motor
