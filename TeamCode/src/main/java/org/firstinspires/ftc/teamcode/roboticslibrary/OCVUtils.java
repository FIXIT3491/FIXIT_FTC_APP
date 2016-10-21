package org.firstinspires.ftc.teamcode.roboticslibrary;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.firstinspires.ftc.teamcode.RC;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by FIXIT on 16-07-04.
 */
public final class OCVUtils {

    final static String LOGTAG = "FTC OpenCV";

    public static Mat bitmapToMat (Bitmap bit, int cvType) {
        Mat newMat = new Mat(bit.getHeight(), bit.getWidth(), cvType);

        Utils.bitmapToMat(bit, newMat);

        return newMat;
    }

    public static Bitmap matToBitmap (Mat mat) {
        Bitmap newBit = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888);

        Utils.matToBitmap(mat, newBit);

        return newBit;
    }

    public static Mat getMatFromAssets(String filename) {
        Mat out = null;

        try {
            AssetManager assetManager = RC.c().getAssets();
            InputStream istr = null;

            try {
                istr = assetManager.open(filename);
            } catch (IOException e) {
                e.printStackTrace();
            }//catch

            Bitmap bitmap = BitmapFactory.decodeStream(istr);

            out = bitmapToMat(bitmap, CvType.CV_8UC3);

        } catch (Exception e) {
            e.printStackTrace();
        }//catch

        return out;
    }//getMatFromAssets

    public static Mat getMatFromFile(String filename) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.ARGB_8888;

        Bitmap bm = BitmapFactory.decodeFile(RC.c().getExternalFilesDir(null).getAbsolutePath() + "/" + filename, opt);

        Mat out = bitmapToMat(bm, CvType.CV_8UC3);

        return out;
    }//getMatFromFile

    public static void hsvNullifyValue(Mat process) {

        int size = (int) (process.total() * process.channels());

        byte[] temp = new byte[size];

        process.get(0, 0, temp);
        for (int i = 2; i < size; i += 3) {
            temp[i] = -1;
        }//for

        process.put(0, 0, temp);

    }

}
