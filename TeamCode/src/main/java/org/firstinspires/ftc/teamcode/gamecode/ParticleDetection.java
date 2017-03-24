package org.firstinspires.ftc.teamcode.gamecode;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcontroller.internal.FtcControllerUtils;
import org.firstinspires.ftc.teamcode.opmodesupport.TeleOpMode;
import org.firstinspires.ftc.teamcode.roboticslibrary.FXTCamera;
import org.firstinspires.ftc.teamcode.util.OCVUtils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;

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

        Mat compare = OCVUtils.bitmapToMat(cam.getImage(), CvType.CV_8UC3);
        Imgproc.cvtColor(compare, compare, Imgproc.COLOR_BGR2HSV_FULL);

        Mat mask = new Mat();
        Core.absdiff(compare, new Scalar(127, 127, 127), mask);
        
        OCVUtils.displayImage(mask);
    }
}
