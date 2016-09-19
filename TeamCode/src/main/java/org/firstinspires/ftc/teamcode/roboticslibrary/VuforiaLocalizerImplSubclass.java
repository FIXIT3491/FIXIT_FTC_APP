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
 /** {@link CloseableFrame} exposes a close() method so that we can proactively
 * reduce memory pressure when we're done with a Frame */


// A hack that works around lack of access in v2.2 to camera image data when Vuforia is running
// Note: this may or may not be supported in future releases.
public class VuforiaLocalizerImplSubclass extends VuforiaLocalizerImpl {

    public Image rgb565 = null;

    static class CloseableFrame extends Frame {
        public CloseableFrame(Frame other) { // clone the frame so we can be useful beyond callback
            super(other);
        }
        public void close() {
            super.delete();
        }
    }

    class VuforiaCallbackSubclass extends VuforiaLocalizerImpl.VuforiaCallback {

        @Override public synchronized void Vuforia_onUpdate(State state) {
            super.Vuforia_onUpdate(state);
            CloseableFrame frame = new CloseableFrame(state.getFrame());

            long numImgs = frame.getNumImages();

            for (int i = 0; i < numImgs; i++) {
                if (frame.getImage(i).getFormat() == PIXEL_FORMAT.RGB565) {
                    rgb565 = frame.getImage(i);
                }
            }

            frame.close();
        }
    }

    public VuforiaLocalizerImplSubclass(VuforiaLocalizer.Parameters parameters) {
        super(parameters);
        stopAR();
        clearGlSurface();

        this.vuforiaCallback = new VuforiaCallbackSubclass();
        startAR();

        // Optional: set the pixel format(s) that you want to have in the callback
        Vuforia.setFrameFormat(PIXEL_FORMAT.RGB565, true);
    }

    void clearGlSurface() {
        if (this.glSurfaceParent != null) {
            appUtil.synchronousRunOnUiThread(new Runnable() {
                @Override public void run() {
                    glSurfaceParent.removeAllViews();
                    glSurfaceParent.getOverlay().clear();
                    glSurface = null;
                }
            });
        }
    }
}
