package org.firstinspires.ftc.teamcode.roboticslibrary;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;

import com.vuforia.CameraDevice;
import com.vuforia.Frame;
import com.vuforia.Image;
import com.vuforia.PIXEL_FORMAT;
import com.vuforia.State;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.internal.VuforiaLocalizerImpl;
import org.firstinspires.ftc.robotcore.internal.opengl.AutoConfigGLSurfaceView;

import java.nio.ByteBuffer;

/**
 * Created by FIXIT on 16-09-15.
 */
public class FXTVuforiaLocalizer extends VuforiaLocalizerImpl {

    public Image imageRGB565;

    class VuforiaCallbackSubclass extends VuforiaCallback
    {
        @Override public synchronized void Vuforia_onUpdate(State state)
        {
            super.Vuforia_onUpdate(state);
            Log.i("REGISTER", "" + (state == null));
            Frame f = state.getFrame();
            Log.i("FRAME", (f == null) + "");
    //
            long num = f.getNumImages();
            Log.i("NUMHELLO", num + "");

            for (int i = 0; i < num; i++) {
//                Log.i("Hello", f.getImage(i).getFormat() + "");
                if (f.getImage(i).getFormat() == PIXEL_FORMAT.RGB565) {


                    if (f.getImage(i).getHeight() * f.getImage(i).getWidth() > 0) {
                        imageRGB565 = f.getImage(i);
                    }

                    if (imageRGB565 != null) {
//                        byte[] array = new byte[imageRGB565.remaining()];
//                        imageRGB565.get(array);
//                        Log.i("Total", "" + (array.length) + "--" + array[0]);
                    }
                }
            }//for

        }
    }

    public FXTVuforiaLocalizer(VuforiaLocalizer.Parameters parameters)
    {
        super(parameters);


        this.vuforiaCallback = new VuforiaCallbackSubclass();

        pauseAR();
        Vuforia.registerCallback(vuforiaCallback);
        resumeAR();

    }

    protected void startAREdited() {
        Object var1 = this.startStopLock;
        synchronized(this.startStopLock) {
            this.showLoadingIndicator(0);
            this.updateActivityOrientation();
            this.vuforiaFlags = 2;
            Vuforia.setInitParameters(this.activity, this.vuforiaFlags, this.parameters.vuforiaLicenseKey);
            boolean initProgress = true;

            int initProgress1;
            do {
                initProgress1 = Vuforia.init();
            } while(initProgress1 >= 0 && initProgress1 < 100);

            if(initProgress1 < 0) {
                throwFailure("%s", new Object[]{this.getInitializationErrorString(initProgress1)});
            }

            this.initTracker();
            Vuforia.registerCallback(this.vuforiaCallback);
            boolean depthSize = true;
            boolean stencilSize = false;
            final boolean translucent = Vuforia.requiresAlpha();

            if(this.glSurfaceParent != null) {
                this.appUtil.synchronousRunOnUiThread(new Runnable() {
                    public void run() {
                        FXTVuforiaLocalizer.super.glSurface = new AutoConfigGLSurfaceView(FXTVuforiaLocalizer.super.activity);
                        FXTVuforiaLocalizer.super.glSurface.init(translucent, 16, 0);
//                        FXTVuforiaLocalizer.super.glSurface.setRenderer(FXTVuforiaLocalizer.super.glSurfaceViewRenderer);
//                        FXTVuforiaLocalizer.super.glSurface.setVisibility(View.INVISIBLE);
//                        VuforiaLocalizerImpl.this.glSurfaceParent.addView(VuforiaLocalizerImpl.this.glSurface);
                    }
                });
            }
            this.wantCamera = true;
            this.startCamera(this.parameters.cameraDirection.direction);
            CameraDevice.getInstance().setFocusMode(2);
            this.rendererIsActive = true;
            this.showLoadingIndicator(4);
        }
    }

    private String getInitializationErrorString(int code) {
        switch(code) {
            case -9:
                return this.activity.getString(com.qualcomm.robotcore.R.string.VUFORIA_INIT_LICENSE_ERROR_PRODUCT_TYPE_MISMATCH);
            case -8:
                return this.activity.getString(com.qualcomm.robotcore.R.string.VUFORIA_INIT_LICENSE_ERROR_CANCELED_KEY);
            case -7:
                return this.activity.getString(com.qualcomm.robotcore.R.string.VUFORIA_INIT_LICENSE_ERROR_NO_NETWORK_TRANSIENT);
            case -6:
                return this.activity.getString(com.qualcomm.robotcore.R.string.VUFORIA_INIT_LICENSE_ERROR_NO_NETWORK_PERMANENT);
            case -5:
                return this.activity.getString(com.qualcomm.robotcore.R.string.VUFORIA_INIT_LICENSE_ERROR_INVALID_KEY);
            case -4:
                return this.activity.getString(com.qualcomm.robotcore.R.string.VUFORIA_INIT_LICENSE_ERROR_MISSING_KEY);
            case -3:
                return this.activity.getString(com.qualcomm.robotcore.R.string.VUFORIA_INIT_ERROR_NO_CAMERA_ACCESS);
            case -2:
                return this.activity.getString(com.qualcomm.robotcore.R.string.VUFORIA_INIT_ERROR_DEVICE_NOT_SUPPORTED);
            default:
                return this.activity.getString(com.qualcomm.robotcore.R.string.VUFORIA_INIT_LICENSE_ERROR_UNKNOWN_ERROR);
        }
    }

    private void updateActivityOrientation() {
        Configuration config = this.activity.getResources().getConfiguration();
        switch(config.orientation) {
            case 0:
            case 1:
            default:
                this.isPortrait = true;
                break;
            case 2:
                this.isPortrait = false;
        }

    }
}
