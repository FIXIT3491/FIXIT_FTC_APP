package org.firstinspires.ftc.teamcode.gamecode;

import android.speech.tts.TextToSpeech;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.RC;
import org.firstinspires.ftc.teamcode.opmodesupport.AutoOpMode;
import org.firstinspires.ftc.teamcode.robots.Fermion;
import org.firstinspires.ftc.teamcode.robots.Robot;

/**
 * Created by Windows on 2017-02-18.
 */

@Autonomous
public class JudgingInterview extends AutoOpMode implements TextToSpeech.OnInitListener{

    @Override
    public void runOp() throws InterruptedException {
        TextToSpeech text = new TextToSpeech(RC.c(), this);
        text.setSpeechRate(0.9f);
        Fermion fermion = new Fermion(true);
        fermion.stop();
        fermion.startShooterControl();

        waitForStart();

        text.speak("Hello, my name is fermion. Today I am going to describe myself." +
                "If you look down at my wheels, you will notice that I have 4 mecanum wheels." +
                "These allow me to drive in any direction and spin simultaneously. This is really helpful navigating shopping malls... oh wait.... for pushing beacons." +
                "Next you'll notice my collector. Guy, please put a ball in my collector. Don't get too close, I may bite", TextToSpeech.QUEUE_ADD, null);
        while (opModeIsActive() && text.isSpeaking()){
            idle();
        }

        fermion.setCollectorState(Robot.IN);
        sleep(4000);
        fermion.setCollectorState(Robot.STOP);

        text.speak("Now that I have a ball, I can store it in my trough. Once we are ready to shoot, I prime the shooter.", TextToSpeech.QUEUE_ADD, null);
        while (opModeIsActive() && text.isSpeaking()){
            idle();
        }

        text.speak("Then I open the servo to let the ball into the shooter.", TextToSpeech.QUEUE_ADD, null);
        fermion.door.goToPos("open");
        sleep(1000);
        fermion.door.goToPos("close");
        sleep(1000);

        text.speak("Now, I will shoot a particle.", TextToSpeech.QUEUE_ADD, null);
        fermion.shoot();
        fermion.waitForShooterState(Fermion.LOADING);
        while (opModeIsActive() && text.isSpeaking()){
            idle();
        }

        text.speak("Now, if Helen will rotate me, you will see my foam button pressers." +
                "Last but not least, this is my cap ball lifter.", TextToSpeech.QUEUE_ADD, null);
        while (opModeIsActive() && text.isSpeaking()){
            idle();
        }
        fermion.capRelease.goToPos("release");

        fermion.liftCapBall();
        sleep(3000);
        fermion.lowerCapBall();
        sleep(3000);
        fermion.lifter.stop();

        text.speak("I also have had some problems with balls getting in front me so I use my whiskers to clear balls from in front of the beacons", TextToSpeech.QUEUE_ADD, null);

        while(opModeIsActive() && text.isSpeaking()) {
            idle();
        }//while

        fermion.clearBall();
        sleep(500);
        fermion.resetBallClearing();

    }

    @Override
    public void onInit(int status) {
        RC.t.addData("Talking", "Enabled");
    }
}
