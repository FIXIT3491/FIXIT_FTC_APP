package org.firstinspires.ftc.teamcode.roboticslibrary;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.Log;

import java.util.List;

/**
 * Created by FIXIT on 15-09-20.
 */
public abstract class InvisiCam {

    Camera cam;
    private byte[] imageData;
    public boolean waitingForCallback = false;

    public InvisiCam() {
        cam = Camera.open();

        try {
            cam.setPreviewTexture(new SurfaceTexture(10));
        } catch (Exception e) {
            e.printStackTrace();
        }


        Camera.Parameters params = cam.getParameters();

        List<Camera.Size> sizes = params.getSupportedPictureSizes();

        for (int i = 0; i < sizes.size(); i++) {
            Log.i("Size", sizes.get(i).width + ", " + sizes.get(i).height);
        }

        params.setPreviewSize(160, 120);
        params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        params.setPictureFormat(ImageFormat.JPEG);
        cam.setParameters(params);
        cam.startPreview();

    }

    public void takePhoto() {

        waitingForCallback = true;

        cam.takePicture(null, null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {

                waitingForCallback = false;
            useBitmap(BitmapFactory.decodeByteArray(data, 0, data.length));

            }
        });
    }

    public abstract void useBitmap(Bitmap bit);

    public void dispose() {
        cam.stopPreview();
        cam.release();
    }

}
