package com.example.meetingactivity.Activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TimePicker;

import com.example.meetingactivity.R;
import com.example.meetingactivity.helper.DateTimeHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class Calendar_WriteActivity extends AppCompatActivity  implements View.OnClickListener, OnMapReadyCallback {
    // 객체 선언
    EditText detail_write_name, detail_write_person, detail_write_help;
    Button detail_write_date, detail_write_time, detail_write_bt1, detail_write_bt2;
    int YEAR =0, MONTH =0, DAY = 0;
    int HOUR = 0, MINIUTE=0;

    private GoogleMap map;
    SupportMapFragment mapFragment;
    SearchView searchView;
    boolean permissionCK = false;    // 퍼미션 결과 저장
    Double lat, lon;    // 위도 경도를 알아내기 위해서 사용
    String location;    // 검색한 장소
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar__write);

        detail_write_name = findViewById(R.id.detail_write_name);
        detail_write_person = findViewById(R.id.detail_write_person);
        detail_write_date = findViewById(R.id.detail_write_date);
        detail_write_time = findViewById(R.id.detail_write_time);
        detail_write_help = findViewById(R.id.detail_write_help);
        detail_write_bt1 = findViewById(R.id.detail_write_bt1);
        detail_write_bt2 = findViewById(R.id.detail_write_bt2);

        // 지도
        searchView = findViewById(R.id.searchView);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        // Dialog에 출력하기 위해 현재 시스템 날짜를 구하여 전역변수에 미리 셋팅
        int[] date = DateTimeHelper.getInstance().getDate();
        YEAR = date[0];
        MONTH = date[1];
        DAY = date[2];
        // Dialog에 출력하기 위해 현재 시스템 시간를 구하여 전역변수에 미리 셋팅
        int[] time = DateTimeHelper.getInstance().getTime();
        HOUR = time[0];
        MINIUTE = time[1];

        MapActivity();
        mapFragment.getMapAsync(this);

        // 이벤트 설정
        detail_write_date.setOnClickListener(this);
        detail_write_time.setOnClickListener(this);
        detail_write_bt1.setOnClickListener(this);
        detail_write_bt2.setOnClickListener(this);

        // 퍼미션 체크
        permissionCheck();
    }

    //    맵 실행 코드
    private void MapActivity() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // 검색창에서 입력한 값을 location에 저장
                location = searchView.getQuery().toString();
                List<Address> addressesList = null;

                if(location != null || !location.equals("")){
                    //
                    Geocoder geocoder = new Geocoder(Calendar_WriteActivity.this);
                    try{
                        // 검색으로 위도 경도 값 얻오는 방법
                        addressesList = geocoder.getFromLocationName(location, 1);
                        lat = addressesList.get(0).getLatitude();
                        lon = addressesList.get(0).getLongitude();
                    }catch (IOException e){
                        // 주소 변환 실패시 실행
                        e.printStackTrace();
                    }
                    Address address = addressesList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    map.addMarker(new MarkerOptions().position(latLng).title(location));
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                } else {
                    return false;
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void permissionCheck() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            }
        } else {
            permissionCK = true;
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
//            날짜 Dialog
            case R.id.detail_write_date:
                showDatePickerDialog();
                break;
//          시간 Dialog
            case R.id.detail_write_time:
                showTimePickerDialog();
                break;
//                저장 버튼
            case R.id.detail_write_bt1:
                Intent intent = new Intent(this, Calendar_ReadActivity.class);
                Log.d("[test]", "write -> lat : " + lat + " / " + "lon : " + lon);
                intent.putExtra("lat", lat);
                intent.putExtra("lon", lon);
                intent.putExtra("location", location);
                startActivity(intent);
                break;
//                취소 버튼
            case R.id.detail_write_bt2:
                finish();
                break;
        }
    }
    //    시간 dialog
    private void showTimePickerDialog() {
        // 원본 데이터 백업
        final int temp_hh = HOUR;
        final int temp_nn = MINIUTE;
        // TimePickerDialog 객체 생성
        // TimePickerDialog(Context, 이벤트 핸들러, 시, 분, 24시간제 사용여부)
        // 24시간제 사용여부 : treu=24시간제, false=12시간제
        TimePickerDialog dialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                // 사용자가 선택한 값을 전역변수에 저장
                HOUR = hourOfDay;
                MINIUTE = minute;
                detail_write_time.setText(HOUR +"시" + MINIUTE +"분");
            }
        }, HOUR, MINIUTE, false);

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                HOUR = temp_hh;
                MINIUTE = temp_nn;
            }
        });
        dialog.setTitle("시간 선택");
        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setMessage("약속시간을 선택하세요.");
        dialog.setCancelable(false);
        dialog.show();
    }
    //    날짜 dialog
    private void showDatePickerDialog() {
        // 원본 데이터 백업
        final int temp_yy = YEAR;
        final int temp_mm = MONTH;
        final int temp_dd = DAY;

        // DatePickerDialog 객체 생성
        // DatePickerDialog(Context, 이벤트 핸들러, 년, 월, 일)
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // 사용자가 선택한 값을 전역변수에 저장
                YEAR = year;
                MONTH = month;
                DAY = dayOfMonth;
                detail_write_date.setText(YEAR + "년 " + MONTH + "월 " + DAY + "일");
            }
        }, YEAR, MONTH, DAY);
        // 사용자가 Back 키나 "취소"를 눌렸을 대, 동작하는 이벤트 정의
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // 백업해 두었던 값을 원복시킴 : 버전에 따라 좀 다르기 때문에, 이번 버전에선느 필요 없음
                YEAR = temp_yy;
                MONTH = temp_mm;
                DAY = temp_dd;
            }
        });
        Log.d("[test]", temp_yy + " / " + temp_mm + " / " + temp_dd);
        dialog.setTitle("날짜 선택");
        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setMessage("생일을 선택하세요.");
        dialog.show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
    }
}
