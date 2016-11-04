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

    public TrackBall(String xAddr, String yAddr) {
        xEnc = RC.h.dcMotor.get(xAddr);
        yEnc = RC.h.dcMotor.get(yAddr);
    }//TrackBall

    public Point getXY() {
        return new Point(xEnc.getCurrentPosition(), yEnc.getCurrentPosition());
    }//Point


}
