package org.firstinspires.ftc.teamcode.velocityvortexopmodes;

import android.graphics.Bitmap;
import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.opmodesupport.TeleOpMode;
import org.firstinspires.ftc.teamcode.roboticslibrary.FXTCamera;
import org.firstinspires.ftc.teamcode.util.CircleDetector;

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
        double[] circle = CircleDetector.findBestCircle2(bit, true);

        Log.i("Circle", Arrays.toString(circle));
    }

    public void stop() {
        cam.destroy();
    }
}
