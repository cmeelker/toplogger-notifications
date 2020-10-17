package com.example.chroslog;

import java.util.Calendar;
import java.util.Date;

public class DesiredSlot {
    Calendar date;

    public DesiredSlot(Calendar date) {
        this.date = date;
    }

    // Methode om availability the checken,
    // mailen wanneer er een slot available is.
    // Deze methode continue voor elk slot aanroepen in main.
}
