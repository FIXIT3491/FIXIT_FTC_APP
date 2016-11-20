package org.firstinspires.ftc.teamcode.newhardware.FXTSensors;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.RC;
import org.opencv.core.Point;

/**
 * Created by FIXIT on 16-10-08.
 */
public class TrackBall {

    DcMotor xEnc;
    DcMotor yEnc;
    private Point lastGivenPoint;

    public TrackBall(String xAddr, String yAddr) {
        xEnc = RC.h.dcMotor.get(xAddr);
        yEnc = RC.h.dcMotor.get(yAddr);

        lastGivenPoint = new Point(0, 0);
    }//TrackBall

    public Point getXY() {
        lastGivenPoint = new Point(xEnc.getCurrentPosition(), yEnc.getCurrentPosition());
        return lastGivenPoint;
    }//Point

    public Point getXYIncrement() {
        Point ret = new Point(xEnc.getCurrentPosition() - lastGivenPoint.x, yEnc.getCurrentPosition() - lastGivenPoint.y);
        getXY();
        return ret;
    }//Point

}
