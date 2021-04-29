package com.example.chroslog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Slot {

    Date start_date;
    Date end_date;
    boolean selected = false;

    public Slot(Date start_date, Date end_date, boolean selected) {
        this.start_date = start_date;
        this.end_date = end_date;
        this.selected = selected;
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

    public Calendar get_start_calendar(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.start_date);
        return calendar;
    }

    public boolean isSelected() {
        return selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
