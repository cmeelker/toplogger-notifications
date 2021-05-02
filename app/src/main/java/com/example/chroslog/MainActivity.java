package com.example.chroslog;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createNotificationChannel();

        Log.d("debugTag", "Back in Main");

        // Get our list from sharedPrefs
        List<DesiredSlot> desiredSlots = SharedPrefsHelper.getFromSharedPrefs(this);

        // Start the service which checks the API every x minutes
        Intent intent = new Intent(this, CheckSlotsService.class);
        startService(intent);

        setContentView(R.layout.activity_main);

        CustomListAdapterHome listAdapter = new CustomListAdapterHome(this, desiredSlots);

        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(listAdapter);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "roggelpot_channel";
            String description = "roggelpot_channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("roggelpot_channel", name, importance);
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




