package org.firstinspires.ftc.teamcode.newhardware.FXTSensors;

import android.util.Log;

import org.firstinspires.ftc.teamcode.RC;
import org.firstinspires.ftc.teamcode.roboticslibrary.BitUtils;

import java.util.Arrays;

@SuppressWarnings("unused")
public class AdafruitIMU extends I2cSensor {

    public static final int ADDRESS_A = 0x28;
    public static final int ADDRESS_B = 0x29;
    public static final int ID        = 0xA0;
    public static final int                         //From Adafruit_BNO055.h

    /* Page id register definition */
    PAGE_ID_ADDR                                     = 0X07,

    /* PAGE0 REGISTER DEFINITION START*/
    CHIP_ID_ADDR                                     = 0x00,
            ACCEL_REV_ID_ADDR                                = 0x01,
            MAG_REV_ID_ADDR                                  = 0x02,
            GYRO_REV_ID_ADDR                                 = 0x03,
            SW_REV_ID_LSB_ADDR                               = 0x04,
            SW_REV_ID_MSB_ADDR                               = 0x05,
            BL_REV_ID_ADDR                                   = 0X06,

    /* Accel data register */
    ACCEL_DATA_X_LSB_ADDR                            = 0X08,
            ACCEL_DATA_X_MSB_ADDR                            = 0X09,
            ACCEL_DATA_Y_LSB_ADDR                            = 0X0A,
            ACCEL_DATA_Y_MSB_ADDR                            = 0X0B,
            ACCEL_DATA_Z_LSB_ADDR                            = 0X0C,
            ACCEL_DATA_Z_MSB_ADDR                            = 0X0D,

    /* Mag data register */
    MAG_DATA_X_LSB_ADDR                              = 0X0E,
            MAG_DATA_X_MSB_ADDR                              = 0X0F,
            MAG_DATA_Y_LSB_ADDR                              = 0X10,
            MAG_DATA_Y_MSB_ADDR                              = 0X11,
            MAG_DATA_Z_LSB_ADDR                              = 0X12,
            MAG_DATA_Z_MSB_ADDR                              = 0X13,

    /* Gyro data registers */
    GYRO_DATA_X_LSB_ADDR                             = 0X14,
            GYRO_DATA_X_MSB_ADDR                             = 0X15,
            GYRO_DATA_Y_LSB_ADDR                             = 0X16,
            GYRO_DATA_Y_MSB_ADDR                             = 0X17,
            GYRO_DATA_Z_LSB_ADDR                             = 0X18,
            GYRO_DATA_Z_MSB_ADDR                             = 0X19,

    /* For IMU mode, the register addresses 0X1A thru 0X2D (20 bytes) should be read consecutively */

    /* Euler data registers */
    EULER_H_LSB_ADDR                                 = 0X1A,
            EULER_H_MSB_ADDR                                 = 0X1B,
            EULER_R_LSB_ADDR                                 = 0X1C,
            EULER_R_MSB_ADDR                                 = 0X1D,
            EULER_P_LSB_ADDR                                 = 0X1E,
            EULER_P_MSB_ADDR                                 = 0X1F,

    /* Quaternion data registers */
    QUATERNION_DATA_W_LSB_ADDR                       = 0X20,
            QUATERNION_DATA_W_MSB_ADDR                       = 0X21,
            QUATERNION_DATA_X_LSB_ADDR                       = 0X22,
            QUATERNION_DATA_X_MSB_ADDR                       = 0X23,
            QUATERNION_DATA_Y_LSB_ADDR                       = 0X24,
            QUATERNION_DATA_Y_MSB_ADDR                       = 0X25,
            QUATERNION_DATA_Z_LSB_ADDR                       = 0X26,
            QUATERNION_DATA_Z_MSB_ADDR                       = 0X27,

