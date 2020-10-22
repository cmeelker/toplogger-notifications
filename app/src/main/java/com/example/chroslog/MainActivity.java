package com.example.chroslog;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("debugTag", "Back in Main");

        // Get our list from sharedPrefs
        List<DesiredSlot>  desiredSlots = SharedPrefsHelper.getFromSharedPrefs(this);

        // For each slot in the list where keepLooking == true, check if there is an empty spot
        // WHILE LOOP, that runs every minute
        for (int i = 0; i < desiredSlots.size(); i++){
            if (desiredSlots.get(i).keepLooking){
                DesiredSlot.do_api_call(this, desiredSlots.get(i), i);
            }
        }

        setContentView(R.layout.activity_main);

        CustomListAdapter listAdapter = new CustomListAdapter(this, desiredSlots);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(listAdapter);
    }

    public void showTimePicker(View v){
        DateTimePickerActivity newPicker = new DateTimePickerActivity();
        Intent intent = new Intent(MainActivity.this, DateTimePickerActivity.class);
        startActivity(intent);
    }
}




