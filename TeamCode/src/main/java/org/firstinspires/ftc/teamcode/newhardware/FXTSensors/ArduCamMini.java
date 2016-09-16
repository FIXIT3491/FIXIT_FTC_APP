package org.firstinspires.ftc.teamcode.newhardware.FXTSensors;


import android.os.AsyncTask;

import org.firstinspires.ftc.teamcode.RC;

/**
 * Created by FIXIT on 16-07-07.
 */
public class ArduCamMini {

    /*
    As of now, there's no way this will work. Well, let's say there's a 0.01% chance.
    The I2C to SPI Bridge we might use has the ability to interface with 4 different SPI slaves

    My first assumption: selecting which Slave Select pin to write to is how you choose which slave to write to
    So, essentially, to write to the SPI slave's data register, we first specify the Slave Select (SS) pin
    And then write the register address, then the actual data

    Second assumption: in the ArduCam code, when they're writing through SPI, they're writing to the camera module
    as opposed to the ArduCam board they've created.
     */

    private String LOGTAG = "ArduCamMini";

    /*
    CAMERA CONSTANTS
     */

    /*
    Array of values to write to camera module
    Honestly, I have no idea what ANY of these do
    Other than each one sets an image processing parameter.
    But the ArduCam people are writing this, so...
    */
    private final static byte[][] INIT_VALUES =
                    {{(byte) 0xff, 0x00}, {0x2c, (byte) 0xff}, {0x2e, (byte) 0xdf}, {(byte) 0xff, 0x1}, {0x3c, 0x32},
                    {0x11, 0x0}, {0x9, 0x2}, {0x4, (byte) 0xa8}, {0x13, (byte) 0xe5}, {0x14, 0x48}, {0x2c, 0xc}, {0x33, 0x78},
                    {0x3a, 0x33}, {0x3b, (byte) 0xfb}, {0x3e, 0x0}, {0x43, 0x11}, {0x16, 0x10}, {0x39, 0x2}, {0x35, (byte) 0x88},

                    {0x22, 0xa}, {0x37, 0x40}, {0x23, 0x0}, {0x34, (byte) 0xa0}, {0x6, 0x2}, {0x6, (byte) 0x88}, {0x7, (byte) 0xc0},
                    {0xd, (byte) 0xb7}, {0xe, 0x1}, {0x4c, 0x0}, {0x4a, (byte) 0x81}, {0x21, (byte) 0x99}, {0x24, 0x40}, {0x25, 0x38},
                    {0x26, (byte) 0x82}, {0x5c, 0x0}, {0x63, 0x0}, {0x46, 0x22}, {0xc, 0x3a}, {0x5d, 0x55}, {0x5e, 0x7d}, {0x5f, 0x7d},
                    {0x60, 0x55}, {0x61, 0x70}, {0x62, (byte) 0x80}, {0x7c, 0x5}, {0x20, (byte) 0x80}, {0x28, 0x30}, {0x6c, 0x0},
                    {0x6d, (byte) 0x80}, {0x6e, 0x0}, {0x70, 0x2}, {0x71, (byte) 0x94}, {0x73, (byte) 0xc1}, {0x3d, 0x34}, {0x12, 0x4},
                    {0x5a, 0x57}, {0x4f, (byte) 0xbb}, {0x50, (byte) 0x9c}, {(byte) 0xff, 0x0}, {(byte) 0xe5, 0x7f}, {(byte) 0xf9, (byte) 0xc0},
                    {0x41, 0x24}, {(byte) 0xe0, 0x14}, {0x76, (byte) 0xff}, {0x33, (byte) 0xa0}, {0x42, 0x20}, {0x43, 0x18}, {0x4c, 0x0},
                    {(byte) 0x87,(byte)0xd0}, {(byte) 0x88, 0x3f}, {(byte) 0xd7, 0x3}, {(byte) 0xd9, 0x10}, {(byte) 0xd3, (byte) 0x82},
                    {(byte) 0xc8, 0x8}, {(byte) 0xc9, (byte) 0x80}, {0x7c, 0x0}, {0x7d, 0x0}, {0x7c, 0x3}, {0x7d, 0x48}, {0x7d, 0x48},
                    {0x7c, 0x8}, {0x7d, 0x20}, {0x7d, 0x10}, {0x7d, 0xe}, {(byte) 0x90, 0x0}, {(byte) 0x91, 0xe}, {(byte) 0x91, 0x1a},
                    {(byte) 0x91, 0x31}, {(byte) 0x91, 0x5a}, {(byte) 0x91, 0x69}, {(byte) 0x91, 0x75}, {(byte) 0x91, 0x7e},
                    {(byte) 0x91, (byte) 0x88}, {(byte) 0x91, (byte) 0x8f}, {(byte) 0x91, (byte) 0x96}, {(byte) 0x91, (byte) 0xa3},
                    {(byte) 0x91, (byte) 0xaf}, {(byte) 0x91, (byte) 0xc4}, {(byte) 0x91, (byte) 0xd7}, {(byte) 0x91, (byte) 0xe8},
                    {(byte) 0x91, 0x20}, {(byte) 0x92, 0x0},

                    {(byte) 0x93, 0x6}, {(byte) 0x93, (byte) 0xe3}, {(byte) 0x93, 0x3}, {(byte) 0x93, 0x3}, {(byte) 0x93, 0x0},
                    {(byte) 0x93, 0x2}, {(byte) 0x93, 0x0}, {(byte) 0x96, 0x0}, {(byte) 0x97, 0x8}, {(byte) 0x97, 0x19}, {(byte) 0x97, 0x2},
                    {(byte) 0x97, 0xc}, {(byte) 0x97, 0x24}, {(byte) 0x97, 0x30}, {(byte) 0x97, 0x28}, {(byte) 0x97, 0x26}, {(byte) 0x97, 0x2},
                    {(byte) 0x97, (byte) 0x98}, {(byte) 0x97, (byte) 0x80}, {(byte) 0x97, 0x0}, {(byte) 0x97, 0x0}, {(byte) 0xa4, 0x0},
                    {(byte) 0xa8, 0x0}, {(byte) 0xc5, 0x11}, {(byte) 0xc6, 0x51}, {(byte) 0xbf, (byte) 0x80}, {(byte) 0xc7, 0x10}, {(byte) 0xb6, 0x66},
                    {(byte) 0xb8, (byte) 0xa5}, {(byte) 0xb7, 0x64}, {(byte) 0xb9, 0x7c}, {(byte) 0xb3, (byte) 0xaf}, {(byte) 0xb4, (byte) 0x97},
                    {(byte) 0xb5, (byte) 0xff}, {(byte) 0xb0, (byte) 0xc5}, {(byte) 0xb1, (byte) 0x94}, {(byte) 0xb2, 0xf}, {(byte) 0xc4, 0x5c},
                    {(byte) 0xa6, 0x0}, {(byte) 0xa7, 0x20}, {(byte) 0xa7, (byte) 0xd8}, {(byte) 0xa7, 0x1b}, {(byte) 0xa7, 0x31},
                    {(byte) 0xa7, 0x0}, {(byte) 0xa7, 0x18}, {(byte) 0xa7, 0x20}, {(byte) 0xa7, (byte) 0xd8}, {(byte) 0xa7, 0x19}, {(byte) 0xa7, 0x31},
                    {(byte) 0xa7, 0x0}, {(byte) 0xa7, 0x18}, {(byte) 0xa7, 0x20}, {(byte) 0xa7, (byte) 0xd8}, {(byte) 0xa7, 0x19}, {(byte) 0xa7, 0x31}, {(byte) 0xa7, 0x0},
                    {(byte) 0xa7, 0x18}, {0x7f, 0x0}, {(byte) 0xe5, 0x1f}, {(byte) 0xe1, 0x77}, {(byte) 0xdd, 0x7f}, {(byte) 0xc2, 0xe},

                    {(byte) 0xff, 0x0}, {(byte) 0xe0, 0x4}, {(byte) 0xc0, (byte) 0xc8}, {(byte) 0xc1, (byte) 0x96}, {(byte) 0x86, 0x3d},
                    {0x51, (byte) 0x90}, {0x52, 0x2c}, {0x53, 0x0}, {0x54, 0x0}, {0x55, (byte) 0x88}, {0x57, 0x0},

                    {0x50, (byte) 0x92}, {0x5a, 0x50}, {0x5b, 0x3c}, {0x5c, 0x0}, {(byte) 0xd3, 0x4}, {(byte) 0xe0, 0x0},

                    {(byte) 0xff, 0x0}, {0x5, 0x0},

                    {(byte) 0xda, 0x8}, {(byte) 0xd7, 0x3}, {(byte) 0xe0, 0x0},

                    {(byte) 0x5, 0x0},
            };


