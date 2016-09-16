package org.firstinspires.ftc.teamcode;

import android.app.Application;
import android.content.Context;
import android.view.View;

import org.firstinspires.ftc.teamcode.roboticslibrary.FXTCamera;
import org.firstinspires.ftc.teamcode.roboticslibrary.FXTTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.HashMap;

/**
 * Created by FIXIT on 15-08-21.
 */
public class RC extends Application {

    public static Context c;
    public static OpMode o;
    public static FXTTelemetry t;
    public static HardwareMap h;
    public static FtcRobotControllerActivity a;
    public static FXTCamera cam;
    public static HashMap<String, Object> adjust;

    public static void setOpMode(OpMode op) {
        o = op;
        h = op.hardwareMap;
        t = new FXTTelemetry();
        t.setTelemetry(op.telemetry);
    }//setOpMode

    public static void setCam(FXTCamera camera) {
        cam = camera;
    }//setCam

    public static boolean isOpModeActive() {
        if (o != null && o instanceof LinearOpMode) {
            return ((LinearOpMode) o).opModeIsActive();
        }//if

        return false;
    }//isOpModeActive

    public void onCreate() {
        super.onCreate();
        c = getApplicationContext();
    }

    public void stop() {
        t.close();
        cam.destroy();

        a.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                a.camView.setVisibility(View.INVISIBLE);
                a.display.setVisibility(View.INVISIBLE);
            }//run
        });
    }//stop

    public static void addAdjustingValue(String name, Object toAdjust) {
        if (adjust == null) {
            adjust = new HashMap<String, Object>();
        }//if

        adjust.put(name, toAdjust);
    }

}
