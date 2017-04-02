package org.firstinspires.ftc.teamcode.gamecode;

import android.speech.tts.TextToSpeech;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.RC;
import org.firstinspires.ftc.teamcode.robots.Fermion;
import org.firstinspires.ftc.teamcode.robots.Robot;

/**
 * Created by Windows on 2017-02-18.
 */

@Autonomous
public class JudgingInterview extends LinearOpMode implements TextToSpeech.OnInitListener{

    @Override
    public void runOpMode() throws InterruptedException {
        TextToSpeech text = new TextToSpeech(RC.c(), this);
        Fermion fermion = new Fermion(false);
        fermion.stop();
        fermion.startShooterControl();

        waitForStart();

        text.speak("Hello, my name is fermion. Today I am going to describe myself." +
                "If you look down at my wheels, you will notice that I have 4 mecanum wheels." +
                "These allow me to drive in any direction and spin simultaneously. This is really helpful for pushing beacons." +
                "Next you'll notice my collector. Guy, please put a ball in my collector.", TextToSpeech.QUEUE_ADD, null);
        while (opModeIsActive() && text.isSpeaking()){
            idle();
        }

        fermion.setCollectorState(Robot.IN);
        sleep(2000);
        fermion.setCollectorState(Robot.STOP);

        text.speak("Now that I have a ball, I can store it in my trough. Once we are ready to shoot, I prime the shooter.", TextToSpeech.QUEUE_ADD, null);
        while (opModeIsActive() && text.isSpeaking()){
            idle();
        }

        text.speak("Then I open the servo to let the ball into the shooter.", TextToSpeech.QUEUE_ADD, null);
        fermion.door.goToPos("open");
        sleep(1000);
        fermion.door.goToPos("close");

        text.speak("Now, I will shoot a particle.", TextToSpeech.QUEUE_ADD, null);
        fermion.shoot();
        fermion.waitForShooterState(Fermion.FIRE);
        while (opModeIsActive() && text.isSpeaking()){
            idle();
        }

        text.speak("Now, if Helen will rotate me, you will see my foam button pressers." +
                "Last but not least, this is my cap ball lifter.", TextToSpeech.QUEUE_ADD, null);
        while (opModeIsActive() && text.isSpeaking()){
            idle();
        }

        fermion.liftCapBall();
        sleep(3000);
        fermion.lowerCapBall();
        sleep(3000);
        fermion.lifter.stop();

        text.speak("Thank you, now I will hand you off to Aila to talk about programming.", TextToSpeech.QUEUE_ADD, null);



    }

    @Override
    public void onInit(int status) {
        RC.t.addData("Talking", "Enabled");
    }
}