    /* Linear acceleration data registers */
    LINEAR_ACCEL_DATA_X_LSB_ADDR                     = 0X28,
            LINEAR_ACCEL_DATA_X_MSB_ADDR                     = 0X29,
            LINEAR_ACCEL_DATA_Y_LSB_ADDR                     = 0X2A,
            LINEAR_ACCEL_DATA_Y_MSB_ADDR                     = 0X2B,
            LINEAR_ACCEL_DATA_Z_LSB_ADDR                     = 0X2C,
            LINEAR_ACCEL_DATA_Z_MSB_ADDR                     = 0X2D,

    /* Gravity data registers */
    GRAVITY_DATA_X_LSB_ADDR                          = 0X2E,
            GRAVITY_DATA_X_MSB_ADDR                          = 0X2F,
            GRAVITY_DATA_Y_LSB_ADDR                          = 0X30,
            GRAVITY_DATA_Y_MSB_ADDR                          = 0X31,
            GRAVITY_DATA_Z_LSB_ADDR                          = 0X32,
            GRAVITY_DATA_Z_MSB_ADDR                          = 0X33,

    /* Temperature data register */
    TEMP_ADDR                                        = 0X34,

    /* Status registers */
    CALIB_STAT_ADDR                                  = 0X35,
            SELFTEST_RESULT_ADDR                             = 0X36,
            INTR_STAT_ADDR                                   = 0X37,

    SYS_CLK_STAT_ADDR                                = 0X38,
            SYS_STAT_ADDR                                    = 0X39,
            SYS_ERR_ADDR                                     = 0X3A,

    /* Unit selection register */
    UNIT_SEL_ADDR                                    = 0X3B,
            DATA_SELECT_ADDR                                 = 0X3C,

    /* Mode registers */
    OPR_MODE_ADDR                                    = 0X3D,
            PWR_MODE_ADDR                                    = 0X3E,

    SYS_TRIGGER_ADDR                                 = 0X3F,
            TEMP_SOURCE_ADDR                                 = 0X40,

    /* Axis remap registers */
    AXIS_MAP_CONFIG_ADDR                             = 0X41,
            AXIS_MAP_SIGN_ADDR                               = 0X42,

    /* SIC registers */
    SIC_MATRIX_0_LSB_ADDR                            = 0X43,
            SIC_MATRIX_0_MSB_ADDR                            = 0X44,
            SIC_MATRIX_1_LSB_ADDR                            = 0X45,
            SIC_MATRIX_1_MSB_ADDR                            = 0X46,
            SIC_MATRIX_2_LSB_ADDR                            = 0X47,
            SIC_MATRIX_2_MSB_ADDR                            = 0X48,
            SIC_MATRIX_3_LSB_ADDR                            = 0X49,
            SIC_MATRIX_3_MSB_ADDR                            = 0X4A,
            SIC_MATRIX_4_LSB_ADDR                            = 0X4B,
            SIC_MATRIX_4_MSB_ADDR                            = 0X4C,
            SIC_MATRIX_5_LSB_ADDR                            = 0X4D,
            SIC_MATRIX_5_MSB_ADDR                            = 0X4E,
            SIC_MATRIX_6_LSB_ADDR                            = 0X4F,
            SIC_MATRIX_6_MSB_ADDR                            = 0X50,
            SIC_MATRIX_7_LSB_ADDR                            = 0X51,
            SIC_MATRIX_7_MSB_ADDR                            = 0X52,
            SIC_MATRIX_8_LSB_ADDR                            = 0X53,
            SIC_MATRIX_8_MSB_ADDR                            = 0X54,

    /* Accelerometer Offset registers */
    ACCEL_OFFSET_X_LSB_ADDR                                 = 0X55,
            ACCEL_OFFSET_X_MSB_ADDR                                 = 0X56,
            ACCEL_OFFSET_Y_LSB_ADDR                                 = 0X57,
            ACCEL_OFFSET_Y_MSB_ADDR                                 = 0X58,
            ACCEL_OFFSET_Z_LSB_ADDR                                 = 0X59,
            ACCEL_OFFSET_Z_MSB_ADDR                                 = 0X5A,

