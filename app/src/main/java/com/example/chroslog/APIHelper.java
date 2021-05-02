package com.example.chroslog;

import java.text.SimpleDateFormat;
import java.util.Date;

public class APIHelper {
    static public String create_api_url(Date date){
        // TO DO: Add Gym
        SimpleDateFormat api_format_date = new SimpleDateFormat("yyyy-MM-dd"); // 2020-10-15
        String date_string =  api_format_date.format(date);
        // Gym 20 = Sterk, Gym 11 = EH
        // Reservation_area = 4 voor sterk, 15 voor EH, 67 voor buiten EH
        String gym = "11";
        String reservation_area = "67";
        String url = "https://api.toplogger.nu/v1/gyms/" + gym + "/slots?date=" + date_string + "&reservation_area_id=" + reservation_area + "&slim=true";
        return url;
    }
}
