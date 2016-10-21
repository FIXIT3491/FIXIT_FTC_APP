package org.firstinspires.ftc.teamcode.resqopmodes;

import org.firstinspires.ftc.teamcode.opmodesupport.TeleOpMode;

/**
 * Created by FIXIT on 16-07-05.
 */
public class ImageTake extends TeleOpMode {

    @Override
    public void initialize() {

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


//        ImageAnalyzer.saveToInternalStorage(Bitmap.createScaledBitmap(RC.cam.image, 300, 400, false), "field.jpg");
    }

    @Override
    public void loopOpMode() {

    }
}
