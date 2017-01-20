package org.firstinspires.ftc.teamcode.gamecode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.RC;
import org.firstinspires.ftc.teamcode.opmodesupport.AutoOpMode;
import org.firstinspires.ftc.teamcode.robots.Fermion;
import org.firstinspires.ftc.teamcode.robots.Robot;

/**
 * Created by Windows on 2017-01-15.
 */
@Autonomous
public class FermionShotCap extends AutoOpMode {

        @Override
        public void runOp() throws InterruptedException {
            final Fermion muon = new Fermion(true);

            RC.t.addData("asdgasdfasdf");
            muon.startShooterControl();
            muon.prime();
            waitForStart();

            sleep(10 * 1000);

            muon.right(1);
            sleep(1100);
            muon.stop();
            muon.shoot();

            if(RC.globalBool("2Balls")){
                muon.waitForState(Fermion.LOADED);
                muon.door.goToPos("open");
                muon.collector.setPower(-1);
                muon.shoot();
                sleep(1000);
                muon.door.goToPos("close");
                muon.setCollectorState(Robot.STOP);
            }//if

            muon.waitForState(Fermion.FIRE);

            muon.right(1);
            sleep(1000);
            muon.stop();

            muon.right(0.4);
            sleep(500);
            muon.stop();

        }//runOp
}