    public static final byte CAMERA_I2C_WRITE_ADDR = 0x60;
    public static final byte CAMERA_I2C_READ_ADDR = 0x61;

    /*
    BRIDGE CONSTANTS
     */

    public final static byte ARDUCHIP_FIFO = 0x04;

    public final static byte BRIDGE_ADDRESS_000 =    0X50,
                             BRIDGE_ADDRESS_001 =    0X52,
                             BRIDGE_ADDRESS_010 =    0X54,
                             BRIDGE_ADDRESS_011 =    0X56,
                             BRIDGE_ADDRESS_100 =    0X58,
                             BRIDGE_ADDRESS_101 =    0X5A,
                             BRIDGE_ADDRESS_110 =    0X5C,
                             BRIDGE_ADDRESS_111 =    0X5E;

    private I2cSensor camera;
    private I2cSensor bridge;
    private volatile boolean cameraInit = false;
    private volatile boolean bridgeInit = false;

    private static final int INIT_TIME_OUT = 15;

    //Initializing ArduCam...
    public ArduCamMini(String camName, String bridgeName) {
        camera = new I2cSensor(camName, CAMERA_I2C_READ_ADDR, CAMERA_I2C_WRITE_ADDR);
        bridge = new I2cSensor(bridgeName, BRIDGE_ADDRESS_000);

        //initialize both devices at the same time to hurry things along
        new CamInitTask().execute();
        new BridgeInitTask().execute();

        long startTime = System.currentTimeMillis();

        while (!cameraInit || !bridgeInit) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }//catch

