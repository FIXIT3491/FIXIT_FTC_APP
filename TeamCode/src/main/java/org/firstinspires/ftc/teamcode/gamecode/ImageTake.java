package org.firstinspires.ftc.teamcode.gamecode;

import android.graphics.Bitmap;

import org.firstinspires.ftc.teamcode.RC;
import org.firstinspires.ftc.teamcode.opmodesupport.TeleOpMode;
import org.firstinspires.ftc.teamcode.roboticslibrary.ImageAnalyzer;

/**
 * Created by FIXIT on 16-07-05.
 */
public class ImageTake extends TeleOpMode {

    @Override
    public void initialize() {
        RC.cam.start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        ImageAnalyzer.saveToInternalStorage(Bitmap.createScaledBitmap(RC.cam.image, 300, 400, false), "field.jpg");
    }

    @Override
    public void loopOpMode() {

    }
}
