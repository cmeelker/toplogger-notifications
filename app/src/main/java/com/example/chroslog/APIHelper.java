package com.example.chroslog;

import com.baeldung.enums.values.Gym;

import java.text.SimpleDateFormat;
import java.util.Date;

public class APIHelper {
    static public String create_api_url(Date date, Gym gym){
        SimpleDateFormat api_format_date = new SimpleDateFormat("yyyy-MM-dd"); // 2020-10-15
        String date_string =  api_format_date.format(date);
        String url = "https://api.toplogger.nu/v1/gyms/" + gym.getId() + "/slots?date=" + date_string + "&reservation_area_id=" + gym.getReservation_area() + "&slim=true";
        return url;
    }
}
