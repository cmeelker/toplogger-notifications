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
import java.util.List;

// Works in debug mode, so we just need to wait a bit somewhere??

// Nadenken over refactor, waarbij slots_available niet telksens naar storage wordt geschreven,
// Maar gewoon wordt berekend voor elk slot dat is de storage staat.
// Zo hoeven we alleen een IO toe doen wanneer je een slot toevoegt.

public class DesiredSlot {
    Calendar date;
    int slots_available = 99; // JUST TAKES THIS ONE, SLOTS_AVAILABLE IS NOT UPDATED...

    public DesiredSlot(Calendar date) {
        this.date = date;
    }

    public static List<DesiredSlot> update_all_slots(Context context, final List<DesiredSlot> slots){

        if (slots != null){
            for (int i = 0; i < slots.size(); i++){
                DesiredSlot new_slot = update_available_slots(context, slots.get(i));
            }
            // update storage file with new slot object
            IOHelper.writeToStorage(context, slots);
        }

        // TO DO: HACKY DELAY SO IT WORKS
        for (int i = 0; i < 1000; i++){
            List<DesiredSlot> joe = IOHelper.getFromStorage(context);
        }

        return slots; // IF I PUT BREAKPOINT HERE IT WORKS! Maybe need AsyncTask?
    }

    // Function checks how many slots are available, and then updates the DesiredSlot Object
    public static DesiredSlot update_available_slots(final Context context, final DesiredSlot slot){
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

        return slot;
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
        int available_slots = 88;

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
