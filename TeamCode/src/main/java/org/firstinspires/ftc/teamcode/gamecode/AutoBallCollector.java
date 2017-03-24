package org.firstinspires.ftc.teamcode.gamecode;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.opmodesupport.AutoOpMode;
import org.firstinspires.ftc.teamcode.roboticslibrary.FXTCamera;
import org.firstinspires.ftc.teamcode.robots.Fermion;
import org.firstinspires.ftc.teamcode.util.CircleDetector;

import java.util.Arrays;

/**
 * Created by Windows on 2017-03-24.
 */
@Autonomous
public class AutoBallCollector extends AutoOpMode {
    @Override
    public void runOp() throws InterruptedException {
        FXTCamera cam = new FXTCamera(FXTCamera.FACING_BACKWARD, true);
        Fermion f = new Fermion(true);

        waitForStart();

//        while (opModeIsActive()) {

            double[] info = CircleDetector.findBestCircle(cam.getImage());
            double angleH = (info[0] * 76 / 1920.833153);

            Log.i("Info", Arrays.toString(info));
//            f.imuTurnR(angleH, 0.5);
            sleep(4000);
//        }//while

    }
}
