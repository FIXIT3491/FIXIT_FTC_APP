package com.qualcomm.ftcrobotcontroller.roboticslibrary;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.vuforia.CameraDevice;
import com.vuforia.DataSet;
import com.vuforia.ObjectTracker;
import com.vuforia.STORAGE_TYPE;
import com.vuforia.State;
import com.vuforia.Trackable;
import com.vuforia.TrackableResult;
import com.vuforia.Tracker;
import com.vuforia.TrackerManager;
import com.vuforia.Vuforia;
import com.vuforia.VuforiaBase;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class that allows for the implementation of the Vuforia sdk in the ftc app.
 * Most of this code is modified and comes from the Vuforia Sample app
 */
public class FTCVuforia implements Vuforia.UpdateCallbackInterface {

    private final Object shutdownLock = new Object();
    private final Object dataLock = new Object();

    String LOGTAG = "FTC Vuforia";

    private InitVuforiaTask mInitTask;
    private LoadTrackerTask mLoadTask;

    private Activity activity;
    private boolean mStarted = false;
    private boolean mInit = false;
    private boolean mCameraRunning = false;

    private HashMap<String, double[]> vuforiaData = new HashMap<String, double[]>();

    private ArrayList<DataSet> mDatasets = new ArrayList<DataSet>();
    private ArrayList<String> fileNames = new ArrayList<String>();

    /**
     * This is the constructor for the Vuforia object
     *
     * @param activity The activity that the task is bound to. Likely
     *                 to be FtcRobotControllerActivity
     */
    public FTCVuforia(Activity activity) {
        this.activity = activity;
    }//FTCVuforia

    /**
     * This starts the background task to load the JNI libraries for Vuforia
     */
    public void initVuforia() {

        try {
            mInitTask = new InitVuforiaTask();
            mInitTask.execute();
        } catch (Exception e) {
            e.printStackTrace();
            onInitDone(false);
        }//catch
    }//initVuforia

    /**
     * After the libraries are loaded the camera is started by Vuforia
     *
     * @param success Whether or not the JNI libraries were correctly loaded
     * @return true if the camera starts false if not
     */
    private boolean onInitDone(boolean success) {

        if (success) {
            startVuforiaCamera();

            mInit = true;
            return true;
        }//if

        return false;
    }//onInitDone

    /**
     * This kills any initialization processes. It stops that camera and Vuforia to save battery
     * @throws Exception if the Vuforia SDK cannot de-initialize
     */
    public void destroy() throws Exception {

        // Cancel potentially running background tasks
        if (mInitTask != null && mInitTask.getStatus() != InitVuforiaTask.Status.FINISHED) {
            mInitTask.cancel(true);
        }//if

        if (mLoadTask != null && mLoadTask.getStatus() != LoadTrackerTask.Status.FINISHED) {
            mLoadTask.cancel(true);
        }//if

        mInitTask = null;
        mLoadTask = null;

        mInit = false;
        mStarted = false;

        stopCamera();

        fileNames.clear();

        // Ensure that all asynchronous operations to initialize Vuforia
        // and loading the tracker datasets do not overlap:
        synchronized (shutdownLock) {

            boolean unloadTrackersResult;
            boolean deinitTrackersResult;

            // Destroy the tracking data set:
            unloadTrackersResult = doUnloadTrackersData();

            // Deinitialize the trackers:
            deinitTrackersResult = doDeinitTrackers();

            // Deinitialize Vuforia SDK:
            Vuforia.deinit();

            if (!unloadTrackersResult) {
                throw new RuntimeException("Trackers Failed to Unload");
            }//if

            if (!deinitTrackersResult) {
                throw new RuntimeException("Failed to deinitialize trackers");
            }//if

        }//synchronized
    }//destroy

    /**
     * If the activity's onPause method is called the camera should
     * be released and tracking temporarily disabled.
     * To learn about onPause look an Activity lifecycle
     */
    public void pauseVuforia() {

        if (mInit) {
            if (mStarted) {
                stopCamera();
            }//if

            Vuforia.onPause();
        }//if

    }//pauseVuforia

