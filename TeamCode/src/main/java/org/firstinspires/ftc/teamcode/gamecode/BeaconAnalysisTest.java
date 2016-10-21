package org.firstinspires.ftc.teamcode.gamecode;

import android.support.annotation.Nullable;
import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.vuforia.Image;
import com.vuforia.PIXEL_FORMAT;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.R;
import org.firstinspires.ftc.teamcode.RC;
import org.firstinspires.ftc.teamcode.opmodesupport.FXTLinearOpMode;
import org.firstinspires.ftc.teamcode.robots.NewRobot;

/**
 * Created by FIXIT on 16-10-07.
 */
@Autonomous
public class BeaconAnalysisTest extends FXTLinearOpMode {

    final int BEACON_BUFFER_DISTANCE = 60;

    @Override
    public void runOp() throws InterruptedException {
        VuforiaLocalizer.Parameters params = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId);
        params.vuforiaLicenseKey = RC.VUFORIA_LICENSE_KEY;
        params.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;

        VuforiaLocalizer locale = ClassFactory.createVuforiaLocalizer(params);
        Vuforia.setFrameFormat(PIXEL_FORMAT.RGB565, true);
        locale.setFrameQueueCapacity(1);

        VuforiaTrackables beacons = locale.loadTrackablesFromAsset("FTC_2016-17");
        VuforiaTrackableDefaultListener gears = (VuforiaTrackableDefaultListener) beacons.get(3).getListener();

        waitForStart();
        beacons.activate();

        while (!gears.isVisible()) {
            delay(1);
        }//while

        while (opModeIsActive()) {
            int beaconConfig = NewRobot.BEACON_NOT_VISIBLE;
            while (beaconConfig == NewRobot.BEACON_NOT_VISIBLE) {
                beaconConfig = NewRobot.getBeaconConfig(getImageFromFrame(locale.getFrameQueue().take(), PIXEL_FORMAT.RGB565), beacons.get(3), locale.getCameraCalibration());
            }//while

            if (beaconConfig == NewRobot.BEACON_RED_BLUE) {
                Log.i("RED", "BLUE");
            } else if (beaconConfig != NewRobot.BEACON_NOT_VISIBLE) {
                Log.i("BLUE", "RED");
            } else {
                Log.i("BEAC", "== -1");
            }//else

            delay(500);
        }//while
    }//runOp


    @Nullable
    public static Image getImageFromFrame(VuforiaLocalizer.CloseableFrame frame, int format) {

        long numImgs = frame.getNumImages();
        for (int i = 0; i < numImgs; i++) {
            if (frame.getImage(i).getFormat() == format) {
                return frame.getImage(i);
            }//if
        }//for

        return null;
    }

}
