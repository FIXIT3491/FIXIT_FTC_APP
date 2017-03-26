package org.firstinspires.ftc.teamcode.gamecode;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcontroller.internal.FtcControllerUtils;
import org.firstinspires.ftc.teamcode.R;
import org.firstinspires.ftc.teamcode.RC;
import org.firstinspires.ftc.teamcode.opmodesupport.AutoOpMode;
import org.firstinspires.ftc.teamcode.roboticslibrary.FXTCamera;
import org.firstinspires.ftc.teamcode.robots.Fermion;
import org.firstinspires.ftc.teamcode.util.CircleDetector;
import org.firstinspires.ftc.teamcode.util.OCVUtils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.Arrays;

/**
 * Created by Windows on 2017-03-24.
 */
@Autonomous
public class AutoBallCollector extends AutoOpMode {
    @Override
    public void runOp() throws InterruptedException {
        FXTCamera cam = new FXTCamera(FXTCamera.FACING_BACKWARD, true);

        waitForStart();

        sleep(1000);

        Bitmap frame = cam.photo();
        CircleDetector.findBestCircle(frame);

        cam.destroy();
    }
}
