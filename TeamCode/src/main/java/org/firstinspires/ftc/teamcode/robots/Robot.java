package org.firstinspires.ftc.teamcode.robots;

import org.firstinspires.ftc.teamcode.RC;
import org.firstinspires.ftc.teamcode.newhardware.FXTCRServo;
import org.firstinspires.ftc.teamcode.newhardware.FXTDevice;
import org.firstinspires.ftc.teamcode.newhardware.FXTServo;
import org.firstinspires.ftc.teamcode.newhardware.LinearServo;
import org.firstinspires.ftc.teamcode.newhardware.Motor;

import org.xmlpull.v1.XmlPullParser;

import java.util.HashMap;

/**
 * Created by FIXIT on 15-08-18.
 * Default template for all raw.robots. Include basic motion methods.
 */
public class Robot {
    /**
     * Motions of any robot
     */

    public static final int UP = 0;
    public static final int DOWN = 1;
    public static final int LEFT = 2;
    public static final int RIGHT = 3;
    public static final int FORWARD = 4;
    public static final int BACKWARD = 5;
    public static final int STOP = 6;
    public static final int IN = 7;
    public static final int OUT = 8;
    public static final int OPEN = 9;
    public static final int CLOSE = 10;
    /**
     * This is the left drive system motor
     */
    public Motor motorL;
    /**
     * This is the right drive system motor
     */
    public Motor motorR;
    /**
     * The diameter of the wheel in inches. Default is 4 inches
     */
    public int wheelDiameter = 4;

    /**
     * Creates a default robot with a drive system in which one motor must be reversed
     * to make driving forward simpler to understand. the XML config file must call the drive system motors
     * driveR and driveL
     */
    public Robot() {
        motorL = new Motor("driveL");
        motorR = new Motor("driveR");
        this.motorR.setReverse(true);

    }//Robot

    /**
     * Creates a default Robot with the drive system being two motors
     * that can be named whatever
     * @param driveL The left drive system motor
     * @param driveR The right drive system motor
     */
    public Robot(Motor driveL, Motor driveR) {
        this.motorL = driveL;
        this.motorR = driveR;
        this.motorR.setReverse(true);

    }//Robot

    /**
     * Creates a robot based on he name of the two motors in XML config file
     * @param driveL The name of the left drive system motor
     * @param driveR The name of the right drive system motor
     */
    public Robot(String driveL, String driveR) {
        this(new Motor(driveL), new Motor(driveR));
    }//Robot

    public static HashMap<String, FXTDevice> parseRobotXML (int fileId) {

        String lastAddedDevice = "";
        XmlPullParser xpp = RC.c().getResources().getXml(fileId);
        HashMap<String, FXTDevice> devices = new HashMap<String, FXTDevice>();

        try {
            while (xpp.next() != XmlPullParser.END_DOCUMENT) {
                if (xpp.getEventType() == XmlPullParser.START_TAG) {
                    String tagName = xpp.getName();
                    String name = xpp.getAttributeValue(null, "name");

                    if (tagName.equals("Motor")) {
                        Motor m = new Motor(name);

                        if (xpp.getAttributeValue(null, "reversed") != null) {
                            m.setReverse(true);
                        }//if

//                        if (xpp.getAttributeValue(null, "target").equals("checkTimer")) {
//                            m.toggleChecking(true);
//                        } else if (xpp.getAttributeValue(null, "target").equals("fix")) {
//                            m.toggleTargetFixing(true);
//                        }//elseif

                        devices.put(name, m);
                        lastAddedDevice = name;

                    } else if (tagName.contains("Servo")) {

                        FXTServo s;
                        FXTCRServo cs;

                        if (tagName.equals("ContServo")) {
                            cs = new FXTCRServo(name);

                            double zero = Double.parseDouble(xpp.getAttributeValue(null, "zero"));

                            devices.put(name, cs);
                        } else if (tagName.equals("LinearServo")) {
                            s = new LinearServo(name);
                            devices.put(name, s);
                        } else {
                            s = new FXTServo(name);
                            devices.put(name, s);
                        }//else

                        lastAddedDevice = name;

                    } else if (tagName.contains("Pos")) {

                        ((FXTServo) devices.get(lastAddedDevice)).addPos(name, Double.parseDouble(xpp.getAttributeValue(null, "val")));

                    }//elseif
                } else if (xpp.getEventType() == XmlPullParser.END_TAG) {

                    if (xpp.getName().contains("Servo")) {
                        String defaultPos = xpp.getAttributeValue(null, "val");

                        try {
                            ((FXTServo) devices.get(lastAddedDevice)).setPosition(Double.parseDouble(defaultPos));
                        } catch (Exception e) {
                            ((FXTServo) devices.get(lastAddedDevice)).goToPos(defaultPos);
                        }//catch
                    }//if
                }//elseif
            }
        } catch (Exception e) {
            e.printStackTrace();
        }//catch

        return devices;
    }//parseRobotXML

