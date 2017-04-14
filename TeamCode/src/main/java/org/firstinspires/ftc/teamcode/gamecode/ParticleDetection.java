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
        double[] circle = CircleDetector.findBestCircle2(bit);

        Log.i("Circle", Arrays.toString(circle));
    }

    public void stop() {
        cam.destroy();
    }
}