    /* Magnetometer Offset registers */
    MAG_OFFSET_X_LSB_ADDR                                   = 0X5B,
            MAG_OFFSET_X_MSB_ADDR                                   = 0X5C,
            MAG_OFFSET_Y_LSB_ADDR                                   = 0X5D,
            MAG_OFFSET_Y_MSB_ADDR                                   = 0X5E,
            MAG_OFFSET_Z_LSB_ADDR                                   = 0X5F,
            MAG_OFFSET_Z_MSB_ADDR                                   = 0X60,

    /* Gyroscope Offset register s*/
    GYRO_OFFSET_X_LSB_ADDR                                  = 0X61,
            GYRO_OFFSET_X_MSB_ADDR                                  = 0X62,
            GYRO_OFFSET_Y_LSB_ADDR                                  = 0X63,
            GYRO_OFFSET_Y_MSB_ADDR                                  = 0X64,
            GYRO_OFFSET_Z_LSB_ADDR                                  = 0X65,
            GYRO_OFFSET_Z_MSB_ADDR                                  = 0X66,

    /* Radius registers */
    ACCEL_RADIUS_LSB_ADDR                                   = 0X67,
            ACCEL_RADIUS_MSB_ADDR                                   = 0X68,
            MAG_RADIUS_LSB_ADDR                                     = 0X69,
            MAG_RADIUS_MSB_ADDR                                     = 0X6A;
    public static final int                         //From Adafruit_BNO055.h
            POWER_MODE_NORMAL                                       = 0X00,
            POWER_MODE_LOWPOWER                                     = 0X01,
            POWER_MODE_SUSPEND                                      = 0X02;
    public static final int                         //From Adafruit_BNO055.h
      /* Operation mode settings*/
            OPERATION_MODE_CONFIG                                   = 0X00,
            OPERATION_MODE_ACCONLY                                  = 0X01,
            OPERATION_MODE_MAGONLY                                  = 0X02,
            OPERATION_MODE_GYRONLY                                  = 0X03,
            OPERATION_MODE_ACCMAG                                   = 0X04,
            OPERATION_MODE_ACCGYRO                                  = 0X05,
            OPERATION_MODE_MAGGYRO                                  = 0X06,
            OPERATION_MODE_AMG                                      = 0X07,
            OPERATION_MODE_IMU                                      = 0X08, //Added to original C++ list
            OPERATION_MODE_IMUPLUS                                  = 0X08,
            OPERATION_MODE_COMPASS                                  = 0X09,
            OPERATION_MODE_M4G                                      = 0X0A,
            OPERATION_MODE_NDOF_FMC_OFF                             = 0X0B,
            OPERATION_MODE_NDOF                                     = 0X0C;

    public static final int DEGREES = 0;
    public static final int RADIANS = 1;
    public static final int CELSIUS = 0;
    public static final int FAHRENHEIT = 1;

    public static final int
            MAG_CALIBRATED = 0x3,
            ACCEL_CALIBRATED = 0xC,
            GYRO_CALIBRATED = 0x30,
            SYSTEM_CALIBRATED = 0xC0,
            MAG_TEST = 0x02,
            ACCEL_TEST = 0x01,
            GYRO_TEST = 0x03,
            MCU_TEST = 0x04;

    public static final int YAW = 0;
    public static final int ROLL = 1;
    public static final int PITCH = 2;

    private double[][] offsets = new double[4][3];

    private static String TAG = "AdafruitIMU";

    private AdafruitIMU(String name) {
        super(name, (byte) (ADDRESS_A * 2));
    }//calib

