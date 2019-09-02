package com.example.meetingactivity.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.meetingactivity.R;
import com.example.meetingactivity.model.Calendar;

import java.util.List;

public class CalendarAdapter extends ArrayAdapter<Calendar> {
    Activity activity;
    int resource;

    public CalendarAdapter(Context context, int resource, List<Calendar> objects) {
        super(context, resource, objects);
        activity = (Activity) context;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = activity.getLayoutInflater().inflate(resource, null);
        }

        Calendar item = getItem(position);
        if(item != null){
            TextView calendar_Name = convertView.findViewById(R.id.calendar_Name);
            TextView calendar_Date = convertView.findViewById(R.id.calendar_Date);
            TextView calendar_Money = convertView.findViewById(R.id.calendar_Money);
            TextView calendar_Dday = convertView.findViewById(R.id.calendar_Dday);

            calendar_Name.setText(item.getSch_title());
            calendar_Date.setText(item.getSch_year() + "-" + item.getSch_month() + "-" + item.getSch_day());
            calendar_Money.setText(item.getSch_amount());
            calendar_Dday.setText(item.getSch_day());
        }
        return convertView;
    }
}
