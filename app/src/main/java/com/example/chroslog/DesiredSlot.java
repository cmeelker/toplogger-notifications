package com.example.chroslog;

import android.content.Context;
import android.view.View;

import java.util.Calendar;
import java.util.List;

public class DesiredSlot {
    Calendar date;
    boolean keepLooking = true;

    public DesiredSlot(Calendar date) {
        this.date = date;
    }

    public static void deleteSlot(Context context, int i){
        // Load sharedPrefs
        List<DesiredSlot> desiredSlots = SharedPrefsHelper.getFromSharedPrefs(context);

        // Remove the slot by index
        desiredSlots.remove(i);

        // Put new list in sharedPrefs
        SharedPrefsHelper.writeToSharedPrefs(context, desiredSlots);
    }
}