    /**
     * If the activity resumes from bring paused Vuforia must start
     * tracking again. Also the camera is reopened.
     */
    public void resumeVuforia() {
        if (mInit) {
            Vuforia.onResume();

            if (mStarted) {
                startVuforiaCamera();
            }//if
        }//if
    }//resumeVuforia

    /**
     * Initializes Vuforia's object tracker
     * @return true if successful
     */
    private boolean doInitTrackers() {

        TrackerManager tManager = TrackerManager.getInstance();
        Tracker track = tManager.initTracker(ObjectTracker.getClassType());

        return track != null;
    }//doInitTrackers

    /**
     * De initializes Vuforia's object trackers
     * @return true if successful
     */
    private boolean doDeinitTrackers() {

        TrackerManager tManager = TrackerManager.getInstance();
        return tManager.deinitTracker(ObjectTracker.getClassType());
    }//doDeinitTrackers

    /**
     * This gets the trackers to begin looking for any objects requested.
     * @return true if successful
     */
    private boolean doStartTrackers() {

        Tracker track = TrackerManager.getInstance().getTracker(ObjectTracker.getClassType());

        if (track != null) {
            track.start();
        } else {
            return false;
        }//else

        return true;
    }//doStartTrackers

    /**
     * Stops tracking objects
     */
    private void doStopTrackers() {

        Tracker objectTracker = TrackerManager.getInstance().getTracker(ObjectTracker.getClassType());

        if (objectTracker != null) {
            objectTracker.stop();
        }//if
    }//doStopTrackers

    /**
     * Add trackable datasets into Vuforia
     * Must be called before initialization
     * @param files All the files to be trackes
     */
    public void addTrackables(String... files) {
        for (int i = 0; i < files.length; i++) {
            fileNames.add(files[i]);
        }//for
    }//addTrackables

    /**
     * This loads user defined data files to tell Vuforia what to look for
     * @return true if the data is successfully loaded
     */
    private boolean doLoadTrackersData() {

        TrackerManager tManager = TrackerManager.getInstance();
        ObjectTracker objectTracker = (ObjectTracker) tManager.getTracker(ObjectTracker.getClassType());

        if (objectTracker == null) {
            return false;
        }//if

        for (int i = 0; i < fileNames.size(); i++) {
            mDatasets.add(objectTracker.createDataSet());

            if (mDatasets.get(i) == null) {
                return false;
            }//if

            Log.i(LOGTAG, "Loading Dataset: " + fileNames.get(i));

            if (!mDatasets.get(i).load(fileNames.get(i), STORAGE_TYPE.STORAGE_APPRESOURCE)) {
                return false;
            }//if

            if (!objectTracker.activateDataSet(mDatasets.get(i))) {
                return false;
            }//if

            int numTrackables = mDatasets.get(i).getNumTrackables();
            for (int count = 0; count < numTrackables; count++) {
                Trackable trackable = mDatasets.get(i).getTrackable(count);

                String name = "Current Dataset: " + trackable.getName();
                trackable.setUserData(name);

                Log.d(LOGTAG, "UserData: Set the following user data " + trackable.getUserData());
            }//for

        }//for

        return true;
    }//doLoadTrackersData

    /**
     * Unloads all trackable data files when Vuforia is finished
     * @return true if successful
     */
    private boolean doUnloadTrackersData() {

        // Indicate if the trackers were unloaded correctly
        boolean result = true;

        TrackerManager tManager = TrackerManager.getInstance();
        ObjectTracker objectTracker = (ObjectTracker) tManager.getTracker(ObjectTracker.getClassType());

        if (objectTracker == null) {
            return false;
        }//if

        for (int i = 0; i < mDatasets.size(); i++) {
            if (mDatasets.get(i) != null && mDatasets.get(i).isActive()) {
                if (objectTracker.getActiveDataSet().equals(mDatasets.get(i)) && !objectTracker.deactivateDataSet(mDatasets.get(i))) {
                    result = false;
                } else if (!objectTracker.destroyDataSet(mDatasets.get(i))) {
                    result = false;
                }//else if

                mDatasets.remove(i);
            }//if
        }//for

        return result;
    }//doUnloadTrackersData

