package com.example.chroslog;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.baeldung.enums.values.Gym;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class CheckSlotsService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent notificationIntent = new Intent(this, CheckSlotsService.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, "roggelpot_channel")
                .setContentTitle("I am checking Toplogger!")
                .setContentText("This is a foreground service")
                .setSmallIcon(R.drawable.checking_icon)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(4, notification);

        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        Log.d("debugTag", "Checking API");

                        Context context = getApplication();

                        List<DesiredSlot> desiredSlots = SharedPrefsHelper.getFromSharedPrefs(context);

                        for (int i = 0; i < desiredSlots.size(); i++){
                            if (desiredSlots.get(i).keepLooking){
                                do_api_call(context, desiredSlots.get(i), i);
                            }
                        }
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

    private void createNotification(Context context, DesiredSlot slot){
        SimpleDateFormat notification_format = new SimpleDateFormat("EEEE dd MMM HH:mm");
        String date_string =  notification_format.format(slot.start_date.getTime());

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "roggelpot_channel")
                .setSmallIcon(R.drawable.mountain_icon)
                .setContentTitle("Slot available at " + slot.gym.getName())
                .setContentText(date_string)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(date_string))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        // TO DO: This way we can only have one notification
        notificationManager.notify(1, builder.build());
    }

    // Function checks how many slots are available, and then updates the DesiredSlot Object
    public void do_api_call(final Context context, final DesiredSlot slot, final int i){
        String url = APIHelper.create_api_url(slot.start_date, slot.gym);
        RequestQueue queue = Volley.newRequestQueue(context);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        // Process the request
                        try {
                            if (is_slot_available(response, slot.start_date)) {
                                // Edit keepLooking in memory
                                SharedPrefsHelper.editKeepLooking(context, i, false);
                                Log.d("debugTag", "Send notification");

                                // Send push message!
                                createNotification(context, slot);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error handling
                    }
                });
        queue.add(jsonArrayRequest);
    }

    // This function reads the request, and return how many slots are available on our desired time.
    private boolean is_slot_available(JSONArray response, Date date) throws JSONException {
        SimpleDateFormat api_format_time = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:00.000XXX");
        String time = api_format_time.format(date);

        for (int i=0; i < response.length(); i++) {
            JSONObject slot = response.getJSONObject(i);
            Object slot_date = slot.get("start_at");

            if (slot_date.equals(time)) {
                int spots = (int) slot.get("spots");
                int spots_booked = (int) slot.get("spots_booked");
                return spots > spots_booked;
            }
        }
        return false;
    }
}

