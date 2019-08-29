package com.example.meetingactivity.Activity;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.meetingactivity.R;
import com.example.meetingactivity.model.MoimUser;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
    Button button1, button2, button3, button4, button5, button6;
    AsyncHttpClient client;
    HttpResponse response;
    MoimUser moimUSetting;
    String filePath;
    // 모임쪽 설정하는 코드 참고해서 색상, 배너, 만들기
    // 모임정보 수정
    String URLMoim = "http://192.168.0.93:8080/moim.4t.spring/testUpdateMoim.tople";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        client = new AsyncHttpClient();
        response = new HttpResponse();

        // 임시
        moimUSetting = (MoimUser) getIntent().getSerializableExtra("item");
        filePath = getIntent().getStringExtra("filePath");

        button1 = findViewById(R.id.button3);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        button5 = findViewById(R.id.button5);
        button6 = findViewById(R.id.button6);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.button3) {     // 모임이름 변경

            moimUSetting.setMoimname("input");
        } else if(v.getId() == R.id.button2) {      // 모임소개 변경

            moimUSetting.setProd("prod");
        } else if(v.getId() == R.id.button3) {      // 모임배너 변경 (임시)
            Intent intent = new Intent(this, BannerActivity.class);
            startActivity(intent);
            moimUSetting.setPic(filePath);
        } else if(v.getId() == R.id.button4) {      // 대표색상 변경 (임시)

            moimUSetting.setColor("color");
        } else if(v.getId() == R.id.button5) {      // 모임탈퇴    모임장이나 관리자가 한명일 경우 권환위임이 필요함

        } else if(v.getId() == R.id.button6) {      // 모임삭제 변경     모임장일 경우만 가능
            // 모임 삭제(다이얼로그로) - 모임코드로 테이블 삭제
            showConfirmDialog();
        } else if(v.getId() == R.id.button6) {      // 돌아가기
            finish();
        }
    }



    private void showConfirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("모임삭제");
        builder.setMessage("모임을 없애시겠습니까?");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setCancelable(false);
        // 긍정 버튼 추가 및 이벤트 설정
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 경고 알럴트
                warning();
            }
        });
        // 부정 버튼 추가 및 이벤트 설정
        builder.setNegativeButton("No" ,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(SettingActivity.this, "모임삭제를 취소했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        // 설정한 정보로 Dialog 생성
        AlertDialog dialog = builder.create();
        // Dialog를 화면에 표시
        dialog.show();
    }

    private void warning() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // 제목 설정
        builder.setTitle("경고");
        // 내용 설정
        builder.setMessage("모임에 관한 정보가 전부 지워집니다. 정말 삭제하시겠습니까?");
        // 아이콘 이미지 설정
        builder.setIcon(R.mipmap.ic_launcher);
        // 하드웨어 BackKey가 눌렸을 때, 창이 닫히지 않도록 설정
        builder.setCancelable(false);
        // 확인 버튼의 추가 및 이벤트 설정
        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(SettingActivity.this, "모임을 삭제했습니다.", Toast.LENGTH_SHORT).show();
                // 모임삭제 재기형한테 전송

            }
        });
        builder.setNegativeButton("No" ,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(SettingActivity.this, "모임삭제를 취소했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        // 설정한 정보로 Dialog 생성
        AlertDialog dialog = builder.create();
        // Dialog를 화면에 표시
        dialog.show();
    }

    class HttpResponse extends AsyncHttpResponseHandler {

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String strJson = new String(responseBody);
            try {
                JSONObject json = new JSONObject(strJson);
                String rt = json.getString("rt");
                if(rt.equals("OK")) {
                    Toast.makeText(SettingActivity.this, "삭제 성공", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(SettingActivity.this, "삭제 실패", Toast.LENGTH_SHORT).show();
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
