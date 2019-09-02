package com.example.meetingactivity.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.meetingactivity.Fragment.BoardFragment;
import com.example.meetingactivity.Fragment.CalendarFragment;
import com.example.meetingactivity.Fragment.InforFragment;
import com.example.meetingactivity.Fragment.PhotoFragment;
import com.example.meetingactivity.model.Mypage;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;

import java.util.ArrayList;
import java.util.List;

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
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {
        requstMe();

        Bundle bundle1 = new Bundle();
        bundle1.putString("user_id", intent.getStringExtra("user_id")); // Key, Value
        bundle1.putSerializable("item", intent.getSerializableExtra("item")); // Key, Value

        switch (position) {
            case 0:
                InforFragment inforFragment = new InforFragment();
                inforFragment.setArguments(bundle1);
                return inforFragment;

            case 1:
                BoardFragment boardFragment = new BoardFragment();
                //Fragment fragment = new BoardFragment(); // Fragment 생성
                boardFragment.setArguments(bundle1);
                return boardFragment;


            case 2:
                PhotoFragment photoFragment = new PhotoFragment();
                return photoFragment;

            case 3:
                CalendarFragment calendarFragment = new CalendarFragment();
                //Fragment fragment = new BoardFragment(); // Fragment 생성

                calendarFragment.setArguments(bundle1);
                return calendarFragment;

            default:
                return null;

        }

    }

    private void requstMe() {
        //카카오서버에 기존 요청 정보외에 추가 정보(커스텀 파라미터등)을 요청하기 위한 키값
        final List<String> keys = new ArrayList<>();

        UserManagement.getInstance().me(keys, new MeV2ResponseCallback() {
            //v1에서는 세션종료에러, 비회원에러, 그외 에러가 있었으나 기존 v1이 종료되고 v2가 적용됨에 따라 비회원 에러는 사라짐

            //의도치 않은 세션 종료로 인한 에러
            @Override
            public void onSessionClosed(ErrorResult errorResult) {
            }

            //세션 종료로 인한 에러를 제외한 모든 에러
            @Override
            public void onFailure(ErrorResult errorResult) {
            }

            @Override
            public void onSuccess(MeV2Response response) {
            }
        });
    }


    @Override

    public int getCount() {


        return mPageCount;
    }

}
