package com.example.rapidcabs;

import android.content.Context;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;

public class PastRidesAdapter extends ArrayAdapter<PastRide> {

    ArrayList<PastRide> rides;
    public PastRidesAdapter(@NonNull Context context, ArrayList<PastRide> al) {
        super(context, 0, al);
        rides=al;
    }

    @NonNull
    @Override
    public View getView(int pos, @NonNull View convertView, @NonNull ViewGroup parent){
        if(convertView == null) {
            convertView = View.inflate(getContext(), R.layout.past_rides_item, null);
        }

        convertView = View.inflate(getContext(), R.layout.past_rides_item, null);

        PastRide curent = rides.get(pos);
        Log.d("Current Ride", String.valueOf(curent.getDist()));
        Log.d("Current Ride", String.valueOf(curent.getD()));

        TextView date = convertView.findViewById(R.id.date);
        TextView distance = convertView.findViewById(R.id.distance);

        String pattern = "MM/dd/yyyy HH:mm:ss";
        DateFormat df = new SimpleDateFormat(pattern);
        String todayAsString = df.format(curent.getD());

        date.setText(todayAsString);
        distance.setText(String.valueOf(curent.getDist()));

        return convertView;
    }
}
