package org.firstinspires.ftc.teamcode.gamecode;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
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
import org.firstinspires.ftc.teamcode.robots.*;
import org.firstinspires.ftc.teamcode.util.VortexUtils;

/**
 * Created by Windows on 2016-12-28.
 */
@Autonomous
public class ShooterTester extends AutoOpMode {

    @Override
    public void runOp() throws InterruptedException{

        Fermion lepton = new Fermion(true);

        waitForStart();

        int target = lepton.shooter.getCurrentPosition();
        for (int i = 0; i < 10; i++) {
            target += lepton.shooter.getNumTiksPerRev() / 2;
            lepton.shooter.setPower(1);

            while (lepton.shooter.getCurrentPosition() < target) {
                idle();
            }//while

            lepton.shooter.stop();

            lepton.door.goToPos("open");
            lepton.setCollectorState(Robot.IN);

            Log.i("Tiks", lepton.shooter.getCurrentPosition() + ", " + lepton.shooter.getNumTiksPerRev());

            sleep(1000);

            lepton.door.goToPos("close");
            lepton.setCollectorState(Robot.STOP);
        }//for

    }
}//ShooterTester