package com.qualcomm.ftcrobotcontroller.gamecode;

import com.qualcomm.ftcrobotcontroller.RC;
import com.qualcomm.ftcrobotcontroller.newhardware.FXTSensors.DigitalUltrasonicSensor;
import com.qualcomm.ftcrobotcontroller.newhardware.FXTSensors.UltrasonicPanSensor;
import com.qualcomm.ftcrobotcontroller.opmodesupport.LoopThread;
import com.qualcomm.ftcrobotcontroller.opmodesupport.TeleOpMode;

/**
 * Created by FIXIT on 16-08-03.
 */
public class AUltrasonicPanOp extends TeleOpMode {

    LoopThread loop;
    DigitalUltrasonicSensor ultra;

    boolean move = false;

    @Override
    public void initialize() {
        loop = new LoopThread(5);
        ultra = new DigitalUltrasonicSensor("echo", "trigger");
        loop.addDevice(ultra);
        RC.t.setDataLogFile("scanner", true);

    }//init

    public void start() {
        loop.begin();

    }

    @Override
    public void loopOpMode() {

        if (move) {
            ultra.pulse();
        } else if (ultra.returnValue() > 0){
            RC.t.dataLogData(ultra.returnValue() + "\n");
        }//else

        move = !move;
    }//loopOpMode

    public void stop() {
        loop.destroy();
    }
}
