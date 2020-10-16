package com.example.chroslog;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DisplayMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        // Display the selected time
        TextView textView = findViewById(R.id.textView);
        textView.setText(message);

        slot_api_call(message);

    }

    // Function does API call and writes output to textView2.
    private void slot_api_call(final String time){
        final TextView textView2 = findViewById(R.id.textView2);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://api.toplogger.nu/v1/gyms/20/slots?date=2020-10-15&reservation_area_id=4&slim=true";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        // Call function that checks if slot is available for our time
                        Boolean availability = null;
                        try {
                            availability = check_availability(response, time);
                            // Write to textview
                            textView2.setText(availability.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        textView2.setText("That didn't work!");
                    }
                });
        queue.add(jsonArrayRequest);
    }

    private boolean check_availability(JSONArray response, String time) throws JSONException {
        // Availability is false by default
        // We go over every row.
        // Check whether requested time has free spots
        boolean avail = false;
        String formatted_date = format_date("15-10-2020T23:00");

        for (int i=0; i < response.length(); i++) {
            JSONObject slot = response.getJSONObject(i);
            Object joe = slot.get("start_at");
            if (joe.equals(formatted_date)) {
                int spots = (int) slot.get("spots");
                int spots_booked = (int) slot.get("spots_booked");
                if (spots > spots_booked) {
                    avail = true;
                }
            }
        }
        return avail;
    }

    private String format_date(String datetime){
        // WE ONLY NEED OUTPUT FORMAT WHEN WE HAVE AN ACTUAL DATE SELECTER
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy'T'HH:mm");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:00.000'+'02:00");
        Date date = null;
        try {
            date = inputFormat.parse(datetime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = outputFormat.format(date);
        return formattedDate;
    }

}