    public AdafruitIMU (String name, int operationMode) {

        super(name, (byte) (ADDRESS_A * 2));

        beginCallBack(CHIP_ID_ADDR);

        write(PAGE_ID_ADDR, 0);

        if (castByte(read(CHIP_ID_ADDR, 1)[0]) != ID) {
            delay(650);

            if (castByte(read(CHIP_ID_ADDR, 1)[0]) != ID) {

                RC.t.addData(TAG, "Chip ID not recognized: " + read(CHIP_ID_ADDR, 1)[0]);
                return;
            }//if

        }//if

        write(OPR_MODE_ADDR, OPERATION_MODE_CONFIG);

        if (!resetIMU(5)) {
            return;
        }//if

        delay(50);

        write(PWR_MODE_ADDR, POWER_MODE_NORMAL);

        write(PAGE_ID_ADDR, 0);


        if (!selfTestIMU(5, getTestValue(operationMode))) {
            return;
        }//if

        delay(50);

//        if (!calibrateIMU(5, getCalibValue(operationMode))) {
//            return;
//        }

        write(OPR_MODE_ADDR, operationMode);

        setRegisterAddress(ACCEL_DATA_X_LSB_ADDR);
    }

    public int getTestValue(int operMode) {

        int val = 0;

        if (operMode == OPERATION_MODE_ACCONLY
                || operMode == OPERATION_MODE_ACCMAG
                || operMode == OPERATION_MODE_ACCGYRO
                || operMode == OPERATION_MODE_AMG
                || operMode == OPERATION_MODE_IMU
                || operMode == OPERATION_MODE_COMPASS
                || operMode == OPERATION_MODE_M4G
                || operMode == OPERATION_MODE_NDOF_FMC_OFF
                || operMode == OPERATION_MODE_NDOF) {
            val |= ACCEL_TEST;
        }

        if (operMode == OPERATION_MODE_MAGONLY
                || operMode == OPERATION_MODE_ACCMAG
                || operMode == OPERATION_MODE_MAGGYRO
                || operMode == OPERATION_MODE_AMG
                || operMode == OPERATION_MODE_COMPASS
                || operMode == OPERATION_MODE_M4G
                || operMode == OPERATION_MODE_NDOF_FMC_OFF
                || operMode == OPERATION_MODE_NDOF) {
            val |= MAG_TEST;
        }

        if (operMode == OPERATION_MODE_GYRONLY
                || operMode == OPERATION_MODE_ACCGYRO
                || operMode == OPERATION_MODE_MAGGYRO
                || operMode == OPERATION_MODE_AMG
                || operMode == OPERATION_MODE_IMU
                || operMode == OPERATION_MODE_NDOF_FMC_OFF
                || operMode == OPERATION_MODE_NDOF) {
            val |= GYRO_TEST;
        }

        val |= MCU_TEST;

        return val;
    }

    public int getCalibValue(int operMode) {

        int val = 0;

        if (operMode == OPERATION_MODE_ACCONLY
                || operMode == OPERATION_MODE_ACCMAG
                || operMode == OPERATION_MODE_ACCGYRO
                || operMode == OPERATION_MODE_AMG
                || operMode == OPERATION_MODE_IMU
                || operMode == OPERATION_MODE_COMPASS
                || operMode == OPERATION_MODE_M4G
                || operMode == OPERATION_MODE_NDOF_FMC_OFF
                || operMode == OPERATION_MODE_NDOF) {
            val |= ACCEL_CALIBRATED;
        }

        if (operMode == OPERATION_MODE_MAGONLY
                || operMode == OPERATION_MODE_ACCMAG
                || operMode == OPERATION_MODE_MAGGYRO
                || operMode == OPERATION_MODE_AMG
                || operMode == OPERATION_MODE_COMPASS
                || operMode == OPERATION_MODE_M4G
                || operMode == OPERATION_MODE_NDOF_FMC_OFF
                || operMode == OPERATION_MODE_NDOF) {
            val |= MAG_CALIBRATED;
        }

        if (operMode == OPERATION_MODE_GYRONLY
                || operMode == OPERATION_MODE_ACCGYRO
                || operMode == OPERATION_MODE_MAGGYRO
                || operMode == OPERATION_MODE_AMG
                || operMode == OPERATION_MODE_IMU
                || operMode == OPERATION_MODE_NDOF_FMC_OFF
                || operMode == OPERATION_MODE_NDOF) {
            val |= GYRO_CALIBRATED;
        }

        val |= SYSTEM_CALIBRATED;

        return val;
    }

