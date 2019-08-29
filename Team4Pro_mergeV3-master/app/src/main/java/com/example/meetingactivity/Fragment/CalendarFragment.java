package com.example.meetingactivity.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import com.example.meetingactivity.Activity.Calendar_WriteActivity;
import com.example.meetingactivity.R;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CalendarFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends Fragment implements CalendarView.OnDateChangeListener {
    // TODO: Rename parameter arguments, choose names that match
    // 객체 설정
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    CalendarView calendarView;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CalendarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalendarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalendarFragment newInstance(String param1, String param2) {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        System.out.println("!!!!!!33333333333333333333333333333333");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        이유는 모르지만 fragment에서는 View를 통해서 layout을 초기화 해줘야 한다.
        final View view = inflater.inflate(R.layout.fragment_calendar, container, false);
//        객체 초기화
        calendarView = (CalendarView) view.findViewById(R.id.calendarView);
//        달력 이벤트 처리
        calendarView.setOnDateChangeListener(this);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

//    calendar 이벤트 처리
    @Override
    public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
        // 커스텀 다이얼로그에 Date를 보내기 위해서 작성
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
//        월 ~ 일요일까지 각 값을 가져오기 위해서 사용
        int week = calendar.get(Calendar.DAY_OF_WEEK);
//        요일을 뿌려주기 위해서 사용
        String dayOfWeek = "";
        switch (week) {
            case 1:
                dayOfWeek = "일요일";
                break;
            case 2:
                dayOfWeek = "월요일";
                break;
            case 3:
                dayOfWeek = "화요일";
                break;
            case 4:
                dayOfWeek = "수요일";
                break;
            case 5:
                dayOfWeek = "목요일";
                break;
            case 6:
                dayOfWeek = "금요일";
                break;
            case 7:
                dayOfWeek = "토요일";
                break;
        }
        showDetailDialog(year, month, dayOfMonth, dayOfWeek);
    }

    private void showDetailDialog(int year, int month, int dayOfMonth, String dayOfWeek) {
        final View detailDialog = getLayoutInflater().inflate(R.layout.calendar_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setView(detailDialog);

        // 날짜 객체 초기화
        FloatingActionButton Detail_add = detailDialog.findViewById(R.id.Detail_add);
        TextView Detail_day = detailDialog.findViewById(R.id.Detail_day);
        TextView Detail_year = detailDialog.findViewById(R.id.Detail_year);
        TextView Detail_month = detailDialog.findViewById(R.id.Detail_month);
        TextView Detail_week = detailDialog.findViewById(R.id.Detail_week);

        // 날짜 객체 설정
        Detail_day.setText(Integer.toString(dayOfMonth) + "일");
        Detail_year.setText(Integer.toString(year) + "년");
        Detail_month.setText(Integer.toString(month) + "월");
        Detail_week.setText(dayOfWeek);

        Detail_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 화면 전환
                Intent intent = new Intent(getActivity(), Calendar_WriteActivity.class);
                startActivity(intent);
            }
        });
//          화면 전환
//        CalendarWriteFragment writeFragment = new CalendarWriteFragment();
//        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.Detail_add, writeFragment, null).commit();
        builder.show();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
