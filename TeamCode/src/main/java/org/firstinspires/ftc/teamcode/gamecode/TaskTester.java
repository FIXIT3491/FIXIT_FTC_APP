package org.firstinspires.ftc.teamcode.gamecode;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.opmodesupport.AutoOpMode;
import org.firstinspires.ftc.teamcode.roboticslibrary.TaskHandler;

/**
 * Created by Windows on 2017-04-13.
 */
@Autonomous
public class TaskTester extends AutoOpMode{

    @Override
    public void runOp() throws InterruptedException {

        TaskHandler.addLoopedTask("Testeroo", new Runnable() {
            @Override
            public void run() {
                Log.i("Hiyay", "Bammbi");
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, 5);
        sleep(500);
        TaskHandler.pauseTask("Testeroo");
        sleep(500);
        TaskHandler.resumeTask("Testeroo");
        sleep(1000);

    }
}
