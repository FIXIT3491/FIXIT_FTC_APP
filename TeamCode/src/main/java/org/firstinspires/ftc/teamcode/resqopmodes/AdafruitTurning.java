package org.firstinspires.ftc.teamcode.resqopmodes;

import org.firstinspires.ftc.teamcode.RC;
import org.firstinspires.ftc.teamcode.opmodesupport.TeleOpMode;
import org.firstinspires.ftc.teamcode.robots.Lily;

/**
 * Created by FIXIT on 15-08-27.
 * TeleOp File
 */
public class AdafruitTurning extends TeleOpMode {

    Lily lily;
    int stage = 0;

    @Override
    public void initialize() {

        lily = new Lily();

    }

    @Override
    public void loopOpMode() {

        if (stage == 0 && lily.allReady()) {
            lily.imuTurnL(90, 0.5);
            stage++;
        }
        RC.t.addData("Angle", lily.adafruit.getEulerAngles()[0]);
        lily.checkAllSystems();
    }


    public float[] process() {

        float[] gravValues = null;
        float[] magValues = null;

        float[] crossProduct = getCrossProduct(magValues, gravValues);

        crossProduct = getCrossProduct(crossProduct, gravValues);

        makeUnitVector(crossProduct);

        return crossProduct;
    }

    public void makeUnitVector(float[] vector) {
        float magnitude = (float) Math.sqrt(vector[0] * vector[0] + vector[1] * vector[1] + vector[2] * vector[2]);

        vector[0] /= magnitude;
        vector[1] /= magnitude;
        vector[2] /= magnitude;
    }

    public float[] getCrossProduct(float[] vectorA, float[] vectorB) {
        float[] vectorC = new float[3];

        vectorC[0] = vectorA[1] * vectorB[2] - vectorA[2] * vectorB[1];
        vectorC[1] = vectorA[2] * vectorB[0] - vectorA[0] * vectorB[2];
        vectorC[2] = vectorA[0] * vectorB[1] - vectorA[1] * vectorB[0];

        return vectorC;
    }
}