    public double[] getData(int dataAddress, int byteCount) {

        double scale = 16.0;
        byte[] rawData = read(dataAddress, byteCount);

        if (dataAddress == ACCEL_DATA_X_LSB_ADDR) {
            scale = 100.0;
        } else if (dataAddress == EULER_H_LSB_ADDR
                || dataAddress == GYRO_DATA_X_LSB_ADDR
                || dataAddress == MAG_DATA_X_LSB_ADDR) {
            scale = 16.0;
        }

        return createVector(rawData, scale);
    }

    public double[] getEulerAngles() {

        byte[] rawData = read(EULER_H_LSB_ADDR, 6);

        double[] data = createVector(rawData, 16.0);
        data[0] -= offsets[0][0];
        data[1] -= offsets[0][1];
        data[2] -= offsets[0][2];

        return data;
    }//getEulerAngles

    public void updateEulerOffsets(double... eulerOffsets) {
        offsets[0][0] = eulerOffsets[0];
        offsets[0][1] = eulerOffsets[1];
        offsets[0][2] = eulerOffsets[2];
    }//updateEulerOffsets

    public double[] getAccelData() {

        byte[] rawData = read(ACCEL_DATA_X_LSB_ADDR, 6);

        double[] data = createVector(rawData, 100.0);
        data[0] -= offsets[1][0];
        data[1] -= offsets[1][1];
        data[2] -= offsets[1][2];

        return data;
    }

    public double[] getLinearAccelData() {

        byte[] rawData = read(LINEAR_ACCEL_DATA_X_LSB_ADDR, 6);

        double[] data = createVector(rawData, 100.0);
        data[0] -= offsets[1][0];
        data[1] -= offsets[1][1];
        data[2] -= offsets[1][2];

        return data;
    }

    public void updateAccelOffsets(double... accelOffsets) {
        offsets[1][0] = accelOffsets[0];
        offsets[1][1] = accelOffsets[1];
        offsets[1][2] = accelOffsets[2];
    }//updateEulerOffsets

    public double[] getGyroAngles() {

        byte[] rawData = read(GYRO_DATA_X_LSB_ADDR, 6);

        double[] data = createVector(rawData, 16.0);
        data[0] -= offsets[2][0];
        data[1] -= offsets[2][1];
        data[2] -= offsets[2][2];

        return data;
    }

    public void updateGyroOffsets(double... gyroOffsets) {
        offsets[2][0] = gyroOffsets[0];
        offsets[2][1] = gyroOffsets[1];
        offsets[2][2] = gyroOffsets[2];
    }//updateEulerOffsets

    public double[] getMagAxes() {

        byte[] rawData = read(MAG_DATA_X_LSB_ADDR, 6);

        double[] data = createVector(rawData, 16.0);
        data[0] -= offsets[3][0];
        data[1] -= offsets[3][1];
        data[2] -= offsets[3][2];

        return data;
    }

    public void updateMagOffsets(double... magOffsets) {
        offsets[3][0] = magOffsets[0];
        offsets[3][1] = magOffsets[1];
        offsets[3][2] = magOffsets[2];
    }//updateEulerOffsets

    public double[] createVector(byte[] rawData, double scale) {

        double[] vector = new double[3];

        for (int i = 0; i < vector.length; i++) {
            vector[i] = BitUtils.combineBytes(rawData[2 * i], rawData[2 * i + 1]);
            vector[i] /= scale;
        }//for

        return vector;
    }

    public void setUnits(int tempUnit, int angleUnit) {

        int unitByte = (1 << 7
                | tempUnit << 4
                | angleUnit << 2
                | angleUnit << 1);

        Log.i(TAG, "setUnits " + Integer.toBinaryString(unitByte & 0xff));

        write(UNIT_SEL_ADDR, unitByte);

    }


