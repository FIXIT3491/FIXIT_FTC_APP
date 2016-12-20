package org.firstinspires.ftc.teamcode.roboticslibrary;

import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.view.TextureView;
import android.widget.Toast;

import org.firstinspires.ftc.robotcontroller.internal.FtcControllerUtils;
import org.firstinspires.ftc.teamcode.R;
import org.firstinspires.ftc.teamcode.RC;

/**
 * Created by FIXIT on 15-09-20.
 */
@SuppressWarnings("deprecation")
public class FXTCamera implements TextureView.SurfaceTextureListener {

    Camera cam;
    private Bitmap lastTakenBit = Bitmap.createBitmap(640, 480, Bitmap.Config.ARGB_8888);
    private TextureView previewTexture;

    private boolean displayStream = false;

    public final static int FACING_BACKWARD = Camera.CameraInfo.CAMERA_FACING_BACK;
    public final static int FACING_FORWARD = Camera.CameraInfo.CAMERA_FACING_FRONT;

    public FXTCamera(int direction, boolean displayStream) {

        int numCams = Camera.getNumberOfCameras();

        int camID = 0;
        for (int i = 0; i < numCams; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == direction) {
                camID = i;
                break;
            }//if
        }//for

        try {
            cam = Camera.open(camID);
        } catch (RuntimeException e) {
            Toast.makeText(RC.c(), "The camera is already being used in another app!", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return;
        }//catch

        Camera.Parameters params = cam.getParameters();
        params.setPreviewSize(640, 480);
        params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        params.setPictureFormat(ImageFormat.JPEG);

        cam.setParameters(params);

        this.displayStream = displayStream;

        this.previewTexture = new TextureView(RC.c());
        if (displayStream) {
            this.previewTexture.setRotation(90f);
            FtcControllerUtils.addView(previewTexture, R.id.cameraMonitorViewId);
        }//if

        this.previewTexture.setSurfaceTextureListener(this);

    }//FXTCamera

    public void pause() {
        cam.stopPreview();
    }//pause

    public void resume() {
        cam.startPreview();
    }//resume

    public void destroy() {

        try {
            if (displayStream) {
                FtcControllerUtils.emptyView(R.id.cameraMonitorViewId);
            }//if

            cam.stopPreview();
            cam.release();
        } catch (Exception e) {
            e.printStackTrace();
        }//catch

    }//destroy

    public Bitmap photo() {
        synchronized (lastTakenBit) {
            return lastTakenBit;
        }//synchronized
    }//photo

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        try {
            cam.setPreviewTexture(previewTexture.getSurfaceTexture());
        } catch (Exception e) {
            e.printStackTrace();
        }//catch

        resume();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        //do nothing...
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        destroy();
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

        synchronized (lastTakenBit) {
            lastTakenBit = previewTexture.getBitmap();
        }//synchronized

    }//onSurfaceTextureUpdated

}//FXTCamera