    /**
     * Starts the camera
     * @return false if there is a camera error
     */
    private boolean startVuforiaCamera() {

        if (!CameraDevice.getInstance().init(CameraDevice.CAMERA_DIRECTION.CAMERA_DIRECTION_DEFAULT)) {
            return false;
        }//if

        if (!CameraDevice.getInstance().selectVideoMode(CameraDevice.MODE.MODE_DEFAULT)) {
            return false;
        }//if

        if (!CameraDevice.getInstance().start()) {
            return false;
        }//if

        doStartTrackers();

        if (!CameraDevice.getInstance().setFocusMode(CameraDevice.FOCUS_MODE.FOCUS_MODE_CONTINUOUSAUTO)) {

            if (!CameraDevice.getInstance().setFocusMode(CameraDevice.FOCUS_MODE.FOCUS_MODE_TRIGGERAUTO)) {

                CameraDevice.getInstance().setFocusMode(CameraDevice.FOCUS_MODE.FOCUS_MODE_NORMAL);
            }//if
        }//if

        mCameraRunning = true;
        return true;
    }//startVuforiaCamera

    /**
     * Turns off the camera
     */
    private void stopCamera() {

        if (mCameraRunning) {
            doStopTrackers();
            CameraDevice.getInstance().stop();
            CameraDevice.getInstance().deinit();
            mCameraRunning = false;
        }//if

    }//stopCamera

    /**
     * This is the Vuforia callback. When Vuforia finds an object
     * this method is called
     * @param s Info about the objects
     */
    @Override
    public void Vuforia_onUpdate(State s) {

        synchronized (dataLock) {

            int numResults = s.getNumTrackableResults();
            vuforiaData.clear();

            for (int i = 0; i < numResults; i++) {
                TrackableResult result = s.getTrackableResult(i);

                if (result != null) {

                    // Convert values into useful data
                    //Angles are in radians distances are relative to the calibration image
                    //Also a timestamp is added
                    float[] data = result.getPose().getData();

                    float[][] rotation = {{data[0], data[1], data[2]},
                            {data[4], data[5], data[6]},
                            {data[8], data[9], data[10]}};

                    double thetaX = Math.atan2(rotation[2][1], rotation[2][2]);
                    double thetaY = Math.atan2(-rotation[2][0], Math.sqrt(rotation[2][1] * rotation[2][1] + rotation[2][2] * rotation[2][2]));
                    double thetaZ = Math.atan2(rotation[1][0], rotation[0][0]);

                    double[] tempVuforiaData = new double[7];

                    tempVuforiaData[0] = thetaX;
                    tempVuforiaData[1] = thetaY;
                    tempVuforiaData[2] = thetaZ;
                    tempVuforiaData[3] = data[3];
                    tempVuforiaData[4] = data[7];
                    tempVuforiaData[5] = data[11];
                    tempVuforiaData[6] = System.currentTimeMillis();

                    vuforiaData.put(result.getTrackable().getName(), tempVuforiaData);
                }//if
            }//for
        }//synchronized

    }//Vuforia_onUpdate

    /**
     * This returns the most recent data set
     * The data key is the dataset name given in calibration
     * In the array of info the indeces are
     * <ol start="0">
     *     <li>rotation x(roll)</li>
     *     <li>rotation y(pitch)</li>
     *     <li>rotation z(yaw)</li>
     *     <li>distance x</li>
     *     <li>distance y</li>
     *     <li>distance z</li>
     *     <li>time the data was recorded in milliseconds</li>
     * @return the data
     */
    public HashMap<String, double[]> getVuforiaData() {
        synchronized (dataLock) {
            return vuforiaData;
        }//synchronized
    }//getVuforiaData

