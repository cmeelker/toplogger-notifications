package com.example.chroslog;

import android.app.Activity;
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

public class CustomListAdapterHome extends ArrayAdapter {

    // To reference the Activity
    private final Activity context;

    // The data for the list
    private final List<DesiredSlot> slots;

    SimpleDateFormat firstLineFormat = new SimpleDateFormat("EEEE HH:mm");      // Monday 19:00
    SimpleDateFormat secondLineFormat = new SimpleDateFormat("dd MMMM yyyy");   // 18 October 2020

    public CustomListAdapterHome(Activity context, List<DesiredSlot> slots){

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
        ImageView checkboxField = rowView.findViewById(R.id.checkImage);

        // Create the actual lines from the data
        final String[] firstLines = createDateLines(slots, firstLineFormat);
        String[] secondLines = createDateLines(slots, secondLineFormat);

        // Create list of images
        int[] images = createImagesList(slots);

        // This code sets the values of the objects to values from the arrays
        firstLineField.setText(firstLines[position]);
        secondLineField.setText(secondLines[position]);
        checkboxField.setImageResource(images[position]);

        ImageView imageView = rowView.findViewById(R.id.deleteIcon);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Delete item from list in sharedPrefs
                DesiredSlot.deleteSlotFromSharedPrefs(getContext(), position);

                // And delete from list the ArrayAdapter has
                slots.remove(position);

                // Reload list
                notifyDataSetChanged();
            }
        });

        return rowView;
    };

    private int[] createImagesList(List<DesiredSlot> slots){
        int[] list = new int[slots.size()];
        for (int i = 0; i < slots.size(); i++){
            int image;
            list[i] = slots.get(i).gym.getLogo();
            // TO DO: ADD LOGOS with keeplooking asset
//
//            if (slots.get(i).keepLooking == true) {
//                image = R.drawable.empty_check;
//            } else {
//                image = R.drawable.check;
//            }
//            list[i] = image;
        }
        return list;
    }

    // Format the date type into a nice string
    private String[] createDateLines(List<DesiredSlot> slots, SimpleDateFormat format){
        String[] lines = new String[slots.size()];
        for (int i = 0; i < slots.size(); i++){
            Date date = slots.get(i).start_date;
            String firstLine = format.format(date);
            lines[i] = firstLine;
        }
        return lines;
    }
}