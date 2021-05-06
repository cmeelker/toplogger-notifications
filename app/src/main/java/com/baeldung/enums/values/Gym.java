package com.baeldung.enums.values;

import com.example.chroslog.R;

public enum Gym {
    Energiehaven_buiten   ("Energiehaven - buiten", 11,67, R.mipmap.eh_outdoor),
    Sterk  ("Sterk", 20,4,  R.mipmap.sterk),
    Energiehaven_binnen("Energiehaven - Binnen", 11,15, R.mipmap.eh_indoor)

    ; // semicolon needed when fields / methods follow

    private final String name;
    private final int id;
    private final int reservation_area;
    private final int logo;

    private Gym(String name, int id, int reservation_area, int logo) {
        this.name = name;
        this.id = id;
        this.reservation_area = reservation_area;
        this.logo = logo;
    }

    public String getName() {
        return this.name;
    }

    public int getId() {
        return this.id;
    }

    public int getReservation_area() {
        return this.reservation_area;
    }

    public int getLogo() {
        return this.logo;
    }

}