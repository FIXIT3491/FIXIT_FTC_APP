package org.firstinspires.ftc.teamcode.opmodesupport;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import org.firstinspires.ftc.teamcode.RC;
import org.firstinspires.ftc.teamcode.roboticslibrary.FXTTelemetry;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FIXIT on 8/19/2015.
 */
@DoNotRegister
public abstract class TeleOpMode extends OpMode {

    /**
     * Joystick representing the first gamepad
     * @see com.qualcomm.robotcore.hardware.Gamepad
     */
    protected Joystick joy1;

    /**
     * Joystick representing the second gamepad
     * @see com.qualcomm.robotcore.hardware.Gamepad
     */
    protected Joystick joy2;

    /**
     * Number of milliseconds between each time the computer datalogs
     */
    private int interval = 100;

    /**
     * Allows or stops datalogging
     */
    private boolean datalog = false;

    /**
     * Game-pads that are used by the joysticks
     * @see TeleOpMode.Joystick
     * @see com.qualcomm.robotcore.hardware.Gamepad
     */
    private Gamepad[] gamepads = new Gamepad[2];

    /**
     * List of long numbers. Used for timers
     */
    private List<Long> startNanoTimes = new ArrayList<Long>();

    protected int cycles = 0;

    protected String TAG = "FIX IT";

    /**
     * Method that runs once at the beginning of the opmode
     */
    @Override
    public void init() {
        joy1 = new Joystick(1);
        joy2 = new Joystick(2);

        addTimer();
        RC.setOpMode(this);

        initialize();

        RC.t.addData("OpMode", "Initialization Complete");

    }//init

    public abstract void initialize();

    /**
     * Method that runs repeatedly. It updates the gamepads and datalogs any lines or sensors
     */
    @Override
    public void loop() {
        gamepads[0] = gamepad1;
        gamepads[1] = gamepad2;
        cycles++;
        loopOpMode();

    }

    public abstract void loopOpMode();

    /**
     * Method that is run at the end of the opmode. It closes the datalogger in FXTTelemetry
     * @see FXTTelemetry
     */
    @Override
    public void stop() {
        RC.cam.stop();
        RC.t.close();
    }

    public void start() {
        clearTimer();
    }

    /**
     * AUTOMATIC DATALOGGING METHODS
     */

