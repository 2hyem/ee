package com.example.meetingactivity.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.meetingactivity.R;
import com.example.meetingactivity.model.MemberTest;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

public class MemberAdapter extends ArrayAdapter<MemberTest> {
    Activity activity;
    int resource;
    ImageLoader imageLoader;
    DisplayImageOptions options;


    public MemberAdapter(Context context, int resource, List<MemberTest> objects) {
        super(context, resource, objects);
        activity = (Activity) context;
        this.resource = resource;
        imageLoaderInit();
    }

    private void imageLoaderInit() {
        imageLoader = ImageLoader.getInstance();
        if(!imageLoader.isInited()){
            ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(activity);
            imageLoader.init(configuration);
        }

        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
        builder.showImageOnLoading(R.drawable.ic_stub);
        builder.showImageOnFail(R.drawable.ic_error);
        builder.showImageForEmptyUri(R.drawable.ic_empty);
        builder.cacheInMemory(true);
        builder.cacheOnDisk(true);
        builder.considerExifParams(true);
        options = builder.build();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = activity.getLayoutInflater().inflate(resource, null);
        }

        MemberTest item = getItem(position);
        if(item != null){
            ImageView memitem_img = convertView.findViewById(R.id.memitem_img);
            TextView memitem_name = convertView.findViewById(R.id.memitem_name);
            TextView memitem_permit = convertView.findViewById(R.id.memitem_permit);

            if(!item.getThumbnail_image().equals("")){
                imageLoader.displayImage(item.getThumbnail_image(), memitem_img, options);
            }
            memitem_name.setText(item.getNickname());
            memitem_permit.setText(Integer.toString(item.getPermit()));
        }

        return convertView;
    }
}
