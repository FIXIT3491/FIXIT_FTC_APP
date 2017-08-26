package org.firstinspires.ftc.teamcode.velocityvortexopmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.opmodesupport.AutoOpMode;
import org.firstinspires.ftc.teamcode.robots.Fermion;
import org.firstinspires.ftc.teamcode.robots.Robot;

/**
 * Created by FIXIT on 2017-04-13.
 */
@Autonomous
public class DemoFermion extends AutoOpMode {
    @Override
    public void runOp() throws InterruptedException {
        Fermion f = new Fermion(true);

        waitForStart();
        f.startShooterControl();

        f.setCollectorState(Robot.IN);

        while (opModeIsActive() && f.getUltrasonicDistance(0) > 100) {
            idle();
        }//while

        f.setCollectorState(Robot.STOP);

        f.door.goToPos("open");
        sleep(1000);
        f.door.goToPos("close");
        f.imuTurnR(90, 0.25);

        f.shoot();
        f.waitForShooterState(Fermion.LOADING);

        f.liftCapBall();

        sleep(2000);

        f.lowerCapBall();

        sleep(2000);

        f.lifter.stop();

    }
}
