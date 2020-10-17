package com.example.chroslog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CheckForDateTime();
        setContentView(R.layout.activity_main);

    }

    /** Called when the user taps the Send button */
    public void sendMessage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    private void CheckForDateTime(){
         if(getIntent().getBooleanExtra("fromDatePicker", false)){
             int year = getIntent().getIntExtra("year", 0);
             int month = getIntent().getIntExtra("month", 0);
             int day = getIntent().getIntExtra("day", 0);
             showTimePickerDialog();
         }
    }
    public void showTimePickerDialog() {
        DialogFragment newFragment = new DateTimeDialog.TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }


    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DateTimeDialog.DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
}




