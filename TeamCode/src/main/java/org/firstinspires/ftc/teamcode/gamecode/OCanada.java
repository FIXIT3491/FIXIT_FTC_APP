package org.firstinspires.ftc.teamcode.gamecode;

import android.media.MediaPlayer;

import com.qualcomm.ftcrobotcontroller.R;
import org.firstinspires.ftc.teamcode.RC;
import org.firstinspires.ftc.teamcode.opmodesupport.TeleOpMode;

/**
 * Created by Windows on 2016-02-05.
 */
public class OCanada extends TeleOpMode {

    MediaPlayer mediaPlayer;

    @Override
    public void initialize() {
        mediaPlayer = MediaPlayer.create(RC.c(), R.raw.can);
    }

    @Override
    public void stop() {
        super.stop();
        mediaPlayer.release();
    }

    @Override
    public void loopOpMode() {

        if (joy2.buttonRight() && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        } else if (joy2.buttonLeft() && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            mediaPlayer.reset();
        }

    }
}
