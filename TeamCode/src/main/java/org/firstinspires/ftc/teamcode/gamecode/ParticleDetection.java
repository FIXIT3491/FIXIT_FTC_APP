package org.firstinspires.ftc.teamcode.gamecode;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcontroller.internal.FtcControllerUtils;
import org.firstinspires.ftc.teamcode.opmodesupport.TeleOpMode;
import org.firstinspires.ftc.teamcode.roboticslibrary.FXTCamera;
import org.firstinspires.ftc.teamcode.util.CircleDetector;
import org.firstinspires.ftc.teamcode.util.OCVUtils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Windows on 2017-03-04.
 */
@Autonomous
public class ParticleDetection extends TeleOpMode {

    FXTCamera cam;

    @Override
    public void initialize() {
        cam = new FXTCamera(FXTCamera.FACING_BACKWARD, true);
    }

    @Override
    public void loopOpMode() {

        Bitmap bit = cam.photo();
        double[] circle = CircleDetector.findBestCircle(bit, 0, 0);

        Log.i("XCircle", Arrays.toString(circle));



        double vFOV = Math.toRadians(cam.getBaseCamera().getParameters().getHorizontalViewAngle());

        double f = (bit.getHeight() / 2.0) / Math.tan(vFOV);

        Log.i("X", "" + Math.toDegrees(Math.atan((circle[1] - bit.getHeight() / 2) / f)));
    }

    public void stop() {
        cam.destroy();
    }
}
