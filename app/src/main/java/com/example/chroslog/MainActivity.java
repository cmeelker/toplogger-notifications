package com.example.chroslog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
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

        createNotificationChannel();

        // Get our list from sharedPrefs
        List<DesiredSlot>  desiredSlots = SharedPrefsHelper.getFromSharedPrefs(this);

        // Start the service which checks the API every x minutes
        Intent intent = new Intent(this, CheckSlotsService.class);
        startService(intent);

        setContentView(R.layout.activity_main);

        CustomListAdapter listAdapter = new CustomListAdapter(this, desiredSlots);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(listAdapter);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "sterk_channel";
            String description = "sterk_channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("sterk_channel", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void showTimePicker(View v){
        DateTimePickerActivity newPicker = new DateTimePickerActivity();
        Intent intent = new Intent(MainActivity.this, DateTimePickerActivity.class);
        startActivity(intent);
    }
}




