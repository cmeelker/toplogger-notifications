package com.example.chroslog;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

// This class is needed to
// 1. Transform our List<DesiredSlot> object to a string which can be stored in the SharedPreferences
// 2. Catch the error of empty SharedPrefs in case we don't have any desired slots yet

public class SharedPrefsHelper {
    // Type of our object
    public static Type listType = new TypeToken<ArrayList<DesiredSlot>>(){}.getType(); // Get type List<DesiredSlot>

    // Create JSON string from List<DesiredSlot> Object and write to sharedPrefs
    public static void writeToSharedPrefs(Context context, List<DesiredSlot> newSlots){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(newSlots);
        editor.putString("desired_slots", json);
        editor.apply();
    }

    // Get JSON string from sharedPrefs and return List<DesiredSlot> Object
    public static List<DesiredSlot> getFromSharedPrefs(Context context){
        // This function return empty list in case SharedPrefs is still null

        // Empty list, in case sharedPrefs is emtpy
        List<DesiredSlot> desiredSlots = new ArrayList<DesiredSlot>();

        // Get sharedPrefs
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);

        String json = sharedPref.getString("desired_slots","");
        Gson gson = new Gson();
        List<DesiredSlot> sharedPrefsList = gson.fromJson(json, listType);

        // If sharedPrefs if NOT empty, we will use that list
        if (sharedPrefsList != null){
            // Remove slots that are in the past
            List<DesiredSlot> cleaned_slots = removeOldSlots(sharedPrefsList);
            desiredSlots = cleaned_slots;

            // Write new list to shared prefs
            SharedPrefsHelper.writeToSharedPrefs(context, cleaned_slots);
        }

        return desiredSlots;
    }

    static private List<DesiredSlot> removeOldSlots(List<DesiredSlot> desiredSlots){
        for (int i=0; i < desiredSlots.size(); i++) {
            DesiredSlot slot = desiredSlots.get(i);
            Calendar calendar = Calendar.getInstance();
            Date today_date = calendar.getTime();

             if (today_date.after(slot.start_date)){
                desiredSlots.remove(i);
            }
        }
        return desiredSlots;
    }

    public static void editKeepLooking(Context context, int i, boolean keepLooking){
        // Get our list from sharedPrefs
        List<DesiredSlot> desiredSlots = getFromSharedPrefs(context);

        // Get slot we want to edit
        DesiredSlot slot = desiredSlots.get(i);

        // Edit de slot, and update the list
        slot.keepLooking = keepLooking;
        desiredSlots.set(i, slot);

        // Write new list to sharedPrefs
        writeToSharedPrefs(context, desiredSlots);
    }
}
