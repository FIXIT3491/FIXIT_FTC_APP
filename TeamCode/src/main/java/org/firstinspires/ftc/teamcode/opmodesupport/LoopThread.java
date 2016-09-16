package org.firstinspires.ftc.teamcode.opmodesupport;

import org.firstinspires.ftc.teamcode.newhardware.FXTDevice;

import java.util.ArrayList;

/**
 * Created by Windows on 2016-03-26.
 */
public class LoopThread {

    Thread wrapped;
    boolean exitLoop = false;
    boolean pause = false;

    public ArrayList<FXTDevice> devices = new ArrayList<FXTDevice>();

    public LoopThread(final int delay) {
        wrapped = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!exitLoop) {

                    if (!pause) {

                        for (int i = 0; i < devices.size(); i++) {
                            synchronized(devices.get(i)) {
                                devices.get(i).check();
                            }//synch
                        }//for

                    }//if

                    try {
                        Thread.sleep(0, delay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }//catch
                }//while
            }
        });
    }//constructor

    //THREAD METHODS

    public void begin() {
        wrapped.start();
    }//begin

    public void destroy() {
        exitLoop = true;
    }//stop

    public void pause() {
        pause = true;
    }//pause

    public void resume() {
        pause = false;
    }//resume

    public void addDevice(FXTDevice dev) {
        devices.add(dev);
    }//addDevice

}
