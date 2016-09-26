package com.taxi.clickcar;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * Created by Назар on 11.06.2016.
 */
public class Utils {
    public static String json="";
    public Utils(){}
    public static String readFromFile(File f){
        json="";
        //Read text from file
        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        }
        catch (IOException e) {

        }
        json=text.toString();
        return json;
    }
    public static void writeToFile(File f,String _json){
        FileWriter writer = null;
        try {
            writer = new FileWriter(f);
            writer.append(_json);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
