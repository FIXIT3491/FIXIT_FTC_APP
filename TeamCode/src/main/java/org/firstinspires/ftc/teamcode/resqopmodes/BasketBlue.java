package org.firstinspires.ftc.teamcode.resqopmodes;

import org.firstinspires.ftc.teamcode.RC;
import org.firstinspires.ftc.teamcode.opmodesupport.TeleOpMode;
import org.firstinspires.ftc.teamcode.roboticslibrary.ImageAnalyzer;
import org.firstinspires.ftc.teamcode.robots.Lily;

/**
 * Created by FIXIT on 15-11-14.
 */
public class BasketBlue extends TeleOpMode {

    Lily lily;
    int stage = 0;
    ImageAnalyzer analysis;
    int bluePos = 0;
    boolean analyzeImage = false;
    double speedToSet = 0;

    //revert to 4:44pm

    @Override
    public void initialize() {
        setDataLogFile("power", true);
        dataLogData("MotorL, MotorR");
        lily = new Lily();
        analysis = new ImageAnalyzer();
    }

    public void stop() {
        super.stop();
        lily.brush.stop();
    }

    @Override
    public void start() {
        lily.targetAngle = (int) lily.adafruit.getEulerAngles()[0];
    }

    @Override
    public void loopOpMode() {
        if (analyzeImage) {
//            analysis.updateImage(FtcRobotControllerActivity.camera.image);
            analysis.applyColourFilter();
            bluePos = (int) analysis.findCenters()[1];
            RC.t.addData("RedPos", bluePos);
            if (bluePos > 180) {
                lily.forward(0.1);
            } else if (bluePos < 160) {
                lily.backward(0.1);
            } else {
                lily.halt();
            }
        }

        if (stage == 0 && lily.allReady()) {
            lily.forward(0.4);
            stage++;
        } else if (stage == 1 && lily.EOPD.getLightDetected() > 0.1) {
            lily.imuTurnL(lily.targetAngle, 0.5);
            stage++;
        } else if (stage == 2 && lily.allReady()) {
            lily.backward(0.1);
            analyzeImage = true;
            stage++;
        } else if (stage == 3 && bluePos > 165 && bluePos < 180) {
            analyzeImage = false;
            lily.backwardDistance(210, 0.3);
            analysis.saveToInternalStorage(analysis.imageToBitmap(), "stopped");
            stage++;
        } else if (stage == 4 && lily.allReady()) {
            lily.people.setPower(-1);
            clearTimer();
            stage++;
        } else if (stage == 5 && getMilliSeconds() > 1200) {
            lily.people.setPower(0.3);
            lily.forwardDistance(1020, 0.3);
            clearTimer();
            stage++;
        } if (stage == 6 && getMilliSeconds() > 1000 && lily.allReady()) {
            lily.people.stop();
            stage++;
        }

        RC.t.addData("hello", "hiya");
        RC.t.addData("Stage", stage);
        dataLogData(lily.motorL.getPower() + "," + lily.motorR.getPower());

        lily.checkAllSystems();
    }
}
