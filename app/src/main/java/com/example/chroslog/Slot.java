package com.example.chroslog;

import android.content.Context;

import com.baeldung.enums.values.Gym;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

abstract class Slot {
    Date start_date;
    Date end_date;
    Gym gym;

    public Slot(Date start_date, Date end_date, Gym gym) {
        this.start_date = start_date;
        this.end_date = end_date;
        this.gym = gym;
    }

    public String getSlotDuration(){
        long diffInMillies = Math.abs(end_date.getTime() - start_date.getTime());
        double total_minutes = (double) TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS);
        int hours = (int) total_minutes/60;
        int minutes = (int) total_minutes - (hours*60);
        String duration = hours + " hour";
        if (hours > 1){
            duration += "s";
        }
        if (minutes > 0){
            duration += " " + minutes + " minutes";
        }
        return duration;
    }

    public String getTimeString(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");      // 19:00
        return dateFormat.format(date);
    }
}

// Slot type that is used in our wish list of slots.
class DesiredSlot extends Slot{
    boolean keepLooking = true;
    public DesiredSlot(Date start_date, Date end_date, Gym gym) {
        super(start_date, end_date, gym);
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

    public SelectableSlot(Date start_date, Date end_date, Gym gym) {
        super(start_date, end_date, gym);
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
