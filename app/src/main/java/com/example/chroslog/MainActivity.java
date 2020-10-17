package com.example.chroslog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    // public static String val = "empty";
    public static List<DesiredSlot> desiredSlots = new ArrayList<DesiredSlot>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView3 = findViewById(R.id.textView3);
        // textView3.setText((CharSequence) MainActivity.desiredSlots.get(0));

        for (int i = 0; i < MainActivity.desiredSlots.size(); i++){
            System.out.println(MainActivity.desiredSlots.get(i).year);
            System.out.println(MainActivity.desiredSlots.get(i).month);
            System.out.println(MainActivity.desiredSlots.get(i).day);
            System.out.println(MainActivity.desiredSlots.get(i).hour);
            System.out.println(MainActivity.desiredSlots.get(i).minute);
        }
    }

    /** Called when the user taps the Send button */
    public void sendMessage(View v) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void showTimePicker(View v){
        DateTimePickerActivity newPicker = new DateTimePickerActivity();
        Intent intent = new Intent(this, DateTimePickerActivity.class);
        startActivity(intent);
    }
}




