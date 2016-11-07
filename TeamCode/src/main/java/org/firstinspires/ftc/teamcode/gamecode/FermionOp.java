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
import org.firstinspires.ftc.teamcode.opmodesupport.AutoOpMode;
import org.firstinspires.ftc.teamcode.opmodesupport.LinearTeleOpMode;
import org.firstinspires.ftc.teamcode.opmodesupport.TeleOpMode;
import org.firstinspires.ftc.teamcode.roboticslibrary.MathUtils;
import org.firstinspires.ftc.teamcode.robots.*;

/**
 * Created by Windows on 2016-11-04.
 */
@TeleOp
public class FermionOp extends LinearTeleOpMode {
    Fermion fermi;
    VuforiaTrackables beacons;
    VuforiaTrackableDefaultListener gears;
    VuforiaTrackableDefaultListener tools;


    @Override
    public void initialize() {
        fermi = new Fermion(true);

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
        if(joy1.rightBumper()){
            theta = Math.abs(Math.atan2(joy1.x1(), -joy1.y1()));
            Log.i(TAG, "loopOpMode: " + theta);

            if(joy1.x1() < 0) theta *= -1;

            fermi.strafe(Math.toDegrees(MathUtils.roundToNearest(theta, Math.PI / 4, - Math.PI, Math.PI)), 1);
        } else {
            fermi.rightFore.setPower((joy1.x2() - joy1.y1() + joy1.x1()) / 3.0);
            fermi.rightBack.setPower((joy1.x2() - joy1.y1() - joy1.x1()) / 3.0);
            fermi.leftFore.setPower((-joy1.x2() - joy1.y1() - joy1.x1()) / 3.0);
            fermi.leftBack.setPower((-joy1.x2() - joy1.y1() + joy1.x1())/ 3.0);
        }


        if(joy1.buttonA()){
            fermi.absoluteIMUTurn(90, 0.5);
        }

        if(gears.isVisible()){
            try {
                telemetry.addData("Gears", gears.getPose().getTranslation());
            } catch (Exception e){}
        }
    }
}//FermionOp