package com.example.meetingactivity.Activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.meetingactivity.R;
import com.example.meetingactivity.model.MoimUser;
import com.example.meetingactivity.model.Mypage;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener {
    Button button1, button2, button3, button4;

    List<MoimUser> listUsers;
    MoimUser moimUser;


    // 혜민씨한테 받아올 데이터(id, 모임코드)
    Mypage mypageItem;

    MoimUser moimUSetting;

    String user_id;
    // 로그인 되어있는 사람의 모임 유저정보 받아오기


    AsyncHttpClient clientUser;
    HttpResponseUser responseUser;
    // 전체 모임유저 정보
    AsyncHttpClient clientTotUser;
    HttpResponseTotUser responseTotUser;

    // id로 유저정보 불러오기
    String URLUser = "http://192.168.0.93:8080/moim.4t.spring/testID.tople";
    // 모임코드로 전체 유저 정보 얻어오기(리스트) 모임코드 어찌받아올지
    String URLTotUser = "http://192.168.0.93:8080/moim.4t.spring/testMoimUsets.tople";

    // 서버에 전달해줄 모임코드
   String moimcode;
    // 받아서 수정해줄 모임정보

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);

        //moimUSetting = (MoimUser) getIntent().getSerializableExtra("item");
        Intent intent = getIntent();
        mypageItem=(Mypage) getIntent().getSerializableExtra("item");
//        moimcode=mypageItem.getMoimcode();
//        Toast.makeText(this,moimcode,Toast.LENGTH_SHORT).show();
        user_id = getIntent().getStringExtra("user_id");


        clientUser = new AsyncHttpClient();
        responseUser = new HttpResponseUser();

        clientTotUser = new AsyncHttpClient();
        responseTotUser = new HttpResponseTotUser();

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);

        getHash();
    }

    @Override
    protected void onResume() {
        super.onResume();
        listUsers = new ArrayList<>();
        //값확인
       // Toast.makeText(this,user_id,Toast.LENGTH_SHORT).show();
        Toast.makeText(this,user_id,Toast.LENGTH_SHORT).show();

        // id로 조회한 유저정보
        RequestParams paramsUser = new RequestParams();
        // 아이디 불러오기
        paramsUser.put("user_id", user_id);

        clientUser.post(URLUser, paramsUser, responseUser);
        // 전체 유저정보
        RequestParams paramsUsers = new RequestParams();
//        paramsUsers.put("moincode", moimUSetting.getMoimcode());
        paramsUser.put("moimcode",moimcode);
        clientTotUser.post(URLTotUser, paramsUsers, responseTotUser);
    }

    private void getHash() {
        try{                                                                    //manifest에서 확인 가능한 패키지 네임
            PackageInfo info = getPackageManager().getPackageInfo("com.example.moimproject4t", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures){
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("[hash]", "hash="+ Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Override
    // 버튼 아래 추가할거 생각하기
    public void onClick(View v) {
        Intent intent;
        String tel = null;
        if(v.getId() == R.id.button1) {     // 메세지보내기
            intent = new Intent(this, MessageActivity.class);
            // 번호받아 오기 로직 추가 (메세지 전송 및 전화)
            // intent.putExtra("폰번호");
            intent.putExtra("listUsers",(Serializable)listUsers);
            startActivity(intent);
        } else if(v.getId() == R.id.button2) {      // 회원관리
            // 회원 관리 (adapter- listview 생성)
            intent = new Intent(this, MemberActivity.class);
            // intent.putExtra("회원정보");
            intent.putExtra("listUsers",(Serializable)listUsers);
            startActivity(intent);
        } else if(v.getId() == R.id.button3) {      // 모임설정
            // 모임설정 & 권환관리
            intent = new Intent(this, SettingActivity.class);
            // 모임이름, 모임소개, 모임배너, 대표색상 변경(모임정보 받아서 넘기기)
            intent.putExtra("item",(Serializable) moimUSetting);
            intent.putExtra("moimUser", (Serializable)moimUser);
            startActivity(intent);
        } else if(v.getId() == R.id.button4) {      // 모임해체

        }
    }


    class HttpResponseUser extends AsyncHttpResponseHandler {
        // 승재한테 받아올 유저정보 수정
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String strJson = new String(responseBody);

            try {
                JSONObject json = new JSONObject(strJson);
                JSONArray users = json.getJSONArray("users");
                for(int i=0; i<users.length(); i++) {
                    JSONObject temp = users.getJSONObject(i);
                    MoimUser moimUser = new MoimUser();
                    moimUser.setPermit(temp.getInt("permit"));
                    moimUser.setId(temp.getString("id"));
                    moimUser.setTel(temp.getString("tel"));
                    moimUser.setMoim(temp.getString("moim"));
                    moimUser.setFav(temp.getString("fav"));
                    moimUser.setName(temp.getString("name"));
                    moimUser.setGender(temp.getString("gender"));
                    moimUser.setBirth(temp.getString("birth"));
                    moimUser.setLoca(temp.getString("loca"));
                    moimUser.setProf(temp.getString("prof"));
                    moimUser.setThumb(temp.getString("thumb"));
                    moimUser.setIsShowDial(temp.getString("isShowDial"));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(getApplicationContext(), "통신 실패", Toast.LENGTH_SHORT).show();
        }
    }

    class HttpResponseTotUser extends AsyncHttpResponseHandler {

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String strJson = new String(responseBody);

            try {
                JSONObject json = new JSONObject(strJson);
                JSONArray users = json.getJSONArray("users");
                for(int i=0; i<users.length(); i++) {
                    JSONObject temp = users.getJSONObject(i);
                    MoimUser moimUser = new MoimUser();
                    moimUser.setPermit(temp.getInt("permit"));
                    moimUser.setId(temp.getString("id"));
                    moimUser.setTel(temp.getString("tel"));
                    moimUser.setMoim(temp.getString("moim"));
                    moimUser.setFav(temp.getString("fav"));
                    moimUser.setName(temp.getString("name"));
                    moimUser.setGender(temp.getString("gender"));
                    moimUser.setBirth(temp.getString("birth"));
                    moimUser.setLoca(temp.getString("loca"));
                    moimUser.setProf(temp.getString("prof"));
                    moimUser.setThumb(temp.getString("thumb"));
                    moimUser.setIsShowDial(temp.getString("isShowDial"));

                    // 리스트에 추가
                    listUsers.add(moimUser);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(getApplicationContext(), "통신 실패", Toast.LENGTH_SHORT).show();
        }
    }
}
