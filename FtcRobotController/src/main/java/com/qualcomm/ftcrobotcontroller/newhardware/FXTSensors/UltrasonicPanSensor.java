package com.qualcomm.ftcrobotcontroller.newhardware.FXTSensors;

import com.qualcomm.ftcrobotcontroller.newhardware.ContinuousServo;
import com.qualcomm.ftcrobotcontroller.newhardware.FXTServo;

/**
 * Created by FIXIT on 16-08-02.
 */
public class UltrasonicPanSensor {

    private DigitalUltrasonicSensor ping;
    private FXTServo rotate;
    private double[] distmap;
    private int sampleFreq;

    public UltrasonicPanSensor(String pingEcho, String pingTrig, String rotAddr, int sampleFreq) {
        ping = new DigitalUltrasonicSensor(pingEcho, pingTrig);
        rotate = new FXTServo(rotAddr);

        this.sampleFreq = sampleFreq;
        distmap = new double[180 / sampleFreq];
    }//UltrasonicPanSensor

    public void pulse() {
        ping.pulse();
    }//pulse

    public void pan() {
        double newTarget = rotate.getPosition() + sampleFreq / 180;

        distmap[(int) (rotate.getPosition() * 180 / sampleFreq)] = ping.returnValue();
        rotate.setPosition(newTarget);
    }//pan

    public double[] getDistmap() {
        return distmap;
    }//getDistmap

}//UltrasonicPanSensor
