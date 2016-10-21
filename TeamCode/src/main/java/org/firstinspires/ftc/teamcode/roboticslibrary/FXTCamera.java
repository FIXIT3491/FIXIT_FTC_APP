package org.firstinspires.ftc.teamcode.roboticslibrary;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.Log;
import android.view.TextureView;
import android.widget.Toast;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import org.firstinspires.ftc.robotcore.internal.AppUtil;
import org.firstinspires.ftc.teamcode.RC;
import org.firstinspires.ftc.teamcode.opmodesupport.TaskHandler;

/**
 * Created by FIXIT on 15-09-20.
 */
public class FXTCamera implements TextureView.SurfaceTextureListener {

    Camera cam;
    private Bitmap lastTakenBit = Bitmap.createBitmap(640, 480, Bitmap.Config.ARGB_8888);
    private TextureView previewTexture;

    private boolean displayStream = false;

    public final static int FACING_FORWARD = Camera.CameraInfo.CAMERA_FACING_FRONT;
    public final static int FACING_BACKWARD = Camera.CameraInfo.CAMERA_FACING_BACK;

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
        params.setPictureSize(640, 480);
        params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        params.setPictureFormat(ImageFormat.JPEG);

        cam.setParameters(params);

        this.displayStream = displayStream;
        if (displayStream) {
            RC.a().addCameraStream();
            this.previewTexture = RC.a().display;
        } else {
            this.previewTexture = new TextureView(RC.c());
        }//else

        this.previewTexture.setSurfaceTextureListener(this);

    }//FXTCamera

    public void pause() {
        cam.stopPreview();
    }//pause

    public void resume() {
        cam.startPreview();
    }//resume

    public void destroy() {

        if (displayStream) {
            RC.a().emptyCameraStream();
        }//if

        cam.stopPreview();
        cam.release();

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
