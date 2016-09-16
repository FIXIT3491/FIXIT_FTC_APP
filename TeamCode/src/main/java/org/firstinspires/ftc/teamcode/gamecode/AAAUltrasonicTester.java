package org.firstinspires.ftc.teamcode.gamecode;

import android.util.Log;

        import org.firstinspires.ftc.teamcode.RC;
        import org.firstinspires.ftc.teamcode.newhardware.FXTSensors.DigitalUltrasonicSensor;
import org.firstinspires.ftc.teamcode.opmodesupport.LoopThread;
import org.firstinspires.ftc.teamcode.opmodesupport.TeleOpMode;

/**
         * Created by Windows on 2016-03-26.
         */
    public class AAAUltrasonicTester extends TeleOpMode {

        LoopThread thread;
        DigitalUltrasonicSensor ultra;
    @Override
    public void initialize() {
        thread = new LoopThread(5);
        ultra = new DigitalUltrasonicSensor("echo", "trig");
        thread.addDevice(ultra);
    }
            public void start() {
                thread.begin();
           clearTimer();
            }

       public void stop() {
            thread.destroy();
       }
            @Override
            public void loopOpMode() {

                if (getMilliSeconds() > 1000) {

                    ultra.pulse();
               Log.i("Called", "IF");

                    clearTimer();
                }

                        RC.t.addData("Ultrasonic cm", ultra.returnValue());
            }
        }