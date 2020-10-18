package com.example.chroslog;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class IOHelper {
    static String yourFileName = "slot_wish_list";

    // Create JSON string from List<DesiredSlot> Object and write to file
    public static void writeToStorage(Context context, List<DesiredSlot> desiredSlots){
        Gson gson = new Gson();
        String json = gson.toJson(desiredSlots);

        try {
            String yourFilePath = context.getFilesDir() + "/" + yourFileName;
            File yourFile = new File(yourFilePath);

            //Create your FileOutputStream, yourFile is part of the constructor
            FileOutputStream fileOutputStream = new FileOutputStream(yourFile);

            //Convert your JSON String to Bytes and write() it
            fileOutputStream.write(json.getBytes());

            //Finally flush and close your FileOutputStream
            fileOutputStream.flush();
            fileOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Get JSON string for storage and return List<DesiredSlot> Object
    public static List<DesiredSlot> getFromStorage(Context context){
        Gson gson = new Gson();
        String text = "";

        try {
            String yourFilePath = context.getFilesDir() + "/" + yourFileName;
            File yourFile = new File(yourFilePath);

            //Make an InputStream with your File in the constructor
            InputStream inputStream = new FileInputStream(yourFile);
            StringBuilder stringBuilder = new StringBuilder();

            //Check to see if your inputStream is null
            //If it isn't use the inputStream to make a InputStreamReader
            //Use that to make a BufferedReader
            //Also create an empty String
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";

                //Use a while loop to append the lines from the Buffered reader
                while ((receiveString = bufferedReader.readLine()) != null){
                    stringBuilder.append(receiveString);
                }

                //Close your InputStream and save stringBuilder as a String
                inputStream.close();
                text = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Use Gson to recreate List<DesiredSlot> from the text String
        Type listType = new TypeToken<ArrayList<DesiredSlot>>(){}.getType(); // Get type List<DesiredSlot>
        List<DesiredSlot> desiredSlots = gson.fromJson(text, listType);
        return desiredSlots;
    }
}
