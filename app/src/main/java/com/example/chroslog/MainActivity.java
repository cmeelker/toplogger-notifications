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

        // Get our list from sharedPrefs
        List<DesiredSlot>  desiredSlots = SharedPrefsHelper.getFromSharedPrefs(this);

        // For each slot in the list where keepLooking == true, check if there is an empty spot
        // TO DO : WHILE LOOP, that runs every minute
        for (int i = 0; i < desiredSlots.size(); i++){
            if (desiredSlots.get(i).keepLooking){
                DesiredSlot.do_api_call(this, desiredSlots.get(i), i);
            }
        }



        createNotificationChannel();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "sterk_channel")
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle("My notification")
                .setContentText("Much longer text that cannot fit one line...")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("What is this field"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        // notificationId is a unique int for each notification that you must define


        setContentView(R.layout.activity_main);

        if (desiredSlots.size() > 2){
            notificationManager.notify(999, builder.build());
        }



        CustomListAdapter listAdapter = new CustomListAdapter(this, desiredSlots);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(listAdapter);


    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
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




