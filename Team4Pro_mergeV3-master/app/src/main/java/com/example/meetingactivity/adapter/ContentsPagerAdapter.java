package com.example.meetingactivity.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.meetingactivity.Fragment.BoardFragment;
import com.example.meetingactivity.Fragment.CalendarFragment;
import com.example.meetingactivity.Fragment.InforFragment;
import com.example.meetingactivity.Fragment.PhotoFragment;
import com.example.meetingactivity.model.Mypage;

public class ContentsPagerAdapter extends FragmentStatePagerAdapter {

    private int mPageCount;

    Intent  intent;

    public ContentsPagerAdapter(FragmentManager fm, int pageCount) {
        super(fm);
        this.mPageCount = pageCount;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                InforFragment inforFragment = new InforFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putString("user_id", intent.getStringExtra("user_id")); // Key, Value
                bundle1.putSerializable("item", intent.getSerializableExtra("item")); // Key, Value
                inforFragment.setArguments(bundle1);

                return inforFragment;

            case 1:
                BoardFragment boardFragment = new BoardFragment();
                //Fragment fragment = new BoardFragment(); // Fragment 생성

                Bundle bundle2 = new Bundle();
                bundle2.putString("user_id", intent.getStringExtra("user_id")); // Key, Value
                bundle2.putSerializable("item", intent.getSerializableExtra("item")); // Key, Value
                boardFragment.setArguments(bundle2);
                return boardFragment;


            case 2:
                PhotoFragment photoFragment = new PhotoFragment();
                return photoFragment;

            case 3:
                CalendarFragment calendarFragment = new CalendarFragment();
                //Fragment fragment = new BoardFragment(); // Fragment 생성

                Bundle bundle3 = new Bundle();
                bundle3.putString("user_id", intent.getStringExtra("user_id")); // Key, Value
                bundle3.putSerializable("item", intent.getSerializableExtra("item")); // Key, Value
                calendarFragment.setArguments(bundle3);
                return calendarFragment;

            default:
                return null;

        }

    }


    @Override

    public int getCount() {


        return mPageCount;
    }

}
