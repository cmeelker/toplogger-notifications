package com.example.chroslog;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

public class CheckSlotsService extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        Log.d("debugTag", "Doing work every minute ");

                        // Do API work
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 60000); // every minute
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

