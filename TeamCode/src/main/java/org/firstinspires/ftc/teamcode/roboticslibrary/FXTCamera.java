package org.firstinspires.ftc.teamcode.roboticslibrary;

import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.view.TextureView;
import android.widget.Toast;

import org.firstinspires.ftc.robotcontroller.internal.FtcControllerUtils;
import org.firstinspires.ftc.teamcode.R;
import org.firstinspires.ftc.teamcode.RC;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by FIXIT on 15-09-20.
 */
@SuppressWarnings("deprecation")
public class FXTCamera implements TextureView.SurfaceTextureListener {

    Camera cam;
    private Bitmap lastTakenBit = Bitmap.createBitmap(640, 480, Bitmap.Config.ARGB_8888);
    private TextureView previewTexture;

    private boolean destroyed = false;
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

        params.setAutoExposureLock(false);
        cam.setParameters(params);

        this.displayStream = displayStream;

        this.previewTexture = new TextureView(RC.c());
        if (displayStream) {
            this.previewTexture.setRotation(90f);
            FtcControllerUtils.addView(previewTexture, R.id.cameraMonitorViewId);
        }//if

        this.previewTexture.setSurfaceTextureListener(this);

    }//FXTCamera

    public Camera getBaseCamera() {
        return cam;
    }//getBaseCamera

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
            destroyed = true;
        } catch (Exception e) {
            e.printStackTrace();
        }//catch

    }//destroy

    public void setExposure(int i) {
        Camera.Parameters params = cam.getParameters();
        params.setExposureCompensation(i);
        cam.setParameters(params);
    }

    public Bitmap photo() {
        synchronized (lastTakenBit) {
            return lastTakenBit;
        }//synchronized
    }//photo

    public static void saveBitmap(Bitmap bm, String name) {
        ContextWrapper cw = new ContextWrapper(RC.c().getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = RC.c().getExternalFilesDir("");
        // Create imageDir
        File mypath = new File(directory, name + ".jpg");

        FileOutputStream file = null;
        try {
            file = new FileOutputStream(mypath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {

            // Use the compress method on the BitMap object to write image to the OutputStream
            bm.compress(Bitmap.CompressFormat.PNG, 100, file);

            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveFrame(String name){
        saveBitmap(lastTakenBit, name);
    }

    public void lockExposure(){
        Camera.Parameters params = cam.getParameters();
        params.setAutoExposureLock(true);
        params.setExposureCompensation(params.getMinExposureCompensation());
        cam.setParameters(params);
    }

    public void unlockExposure(){
        Camera.Parameters params = cam.getParameters();
        params.setAutoExposureLock(false);
        cam.setParameters(params);
    }

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
        if (!destroyed) {
            destroy();
        }//if

        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

        synchronized (lastTakenBit) {
            lastTakenBit = previewTexture.getBitmap();
        }//synchronized

    }//onSurfaceTextureUpdated

    public Bitmap getImage(){
        return lastTakenBit;
    }

}//FXTCamera
