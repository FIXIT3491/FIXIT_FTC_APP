package org.firstinspires.ftc.teamcode.newhardware.FXTSensors;

import org.firstinspires.ftc.teamcode.newhardware.FXTServo;
import org.firstinspires.ftc.teamcode.roboticslibrary.TaskHandler;

import java.util.UUID;

/**
 * Created by FIXIT on 2017-02-16.
 */

public class FXTSonar {

    private FXTAnalogUltrasonicSensor ultra;
    private FXTServo pan;

    private volatile double beginPosition = 0;
    private volatile double endPosition = 1;

    private double progress = 0;
    private int panDirection = 1;

    public double[] currentSonarState;

    public FXTSonar(String ultraSonicAddr, String rangeAddr, String servoAddr) {
        ultra = new FXTAnalogUltrasonicSensor(ultraSonicAddr, rangeAddr);
        pan = new FXTServo(servoAddr);
    }//FXTSonar

    public void setServoRange(double beginPosition, double endPosition) {
        this.beginPosition = beginPosition;
        this.endPosition = endPosition;
        this.progress = beginPosition;

        currentSonarState = new double[(int) ((endPosition - beginPosition) * 360)];
    }//setServoRange

    public void start() {
        TaskHandler.addLoopedTask("FXTSonar." + (int) (Math.random() * 100), new Runnable() {
            @Override
            public void run() {
                currentSonarState[(int) ((progress - beginPosition) * 360)] = ultra.getDistance();

                pan.setPosition(progress);

                progress += panDirection * (endPosition - beginPosition) / 100;

                if (progress == endPosition) {
                    panDirection *= -1;
                }//if

            }//run
        }, 5);//PanServoTask
    }//start

}
