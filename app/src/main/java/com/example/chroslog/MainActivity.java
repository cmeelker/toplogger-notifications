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
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CustomListAdapter listAdapter = new CustomListAdapter(this, MainActivity.desiredSlots);

//        CustomListAdapter listAdapter = new CustomListAdapter(this,
//                createDateLines(MainActivity.desiredSlots, firstLineFormat),
//                createDateLines(MainActivity.desiredSlots, secondLineFormat),
//                createAvailabilityLines(MainActivity.desiredSlots)
//        );
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(listAdapter);
    }

    public void showTimePicker(View v){
        DateTimePickerActivity newPicker = new DateTimePickerActivity();
        Intent intent = new Intent(MainActivity.this, DateTimePickerActivity.class);
        startActivity(intent);
    }
}




