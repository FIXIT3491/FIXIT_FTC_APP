package org.firstinspires.ftc.teamcode.gamecode;

import android.graphics.Bitmap;
import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.vuforia.CameraDevice;
import com.vuforia.HINT;
import com.vuforia.ImageTarget;
import com.vuforia.Matrix34F;
import com.vuforia.ObjectTracker;
import com.vuforia.PIXEL_FORMAT;
import com.vuforia.State;
import com.vuforia.Tool;
import com.vuforia.TrackableResult;
import com.vuforia.Tracker;
import com.vuforia.TrackerManager;
import com.vuforia.Vec2F;
import com.vuforia.Vec3F;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.robotcore.internal.AppUtil;
import org.firstinspires.ftc.teamcode.R;

import org.firstinspires.ftc.teamcode.RC;
import org.firstinspires.ftc.teamcode.opmodesupport.TeleOpMode;
import org.firstinspires.ftc.teamcode.roboticslibrary.OCVUtils;
import org.firstinspires.ftc.teamcode.roboticslibrary.VuforiaLocalizerImplSubclass;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Created by FIXIT on 16-09-15.
 */
@Autonomous
public class NewVuforiaOp extends TeleOpMode {

    VuforiaLocalizerImplSubclass locale;

    boolean hello = false;
    VuforiaTrackables tracks;
    ObjectTracker objTracker;

    @Override
    public void initialize() {


        VuforiaLocalizer.Parameters params = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId);
        params.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        params.vuforiaLicenseKey = "Ad0I0ir/////AAAAAfR3NIO1HkxSqM8NPhlEftFXtFAm6DC5w4Cjcy30WUdGozklFlAkxeHpjfWc4moeL2ZTPvZ+wAoyOnlZxyB6Wr1BRE9154j6K1/8tPvu21y5ke1MIbyoJ/5BAQuiwoAadjptZ8fpS7A0QGPrMe0VauJIM1mW3UU2ezYFSOcPghCOCvQ8zid1Bb8A92IkbLcBUcv3DEC6ia4SEkbRMY7TpOh2gzsXdsue4tqj9g7vj7zBU5Hu4WhkMDJRsThn+5QoHXqvavDsCElwmDHG3hlEYo7qN/vV9VcQUX9XnVLuDeZhkp885BHK5vAe8T9W3Vxj2H/R4oijQso6hEBaXsOpCHIWGcuphpoe9yoQlmNRRZ97";

        locale = new VuforiaLocalizerImplSubclass(params);

        Vuforia.setFrameFormat(PIXEL_FORMAT.RGB565, true);
        Vuforia.setHint(HINT.HINT_MAX_SIMULTANEOUS_IMAGE_TARGETS, 4);

        tracks = locale.loadTrackablesFromAsset("FTC_2016-17");
        tracks.get(0).setName("Wheels");
        tracks.get(1).setName("Tools");
        tracks.get(2).setName("Lego");
        tracks.get(3).setName("Gears");

        tracks.activate();
    }

    @Override
    public void loopOpMode() {

        for (VuforiaTrackable track : tracks) {


            OpenGLMatrix pose = ((VuforiaTrackableDefaultListener) track.getListener()).getRawPose();

            if (pose != null) {

                Matrix34F rawPose = new Matrix34F();
                float[] poseData = Arrays.copyOfRange(pose.transposed().getData(), 0, 12);

                rawPose.setData(poseData);

                if (!hello && locale.rgb565 != null) {
                    float[][] corners = getCorners(rawPose);

                    Bitmap bm = Bitmap.createBitmap(locale.rgb565.getWidth(), locale.rgb565.getHeight(), Bitmap.Config.RGB_565);
                    bm.copyPixelsFromBuffer(locale.rgb565.getPixels());

                    Mat crop = OCVUtils.bitmapToMat(bm, CvType.CV_8UC3);
                    float x = Math.min(Math.min(corners[1][0], corners[3][0]), Math.min(corners[0][0], corners[2][0]));
                    float y = Math.min(Math.min(corners[1][1], corners[3][1]), Math.min(corners[0][1], corners[2][1]));
                    float width = Math.max(Math.abs(corners[0][0] - corners[2][0]), Math.abs(corners[1][0] - corners[3][0]));
                    float height = Math.max(Math.abs(corners[0][1] - corners[2][1]), Math.abs(corners[1][1] - corners[3][1]));

                    Log.i("Size", x + "---|" + y + "----|" + width + "---|" + height + "---|" + crop.size());
                    Mat cropped = new Mat(crop, new Rect((int) x, (int) y, (int) width, (int) height));

                    final Bitmap bit = OCVUtils.matToBitmap(cropped);

                    AppUtil.getInstance().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((FtcRobotControllerActivity) AppUtil.getInstance().getActivity()).img.setImageBitmap(bit);
                        }
                    });

                    hello = true;

                }

            }//if

        }

    }

    public float[][] getCorners(Matrix34F pose) {

        float[][] corners = new float[4][2];
        Vec2F[] cornerVecs = new Vec2F[4];

        cornerVecs[0] = Tool.projectPoint(locale.getCameraCalibration(), pose, new Vec3F(-127, 92, 0)); //upper left
        cornerVecs[1] = Tool.projectPoint(locale.getCameraCalibration(), pose, new Vec3F(127, 92, 0)); //upper right
        cornerVecs[2] = Tool.projectPoint(locale.getCameraCalibration(), pose, new Vec3F(127, -92, 0)); //lower right
        cornerVecs[3] = Tool.projectPoint(locale.getCameraCalibration(), pose, new Vec3F(-127, -92, 0)); //lower left

        for (int i = 0; i < 4; i++) {
            corners[i] = cornerVecs[i].getData();
        }//for

        corners[0][0] -= Math.abs(corners[0][0] - corners[3][0]);
        corners[0][0] = Math.max(corners[0][0], 0);

        corners[0][0] -= Math.abs(corners[0][1] - corners[3][1]);
        corners[0][1] = Math.max(corners[0][1], 0);


        return corners;
    }



}