            //if init has taken too long, we give up
            if (System.currentTimeMillis() - startTime > INIT_TIME_OUT * 1000) {
                break;
            }//if
        }//while

        if (cameraInit) {
            RC.t.addData(LOGTAG, "Camera initialized!");
        } else {
            RC.t.addData(LOGTAG, "Camera failed to initialize!");
        }//else

        if (bridgeInit) {
            RC.t.addData(LOGTAG, "I2C to SPI Bridge initialized!");
        } else {
            RC.t.addData(LOGTAG, "I2C to SPI Bridge failed to initialize!");
        }//else

    }//constructor

    public boolean andBool(int and) {
        return and == 1;
    }

    public void startCapture() {

        //trigger image capture
        writeSPI(ARDUCHIP_FIFO, 0x02);

        while (readSPI()[0] != 1);



    }

    public void writeSPI(int...data) {

        data[0] |= 0x80; //ArduCam does this...

        bridge.write(0x01, data); //use SS0 pin (may change?)
    }

    public byte[] readSPI() {

        return null;
    }

    private class CamInitTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            synchronized(camera) {
                camera.beginCallBack(0x0A);

                //verifying ov7725
                byte pidM = camera.read(0x0A, 1)[0];
                byte pidL = camera.read(0x0B, 1)[0];

                if (pidM != 0x26 || pidL != 0x42) {
                    RC.t.addData(LOGTAG, "Camera not detected!");
                    return null;
                }//if

                camera.write(0xFF, 0x01);
                camera.write(0x12, 0x80); //resets all register values

                I2cSensor.delay(100); //Arducam people are doing it...

                //write the massive array of data at the top of this page
                for (int i = 0; i < INIT_VALUES.length; i++) {
                    camera.write(INIT_VALUES[i][0], INIT_VALUES[i][1]);
                }//for

            }//synchronized

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            cameraInit = true; //TODO: actually check if cam is initialized
        }
    }

    private class BridgeInitTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            synchronized(bridge) {

                //writing process taken from SandboxElectronics
                bridge.beginCallBack(0x00); //using dummy register address

                //we might want to turn down the output freqency of the SPI
                bridge.write(0xF0, 0x00); //configuring SPI interface
                bridge.write(0xF4, 0x00); //setting pins to SSx as opposed to GPIO
                bridge.write(0xF6, 0x00); //disabling all pins as GPIOs
                bridge.write(0xF7, 0xAA); //setting all SSx to be input-only (high impedance)

                writeSPI(ARDUCHIP_FIFO, 0x01);
                writeSPI(0x01, 0x00);
            }//synchronized

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            bridgeInit = true; //TODO: actually check if bridge is initialized

        }
    }

}
