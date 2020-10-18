package com.example.chroslog;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CustomListAdapter extends ArrayAdapter {

    // To reference the Activity
    private final Activity context;

    // The data for the list
    private final List<DesiredSlot> slots;

    SimpleDateFormat firstLineFormat = new SimpleDateFormat("EEEE HH:mm");      // Monday 19:00
    SimpleDateFormat secondLineFormat = new SimpleDateFormat("dd MMMM yyyy");   // 18 October 2020

    public CustomListAdapter(Activity context, List<DesiredSlot> slots){

        super(context,R.layout.listview_row , slots);

        this.context=context;
        this.slots = slots;
    };

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.listview_row, null,true);

        // This code gets references to objects in the listview_row.xml file
        TextView firstLineField = (TextView) rowView.findViewById(R.id.firstLineText);
        TextView secondLineField = (TextView) rowView.findViewById(R.id.secondLineText);
        TextView availabilityField = (TextView) rowView.findViewById(R.id.availabilityText);

        // Create the actual lines from the data
        String[] firstLines = createDateLines(slots, firstLineFormat);
        String[] secondLines = createDateLines(slots, secondLineFormat);
        String[] availability = createAvailabilityLines(slots);

        // This code sets the values of the objects to values from the arrays
        firstLineField.setText(firstLines[position]);
        secondLineField.setText(secondLines[position]);
        availabilityField.setText(availability[position]);

        return rowView;
    };

    // Format the calendar type into a nice string
    private String[] createDateLines(List<DesiredSlot> Slots, SimpleDateFormat format){
        String[] lines = new String[Slots.size()];
        for (int i = 0; i < slots.size(); i++){
            Calendar calendar = slots.get(i).date;
            Date date = calendar.getTime();
            String firstLine = format.format(date);
            lines[i] = firstLine;
        }
        return lines;
    }

    private String[] createAvailabilityLines(List<DesiredSlot> Slots){
        String[] lines = new String[Slots.size()];
        for (int i = 0; i < slots.size(); i++){
            int slots_available = slots.get(i).slots_available;
            if (slots_available == 0){
                lines[i] = "full";
            } else {
                if (slots_available == 1){
                    lines[i] = "1 slot available";
                } else {
                    lines[i] = slots_available + " slots available";
                }
            }
        }
        return lines;
    }
}