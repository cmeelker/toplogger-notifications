package com.example.chroslog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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

    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        final View rowView=inflater.inflate(R.layout.listview_row, null,true);

        // This code gets references to objects in the listview_row.xml file
        final TextView firstLineField = (TextView) rowView.findViewById(R.id.firstLineText);
        TextView secondLineField = (TextView) rowView.findViewById(R.id.secondLineText);

        // Create the actual lines from the data
        final String[] firstLines = createDateLines(slots, firstLineFormat);
        String[] secondLines = createDateLines(slots, secondLineFormat);

        // This code sets the values of the objects to values from the arrays
        firstLineField.setText(firstLines[position]);
        secondLineField.setText(secondLines[position]);

        ImageView imageView = rowView.findViewById(R.id.deleteIcon);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Delete item from list in sharedPrefs
                DesiredSlot.deleteSlot(getContext(), position);

                // And delete from list the ArrayAdapter has
                slots.remove(position);

                // Reload list
                notifyDataSetChanged();
            }
        });

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
}