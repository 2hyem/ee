package com.example.meetingactivity.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.meetingactivity.R;
import com.example.meetingactivity.helper.FileUtils;
import com.example.meetingactivity.helper.PhotoHelper;

import java.io.File;

public class BannerActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView imageView;
    Button button1, button2;
    String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);

        button1 = findViewById(R.id.button3);
        button2 = findViewById(R.id.button2);
        imageView = findViewById(R.id.imageView);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.button3) {
            showListDialog();
        } else if(v.getId() == R.id.button2) {
            Intent intent = new Intent(this,SettingActivity.class);
            intent.putExtra("filePath", filePath);
            startActivity(intent);
        }

    }

    private void showListDialog() {
        Intent gIntent = new Intent();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] items = {"새로 촬영하기", "갤러리에서 가져오기"};
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // 새로 촬영하기
                        filePath = PhotoHelper.getInstance().getNewPhotoPath();
                        // 카메라 앱 호출을 위한 암묵적 인텐트
                        File file = new File(filePath);
                        Uri uri = null;

                        Intent camera_Intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            uri = FileProvider.getUriForFile(getApplicationContext(),
                                    getApplicationContext().getPackageName() + ".fileprovider", file);
                            camera_Intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            camera_Intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        } else {
                            uri = Uri.fromFile(file);
                        }
                        Log.d("[INFO]", uri.toString());
                        camera_Intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                        startActivityForResult(camera_Intent, 100);
                        break;
                    case 1: // 갤러리에서 가져오기
                        Intent gIntent = null;
                        if(Build.VERSION.SDK_INT >= 19) {
                            gIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                            gIntent.addCategory(Intent.CATEGORY_OPENABLE);
                        } else {
                            gIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        }
                        // 이미지 파일만 필터링
                        gIntent.setType("image/*");
                        gIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                        startActivityForResult(gIntent, 101);
                        break;
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case 100:   // 카메라 앱
                // 촬영 결과를 갤러리에  등록
                Intent photoIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                        Uri.parse("file://" + filePath));
                sendBroadcast(photoIntent);
                break;
            case 101:   // 갤러리 앱
                if(resultCode == RESULT_OK) {
                    // 선택한 파일 경로 얻기
                    filePath = FileUtils.getPath(this, data.getData());
                    Log.d("[INFO]", "filePath = " + filePath);
                }
                break;
        }
    }
}
