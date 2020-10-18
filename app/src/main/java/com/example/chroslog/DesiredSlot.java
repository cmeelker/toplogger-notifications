package com.example.chroslog;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DesiredSlot {
    Calendar date;
    int slots_available;

    public DesiredSlot(Calendar date) {
        this.date = date;
    }

    // Function checks how many slots are available, and then updates the DesiredSlot Object
    public static void update_available_slots(Context context, final DesiredSlot slot){
        String url = api_url(slot.date.getTime());
        RequestQueue queue = Volley.newRequestQueue(context);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        // Process the request
                        try {
                            slot.slots_available = check_available_slots(response, slot.date.getTime());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error handling
                    }
                });
        queue.add(jsonArrayRequest);




        // TO DO
        // Sends push message whenever full = false!
    }

    private static String api_url(Date date){
        SimpleDateFormat api_format_date = new SimpleDateFormat("yyyy-MM-dd"); // 2020-10-15
        String date_string =  api_format_date.format(date);
        String url = "https://api.toplogger.nu/v1/gyms/20/slots?date=" + date_string + "&reservation_area_id=4&slim=true";
        return url;
    }

    // This function reads the request, and return how many slots are available on our desired time.
    private static int check_available_slots(JSONArray response, Date date) throws JSONException {
        SimpleDateFormat api_format_time = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:00.000'+'01:00");
        String time = api_format_time.format(date);
        int available_slots = 99;

        for (int i=0; i < response.length(); i++) {
            JSONObject slot = response.getJSONObject(i);
            Object slot_date = slot.get("start_at");
            // Misschien kunnen we de json regel rechtstreeks krijgen ipv if statement
            if (slot_date.equals(time)) {
                int spots = (int) slot.get("spots");
                int spots_booked = (int) slot.get("spots_booked");
                if (spots > spots_booked) {
                    available_slots = spots - spots_booked;
                } else {
                    available_slots = 0;
                }
            }
        }
        return available_slots;
    }
}
