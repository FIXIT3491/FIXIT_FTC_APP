package com.qualcomm.ftcrobotcontroller.roboticslibrary;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by FIXIT on 2015-05-23.
 */
public class DataReader extends BufferedReader {

    int numLines;

    public DataReader(String fileName, Context c) throws IOException {
            super(new FileReader(new File(c.getExternalFilesDir(null).getAbsolutePath() + "/" + fileName + ".txt")));
            mark(Short.MAX_VALUE);

            while (readLine() != null) {
                numLines++;
            }
            reset();
    }

    public String readFile() {

        String data = "";

        try {
            for (int i = 0; i < numLines; i++) {
                data += readLine() + "\n";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

}
