package com.qualcomm.ftcrobotcontroller.roboticslibrary;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.util.Log;
import android.view.TextureView;
import android.view.View;

import com.qualcomm.ftcrobotcontroller.RC;

/**
 * Created by Nirzvi on 2015-05-22.
 */
public class FXTCamera implements TextureView.SurfaceTextureListener {

    final static String LOGTAG = "FXTCamera";

    private Camera cam;
    public Bitmap image;

    private boolean init = false;
    private boolean running = false;
    private TextureView texture;


    public FXTCamera(TextureView texture) {
        texture.setSurfaceTextureListener(FXTCamera.this);
        this.texture = texture;
    }//FXTCamera

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        cam = Camera.open();

        Camera.Parameters param = cam.getParameters();

        param.setAutoWhiteBalanceLock(true);
        param.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_FLUORESCENT);

        cam.setParameters(param);

        init = true;

        try {
            cam.setPreviewTexture(surface);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(LOGTAG, "Camera initialized");

    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        destroy();

        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        image = texture.getBitmap();

        Matrix rot = new Matrix();
        rot.postRotate(90);

        image = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), rot, true);

        onCameraUpdate(image);
    }

    public void onCameraUpdate(Bitmap b) {
        //User-defined method
    }

    public void stop() {
        if (init && running && cam != null) {
            running = false;
            cam.stopPreview();
            RC.a.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    texture.setVisibility(View.INVISIBLE);
                }//run

            });
        }//if
    }

    public void start() {
        if (init && !running && cam != null) {
            running = true;
            RC.a.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    texture.setVisibility(View.VISIBLE);
                }//run

            });
            cam.startPreview();
        }//if
    }

    public void destroy() {
        if (init) {
            Log.d(LOGTAG, "Deconstructing camera...");

            init = false;

            if (running) {
                stop();
            }//if

            cam.release();
        }//if

        cam = null;
    }//destroy

    public Bitmap takePhoto() {

        final byte[][] bytesOfPicture = new byte[1][];

        cam.takePicture(null, null, null, new PictureCallback() {

            Bitmap picture;

            @Override
            public void onPictureTaken(byte[] bytes, Camera camera) {
                bytesOfPicture[0] = bytes.clone();
            }

        });

        return BitmapFactory.decodeByteArray(bytesOfPicture[0], 0, bytesOfPicture[0].length);
    }


}
