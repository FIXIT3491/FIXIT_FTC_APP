package org.firstinspires.ftc.teamcode.gamecode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.opmodesupport.AutoOpMode;
import org.firstinspires.ftc.teamcode.roboticslibrary.FXTCamera;
import org.firstinspires.ftc.teamcode.robots.Fermion;
import org.firstinspires.ftc.teamcode.util.VortexUtils;

/**
 * Created by FIXIT on 16-10-07.
 */
@Autonomous
public class BeaconAnalysisTest extends AutoOpMode {

    /*
    RANGE: 285 to 500 mm away from beacon
    TARGET: 370mm
     */
    FXTCamera cam;

    @Override
    public void runOp() throws InterruptedException {

        Fermion f = new Fermion(true);
        cam = new FXTCamera(FXTCamera.FACING_BACKWARD, true);


        waitForStart();

        while (opModeIsActive()){
            VortexUtils.getBeaconConfig(cam.getImage());

        }

    }//runOp


    public void stopOpMode() {
        if (cam != null)
            cam.destroy();
    }
}
