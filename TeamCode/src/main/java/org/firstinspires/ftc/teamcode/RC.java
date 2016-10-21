package org.firstinspires.ftc.teamcode;

import android.app.Application;
import android.content.Context;
import android.view.View;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import org.firstinspires.ftc.robotcore.internal.AppUtil;
import org.firstinspires.ftc.teamcode.roboticslibrary.FXTTelemetry;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.HashMap;

/**
 * Created by FIXIT on 15-08-21.
 */
public class RC {

    public static OpMode o;
    public static LinearOpMode l;
    public static FXTTelemetry t;
    public static HardwareMap h;
    public final static String VUFORIA_LICENSE_KEY = "Ad0I0ir/////AAAAAfR3NIO1HkxSqM8NPhlEftFXtFAm6DC5w4Cjcy30" +
                                                    "WUdGozklFlAkxeHpjfWc4moeL2ZTPvZ+wAoyOnlZxyB6Wr1BRE9154j6K" +
                                                    "1/8tPvu21y5ke1MIbyoJ/5BAQuiwoAadjptZ8fpS7A0QGPrMe0VauJIM1" +
                                                    "mW3UU2ezYFSOcPghCOCvQ8zid1Bb8A92IkbLcBUcv3DEC6ia4SEkbRMY7" +
                                                    "TpOh2gzsXdsue4tqj9g7vj7zBU5Hu4WhkMDJRsThn+5QoHXqvavDsCElw" +
                                                    "mDHG3hlEYo7qN/vV9VcQUX9XnVLuDeZhkp885BHK5vAe8T9W3Vxj2H/R4" +
                                                    "oijQso6hEBaXsOpCHIWGcuphpoe9yoQlmNRRZ97";

    public static void setOpMode(OpMode op) {
        o = op;
        h = op.hardwareMap;
        t = new FXTTelemetry();
        t.setTelemetry(op.telemetry);

        if (op instanceof LinearOpMode) {
            l = (LinearOpMode) op;
        }//if

    }//setOpMode

    public static Context c() {
        return AppUtil.getInstance().getActivity();
    }//context

    public static FtcRobotControllerActivity a() {
        return ((FtcRobotControllerActivity) AppUtil.getInstance().getActivity());
    }//activity

    public void stop() {
        t.close();
    }//stop

}
