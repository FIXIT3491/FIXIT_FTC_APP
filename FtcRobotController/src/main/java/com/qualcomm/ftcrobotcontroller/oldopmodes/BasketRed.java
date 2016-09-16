package com.qualcomm.ftcrobotcontroller.oldopmodes;

import com.qualcomm.ftcrobotcontroller.RC;
import com.qualcomm.ftcrobotcontroller.opmodesupport.DoNotRegister;
import com.qualcomm.ftcrobotcontroller.opmodesupport.TeleOpMode;
import com.qualcomm.ftcrobotcontroller.roboticslibrary.ImageAnalyzer;
import com.qualcomm.ftcrobotcontroller.robots.Lily;

/**
 * Created by FIXIT on 15-11-14.
 */
@DoNotRegister
public class BasketRed extends TeleOpMode {

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
    }

    @Override
    public void loopOpMode() {

        if (analyzeImage) {
//            analysis.updateImage(FtcRobotControllerActivity.camera.image);
            analysis.applyColourFilter();
            redPos = (int) analysis.findCenters()[0];
            RC.t.addData("BluePos", redPos);
            if (redPos > 180) {
                lily.forward(0.2);
            } else if (redPos < 160) {
                lily.backward(0.2);
            } else {
                lily.halt();
            }
        }

        if (stage == 0 && lily.allReady()) {
            lily.backward(0.4);
            stage++;
        } else if (stage == 1 && lily.EOPD.getLightDetected() > 0.1) {
            lily.imuTurnR(-lily.targetAngle, 0.2);
            stage++;
        } else if (stage == 2 && lily.allReady()) {
            lily.forwardDistance(100, 0.5);
            stage++;
        } else if (stage == 3 && lily.allReady()) {
            lily.backward(0.2);
            analyzeImage = true;
            stage++;
        } else if (stage == 4 && redPos > 165 && redPos < 180) {
            analyzeImage = false;
            lily.backwardDistance(180, 0.5);
            analysis.saveToInternalStorage(analysis.imageToBitmap(), "stopped");
            stage++;
        }  else if (stage == 5 && lily.allReady()) {
            lily.people.setPower(-1);
            clearTimer();
            stage++;
        } else if (stage == 6 && getMilliSeconds() > 1200) {
            lily.people.setPower(0.3);
            lily.backwardDistance(1020, 0.3);
            clearTimer();
            stage++;
        } if (stage == 7 && getMilliSeconds() > 1000 && lily.allReady()) {
            lily.people.stop();
            stage++;
        }

        lily.checkAllSystems();
        RC.t.addData("staagwe", "" + stage);
        RC.t.dataLogData("L", lily.motorL.returnCurrentState());
        RC.t.dataLogData("\nR", lily.motorR.returnCurrentState());
    }
}
