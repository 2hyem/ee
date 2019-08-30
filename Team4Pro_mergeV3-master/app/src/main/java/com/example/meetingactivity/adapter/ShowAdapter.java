package com.example.meetingactivity.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.meetingactivity.Activity.MoimActivity;
import com.example.meetingactivity.Fragment.BoardFragment;
import com.example.meetingactivity.R;
import com.example.meetingactivity.model.Board;
import com.example.meetingactivity.model.MoimUser;

import java.util.List;

public class ShowAdapter extends ArrayAdapter<Board> {
    Activity activity;
    int resource;
    BoardFragment boardFragment;
    public ShowAdapter(Context context, int resource,List<Board> objects) {
        super(context, resource, objects);
        activity=(Activity)context;
        this.resource=resource;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(resource, null);
        }

        Board item = getItem(position);
        if (item != null) {

            TextView textUsername = convertView.findViewById(R.id.textUsername);
            TextView textEditdate = convertView.findViewById(R.id.textEditdate);
            TextView textSubject = convertView.findViewById(R.id.textSubject);
            TextView textContent = convertView.findViewById(R.id.textContent);

            ImageView imageView = convertView.findViewById(R.id.imageView);
            ImageView boardImage = convertView.findViewById(R.id.boardImage);

            textUsername.setText(item.getName());
            textEditdate.setText(item.getEditdate());
            textSubject.setText(item.getSubject());
            textContent.setText(item.getContent());

            // Glide 사용
            Glide.with(boardImage).load(item.getFilename()).error(R.drawable.ic_error_w).placeholder(R.drawable.ic_empty_b).into(boardImage);





        }

        return convertView;
    }
}