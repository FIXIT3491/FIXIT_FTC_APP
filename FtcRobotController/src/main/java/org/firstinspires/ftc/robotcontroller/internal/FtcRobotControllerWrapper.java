package org.firstinspires.ftc.robotcontroller.internal;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.qualcomm.ftccommon.CommandList;
import com.qualcomm.ftccommon.FtcEventLoop;
import com.qualcomm.ftccommon.FtcEventLoopIdle;
import com.qualcomm.ftccommon.configuration.RobotConfigFile;
import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.hardware.HardwareFactory;
import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeAndMeta;
import com.qualcomm.robotcore.eventloop.opmode.OpModeMeta;
import com.qualcomm.robotcore.eventloop.opmode.OpModeRegister;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.factory.RobotFactory;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.robocol.Command;
import com.qualcomm.robotcore.robot.Robot;

import org.firstinspires.ftc.robotcore.internal.AppUtil;
import org.firstinspires.ftc.robotcore.internal.network.RobotCoreCommandList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FIXIT on 16-10-25.
 */
public class FtcRobotControllerWrapper {

    public static FtcRobotControllerActivity activity() {
        return (FtcRobotControllerActivity) AppUtil.getInstance().getActivity();
    }

    public static Context context() {
        return AppUtil.getInstance().getActivity();
    }

    public static void addView(final View v, int containerId) {

        final ViewGroup group = (ViewGroup) activity().findViewById(containerId);

        activity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                group.addView(v);
            }
        });

    }//addView

    public static void emptyView(int containerId) {

        final ViewGroup group = (ViewGroup) activity().findViewById(containerId);

        activity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                group.removeAllViews();
            }
        });

    }//emptyView

    public static void setUpRobotWithoutWifi() {
        EventLoopManager evtLpManager = new EventLoopManager(activity());

        HardwareFactory factory;
        RobotConfigFile file = activity().cfgFileMgr.getActiveConfigAndUpdateUI();

        HardwareFactory hardwareFactory = new HardwareFactory(activity());
        hardwareFactory.setXmlPullParser(file.getXml());
        factory = hardwareFactory;

        OpModeRegister register = activity().createOpModeRegister();
        activity().eventLoop = new FtcEventLoop(factory, register, activity().callback, activity(), activity().programmingModeController);
        FtcEventLoopIdle idleLoop = new FtcEventLoopIdle(factory, activity().callback, activity(), activity().programmingModeController);
        evtLpManager.setIdleEventLoop(idleLoop);

        Robot robot = null;

        try {
            robot = RobotFactory.createRobot(evtLpManager);
            robot.start(activity().eventLoop);
        } catch (RobotCoreException e) {
            e.printStackTrace();
        }//catch

        try {
            activity().eventLoop.getOpModeManager().registerOpModes(register);
        } catch (Exception e) {
            e.printStackTrace();
        }//catch

    }//setUpRobotWithoutWifi

    public static void initOpMode(String opModeName) {
//        processCommand(new Command(CommandList.CMD_INIT_OP_MODE, opModeName));
        activity().eventLoop.getOpModeManager().initActiveOpMode(opModeName);
    }//initOpMode

    public static void runOpMode(String opModeName) {
//        processCommand(new Command(CommandList.CMD_RUN_OP_MODE, opModeName));
        activity().eventLoop.getOpModeManager().startActiveOpMode();
    }//runOpMode

    public static void stopOpMode() {
        activity().eventLoop.getOpModeManager().stopActiveOpMode();
    }//stopOpMode

    public static List<String> getStringsFromOpmodes(List<OpModeMeta> metas) {
        List<String> names = new ArrayList<>();

        for (int i = 0; i < metas.size(); i++) {
            names.add(metas.get(i).name);
        }//for

        return names;
    }

    public static void initializeSpinner(final Spinner spinner) {
        Log.i("sdfdf", "sdfdf");
        activity().opModeNames = FtcRobotControllerWrapper.getStringsFromOpmodes(activity().eventLoop.getOpModeManager().getOpModes());

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity(), android.R.layout.simple_spinner_dropdown_item, activity().opModeNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        activity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                spinner.setAdapter(adapter);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        activity().activeOpModeName = activity().opModeNames.get(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        });

    }

}//FtcRobotControllerWrapper
