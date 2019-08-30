package com.example.meetingactivity.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.meetingactivity.R;
import com.example.meetingactivity.adapter.MypageAdapter;
import com.example.meetingactivity.helper.FileUtils;
import com.example.meetingactivity.helper.PhotoHelper;
import com.example.meetingactivity.helper.RegexHelper;
import com.example.meetingactivity.model.Mypage;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;

public class MypageActivity extends AppCompatActivity   implements View.OnClickListener, AdapterView.OnItemClickListener {
    Button buttonHome, buttonSearch, buttonSet;
    Button buttonMake;

    FloatingActionButton fabWrite, floatButton;
    LinearLayout fabLayoutSubTex, fabLayoutMain, fabLayoutSub;
    LinearLayout linearLayoutMy, linearLayoutMakeMo;
    Animation fab_open, fab_close;
    Boolean isFabOpen = false;

    GridView gridView;
    ArrayList<Mypage> list;
    MypageAdapter adapter;

    //linearLayoutmakeMo 레이아웃
    Button buttonBackToHome, buttonPhoto, buttonSave;
    EditText editTextName, editTextLoca, editTextIntro;

    // 업로드할 사진파일의 경로
    String filePath = null;

    AsyncHttpClient client; // implementation 'com.loopj.android:android-async-http:1.4.9' in build.gradle
    // 응답처리 객체
    HttpResponse response;

    // list, GridView에 URL
    String URL ="http://192.168.0.93:8080/moim.4t.spring/testMoimItems.tople";

    // insert 용 URL
    String URL2 ="http://192.168.0.93:8080/moim.4t.spring/testCreateMoim.tople";

