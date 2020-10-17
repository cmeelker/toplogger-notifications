package com.example.chroslog;

import java.util.Calendar;
import java.util.Date;

public class DesiredSlot {
    Calendar date;
    boolean full = false;
    int slots_available = 1;

    public DesiredSlot(Calendar date) {
        this.date = date;
    }

    public void check_availability(){
        // TO DO
        // This method is going to be called every minute from main.
        // Does the api call
        // Updated full & slots_available if necessary.
        // Sends push message whenever full = false!
    }
}
