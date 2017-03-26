package org.firstinspires.ftc.teamcode.opmodesupport;

import com.qualcomm.robotcore.hardware.Gamepad;

import java.util.Arrays;

/**
 * Created by FIXIT on 16-10-25.
 */
public class Joystick implements Cloneable {

    private long[] lastTimeCalled = new long[14];
    private final static int TAP_REFRESH_PERIOD = 500;

    public Gamepad gamepad;

    public Joystick() {
        Arrays.fill(lastTimeCalled, 0);
    }//Joystick

    public void update(Gamepad gamepad) {
        this.gamepad = gamepad;
    }

    public boolean buttonA() {
        return gamepad.a;
    }

    public boolean buttonB() {
        return gamepad.b;
    }

    public boolean buttonX() {
        return gamepad.x;
    }

    public boolean buttonY() {
        return gamepad.y;
    }


    public boolean buttonUp() {
        return gamepad.dpad_up;
    }

    public boolean buttonDown() {
        return gamepad.dpad_down;
    }

    public boolean buttonRight() {
        return gamepad.dpad_right;
    }

    public boolean buttonLeft() {
        return gamepad.dpad_left;
    }

    public boolean leftBumper() {
        return gamepad.left_bumper;
    }

    public boolean rightBumper() {
        return gamepad.right_bumper;
    }

    public boolean leftTrigger(){
        return (gamepad.left_trigger > 0.1);
    }

    public boolean rightTrigger(){
        return (gamepad.right_trigger > 0.1);
    }

    public boolean buttonStart() {
        return gamepad.start;
    }

    public boolean buttonBack() {
        return gamepad.back;
    }

    /*
    TAP Methods
     */

    public boolean tapA() {
        if (gamepad.a && System.currentTimeMillis() - lastTimeCalled[0] < TAP_REFRESH_PERIOD) {
            return false;
        } else if (gamepad.a) {
            lastTimeCalled[0] = System.currentTimeMillis();
        }//else

        return gamepad.a;
    }//tapA

    public boolean tapB() {
        if (gamepad.b && System.currentTimeMillis() - lastTimeCalled[1] < TAP_REFRESH_PERIOD) {
            return false;
        } else if (gamepad.b) {
            lastTimeCalled[1] = System.currentTimeMillis();
        }//else

        return gamepad.b;
    }//tapB

    public boolean tapX() {
        if (gamepad.x && System.currentTimeMillis() - lastTimeCalled[2] < TAP_REFRESH_PERIOD) {
            return false;
        } else if (gamepad.x) {
            lastTimeCalled[2] = System.currentTimeMillis();
        }//else

        return gamepad.x;
    }//tapX

    public boolean tapY() {
        if (gamepad.y && System.currentTimeMillis() - lastTimeCalled[3] < TAP_REFRESH_PERIOD) {
            return false;
        } else if (gamepad.y) {
            lastTimeCalled[3] = System.currentTimeMillis();
        }//else

        return gamepad.y;
    }//tapY
    
    public boolean tapUp() {
        if (gamepad.dpad_up && System.currentTimeMillis() - lastTimeCalled[4] < TAP_REFRESH_PERIOD) {
            return false;
        } else if (gamepad.dpad_up) {
            lastTimeCalled[4] = System.currentTimeMillis();
        }//else

        return gamepad.dpad_up;
    }//tapUp

    public boolean tapDown() {
        if (gamepad.dpad_down && System.currentTimeMillis() - lastTimeCalled[5] < TAP_REFRESH_PERIOD) {
            return false;
        } else if (gamepad.dpad_down) {
            lastTimeCalled[5] = System.currentTimeMillis();
        }//else

        return gamepad.dpad_down;
    }//tapDown

    public boolean tapRight() {
        if (gamepad.dpad_right && System.currentTimeMillis() - lastTimeCalled[6] < TAP_REFRESH_PERIOD) {
            return false;
        } else if (gamepad.dpad_right) {
            lastTimeCalled[6] = System.currentTimeMillis();
        }//else

        return gamepad.dpad_right;
    }//tapRight

    public boolean tapLeft() {
        if (gamepad.dpad_left && System.currentTimeMillis() - lastTimeCalled[7] < TAP_REFRESH_PERIOD) {
            return false;
        } else if (gamepad.dpad_left) {
            lastTimeCalled[7] = System.currentTimeMillis();
        }//else

        return gamepad.dpad_left;
    }//tapLeft

    public boolean tapLeftBumper() {
        if (gamepad.left_bumper && System.currentTimeMillis() - lastTimeCalled[8] < TAP_REFRESH_PERIOD) {
            return false;
        } else if (gamepad.left_bumper) {
            lastTimeCalled[8] = System.currentTimeMillis();
        }//else

        return gamepad.left_bumper;
    }//tapLeftBumper

    public boolean tapRightBumper() {
        if (gamepad.right_bumper && System.currentTimeMillis() - lastTimeCalled[9] < TAP_REFRESH_PERIOD) {
            return false;
        } else if (gamepad.right_bumper) {
            lastTimeCalled[9] = System.currentTimeMillis();
        }//else

        return gamepad.right_bumper;
    }//tapRightBumper

    public boolean tapLeftTrigger(){
        if (gamepad.left_trigger > 0.1 && System.currentTimeMillis() - lastTimeCalled[10] < TAP_REFRESH_PERIOD) {
            return false;
        } else if (gamepad.left_trigger > 0.1) {
            lastTimeCalled[10] = System.currentTimeMillis();
        }//else

        return gamepad.left_trigger > 0.1;
    }//tapLeftTrigger

    public boolean tapRightTrigger(){
        if (gamepad.right_trigger > 0.1 && System.currentTimeMillis() - lastTimeCalled[11] < TAP_REFRESH_PERIOD) {
            return false;
        } else if (gamepad.right_trigger > 0.1) {
            lastTimeCalled[11] = System.currentTimeMillis();
        }//else

        return gamepad.right_trigger > 0.1;
    }//tapRightTrigger

    public boolean tapStart() {
        if (gamepad.start && System.currentTimeMillis() - lastTimeCalled[12] < TAP_REFRESH_PERIOD) {
            return false;
        } else if (gamepad.start) {
            lastTimeCalled[12] = System.currentTimeMillis();
        }//else

        return gamepad.start;
    }//tapStart

    public boolean tapBack() {
        if (gamepad.back && System.currentTimeMillis() - lastTimeCalled[13] < TAP_REFRESH_PERIOD) {
            return false;
        } else if (gamepad.back) {
            lastTimeCalled[13] = System.currentTimeMillis();
        }//else

        return gamepad.back;
    }//tapBack


    public float x1(){
        return (Math.abs(gamepad.left_stick_x) > 0.09) ? gamepad.left_stick_x : 0;
    }

    public float x2(){
        return (Math.abs(gamepad.right_stick_x) > 0.09)? gamepad.right_stick_x : 0;
    }

    public float y1(){
        return (Math.abs(gamepad.left_stick_y) > 0.09)? gamepad.left_stick_y : 0;
    }

    public float y2(){
        return (Math.abs(gamepad.right_stick_y) > 0.09)? gamepad.right_stick_y : 0;
    }

    public Joystick clone() {
        Joystick clone = new Joystick();
        clone.update(gamepad);
        return clone;
    }

}
