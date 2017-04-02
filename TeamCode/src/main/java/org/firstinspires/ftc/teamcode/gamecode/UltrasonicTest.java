package org.firstinspires.ftc.teamcode.gamecode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.opmodesupport.AutoOpMode;
import org.firstinspires.ftc.teamcode.robots.Fermion;

/**
 * Created by Windows on 2017-03-04.
 */
@Autonomous
public class UltrasonicTest extends AutoOpMode {

    @Override
    public void runOp() throws InterruptedException {

        Fermion top = new Fermion(true);

        waitForStart();
        top.resetTargetAngle();
//        hile(opModeIsActive()){
//            top.setWallDistance(200);
//            top.strafe(0, 0.5, true);
//            top.commandedStrafeAngle = 0;
//            top.setTargetSpeed(0.5);
//            top.startWallFollowing(top.ultraSide, 0, 0.5);
//            while (opModeIsActive() && top.ultra.getDistance() > 400) {
//                idle();
//            }
//            top.strafe(-90, 0.5, true);
//            top.commandedStrafeAngle = -90;
//            top.setTargetSpeed(0.5);
//            top.setWallDistance(400);
//            top.startWallFollowing(top.ultra, -90, 0.5);
//
//            while (opModeIsActive() && top.ultraSide.getDistance() < 600) {
//                idle();
//            }
//
//            top.strafe(180, 0.5, true);
//            top.commandedStrafeAngle = 180;
//            top.setTargetSpeed(0.5);
//            top.setWallDistance(600);
//            top.startWallFollowing(top.ultraSide);
//
//            while (opModeIsActive() && top.ultra.getDistance() < 1500) {
//                idle();
//            }
//
//            top.strafe(90, 0.5, true);
//            top.commandedStrafeAngle = 90;
//            top.setTargetSpeed(0.5);
//            top.setWallDistance(1500);
//            top.startWallFollowing(top.ultra);
//
//            while (opModeIsActive() && top.ultraSide.getDistance() > 300) {
//                idle();
//            }
//        }w
    }
}
