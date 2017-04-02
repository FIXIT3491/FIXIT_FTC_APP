package org.firstinspires.ftc.teamcode.gamecode;

import android.support.annotation.Nullable;
import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.vuforia.CameraDevice;
import com.vuforia.CameraField;
import com.vuforia.Image;
import com.vuforia.PIXEL_FORMAT;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.R;
import org.firstinspires.ftc.teamcode.RC;
import org.firstinspires.ftc.teamcode.newhardware.FXTSensors.FXTAnalogUltrasonicSensor;
import org.firstinspires.ftc.teamcode.opmodesupport.AutoOpMode;
import org.firstinspires.ftc.teamcode.roboticslibrary.FXTCamera;
import org.firstinspires.ftc.teamcode.util.CircleDetector;
import org.firstinspires.ftc.teamcode.util.VortexUtils;

import java.util.Arrays;

/**
 * Created by FIXIT on 16-10-07.
 */
@Autonomous
public class BeaconAnalysisTest extends AutoOpMode {

    /*
    RANGE: 285 to 500 mm away from beacon
    TARGET: 370mm
     */
    FXTCamera cam;

    @Override
    public void runOp() throws InterruptedException {

        cam = new FXTCamera(FXTCamera.FACING_BACKWARD, true);

        waitForStart();
        cam.lockExposure();

        double verticalCamFOV = cam.getBaseCamera().getParameters().getVerticalViewAngle();
        double horizontalCamFOV = cam.getBaseCamera().getParameters().getHorizontalViewAngle();

        while (opModeIsActive()) {
            Log.i("Data", Arrays.toString(CircleDetector.findBestCircle(cam.photo(), verticalCamFOV, horizontalCamFOV)));
        }//while

    }//runOp


    public void stopOpMode() {
        cam.destroy();
    }
}
