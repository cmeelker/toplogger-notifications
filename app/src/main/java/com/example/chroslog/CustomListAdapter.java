package com.example.chroslog;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

// TODO: Add the full/slots_available array here

public class CustomListAdapter extends ArrayAdapter {

    //to reference the Activity
    private final Activity context;

    // e.g. Sunday 12:00
    private final String[] firstLines;

    // e.g. 18 October 2020
    private final String[] secondLines;

    // either full or x slots available
    private final String[] availability;

    public CustomListAdapter(Activity context, String[] firstLinesArray, String[] secondLinesArray, String[] availabilityArray){

        super(context,R.layout.listview_row , firstLinesArray);

        this.context=context;
        this.firstLines = firstLinesArray;
        this.secondLines = secondLinesArray;
        this.availability = availabilityArray;

    };

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.listview_row, null,true);

        //this code gets references to objects in the listview_row.xml file
        TextView firstLineField = (TextView) rowView.findViewById(R.id.firstLineText);
        TextView secondLineField = (TextView) rowView.findViewById(R.id.secondLineText);
        TextView availabilityField = (TextView) rowView.findViewById(R.id.availabilityText);

        //this code sets the values of the objects to values from the arrays
        firstLineField.setText(firstLines[position]);
        secondLineField.setText(secondLines[position]);
        availabilityField.setText(availability[position]);

        return rowView;

    };
}