    /**
     * A background task for intializing the Vuforia sdk
     */
    private class InitVuforiaTask extends AsyncTask<Void, Integer, Boolean> {
        // Initialize with invalid value:
        private int mProgressValue = -1;

        protected Boolean doInBackground(Void... params) {

            // Prevent the onDestroy() method to overlap with initialization:
            synchronized (shutdownLock) {

                Vuforia.setInitParameters(activity, VuforiaBase.GL_20, "Ad0I0ir/////AAAAAfR3NIO1HkxSqM8NPhlEftFXtFAm6DC5w4Cjcy30WUdGozklFlAkxeHpjfWc4moeL2ZTPvZ+wAoyOnlZxyB6Wr1BRE9154j6K1/8tPvu21y5ke1MIbyoJ/5BAQuiwoAadjptZ8fpS7A0QGPrMe0VauJIM1mW3UU2ezYFSOcPghCOCvQ8zid1Bb8A92IkbLcBUcv3DEC6ia4SEkbRMY7TpOh2gzsXdsue4tqj9g7vj7zBU5Hu4WhkMDJRsThn+5QoHXqvavDsCElwmDHG3hlEYo7qN/vV9VcQUX9XnVLuDeZhkp885BHK5vAe8T9W3Vxj2H/R4oijQso6hEBaXsOpCHIWGcuphpoe9yoQlmNRRZ97");

                do {
                    // Vuforia.init() blocks until an initialization step is
                    // complete, then it proceeds to the next step and reports
                    // progress in percents (0 ... 100%).
                    // If Vuforia.init() returns -1, it indicates an error.
                    // Initialization is done when progress has reached 100%.
                    mProgressValue = Vuforia.init();

                    // Publish the progress value:
                    publishProgress(mProgressValue);

                    // We check whether the task has been canceled in the
                    // meantime (by calling AsyncTask.cancel(true)).
                    // and bail out if it has, thus stopping this thread.
                    // This is necessary as the AsyncTask will run to completion
                    // regardless of the status of the component that
                    // started is.
                } while (!isCancelled() && mProgressValue >= 0 && mProgressValue < 100);

                return (mProgressValue > 0);

            }//synchronized

        }//doInBackground

        protected void onPostExecute(Boolean result) {
            // Done initializing Vuforia, proceed to next application
            // initialization status:

            if (result) {
                Log.d(LOGTAG, "InitVuforiaTask.onPostExecute: Vuforia initialization successful");

                boolean initTrackersResult;
                initTrackersResult = doInitTrackers();

                if (initTrackersResult) {
                    try {
                        mLoadTask = new LoadTrackerTask();
                        mLoadTask.execute();
                    } catch (Exception e) {
                        String logMessage = "Loading tracking data set failed";
                        Log.e(LOGTAG, logMessage);
                        onInitDone(true);
                    }//catch

                } else {
                    onInitDone(false);
                }//else

            } else {
                onInitDone(false);
            }//else
        }//onPostExecute
    }//InitVuforiaTask

    // An async task to load the tracker data asynchronously.
    private class LoadTrackerTask extends AsyncTask<Void, Integer, Boolean> {

        protected Boolean doInBackground(Void... params) {

            // Prevent the onDestroy() method to overlap:
            synchronized (shutdownLock) {
                // Load the tracker data set:
                return doLoadTrackersData();
            }//synchronized
        }//doInBackground


        protected void onPostExecute(Boolean result) {
            Log.i(LOGTAG, "" + result);

            if (result) {
                // Hint to the virtual machine that it would be a good time to
                // run the garbage collector:

                // NOTE: This is only a hint. There is no guarantee that the
                // garbage collector will actually be run.
                System.gc();

                Vuforia.registerCallback(FTCVuforia.this);

                mStarted = true;

            }//else

            onInitDone(result);
        }//onPostExecute
    }//LoadTrackerTask

}//FTCVuforia