    //INITIALIZATION METHODS

    //Calibrate
    public boolean calibrateIMU(int timeOut, int calibrationValue) {

        write(SYS_TRIGGER_ADDR, 0xE1);

        long startTime = System.currentTimeMillis();

        byte calibData = 0;

        while (System.currentTimeMillis() - startTime < timeOut * 1000) {

            waitForCallBack();

            calibData = read(CALIB_STAT_ADDR, 1)[0];

            Log.i("Calibration", Integer.toHexString(castByte(calibData)) + "");

            if (castByte(calibData) >= calibrationValue) {

                RC.t.addData(TAG + "-Calibration Success", "Data: " + calibData + ", Elapsed: " + (System.currentTimeMillis() - startTime));
                return true;

            }//if

        }

        RC.t.addData(TAG + "-Calibration Failed", "Data: " + calibData);
        return false;
    }

    public boolean resetIMU (int timeOut) {

        write(SYS_TRIGGER_ADDR, 0x20);

        waitForCallBack();
        waitForCallBack();

        Log.i("Sys Trigger Byte", read(SYS_TRIGGER_ADDR, 1)[0] + "");

        long startTime = System.currentTimeMillis();

        byte idData = 0;

        while (System.currentTimeMillis() - startTime < timeOut * 1000) {

            waitForCallBack();

            idData = read(CHIP_ID_ADDR, 1)[0];

            Log.i(TAG, "Chip ID: " + idData);

            if (idData == (byte) ID) {

                RC.t.addData(TAG + "-Reset Success", "Data: " + idData + ", Elapsed: " + (System.currentTimeMillis() - startTime));
                return true;

            }//if

        }

        RC.t.addData("Chip ID", idData);
        RC.t.addData(TAG + "-Reset Failed", "Data: " + idData);
        return false;
    }

    public boolean selfTestIMU(int timeOut, int testValue) {

        write(SYS_TRIGGER_ADDR, 0x01);

        long startTime = System.currentTimeMillis();

        byte testData = 0;

        while (System.currentTimeMillis() - startTime < timeOut * 1000) {

            waitForCallBack();

            testData = read(SELFTEST_RESULT_ADDR, 1)[0];

            Log.i(TAG, "Self Test: " + testData);

            if (testData >= testValue) {

                RC.t.addData(TAG + "-Test Success", "Data: " + testData + ", Elapsed: " + (System.currentTimeMillis() - startTime));
                return true;

            }//if

        }

        RC.t.addData(TAG + "-Test Failed", "Data: " + testData);
        return false;
    }

    public static void dataLogCalibrationValues(String name, int operationMode) {

        AdafruitIMU adaf = new AdafruitIMU(name);
        adaf.beginCallBack(CHIP_ID_ADDR);

        adaf.write(PAGE_ID_ADDR, 0);

        if (adaf.castByte(adaf.read(CHIP_ID_ADDR, 1)[0]) != ID) {
            adaf.delay(650);

            if (adaf.castByte(adaf.read(CHIP_ID_ADDR, 1)[0]) != ID) {

                RC.t.addData(TAG, "Chip ID not recognized: " + adaf.read(CHIP_ID_ADDR, 1)[0]);
                return;
            }//if

        }//if

        adaf.write(OPR_MODE_ADDR, OPERATION_MODE_CONFIG);

        if (!adaf.resetIMU(5)) {
            return;
        }

        if (!adaf.selfTestIMU(5, adaf.getTestValue(OPERATION_MODE_AMG))) {
            return;
        }

        while (!adaf.calibrateIMU(5, adaf.getCalibValue(operationMode))) {}

        byte[] calibData = adaf.read(ACCEL_OFFSET_X_LSB_ADDR, 16);

        RC.t.addData("HELLO", "HEELOO");

        RC.t.setDataLogFile("adafruitcalibration.txt");
        RC.t.dataLogData(Arrays.toString(calibData));

    }

}