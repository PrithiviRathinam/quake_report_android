package com.example.android.quakereport;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.support.v4.content.ContextCompat.getColor;
import static com.example.android.quakereport.R.id.date;

/**
 * Created by HP-PC on 21-01-2017.
 */

public class QuakeAdapter extends ArrayAdapter<Earthquake> {

    private static final String LOCATION_TAG = " of ";
    public QuakeAdapter(Context context, ArrayList<Earthquake> quakedata){
        super(context,0,quakedata);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem =  convertView;
        String primaryLocation;
        String proximity;
        if(listItem == null)
            listItem = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        Earthquake quakeInfo = getItem(position);
        TextView magTextView = (TextView)listItem.findViewById(R.id.magnitude);
        DecimalFormat decimalFormatter = new DecimalFormat("0.0");
        String formMagnitude = decimalFormatter.format(quakeInfo.getmMagnitude());

        GradientDrawable gradientDrawable = (GradientDrawable)magTextView.getBackground();
        int magColor = getMagnitudeColor(quakeInfo.getmMagnitude());
        int mColor = ContextCompat.getColor(getContext(),magColor);
        gradientDrawable.setColor(mColor);
        magTextView.setText(formMagnitude);
        Date date = new Date(quakeInfo.getmTimeInMilliseconds());
        TextView dateTextView = (TextView) listItem.findViewById(R.id.date);
        String originalLocation = quakeInfo.getmLocation();
        if(originalLocation.contains(LOCATION_TAG)) {
            String[] locationArray = originalLocation.split(LOCATION_TAG);

            proximity = locationArray[0] + LOCATION_TAG;
            primaryLocation = locationArray[1];
        }else
        {
            proximity = getContext().getString(R.string.near_the);
            primaryLocation = originalLocation;
        }
        TextView locationOffset = (TextView) listItem.findViewById(R.id.location_offset);
        locationOffset.setText(proximity);
        TextView location_data = (TextView) listItem.findViewById(R.id.primary_location);
        location_data.setText(primaryLocation);
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM dd, yyyy");
        String dateString = dateFormatter.format(date);
        dateTextView.setText(dateString);
        TextView timeTextView = (TextView) listItem.findViewById(R.id.time);
        SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm:ss a");
        String timeString = timeFormatter.format(date);
        timeTextView.setText(timeString);


        return listItem;
    }

    public int getMagnitudeColor(double magnitudeValue){
        int magnitudeColorResourceId;
        int value = (int)Math.floor(magnitudeValue);
        switch(value){
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
          return magnitudeColorResourceId;


    }

}
