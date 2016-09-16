package org.firstinspires.ftc.teamcode.oldopmodes;

import org.firstinspires.ftc.teamcode.RC;
import org.firstinspires.ftc.teamcode.opmodesupport.DoNotRegister;
import org.firstinspires.ftc.teamcode.opmodesupport.TeleOpMode;
import org.firstinspires.ftc.teamcode.robots.Lily;

/**
 * Created by FTC on 31/10/2015.
 */
@DoNotRegister
public class ArmControl extends TeleOpMode {

    Lily lily;

    @Override
    public void initialize() {

        lily = new Lily(true);

    }

    @Override
    public void loopOpMode() {


        if(joy2.buttonX()){
            lily.turnTable.setPower(0.02);
        } else if(joy2.buttonB()){
            lily.turnTable.setPower(-0.02);
        } else {
            lily.turnTable.stop();
        }

        if(joy2.leftTrigger()){
            lily.elbow.setPower(0.1);
        } else if(joy2.leftBumper()){
            lily.elbow.setPower(-0.1);
        } else {
            lily.elbow.stop();
        }
//
//        if(joy2.rightTrigger()){
//            lily.wrist.setPosition(0.5);
//        } else if(joy2.rightBumper()){
//            lily.wrist.setPower(-0.5);
//        } else {
//            RC.t.addData("Servo", "Stopped");
//            lily.wrist.setPower(0);
//        }

        if(joy2.buttonY()){
            lily.door.goToPos("open");
        } else {
            lily.door.goToPos("closed");
        }

        RC.t.addData("basejoint", "" + lily.elbow.getCurrentPosition());
        RC.t.addData("turntable", "" + lily.turnTable.getCurrentPosition());
        RC.t.addData("wrist", "" + lily.wrist.currentPos);

    }
}
