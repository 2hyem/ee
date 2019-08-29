package com.example.meetingactivity.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.meetingactivity.R;
import com.example.meetingactivity.adapter.CallAdapter;
import com.example.meetingactivity.model.MoimUser;

import java.util.ArrayList;
import java.util.List;


public class MessageActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    Button button1, button2;
    String tel;
    // 회원 전화번호 담아서 넘겨주기
    CallAdapter adapter;
    List<MoimUser> listUsers;
    ListView listView;
    List listTel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        listUsers = new ArrayList<>();
        listUsers = (List<MoimUser>) getIntent().getSerializableExtra("listUsers");
        listTel = new ArrayList();

        button1 = findViewById(R.id.button);
        button2 = findViewById(R.id.button2);
        adapter = new CallAdapter(this, R.layout.call_item, listUsers);
        listView = findViewById(R.id.listView);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        listView.setOnItemClickListener(this);


    }

    @Override
    public void onClick(View v) {
        Intent intent;
        if (v.getId() == R.id.button3) {
            String result = tel.substring(tel.lastIndexOf(","));
            intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + result));
            startActivity(intent);
        } else if (v.getId() == R.id.button2) {
            // 정보 없애는 작업
            finish();
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MoimUser moimUser = adapter.getItem(position);
        // 모임코드 추가하면 여기에서 작업(리스트에 포함해서)
        tel = moimUser.getTel() + ",";
    }

}
