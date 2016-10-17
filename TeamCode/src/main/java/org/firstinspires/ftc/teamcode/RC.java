package org.firstinspires.ftc.teamcode;

import android.app.Application;
import android.content.Context;
import android.view.View;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
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
    public static HashMap<String, Object> adjust;
    public final static String VUFORIA_LICENSE_KEY = "Ad0I0ir/////AAAAAfR3NIO1HkxSqM8NPhlEftFXtFAm6DC5w4Cjcy30WUdGozklFlAkxeHpjfWc4moeL2ZTPvZ+wAoyOnlZxyB6Wr1BRE9154j6K1/8tPvu21y5ke1MIbyoJ/5BAQuiwoAadjptZ8fpS7A0QGPrMe0VauJIM1mW3UU2ezYFSOcPghCOCvQ8zid1Bb8A92IkbLcBUcv3DEC6ia4SEkbRMY7TpOh2gzsXdsue4tqj9g7vj7zBU5Hu4WhkMDJRsThn+5QoHXqvavDsCElwmDHG3hlEYo7qN/vV9VcQUX9XnVLuDeZhkp885BHK5vAe8T9W3Vxj2H/R4oijQso6hEBaXsOpCHIWGcuphpoe9yoQlmNRRZ97";

    public static void setOpMode(OpMode op) {
        o = op;
        h = op.hardwareMap;
        t = new FXTTelemetry();
        t.setTelemetry(op.telemetry);
    }//setOpMode

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
    }//stop

    public static void addAdjustingValue(String name, Object toAdjust) {
        if (adjust == null) {
            adjust = new HashMap<String, Object>();
        }//if

        adjust.put(name, toAdjust);
    }

}
