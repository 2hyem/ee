package com.example.meetingactivity.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.meetingactivity.R;
import com.example.meetingactivity.adapter.AuthorityAdapter;
import com.example.meetingactivity.model.MoimUser;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MemberActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
    Button button;
    AuthorityAdapter adapter;
    List<MoimUser> listUsers;
    ListView listView;
    MoimUser moimUserManage;
    // 모임코드 리스트에 추가해서 받을지 아니면 인텐트로 넘겨받을지 상의

    // 통신 객체 선언
    AsyncHttpClient client;
    HttpResponse response;
    // URL
    String URL = "http://192.168.0.93:8080/moim.4t.spring/testUpdateMoim.tople";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);
        // id/ 바뀐내용, 모임코드
        button = findViewById(R.id.button);
        listUsers = new ArrayList<>();
        listUsers = (List<MoimUser>) getIntent().getSerializableExtra("list");
        adapter = new AuthorityAdapter(this, R.layout.admin_item, listUsers);
        listView = findViewById(R.id.listView);
        client = new AsyncHttpClient();
        response = new HttpResponse(this);

        // 이벤트 설정
        button.setOnClickListener(this);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.button) {
            finish();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        moimUserManage = adapter.getItem(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("회원관리");
        builder.setMessage("권환설정 및 회원추방");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setCancelable(false);
        // 긍정 버튼 추가 및 이벤트 설정
        builder.setPositiveButton("권한설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                permit();
            }
        });
        // 부정 버튼 추가 및 이벤트 설정
        builder.setNegativeButton("회원추방" ,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                kickOut();
            }
        });
        // 권한주기, 멤버강퇴 다이알로그로 진행
    }

    private void permit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("권환주기");
        builder.setMessage(moimUserManage.getName() + "님에게 권한을 주시겠습니까?");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setCancelable(false);
        // 긍정 버튼 추가 및 이벤트 설정
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                permitSelect();
                Toast.makeText(MemberActivity.this,  "권한 레벨을 선택해 주세요.", Toast.LENGTH_SHORT).show();
            }
        });
        // 부정 버튼 추가 및 이벤트 설정
        builder.setNegativeButton("No" ,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MemberActivity.this, "권한 설정을 취소합니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void permitSelect() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("확인");
        //builder.setMessage("TEST");
        //builder.setIcon(R.mipmap.ic_launcher);
        builder.setIcon(android.R.drawable.star_big_on);
        builder.setCancelable(false);
        // 리스트에 출력할 문자열 배열
        final  String [] items = {"모임장", "관리자", "일반회원"};
        // 리스트 : 긍정버튼의 역활
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0) {
                    moimUserManage.setPermit(1);
                } else if(which ==1) {
                    moimUserManage.setPermit(2);
                } else if(which ==2) {
                    moimUserManage.setPermit(3);
                }
                RequestParams params = new RequestParams();
                params.put("id", moimUserManage.getId());
                params.put("moincode", moimUserManage.getMoimcode());
                params.put("permit", moimUserManage.getPermit());
                client.post(URL, params, response);

                Toast.makeText(MemberActivity.this, moimUserManage.getId() + "님이" + items[which] + "로 설정되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        // 부정의 의미를 갖는 버튼
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MemberActivity.this, "권한 설정을 취소합니다.", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void kickOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("강퇴하기");
        builder.setMessage(moimUserManage.getName() + "님을 추방하시겠습니까?");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setCancelable(false);
        // 긍정 버튼 추가 및 이벤트 설정
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                RequestParams params = new RequestParams();
                params.put("id", moimUserManage.getId());
                params.put("moincode", moimUserManage.getMoimcode());
                client.post(URL, params, response);

                Toast.makeText(MemberActivity.this,  moimUserManage.getName() + "님을 추방했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        // 부정 버튼 추가 및 이벤트 설정
        builder.setNegativeButton("No" ,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MemberActivity.this, "추방을 취소합니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    class HttpResponse extends AsyncHttpResponseHandler {
        Activity activity;
        ProgressDialog dialog;

        public HttpResponse(Activity activity) {
            this.activity = activity;
        }
        @Override
        public void onStart() {
            dialog = new ProgressDialog(activity);
            dialog.setMessage("잠시만 기다려주세요...");
            dialog.setCancelable(false);
            dialog.show();
        }
        @Override
        public void onFinish() {
            dialog.dismiss();
            dialog = null;
        }
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            Toast.makeText(activity, "추방 성공", Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(getApplicationContext(), "통신 실패", Toast.LENGTH_SHORT).show();
        }
    }
}
