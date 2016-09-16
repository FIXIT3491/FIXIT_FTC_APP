package org.firstinspires.ftc.teamcode.oldopmodes;

import org.firstinspires.ftc.teamcode.RC;
import org.firstinspires.ftc.teamcode.opmodesupport.DoNotRegister;
import org.firstinspires.ftc.teamcode.opmodesupport.TeleOpMode;
import org.firstinspires.ftc.teamcode.roboticslibrary.ImageAnalyzer;
import org.firstinspires.ftc.teamcode.robots.Lily;

/**
 * Created by FIXIT on 15-11-14.
 */
@DoNotRegister
public class BasketRedWait extends TeleOpMode {

    Lily lily;
    int stage = 0;
    int motionStage = 0;

    ImageAnalyzer analysis;
    boolean analyzeImage = false;
    int redPos = 0;

    //revert to 4:44pm

    @Override
    public void initialize() {
        setDataLogFile("encoders", true);
        lily = new Lily();
        analysis = new ImageAnalyzer();
    }


    @Override
    public void start() {
        lily.targetAngle = (int) lily.adafruit.getEulerAngles()[0];
        clearTimer(1);
    }

    @Override
    public void loopOpMode() {

        if (analyzeImage) {
//            analysis.updateImage(FtcRobotControllerActivity.camera.image);
            analysis.applyColourFilter();
            redPos = (int) analysis.findCenters()[0];
            RC.t.addData("BluePos", redPos);
            if (redPos > 180) {
                lily.forward(0.1);
            } else if (redPos < 160) {
                lily.backward(0.1);
            } else {
                lily.halt();
            }
        }

        if (stage == 0 && lily.allReady() && getMilliSeconds(1) > 15000) {
            lily.backward(0.4);
            stage++;
        } else if (stage == 1 && lily.EOPD.getLightDetected() > 0.1) {
            lily.imuTurnR(-lily.targetAngle, 0.2);
            stage++;
        } else if (stage == 2 && lily.allReady()) {
            lily.forwardDistance(100, 0.5);
            stage++;
        } else if (stage == 3 && lily.allReady()) {
            lily.backward(0.1);
            analyzeImage = true;
            lily.doorL.goToPos("open");
            stage++;
        } else if (stage == 4 && redPos > 165 && redPos < 180) {
            analyzeImage = false;
            lily.backwardDistance(210, 0.5);
            lily.doorL.goToPos("closed");
            analysis.saveToInternalStorage(analysis.imageToBitmap(), "stopped");
            stage++;
        } else if (stage == 5 && lily.allReady()) {
            lily.people.setPower(-1);
            clearTimer();
            stage++;
        } else if (stage == 6 && getMilliSeconds() > 1200) {
            lily.people.setPower(0.3);
            lily.backwardDistance(1020, 0.3);
            clearTimer();
            stage++;
        }
        if (stage == 7 && getMilliSeconds() > 1000 && lily.allReady()) {
            lily.people.stop();
            stage++;
        }

        lily.checkAllSystems();
        RC.t.dataLogData("" + lily.motorL.getCurrentPosition());
    }
}