    /**
     * Method that creates the thread for automatic datalogging
     */
    private void createDataLoggingThread() {
        new Thread() {
            public void run() {
                while (true) {
                    if (datalog && System.currentTimeMillis() % interval < 3) {
                        RC.t.dataLogSensorList();
                    }
                    try {
                        Thread.sleep(10);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }.start();
    }

    /**
     * Method to point Android to what text file to write to
     * @param fileName name of .txt file to write to
     * @param overWrite whether or not to overwrite the data there
     */
    public void setDataLogFile (String fileName, boolean overWrite) {
        RC.t.setDataLogFile(fileName, overWrite);
        datalog = true;
        createDataLoggingThread();
    }

    /**
     * Set how often automatic datalogging should occur
     * @param ms
     */
    protected void setDataLogInterval (int ms) {
        this.interval = ms;
    }

    /**
     * Method to start automatic datalogging
     */
    protected void startDataLogging() {
        this.datalog = true;
    }

    /**
     * Method to stop datalogging
     */
    protected void stopDataLogging() {
        this.datalog = false;
    }



    /**
     * TIMER METHODS
     */

    /**
     * Adds another timer
     */
    public void addTimer() {
        startNanoTimes.add(System.nanoTime());
    }

    /**
     * Removes the timer at the specified index
     */
    public void removeTimer(int index) {
        startNanoTimes.remove(index);
    }

    /**
     * Resets the first timer
     */
    public void clearTimer() {
        startNanoTimes.set(0, System.nanoTime());
    }

    /**
     * Resets the timer at the specified index
     * @param index index of timer to reset
     */
    public void clearTimer(int index) {
        if (startNanoTimes.size() > index) {
            startNanoTimes.set(index, System.nanoTime());
        } else {
            startNanoTimes.add(System.nanoTime());
        }
    }

    /**
     * Gets the count of the first timer
     * @return time since the first timer has been reset (or was created) in nanoseconds
     */
    public long getNanoSeconds() {
        return System.nanoTime() - startNanoTimes.get(0);
    }

    /**
     * Gets the count of the timer at the specified index
     * @param index index of timer to get count from
     * @return time since the timer has been reset (or was created) in nanoseconds
     */
    public long getNanoSeconds(int index) {

        if (startNanoTimes.size() > index) {
            return System.nanoTime() - startNanoTimes.get(index);
        } else {
            startNanoTimes.add(System.nanoTime());
            return 0;
        }

    }

    /**
     * Gets the count of the first timer
     * @return time since the timer has been reset (or was created) in microseconds
     */
    public long getMicroSeconds() {
        return getNanoSeconds() / 1000;
    }

    /**
     * Gets the count of the timer at the specified index
     * @param index index of the timer to get the count from
     * @return time since the timer has been reset (or was created) in microseconds
     */
    public long getMicroSeconds(int index) {
        return getNanoSeconds(index) / 1000;
    }

    /**
     * Gets the count of the first timer
     * @return time since the timer has been reset (or was created) in milliseconds
     */
    public int getMilliSeconds() {
        return (int) (getNanoSeconds() / 1000000);
    }

    /**
     * Gets the count of the timer at the specified index
     * @param index index of the timer to get the count from
     * @return time since the timer has been reset (or was created) in milliseconds
     */
    public int getMilliSeconds(int index) {
        return (int) (getNanoSeconds(index) / 1000000);
    }

    /**
     * Gets the count of the timer at the specified index
     * @return time since the timer has been reset (or was created) in seconds
     */
    public int getSeconds() {
        return (int) (getNanoSeconds() / 1000000000);
    }

    /**
     * Gets the count of the timer at the specified index
     * @param index index of the timer to get the count from
     * @return time since the timer has been reset (or was created) in milliseconds
     */
    public int getSeconds(int index) {
        return (int) (getNanoSeconds(index) / 1000000000);
    }


    /**
     * SOUND METHODS
     */


    /**
     * Method for generating a beep. Used by playSound(int freq, int duration)
     * @param freqOfTone frequency of beep to generate
     */
    private static byte[] genTone(int freqOfTone, int numSamples) {

        double[] sample = new double[numSamples];
        byte[] generatedSnd = new byte[2 * numSamples];
        final int sampleRate = 4000;

        // fill out the array
        for (int i = 0; i < numSamples; ++i) {
            sample[i] = Math.sin(2 * Math.PI * i / (sampleRate / freqOfTone));
        }

        // convert to 16 bit pcm sound array
        // assumes the sample buffer is normalised.
        int idx = 0;
        for (double dVal : sample) {
            short val = (short) (dVal * 32767);
            generatedSnd[idx++] = (byte) (val & 0x00ff);
            generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);
        }

        return generatedSnd;
    }

    /**
     * Method to easily to play a beep with a defined frequency and duration
     *
     * @param frequency frequency of beep to play
     * @param duration  how long to play the beep (in seconds)
     */
    public static void playSound(final int frequency, double duration) {

        if (duration > 500)
            duration /= 1000;
        else if (duration > 50)
            duration = 3;

        final int sampleRate = 4000;
        final int numSamples = (int) (duration * sampleRate);


        new Thread() {

            @Override
            public void run() {

                byte[] generatedSnd;

                AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_ALARM,
                        sampleRate, AudioFormat.CHANNEL_CONFIGURATION_MONO,
                        AudioFormat.ENCODING_PCM_16BIT, numSamples,
                        AudioTrack.MODE_STATIC);

                generatedSnd = genTone(frequency, numSamples);

                audioTrack.write(generatedSnd, 0, numSamples);
                audioTrack.play();
            }

        }.start();
    }



    //Joystick class: based off gamepad object
    public class Joystick {

        int padIndex;

        public Joystick (int gamePad) {
            this.padIndex = gamePad - 1;
        }

        public boolean buttonA() {
            return gamepads[padIndex].a;
        }

        public boolean buttonB() {
            return gamepads[padIndex].b;
        }

        public boolean buttonX() {
            return gamepads[padIndex].x;
        }

        public boolean buttonY() {
            return gamepads[padIndex].y;
        }

        public boolean buttonUp() {
            return gamepads[padIndex].dpad_up;
        }

        public boolean buttonDown() {
            return gamepads[padIndex].dpad_down;
        }

        public boolean buttonRight() {
            return gamepads[padIndex].dpad_right;
        }

        public boolean buttonLeft() {
            return gamepads[padIndex].dpad_left;
        }

        public boolean leftBumper() {
            return gamepads[padIndex].left_bumper;
        }

        public boolean rightBumper() {
            return gamepads[padIndex].right_bumper;
        }

        public boolean leftTrigger(){
            return (gamepads[padIndex].left_trigger > 0.1);
        }

        public boolean rightTrigger(){
            return (gamepads[padIndex].right_trigger > 0.1);
        }

        public boolean buttonStart() {
            return gamepads[padIndex].start;
        }

        public boolean buttonBack() {
            return gamepads[padIndex].back;
        }

        public float x1(){
            return (Math.abs(gamepads[padIndex].left_stick_x) > 0.09) ? -gamepads[padIndex].left_stick_x : 0;
        }

        public float x2(){
            return (Math.abs(gamepads[padIndex].right_stick_x) > 0.09)? -gamepads[padIndex].right_stick_x : 0;
        }

        public float y1(){
            return (Math.abs(gamepads[padIndex].left_stick_y) > 0.09)? -gamepads[padIndex].left_stick_y : 0;
        }

        public float y2(){
            return (Math.abs(gamepads[padIndex].right_stick_y) > 0.09)? -gamepads[padIndex].right_stick_y : 0;
        }


    }


}
