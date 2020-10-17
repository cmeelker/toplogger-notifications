package com.example.chroslog;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static List<DesiredSlot> desiredSlots = new ArrayList<DesiredSlot>();
    SimpleDateFormat firstLineFormat = new SimpleDateFormat("EEEE HH:mm");
    SimpleDateFormat secondLineFormat = new SimpleDateFormat("dd MMMM yyyy");
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CustomListAdapter listAdapter = new CustomListAdapter(this,
                createDateLines(MainActivity.desiredSlots, firstLineFormat),
                createDateLines(MainActivity.desiredSlots, secondLineFormat),
                createAvailabilityLines(MainActivity.desiredSlots)
        );
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(listAdapter);
    }

    // Format the calendar type into a nice string
    private String[] createDateLines(List<DesiredSlot> Slots, SimpleDateFormat format){
        String[] lines = new String[Slots.size()];
        for (int i = 0; i < MainActivity.desiredSlots.size(); i++){
            Calendar calendar = MainActivity.desiredSlots.get(i).date;
            Date date = calendar.getTime();
            String firstLine = format.format(date);
            lines[i] = firstLine;
        }
        return lines;
    }

    private String[] createAvailabilityLines(List<DesiredSlot> Slots){
        String[] lines = new String[Slots.size()];
        for (int i = 0; i < MainActivity.desiredSlots.size(); i++){
            boolean full = MainActivity.desiredSlots.get(i).full;
            int slots_available = MainActivity.desiredSlots.get(i).slots_available;
            if (full){
                lines[i] = "full";
            } else {
                if (slots_available == 1){
                    lines[i] = "1 slot available";
                } else {
                lines[i] = slots_available + " slots available";
                }
            }
        }
        return lines;
    }

    public void showTimePicker(View v){
        DateTimePickerActivity newPicker = new DateTimePickerActivity();
        Intent intent = new Intent(MainActivity.this, DateTimePickerActivity.class);
        startActivity(intent);
    }
}