    /**
     * Effectively pauses the thread without risk of exception
     *
     * @param time The duration the robot waits in milliseconds
     */
    public static void wait(int time) {

        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }//wait

    public boolean allReady() {
        return motorL.isTimeFin() && motorR.isTimeFin();
    }//allReady

    public void checkAllSystems() {
        motorL.updateTimer();
        motorR.updateTimer();
    }//checkAllSystems


    //MOVE METHODS

    /**
     * Powers the left side of the robot
     * @param speed The speed at which the motor is powered value between -1.0 and +1.0
     */
    public void driveL(double speed) {
        motorL.setPower(speed);
    }//driveL

    /**
     * Powers the right side of the robot
     * @param speed The speed at which the motor is powered value between -1.0 and +1.0
     */
    public void driveR(double speed) {
        motorR.setPower(speed);
    }//driveR

    /**
     * Drives the robot forward. Does not stop the robot after execution.
     * @param speed The speed at which the motors turn value between 0.0 and 1.0
     */
    public void forward(double speed) {

        motorL.toggleChecking(false);
        motorR.toggleChecking(false);

        motorL.setPower(speed);
        motorR.setPower(speed);

    }//forward

    /**
     * The method for driving forward. Stops the robot at the end of execution.
     * @param speed The speed at which the motors turn value between 0.0 and 1.0
     * @param time The duration the motors are on
     */
    public void forward(double speed, int time) {
        motorL.setPower(speed);
        motorR.setPower(speed);
        wait(time);
        halt();
    }//forward

    /**
     * Drives the robot backward. Does not stop the robot after execution.
     * @param speed The speed at which the motors turn value between 0.0 and 1.0
     */
    public void backward(double speed) {

        motorL.toggleChecking(false);
        motorR.toggleChecking(false);

        motorL.setPower(-speed);
        motorR.setPower(-speed);

    }//backward

    /**
     * The method for driving backward. Stops the robot at the end of execution.
     * @param speed The speed at which the motors turn value between 0.0 and 1.0
     * @param time The duration the motors are on
     */
    public void backward(double speed, int time) {
        motorL.setPower(-speed);
        motorR.setPower(-speed);
        wait(time);
        halt();
    }//backward

    /**
     * Drive the robot forward for a certain distance using encoders
     * @param mm The distance in millimetres for the robot to drive
     * @param speed The speed at which the motors turn value between 0.0 and 100
     *              If speed > 1 speed will be modified to match the parameters of the Motor
     */
    public void forwardDistance(int mm, double speed) {

        motorL.toggleChecking(true);
        motorR.toggleChecking(true);

        mm *= 1120 / (wheelDiameter * 25.4 * Math.PI); //convert mm to tiks

        motorL.setTargetAndPower(mm, speed);
        motorR.setTargetAndPower(mm, speed);

    }//forwardDistance

    /**
     * Drive the robot backward for a certain distance using encoders
     * @param mm The distance in millimetres for the robot to drive
     * @param speed The speed at which the motors turn value between 0.0 and 100
     *              If speed > 1 speed will be modified to match the parameters of the Motor
     */
    public void backwardDistance(int mm, double speed) {

        motorL.toggleChecking(true);
        motorR.toggleChecking(true);

        mm *= 1120 / (wheelDiameter * 25.4 * Math.PI); //convert mm to tiks

        motorL.setTargetAndPower(-mm, speed); //target checking corrects motor direction
        motorR.setTargetAndPower(-mm, speed);

    }//backwardDistance

    /**
     * Begins turning the robot towards the left
     * @param speed The speed at which the motors turn value between 0.0 and 1.0
     */
     public void turnR (double speed) {

         motorL.setPower(speed);
         motorR.setPower(-speed);

    }//turnR

    /**
     * Begins turning the robot towards the right
     * @param speed The speed at which the motors turn value between 0.0 and 1.0
     */
    public void turnL (double speed) {

        motorL.setPower(-speed);
        motorR.setPower(speed);

    }//turnL

    public void turnR(double speed, int time){
        motorL.setPower(speed);
        motorR.setPower(-speed);
        wait(time);
        halt();
    }

    public void turnL(double speed, int time) {
        motorL.setPower(-speed);
        motorR.setPower(speed);
        wait(time);
        halt();
    }//turnL

    public void encTurnL(int tik, double speed) {

        motorL.setTargetAndPower(-tik, -speed);
        motorR.setTargetAndPower(tik, speed);

    }//encTurnL

    public void encTurnR(int tik, double speed) {

        motorL.setTargetAndPower(tik, speed);
        motorR.setTargetAndPower(-tik, -speed);

    }//encTurnR

    /**
     * Stops the robot
     */
    public void halt() {
        motorL.stop();
        motorR.stop();
    }//halt

    /**
     * Stops the robot then waits for a specified time
     * @param time The duration the robot waits
     */
    public void halt(int time) {
        motorL.stop();
        motorR.stop();
        wait(time);
    }//halt


}//Robot
