package org.firstinspires.ftc.teamcode.gamecode;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.vuforia.PIXEL_FORMAT;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.R;
import org.firstinspires.ftc.teamcode.RC;
import org.firstinspires.ftc.teamcode.opmodesupport.AutoOpMode;
import org.firstinspires.ftc.teamcode.roboticslibrary.FXTCamera;
import org.firstinspires.ftc.teamcode.robots.Fermion;
import org.firstinspires.ftc.teamcode.robots.Robot;
import org.firstinspires.ftc.teamcode.util.VortexUtils;

/**
 * Created by Windows on 2017-01-21.
 */

@Autonomous
public class FermionBlueNoVuf extends AutoOpMode {
    Fermion top;

    @Override
    public void runOp() throws InterruptedException {
        top = new Fermion(true);

        FXTCamera cam = new FXTCamera(FXTCamera.FACING_BACKWARD, true);

        top.startShooterControl();
        top.prime();
        waitForStart();
        top.addVeerCheckRunnable();
        top.resetTargetAngle();

        top.right(1);
        sleep(900);
        top.stop();
        top.shoot();


        top.waitForState(Fermion.FIRE);
        top.absoluteIMUTurn(90, 0.5);

        top.strafe(-40, 1, true);
        while(opModeIsActive() && top.getLight(Robot.LEFT) < Fermion.LIGHT_THRESHOLD){
            Log.i(TAG, "runOp: ultra" + top.ultra.getDistance());
            if(top.ultra.getDistance() < 410){
                top.strafe(-90, 0.3, true);
            }
        }

        top.stop();

        int config = VortexUtils.getBeaconConfig(cam.getImage());

        RC.t.addData("Beacon", config);

        sleep(10000);


    }
}
