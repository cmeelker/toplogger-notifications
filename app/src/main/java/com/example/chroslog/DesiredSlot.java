package com.example.chroslog;

import android.content.Context;
import android.util.Log;

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
import java.util.List;

// Works in debug mode, so we just need to wait a bit somewhere??

// Werken met keepLooking bool. Slots_available hoeft dan niet in de db en ook niet in het lijstje.

// Nadenken over refactor, waarbij slots_available niet telksens naar storage wordt geschreven,
// Maar gewoon wordt berekend voor elk slot dat is de storage staat.
// Zo hoeven we alleen een IO toe doen wanneer je een slot toevoegt.

public class DesiredSlot {
    Calendar date;
    boolean keepLooking = true;

    public DesiredSlot(Calendar date) {
        this.date = date;
    }

    // Function checks how many slots are available, and then updates the DesiredSlot Object
    public static void do_api_call(final Context context, final DesiredSlot slot){
        String url = api_url(slot.date.getTime());
        RequestQueue queue = Volley.newRequestQueue(context);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        // Process the request
                        try {
                            if (is_slot_available(response, slot.date.getTime())) {
                                // Edit keeplooking in memory
                                slot.keepLooking = false;
                                Log.d("debugTag", "Send notification!");
                                // Send pushmessage
                            }
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
    private static boolean is_slot_available(JSONArray response, Date date) throws JSONException {
        // TO DO: FIX ZOMER WINTER TIJD
        SimpleDateFormat api_format_time = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:00.000'+'01:00");
        String time = api_format_time.format(date);

        for (int i=0; i < response.length(); i++) {
            JSONObject slot = response.getJSONObject(i);
            Object slot_date = slot.get("start_at");
            // Misschien kunnen we de json regel rechtstreeks krijgen ipv if statement
            if (slot_date.equals(time)) {
                int spots = (int) slot.get("spots");
                int spots_booked = (int) slot.get("spots_booked");
                return spots > spots_booked;
            }
        }
        return false;
    }
}
