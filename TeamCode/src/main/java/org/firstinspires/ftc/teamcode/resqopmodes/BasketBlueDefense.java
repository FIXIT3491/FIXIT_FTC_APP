package org.firstinspires.ftc.teamcode.resqopmodes;

import org.firstinspires.ftc.teamcode.RC;
import org.firstinspires.ftc.teamcode.opmodesupport.TeleOpMode;
import org.firstinspires.ftc.teamcode.roboticslibrary.ImageAnalyzer;
import org.firstinspires.ftc.teamcode.robots.Lily;

/**
 * Created by FIXIT on 15-11-14.
 */
public class BasketBlueDefense extends TeleOpMode {

    Lily lily;
    int stage = 0;
    ImageAnalyzer analysis;
    int bluePos = 0;
    boolean analyzeImage = false;

    //revert to 4:44pm

    @Override
    public void initialize() {
        setDataLogFile("heading", true);
        lily = new Lily();
    }

    public void stop() {
        super.stop();
        lily.brush.stop();
    }

    @Override
    public void loopOpMode() {
        if (analyzeImage) {
//            analysis.updateImage(FtcRobotControllerActivity.camera.image);
            analysis.applyColourFilter();
            bluePos = (int) analysis.findCenters()[1];
            RC.t.addData("RedPos", bluePos);
//            lily.veerCheck();
        }

        if (stage == 0 && lily.allReady()) {
            lily.forward(0.4);
            stage++;
        } else if (stage == 1 && lily.EOPD.getLightDetected() > 0.1) {
            lily.imuTurnL(-lily.targetAngle, 0.2);
            stage++;
        } else if (stage == 2 && lily.allReady()) {
            lily.backwardDistance(100, 0.5);
            stage++;
        } else if (stage == 3 && lily.allReady()) {
            lily.forward(0.1);
            analyzeImage = true;
            stage++;
        } else if (stage == 4 && bluePos > 165 && bluePos < 190) {
            analyzeImage = false;
            lily.halt();
            //DEPLOY PEOPLE!
            stage++;
        } else if (stage == 5 && lily.allReady()) {
            //undeploy people
            lily.imuTurnL(45, 0.5);
            stage++;
        } else if (stage == 6 && lily.allReady()) {
            lily.forward(0.5);
            clearTimer(1);
            stage++;
        } else if (stage == 7 && getMilliSeconds(1) > 2000) {
            lily.halt();
            stage++;
        }

        lily.checkAllSystems();
    }
}
