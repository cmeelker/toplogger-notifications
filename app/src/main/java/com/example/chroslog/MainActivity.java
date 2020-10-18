package com.example.chroslog;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //public static List<DesiredSlot> desiredSlots = new ArrayList<DesiredSlot>();
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<DesiredSlot> desiredSlots = new ArrayList<DesiredSlot>();
        setContentView(R.layout.activity_main);

        // Load slots file, update if storage isn't empty
        List<DesiredSlot> slots_from_storage = IOHelper.getFromStorage(this);
        if (slots_from_storage != null){
            desiredSlots = slots_from_storage;
        }

        // For each DesiredSlot in our list: update availability
        List<DesiredSlot> new_slots = DesiredSlot.update_all_slots(this, desiredSlots);

        CustomListAdapter listAdapter = new CustomListAdapter(this, new_slots);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(listAdapter);
    }

    public void showTimePicker(View v){
        DateTimePickerActivity newPicker = new DateTimePickerActivity();
        Intent intent = new Intent(MainActivity.this, DateTimePickerActivity.class);
        startActivity(intent);
    }
}




