package com.example.meetingactivity.Activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.meetingactivity.R;
import com.kakao.auth.AccessTokenCallback;
import com.kakao.auth.ApiResponseCallback;
import com.kakao.auth.Session;
import com.kakao.auth.authorization.accesstoken.AccessToken;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.usermgmt.response.model.UserAccount;
import com.kakao.util.helper.log.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editText1, editText2;
    Button button1, button2;

    ImageView imageView_profile;
    TextView textView_name, textView_region;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        button1=findViewById(R.id.button_confirm);
        button2=findViewById(R.id.button_fire);

        editText1 = findViewById(R.id.editText_name);
        editText2 = findViewById(R.id.editText_region);

        textView_name = findViewById(R.id.textView_nick);
        textView_region = findViewById(R.id.textView_region);
        imageView_profile = findViewById(R.id.imageView_profile);
        requestMe();

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);

        //타이틀바 제거
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_confirm:
                requestUpdateProfile();
                break;

            case R.id.button_fire:
                onClickUnlink();
                break;
        }
    }

    //유저정보 받아오기
    private void requestMe() {
        List<String> keys = new ArrayList<>();

        UserManagement.getInstance().me(keys, new MeV2ResponseCallback() {
            //v1에서는 세션종료에러, 미가입자에러, 그외 에러가 있었으나 기존 v1이 종료되고 v2가 적용됨에 따라 비회원 에러는 사라짐

            //의도치 않은 세션 종료로 인한 에러
            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Toast.makeText(UpdateActivity.this, "세션 종료", Toast.LENGTH_SHORT);
                Log.e("[error]", "세션 종료로 인한 에러 발생");

            }

            //세션 종료로 인한 에러를 제외한 모든 에러
            @Override
            public void onFailure(ErrorResult errorResult) {
                String error_message = "SessionClosed외의 에러 발생" + errorResult;
                Logger.d(error_message);
                Log.d("[error]", error_message);
            }

            @Override
            public void onSuccess(MeV2Response response) {
                Long id = response.getId();
                String user_id = Long.toString(id);
                String name = response.getNickname();
                UserAccount account = response.getKakaoAccount();
                String thumb = response.getThumbnailImagePath();
                String profile = response.getProfileImagePath();
                String region = response.getProperties().get("region");

                Log.d("[profile]", "user_id_3:" + user_id);
                Log.d("[profile]", "name_3:" + name);
                Log.d("[profile]", "thumb_3:" + thumb);
                Log.d("[profile]", "profile_3:" + profile);
                Log.d("[profile]", "region_3:" + region);


                textView_name.setText("이름:" + name);
                if (region!=null) {
                    textView_region.setText("지역 : " + region);
                }else{
                    textView_region.setText("지역 : 미입력");
                }
                if (profile!=null) {
                    Glide.with(UpdateActivity.this).load(profile).into(imageView_profile);
                }
                handleScopeError(account);
            }
        });
    }

    //유저정보 변경에 대한 액세스토큰 발급
    private void handleScopeError(UserAccount account) {
        List<String> neededScopes = new ArrayList<>();
        if (account.needsScopeAccountEmail()) {
            neededScopes.add("account_email");
        }
        if (account.needsScopeGender()) {
            neededScopes.add("gender");
        }
        Session.getCurrentSession().updateScopes(this, neededScopes, new
                AccessTokenCallback() {
                    @Override
                    public void onAccessTokenReceived(AccessToken accessToken) {
                        // 유저에게 성공적으로 동의를 받음. 토큰을 재발급 받게 됨.
                        // Log.d("[token]", "sucess/"+accessToken);
                        String AcessToken = accessToken.getAccessToken();
                        String RefreshToken = accessToken.getRefreshToken();

                        Log.d("[token]", "ACCESS TOKEN_3 : " + AcessToken);
                        Log.d("[token]", "RefreshToken_3 : " + RefreshToken);

                    }

                    @Override
                    public void onAccessTokenFailure(ErrorResult errorResult) {
                        // 동의 얻기 실패
                        Log.d("[token]", "fail/" + errorResult);
                    }
                });
    }


    //회원 탈퇴
    private void onClickUnlink() {
        final String appendMessage = "회원 탈퇴 하시겠습니까?";
        new AlertDialog.Builder(this)
                .setMessage(appendMessage)
                .setPositiveButton(getString(R.string.com_kakao_ok_button),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                UserManagement.getInstance().requestUnlink(new UnLinkResponseCallback() {
                                    @Override
                                    public void onFailure(ErrorResult errorResult) {
                                        Logger.e(errorResult.toString());
                                    }

                                    @Override
                                    public void onSessionClosed(ErrorResult errorResult) {
                                        finish();
                                    }

                                    @Override
                                    public void onNotSignedUp() {

                                    }

                                    @Override
                                    public void onSuccess(Long userId) {
                                        Toast.makeText(UpdateActivity.this, "탈퇴되었습니다.", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                                dialog.dismiss();
                            }
                        })
                .setNegativeButton(getString(R.string.com_kakao_cancel_button),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();

    }

    private void requestUpdateProfile() {
        final Map<String, String> properties = new HashMap<String, String>();
        String edit_nickname = editText1.getText().toString();
        String edit_region = editText2.getText().toString();

        Log.d("[test]", "edit_nick : "+ edit_nickname);
        Log.d("[test]", "edit_region : "+ edit_region);


        properties.put("nickname", edit_nickname);

        properties.put("region", edit_region);



        UserManagement.getInstance().requestUpdateProfile(new ApiResponseCallback<Long>() {
            @Override
            public void onSuccess(Long userId) {
                Toast.makeText(UpdateActivity.this, "수정되었습니다.", Toast.LENGTH_SHORT).show();
                requestMe();
            }

            @Override
            public void onNotSignedUp() {

            }

            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                Log.d("[error]", message);
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {

            }
        }, properties);



    }
}


