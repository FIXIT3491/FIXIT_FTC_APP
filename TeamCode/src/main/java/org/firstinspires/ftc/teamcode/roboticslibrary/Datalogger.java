package org.firstinspires.ftc.teamcode.roboticslibrary;

import org.firstinspires.ftc.teamcode.newhardware.FXTSensors.FXTSensor;
import org.firstinspires.ftc.teamcode.robots.Robot;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by User on 8/23/2015.
 */
public class Datalogger {
    private Robot r;
    private DataWriter out;
    private int interval = 50; //How often the datalogger will refresh in Milliseconds
    private long startTime = 0;
    private int [] sensors;
    Thread thread;
    public final static int ENCODER_R = 100;
    public final static int ENCODER_L = 101;
    List<FXTSensor> sensorList = new ArrayList<FXTSensor>();

    public Datalogger(HardwareMap hardwareMap, int[] sensors, String fileName){
        this.sensors = sensors;

        try {
            out = new DataWriter(fileName, true);
        } catch (Exception e) {
            e.printStackTrace();
        }


        for(int i = 0; i < sensors.length; i++){

            if(sensors[i] != ENCODER_R && sensors[i] != ENCODER_L) {

            } else {
               out.write("Encoder" + ((sensors[i] == ENCODER_L) ? "L" : "R"));

            }

            if(i != sensors.length - 1){
                out.write(", ");

            } else {
                out.write(", Time\n" + new Date().toString() + "\n");
                startTime = System.currentTimeMillis();

            }
        }
    }

    public void start(){

        Runnable run = new Runnable() {

            @Override
            public void run() {

                String data = "";
                while(true) {
                    for(int i = 0; i < sensors.length; i++){
                        if(sensors[i] == ENCODER_R){
                            data += r.motorR.getCurrentPosition() + ", ";
                        } else if(sensors[i] == ENCODER_L){
                            data += r.motorL.getCurrentPosition() + ", ";
                        } else {
                            data += sensorList.get(i) + ", ";
                        }
                    }
                    data += (System.currentTimeMillis() - startTime) + "\n";
                    out.write(data);
                    try {
                        Thread.sleep(interval);
                    } catch (InterruptedException e){

                    }
                }
            }
        };
        thread = new Thread(run);
        thread.start();
    }

    public void stop(){
        out.closeWriter();
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public int getInterval() {
        return interval;
    }
}
