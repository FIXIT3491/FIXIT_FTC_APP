package org.firstinspires.ftc.teamcode.gamecode;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.vuforia.PIXEL_FORMAT;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.R;
import org.firstinspires.ftc.teamcode.RC;
import org.firstinspires.ftc.teamcode.opmodesupport.LinearTeleOpMode;
import org.firstinspires.ftc.teamcode.roboticslibrary.MathUtils;
import org.firstinspires.ftc.teamcode.robots.*;

/**
 * Created by Windows on 2016-11-04.
 */
@TeleOp
public class FermionOp extends LinearTeleOpMode {
    Fermion charm;
    VuforiaTrackables beacons;
    VuforiaTrackableDefaultListener gears;
    VuforiaTrackableDefaultListener tools;


    @Override
    public void initialize() {
        charm = new Fermion(true);

        VuforiaLocalizer.Parameters params = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId);
        params.vuforiaLicenseKey = RC.VUFORIA_LICENSE_KEY;
        params.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;


        VuforiaLocalizer locale = ClassFactory.createVuforiaLocalizer(params);
        locale.setFrameQueueCapacity(1);
        Vuforia.setFrameFormat(PIXEL_FORMAT.RGB565, true);


        beacons = locale.loadTrackablesFromAsset("FTC_2016-17");
        gears = (VuforiaTrackableDefaultListener) beacons.get(3).getListener();
        tools = (VuforiaTrackableDefaultListener) beacons.get(1).getListener();
    }

    @Override
    public void onStart() {
        beacons.activate();
    }

    @Override
    public void loopOpMode() {
        double theta = 0;

        if(joy1.y1() == 0 && joy1.x1() == 0){
            charm.rightFore.setPower(-joy1.x2() / 3.0);
            charm.rightBack.setPower(-joy1.x2() / 3.0);
            charm.leftFore.setPower(joy1.x2() / 3.0);
            charm.leftBack.setPower(joy1.x2() / 3.0);
        } else {
            theta = Math.abs(Math.atan2(joy1.x1(), -joy1.y1()));
            Log.i(TAG, "loopOpMode: " + theta);

            if(joy1.x1() < 0) theta *= -1;

            double speed = (joy1.rightBumper())?0.3:1;
            charm.strafe(Math.toDegrees(MathUtils.roundToNearest(theta, Math.PI / 4, -Math.PI, Math.PI)), speed);
        }


        if(joy1.buttonB()){
            charm.absoluteIMUTurn(90, 1);
        }

        if(joy1.buttonA()){
            charm.absoluteIMUTurn(0, 1);
        }

        if(gears.isVisible()){
            try {
                telemetry.addData("Gears", gears.getPose().getTranslation());
            } catch (Exception e){}
        }
    }
}//FermionOp