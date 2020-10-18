package com.example.chroslog;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

public class DateTimePickerActivity extends AppCompatActivity
        implements View.OnClickListener {

    Button btnDatePicker, btnTimePicker;
    EditText txtDate, txtTime;
    private int currentYear, currentMonth, currentDay, currentHour, currentMinute;
    private int selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_slot);

        btnDatePicker=(Button)findViewById(R.id.btn_date);
        btnTimePicker=(Button)findViewById(R.id.btn_time);
        txtDate=(EditText)findViewById(R.id.in_date);
        txtTime=(EditText)findViewById(R.id.in_time);

        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if (v == btnDatePicker) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            currentYear = c.get(Calendar.YEAR);
            currentMonth = c.get(Calendar.MONTH);
            currentDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            selectedYear = year;
                            selectedMonth = monthOfYear;
                            selectedDay = dayOfMonth;

                        }
                    }, currentYear, currentMonth, currentDay);
            datePickerDialog.show();
        }
        if (v == btnTimePicker) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            currentHour = c.get(Calendar.HOUR_OF_DAY);
            currentMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            txtTime.setText(hourOfDay + ":" + minute);
                            selectedHour = hourOfDay;
                            selectedMinute = minute;
                        }
                    }, currentHour, currentMinute, true);
            timePickerDialog.show();
        }
    }

    public void onCreateClick(View v){
        // Create DesiredSlot object and put in list.
        Calendar newDate = Calendar.getInstance();
        newDate.set(selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute);
        DesiredSlot newEntry = new DesiredSlot(newDate);
        MainActivity.desiredSlots.add(newEntry);

        // Then back to the main screen.
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}