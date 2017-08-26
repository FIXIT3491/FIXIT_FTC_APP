package org.firstinspires.ftc.teamcode.velocityvortexopmodes;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.opmodesupport.AutoOpMode;
import org.firstinspires.ftc.teamcode.robots.*;

/**
 * Created by Windows on 2016-12-28.
 */
@Autonomous
public class ShooterTester extends AutoOpMode {

    @Override
    public void runOp() throws InterruptedException{

        Fermion lepton = new Fermion(true);

        waitForStart();

        int target = lepton.shooter.getBaseCurrentPosition();
        for (int i = 0; i < 10; i++) {
            target += lepton.shooter.getNumTiksPerRev() / 2;
            lepton.shooter.setPower(1);

            while (lepton.shooter.getBaseCurrentPosition() < target) {
                idle();
            }//while

            lepton.shooter.stop();

            lepton.door.goToPos("open");
            lepton.setCollectorState(Robot.IN);

            Log.i("Tiks", lepton.shooter.getBaseCurrentPosition() + ", " + lepton.shooter.getNumTiksPerRev());

            sleep(1000);

            lepton.door.goToPos("close");
            lepton.setCollectorState(Robot.STOP);
        }//for

    }
}//ShooterTester