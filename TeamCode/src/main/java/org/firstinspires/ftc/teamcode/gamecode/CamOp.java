package org.firstinspires.ftc.teamcode.gamecode;

import android.graphics.Bitmap;
import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcontroller.internal.GlobalValuesActivity;
import org.firstinspires.ftc.teamcode.RC;
import org.firstinspires.ftc.teamcode.opmodesupport.TeleOpMode;
import org.firstinspires.ftc.teamcode.roboticslibrary.FXTCamera;

/**
 * Created by FIXIT on 16-10-18.
 */
public class CamOp extends TeleOpMode{

    FXTCamera cam;

    @Override
    public void initialize() {
        cam = new FXTCamera(FXTCamera.FACING_BACKWARD, true);
    }

    @Override
    public void loopOpMode() {
//        Bitmap bit = cam.photo();

        Log.i("SFDd", "DSFDSF");

//        Log.i("BITMAP", "Null: " + (bit == null) + "; Size: " + bit.getWidth() + ", " + bit.getHeight() + "; Config: " + bit.getConfig());
    }

    public void stop() {
        super.stop();
        cam.destroy();
    }
}
