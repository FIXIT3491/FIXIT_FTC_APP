package org.firstinspires.ftc.teamcode.newhardware.FXTSensors;

import java.util.ArrayList;

/**
 * Created by FIXIT on 15-10-23.
 */
public abstract class KalmanFilter extends FXTSensor {

    public ArrayList<Double> gaussianMeans = new ArrayList<Double>();
    public ArrayList<Double> gaussianVariances = new ArrayList<Double>();
    public double procMean = 0;
    public double procVariance = 0;

    public KalmanFilter() {

    }

    public void fuseSensorValues() {

        double k = 0;

        k = Math.pow(procVariance, 2) / (Math.pow(procVariance, 2) + Math.pow(gaussianVariances.get(1), 2));

        procMean = procMean + k * (gaussianMeans.get(1) - procMean);

        procVariance = Math.pow(procMean, 2) * (1 - k);
    }

    public abstract void updateEstimate();

}
