package com.example.chroslog;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

abstract class Slot {
    Date start_date;
    Date end_date;

    public Slot(Date start_date, Date end_date) {
        this.start_date = start_date;
        this.end_date = end_date;
    }

    public int getSlotDuration(){
        long diffInMillies = Math.abs(end_date.getTime() - start_date.getTime());
        int diff = (int) TimeUnit.HOURS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        return diff;
    }

    public String getStartTime(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");      // 19:00
        return dateFormat.format(start_date);
    }
}

// Slot type that is used in our wish list of slots.
class DesiredSlot extends Slot{
    boolean keepLooking = true;
    public DesiredSlot(Date start_date, Date end_date) {
        super(start_date, end_date);
    }

    public static void deleteSlotFromSharedPrefs(Context context, int i){
        // Load sharedPrefs
        List<DesiredSlot> desiredSlots = SharedPrefsHelper.getFromSharedPrefs(context);

        // Remove the slot by index
        desiredSlots.remove(i);

        // Put new list in sharedPrefs
        SharedPrefsHelper.writeToSharedPrefs(context, desiredSlots);
    }
}

// Slot type that is used in the 'create notification' screen.
class SelectableSlot extends Slot{
    boolean selected = false;

    public SelectableSlot(Date start_date, Date end_date) {
        super(start_date, end_date);
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
