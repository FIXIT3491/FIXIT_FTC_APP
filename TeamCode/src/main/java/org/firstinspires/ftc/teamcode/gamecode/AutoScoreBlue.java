package org.firstinspires.ftc.teamcode.gamecode;

import android.util.Log;

import org.firstinspires.ftc.teamcode.newhardware.FXTSensors.TrackBall;
import org.firstinspires.ftc.teamcode.opmodesupport.AutoOpMode;
import org.firstinspires.ftc.teamcode.robots.Fermion;

/**
 * Created by Windows on 2017-04-14.
 */

public class AutoScoreBlue extends AutoOpMode {
    @Override
    public void runOp() throws InterruptedException {
        Fermion f = new Fermion(true);

        waitForStart();
        f.capRelease.goToPos("start");

        f.mouse.addAbsoluteCoordinateRunnable(f.imu);

        f.absoluteTrack(new TrackBall.Point(0, 100), 0.3, false);

        f.absoluteTrack(new TrackBall.Point(1220, 1093), 0.6, true);
        Log.i("AbsoluteInfo", f.mouse.getAbsoluteCoord() + "");
    }
}
