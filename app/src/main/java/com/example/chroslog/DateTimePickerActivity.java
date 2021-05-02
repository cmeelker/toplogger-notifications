package com.example.chroslog;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateTimePickerActivity extends Activity {

    MyCustomAdapter dataAdapter = null;
    ArrayList<SelectableSlot> existingSlots = new ArrayList<SelectableSlot>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_slot_new);

        // Get current date
        final Calendar calendar = Calendar.getInstance();

        // Create list of all slots that exists on that date
        existingSlots = listExistingSlots(calendar.getTime());
        displayListView();

        // Set change listener for calendar
        CalendarView calenderView = findViewById(R.id.calendarView);
        calenderView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                                                 @Override
                                                 public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                                                     calendar.set(year, month, dayOfMonth);
                                                     Log.d("debugTag", "selected date: " + calendar.getTime());
                                                     existingSlots = listExistingSlots(calendar.getTime());
                                                     displayListView();
                                                 }
                                             });

        createButtonClick();
    }

    private void displayListView(){
        // Create an ArrayAdapter from the slots Array
        dataAdapter = new MyCustomAdapter(this,
                R.layout.checkbox_item, existingSlots);
        ListView listView = (ListView) findViewById(R.id.listView);

        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);
    }

    private ArrayList<SelectableSlot> listExistingSlots(Date date){
        final ArrayList<SelectableSlot> existing_slots = new ArrayList<SelectableSlot>();
        String url = APIHelper.create_api_url(date);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        JsonArrayRequest stringRequest = new JsonArrayRequest (Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray  response) {
                        SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:00.000XXX");

                        for (int i=0; i < response.length(); i++) {
                            try {
                                // Get slot info
                                JSONObject slot = response.getJSONObject(i);
                                String start_time = slot.getString("start_at");
                                Date start_date = date_format.parse(start_time);
                                String end_time = slot.getString("end_at");
                                Date end_date = date_format.parse(end_time);

                                Log.d("debugTag", "start date is: "+ start_date.toString());

                                // Create slot object and add to list
                                SelectableSlot new_slot = new SelectableSlot(start_date, end_date);

                                // SOMETHING GOES WRONG HERE? VARIABLE STAYS EMPTY
                                existing_slots.add(i, new_slot);

                                Log.d("debugTag", "Added slot");


                            } catch (JSONException | ParseException e) {
                                e.printStackTrace();
                            }
                            }
                        Log.d("debugTag", "Response is: "+ response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("debugTag", "That did't work" + error);
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

        displayListView();
        return existing_slots;
    }


    public void selectAll(View v){
        ArrayList<SelectableSlot> slotList = dataAdapter.slotList;
        for(int i = 0; i< slotList.size(); i++){
            SelectableSlot slot = slotList.get(i);
            slot.setSelected(true);
        }

        displayListView();
    }

    private void createButtonClick() {
        Button myButton = (Button) findViewById(R.id.button);
        myButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ArrayList<SelectableSlot> selectedSlots = new ArrayList<SelectableSlot>();

                ArrayList<SelectableSlot> slotList = dataAdapter.slotList;
                for(int i = 0; i< slotList.size(); i++){
                    SelectableSlot slot = slotList.get(i);
                    if(slot.isSelected()){
                        selectedSlots.add(slot);
                    }
                }

                // Add selected slots to shared preferences
                addNewDesiredSlots(selectedSlots);
            }
        });
    }

    private void addNewDesiredSlots(ArrayList<SelectableSlot> selectedSlots){
        for(int i = 0; i< selectedSlots.size(); i++){
            SelectableSlot slot = selectedSlots.get(i);
            DesiredSlot newEntry = new DesiredSlot(slot.start_date, slot.end_date);

            // Get current list
            List<DesiredSlot> desiredSlots = SharedPrefsHelper.getFromSharedPrefs(this);

            // Add our new entry
            desiredSlots.add(newEntry);

            // Put new list in sharedPrefs
            SharedPrefsHelper.writeToSharedPrefs(this, desiredSlots);
        }

        // Then back to the main screen.
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    private class MyCustomAdapter extends ArrayAdapter<SelectableSlot> {

        private ArrayList<SelectableSlot> slotList;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<SelectableSlot> slotList) {
            super(context, textViewResourceId, slotList);
            this.slotList = new ArrayList<SelectableSlot>();
            this.slotList.addAll(slotList);
        }

        private class ViewHolder {
            TextView code;
            CheckBox name;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.checkbox_item, null);

                holder = new ViewHolder();
                holder.code = (TextView) convertView.findViewById(R.id.code);
                holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
                convertView.setTag(holder);

                holder.name.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v ;
                        SelectableSlot slot = (SelectableSlot) cb.getTag();
                        slot.setSelected(cb.isChecked());
                    }
                });
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            SelectableSlot slot = slotList.get(position);
            holder.code.setText(" (" +  slot.getSlotDuration() + ")");
            holder.name.setText(slot.getStartTime());
            holder.name.setChecked(slot.isSelected());
            holder.name.setTag(slot);

            return convertView;

        }
    }
}
