package org.firstinspires.ftc.teamcode.gamecode;

import android.support.annotation.Nullable;
import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.vuforia.Image;
import com.vuforia.PIXEL_FORMAT;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.R;
import org.firstinspires.ftc.teamcode.RC;
import org.firstinspires.ftc.teamcode.opmodesupport.AutoOpMode;
import org.firstinspires.ftc.teamcode.roboticslibrary.MathUtils;
import org.firstinspires.ftc.teamcode.robots.*;

/**
 * Created by Windows on 2016-11-06.
 */
@Autonomous
public class BeaconPress extends AutoOpMode {

    @Override
    public void runOp() {
        Fermion strange = new Fermion(true);

        VuforiaLocalizer.Parameters params = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId);
        params.vuforiaLicenseKey = RC.VUFORIA_LICENSE_KEY;
        params.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;


        VuforiaLocalizer locale = ClassFactory.createVuforiaLocalizer(params);
        locale.setFrameQueueCapacity(1);
        Vuforia.setFrameFormat(PIXEL_FORMAT.RGB565, true);

        VuforiaTrackables beacons = locale.loadTrackablesFromAsset("FTC_2016-17");
        VuforiaTrackableDefaultListener gears = (VuforiaTrackableDefaultListener) beacons.get(3).getListener();

        telemetry.update();
        waitForStart();

        beacons.activate();
//        sleep(200);
//        while (!gears.isVisible() && opModeIsActive()){
//            strange.strafe(-90, 0.1);
//            sleep(400);
//            strange.stop();
//            sleep(100);
//        }
//        strange.stop();

//
//        double x = gears.getPose().getTranslation().get(0);
//        while(!MathUtils.inRange(x, 0, 10) && opModeIsActive()){
//            if(x < 0) strange.strafe(-90, 0.01);
//            if(x > 10) strange.strafe(90, 0.01);
//            x = gears.getPose().getTranslation().get(0);
//            telemetry.addData("x", x);
//            telemetry.addData("motor", strange.leftBack.returnCurrentState());
//            telemetry.update();
//            Log.i(TAG, "runOp: " + x);
//        }
//        strange.stop();

//        strange.absoluteIMUTurn(0, 0.1);
        int config = Fermion.BEACON_NOT_VISIBLE;
        try{
            config = Fermion.waitForBeaconConfig(
                    getImageFromFrame(locale.getFrameQueue().take(), PIXEL_FORMAT.RGB565),
                    gears, locale.getCameraCalibration(), 5000);
            telemetry.addData("Beacon", config);
            Log.i(TAG, "runOp: " + config);
        } catch (Exception e){
            telemetry.addData("Beacon", "could not not be found");
        }
//        telemetry.addData("x", x);
        telemetry.update();

        if (config == Fermion.BEACON_BLUE_RED) {
            strange.strafeToBeacon(gears, 10, 0.1, new VectorF(40, -0.1f, -0.1f));
        } else {
            strange.strafeToBeacon(gears, 10, 0.1, new VectorF(-60, -0.1f, -0.1f));
        }//else

        strange.forward(0.4);
        sleep(1000);
        strange.stop();

//        int lowerBound = 0;
//        int upperBound = 0;
//
//        if((config == Fermion.BEACON_BLUE_RED)){
//            lowerBound = 0;
//            upperBound = 10;
//        } else {
//            lowerBound = -60;
//            upperBound = -40;
//        }
//
//        while(opModeIsActive() && getMilliSeconds(1) < 5000){
//            if(x < lowerBound) strange.strafe(-90, 0.01);
//            else if(x > upperBound) strange.strafe(90, 0.01);
//            else strange.strafe(0, 0.2);
//            try{
//                x = gears.getPose().getTranslation().get(0);
//            } catch (Exception e){
//                telemetry.addData("Vuforia", "image target no longer visible");
//                x = (lowerBound + upperBound) / 2;
//            }
//            telemetry.addData("x", x);
//            Log.i(TAG, "runOp: " + x);
//            telemetry.update();
//        }
        strange.stop();
    }

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


}//BeaconPress