package com.example.chroslog;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    public static List<DesiredSlot> desiredSlots = new ArrayList<DesiredSlot>();

    String[] nameArray = {"Octopus","Pig","Sheep","Rabbit","Snake","Spider" };

    String[] infoArray = {
            "8 tentacled monster",
            "Delicious in rolls",
            "Great for jumpers",
            "Nice in a stew",
            "Great for shoes",
            "Scary."
    };

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //CustomListAdapter whatever = new CustomListAdapter(this, nameArray, infoArray);
        CustomListAdapter whatever = new CustomListAdapter(this,
                firstLines(MainActivity.desiredSlots),
                secondLines(MainActivity.desiredSlots)
        );
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(whatever);

        // List weergeven van alle slots + optie om een slot te verwijderen.
//        for (int i = 0; i < MainActivity.desiredSlots.size(); i++){
//            System.out.println(MainActivity.desiredSlots.get(i).year);
//            System.out.println(MainActivity.desiredSlots.get(i).month);
//            System.out.println(MainActivity.desiredSlots.get(i).day);
//            System.out.println(MainActivity.desiredSlots.get(i).hour);
//            System.out.println(MainActivity.desiredSlots.get(i).minute);
//        }
    }

    private String[] firstLines(List<DesiredSlot> Slots){
        SimpleDateFormat format = new SimpleDateFormat("EEEE HH:mm");
        String[] firstLines = new String[Slots.size()];
        for (int i = 0; i < MainActivity.desiredSlots.size(); i++){
            Calendar calendar = MainActivity.desiredSlots.get(i).date;
            Date date = calendar.getTime();
            String firstLine = format.format(date);
            firstLines[i] = firstLine;
        }
        return firstLines;
    }

    private String[] secondLines(List<DesiredSlot> Slots){
        SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy");
        String[] secondLines = new String[Slots.size()];
        for (int i = 0; i < MainActivity.desiredSlots.size(); i++){
            Calendar calendar = MainActivity.desiredSlots.get(i).date;
            Date date = calendar.getTime();
            String secondLine = format.format(date);
            secondLines[i] = secondLine;
        }
        return secondLines;
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