    String user_id;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        Intent intent = getIntent();
        user_id = intent.getStringExtra("user_id");

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);

        fabLayoutSubTex = (LinearLayout) findViewById(R.id.fabLayoutSubTex);
        fabLayoutMain = (LinearLayout) findViewById(R.id.fabLayoutMain);
        fabLayoutSub = (LinearLayout) findViewById(R.id.fabLayoutSub);
        linearLayoutMy = findViewById(R.id.linearLayoutMy) ;
        linearLayoutMakeMo = findViewById(R.id.linearLayoutMakeMo);
        buttonHome = findViewById(R.id.buttonHome);
        buttonSearch= findViewById(R.id.buttonSearch);
        buttonSet= findViewById(R.id.buttonSet);
        buttonMake= findViewById(R.id.buttonSave);
        buttonBackToHome= findViewById(R.id.buttonBackToHome);
        gridView = findViewById(R.id.gridView);

        list = new ArrayList<>();

        fabWrite = (FloatingActionButton) findViewById(R.id.fabWrite);
        floatButton = (FloatingActionButton) findViewById(R.id.floatButton);

        adapter = new MypageAdapter(this, R.layout.grid_item,list);

        client = new AsyncHttpClient();
        response = new HttpResponse(this, adapter);

        // 리스트뷰에 어뎁터 연결
        gridView.setAdapter(adapter);
        //  Caused by: java.lang.NullPointerException: Attempt to invoke virtual method 'void android.widget.ListView.setAdapter(android.widget.ListAdapter)' on a null object reference
        //  at com.example.a3_listadapterexam.MainActivity.onCreate(MainActivity.java:30)

        fabLayoutSub.setVisibility(View.INVISIBLE);
        linearLayoutMy.setVisibility(View.VISIBLE);
        linearLayoutMakeMo.setVisibility(View.GONE);

        buttonBackToHome.setOnClickListener(this);
        fabWrite.setOnClickListener(this);
        floatButton.setOnClickListener(this);
        buttonMake.setOnClickListener(this);
        buttonHome.setOnClickListener(this);
        buttonSearch.setOnClickListener(this);
        buttonSet.setOnClickListener(this);
        gridView.setOnItemClickListener(this);

        //linearLayoutmakeMo 레이아웃
        buttonBackToHome = findViewById(R.id.buttonBackToHome);
        buttonPhoto = findViewById(R.id.buttonPhoto);
        buttonSave = findViewById(R.id.buttonSave);

        editTextName = findViewById(R.id.editTextName);
        editTextLoca = findViewById(R.id.editTextLoca);
        editTextIntro = findViewById(R.id.editTextIntro);

        buttonBackToHome.setOnClickListener(this);
        buttonPhoto.setOnClickListener(this);
        buttonSave.setOnClickListener(this);

        // permission MainPage로 넘김*
        // Manifest.permission.WRITE_EXTERNAL_STORAGE
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.buttonHome:
                Toast.makeText(this, "Home now", Toast.LENGTH_SHORT).show();
                break;
            case R.id.buttonSearch:
                intent = new Intent(this, SearchActivity.class);
                startActivityForResult(intent, 200);
                break;

            case R.id.buttonSet:
                final Intent intent = new Intent(MypageActivity.this, com.example.meetingactivity.Activity.SetActivity.class);
                //intent.putExtra("user_id", user_id);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;
            case R.id.floatButton:
                anim();
                break;
            case R.id.fabWrite:
                anim();
                linearLayoutMy.setVisibility(View.GONE);
                linearLayoutMakeMo.setVisibility(View.VISIBLE);
                break;

            //linearLayoutmakeMo 레이아웃
            case R.id.buttonSave: // 모임 만들면서 홈으로 가기

                String loca = editTextName.getText().toString().trim();
                String moimname  = editTextName.getText().toString().trim();
                String prod  = editTextIntro.getText().toString().trim();

                RegexHelper regexHelper = RegexHelper.getInstance();
                String msg = null;
                // 이름 입력값 검사
                if (msg == null && !regexHelper.isValue(moimname)){
                    msg = "모임이름을 입력하세요";
                } else if (msg == null && !regexHelper.isValue(loca)) {
                    msg = "활동영역을 입력하세요";
                } else if (msg == null && !regexHelper.isValue(prod)) {
                    msg = "간단한 모임소개를 입력하세요";
                } else if (msg == null && !regexHelper.isValue(filePath)){
                    msg = "사진을 선택하세요";
                }
                if (msg != null) {
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(this, "moimname = " + moimname, Toast.LENGTH_SHORT).show();

                RequestParams params = new RequestParams();
                ////////////// you should modify for final ///////////////
                params.put("id" , user_id); // user_id 있는아이디 적용
                /////////////////////////////////////////////////////
                params.put("loca" , loca);
                params.put("moimname" , moimname);
                params.put("prod" , prod);
                params.put("color" , "gray"); // 기본값 gray

                try {
                    params.put("pic", new File(filePath));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                client.post(URL2, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        try {
                            JSONObject temp = new JSONObject(new String(responseBody));
                            JSONObject jsonObject = temp.getJSONObject("item");

                            Mypage mypage = new Mypage();
                            mypage.setLoca(jsonObject.getString("loca"));
                            mypage.setProd(jsonObject.getString("prod"));
                            mypage.setColor(jsonObject.getString("color"));
                            mypage.setMoimname(jsonObject.getString("moimname"));
                            mypage.setCount(jsonObject.getInt("count"));
                            mypage.setPermit(jsonObject.getInt("permit"));
                            mypage.setFav(jsonObject.getString("fav"));
                            mypage.setPic(jsonObject.getString("pic"));
                            mypage.setMoimcode(jsonObject.getInt("moimcode"));

                            Intent intent1 = new Intent();
                            //intent1.setClass(MypageActivity.this, MoimActivity.class);
                            //intent1.putExtra("mypage", mypage);
                            //MypageActivity.this.startActivity(intent1);

                            Intent intent = new Intent(MypageActivity.this, MypageActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    }
                });

                linearLayoutMakeMo.setVisibility(View.GONE);
                linearLayoutMy.setVisibility(View.VISIBLE);
                break;

            case R.id.buttonPhoto:  // 사진등록
                showLisDialog();
                break;

            case R.id.buttonBackToHome: // 그냥 뒤로가기
//                Intent intent2 = new Intent(MypageActivity.this, MypageActivity.class);
//                intent2.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                startActivity(intent2);
                hideKeypad();
                linearLayoutMakeMo.setVisibility(View.GONE);
                linearLayoutMy.setVisibility(View.VISIBLE);
                break;

        }
    }

    private void hideKeypad() {
        View view = getCurrentFocus();
        if(view != null) {
            InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Mypage item = adapter.getItem(position);
        // 화면 이동을 위한 명시적 인텐트
        Intent intent = new Intent(this, MoimActivity.class);
        // 인텐트에 데이터 저장
        intent.putExtra("item", item);
        intent.putExtra("user_id", user_id);
        startActivity(intent);
        // 앞에서 가져올것 Main에서 가져오기* requestMe에서
        // Intent에 넣는 클래스는 반드시 직렬화 되어있어야함 Java Beans 에서 "Serializable" 상속 받아야함
    }


    public void anim() {
        //Log.d("[INFO]" , "test" + isFabOpen);
        if (isFabOpen) {
            fabLayoutMain.setVisibility(View.VISIBLE);
            fabLayoutSubTex.setVisibility(View.GONE);
            floatButton.animate().rotationBy(60);
            fabWrite.startAnimation(fab_close);
            fabWrite.setClickable(false);
            isFabOpen = false;

        } else {
            fabLayoutSub.setVisibility(View.VISIBLE);
            fabLayoutSubTex.setVisibility(View.VISIBLE);
            floatButton.animate().rotationBy(-60);
            fabWrite.startAnimation(fab_open);
            fabWrite.setClickable(true);
            isFabOpen = true;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();         // 갔다가 돌아와도 화면 갱신이 가능함*
        adapter.clear(); // List 데이터 삭제
        // 통신 요청
        RequestParams params = new RequestParams();
        //////////////////////////
        params.put("id", user_id);
        //////////////////////
        client.post(URL, params, response);
    }

    // 통신 결과를 받아서 처리할 이너클래스
    public class HttpResponse extends AsyncHttpResponseHandler {
        // 객체 선언
        Activity activity; // Toast를 표시하는 데 사용하기 위한 Context
        MypageAdapter adapter;  // 데이터를 List에 저장
        //ListView listView;// 로딩중임을 표시하기 위한 포커스 조절을 위해 사용
        // View footerView; // 로딩중임을 표시하기 위해서 사용
        ProgressDialog dialog;  // 기다려주세요 ... 다이얼로그

        // Constructor
        public HttpResponse(Activity activity, MypageAdapter adapter) {
            this.activity = activity;
            this.adapter = adapter;
            //this.listView = listView;
        }
        // 통신 시작시, 자동호출
        @Override
        public void onStart() {
            //super.onStart();
            dialog = new ProgressDialog(activity);
            dialog.setMessage("잠시만 기다려주세요...");
            dialog.setCancelable(false);
            dialog.show();
        }
        // 통신 종료
        @Override
        public void onFinish() {
            dialog.dismiss(); // 이거 안넣으면 "잠시만 기다려주세요.." 계속뜸
            dialog = null;
        }

        // 통신성공
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String strJson = new String(responseBody); // byte[]을 String으로 변환
            try {
                JSONObject json = new JSONObject(strJson); // 문자열을 Json 갤체로 만들기
                JSONArray items = json.getJSONArray("items");

                for (int i = 0; i < items.length() ; i++) {
                    JSONObject jsonObject = items.getJSONObject(i);

                    Mypage mypage = new Mypage();
                    mypage.setLoca(jsonObject.getString("loca"));
                    mypage.setProd(jsonObject.getString("prod"));
                    mypage.setColor(jsonObject.getString("color"));
                    mypage.setMoimname(jsonObject.getString("moimname"));
                    mypage.setCount(jsonObject.getInt("count"));
                    mypage.setPermit(jsonObject.getInt("permit"));
                    mypage.setFav(jsonObject.getString("fav"));
                    mypage.setPic(jsonObject.getString("pic"));
                    mypage.setMoimcode(jsonObject.getInt("moimcode"));

                    adapter.add(mypage);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 서버로부터 200코드가 아닌 값이 오면 자동 호출
        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(MypageActivity.this, "통신실패", Toast.LENGTH_SHORT).show();
        }
    }

    // 목록 선택창을 화면에 표시
    private void showLisDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] items = {"새로 촬영하기", "갤러리에서 가져오기"}; // It should be Array
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // 새로 촬영하기
                        filePath = PhotoHelper.getInstance().getNewPhotoPath();
                        // 카메라 앱 호출출 위한 암묵적 인텐트
                        File file = new File(filePath);
                        Uri uri = null;
                        Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            uri = FileProvider.getUriForFile(getApplicationContext(),
                                    getApplicationContext().getPackageName() + ".fileprovider", file);
                            camera_intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            camera_intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        } else {
                            uri = Uri.fromFile(file);
                        }
                        camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                        startActivityForResult(camera_intent, 100);
                        Log.d("[INFO]", uri.toString());

                        break;
                    case 1: // 갤러리에서 가져오기
                        Intent galleryIntent = null;
                        if (Build.VERSION.SDK_INT >= 19) {
                            galleryIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                            galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
                        } else  {
                            galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        }
                        // 임시 파일만 필터링
                        galleryIntent.setType("image/*");
                        galleryIntent.putExtra(Intent.EXTRA_LOCAL_ONLY,true );
                        startActivityForResult(galleryIntent, 101);
                        break;
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d("[INFO]", "resultCode = " + resultCode);
        Log.d("[INFO]", "data = " + data);
        switch (requestCode){
            case 100: // 카메라 앱
                // 촬영 결과를 갤러리 앱에 등록
                Intent photoIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                        Uri.parse("file://" + filePath));
                sendBroadcast(photoIntent);
                break;
            case 101: // 갤러리 앱
                // requestCode : filePath가 아애 안나옴
                // resultCode :  filePath = /storage/emulated/0/DCIM/Camera/IMG_20190802_011808.jpg
                if (resultCode == RESULT_OK){ // resultCode
                    // 선택한 파일 경로 얻기
                    filePath = FileUtils.getPath(this, data.getData());
                    //Log.d("[INFO]", "filePath = " + filePath);
                }
                break;
        }

    }
}
