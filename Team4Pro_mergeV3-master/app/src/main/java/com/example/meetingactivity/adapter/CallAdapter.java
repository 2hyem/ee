package com.example.meetingactivity.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.meetingactivity.R;
import com.example.meetingactivity.model.MoimUser;

import java.util.List;

public class CallAdapter extends ArrayAdapter<MoimUser> {
    Activity activity;
    int resource;


    public CallAdapter(Context context, int resource, List<MoimUser> objects) {
        super(context, resource, objects);
        activity = (Activity) context;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(resource, null);
        }
        MoimUser item = getItem(position);
        if (item != null) {
            ImageView imageView = convertView.findViewById(R.id.imageView);
            TextView textView1 = convertView.findViewById(R.id.textView1);
            TextView textView2 = convertView.findViewById(R.id.textView2);

            if(item.getTel() == null) {
                item.setTel("번호 없음");
            }

            Glide.with(activity).load(item.getThumb()).into(imageView);
            textView1.setText("아이디 = " + item.getId() + " / 이름 = " + item.getName());
            textView2.setText("전화번호 = " +item.getTel() + "권한 = " + item.getPermit());

        }
        return convertView;
    }
}
