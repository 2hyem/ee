package com.example.meetingactivity.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.meetingactivity.R;
import com.example.meetingactivity.adapter.BoardAdapter;
import com.example.meetingactivity.model.Board;
import com.example.meetingactivity.model.Mypage;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;



public class InforFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    ListView listNotice2;
    ArrayList<Board> list;
    BoardAdapter adapter;

    TextView txtTitle;
    AsyncHttpClient client;
    HttpResponse response;
    //URL
    String URL=  "http://192.168.0.93:8080/moim.4t.spring/selectBoardFeel.tople";

    //URL
   // String URLCal = "http://192.168.0.93:8080/moim.4t.spring/selectMoimSchedule.tople";


    // 데이터 가져올 객체 선언
    String user_id;
    Mypage item;




    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static Object OnFragmentInteractionListener;

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public InforFragment() {
        // Required empty public constructor
    }





    public static InforFragment newInstance(String param1, String param2) {
        InforFragment fragment = new InforFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    //프래그먼트가 초기화 될 때 호출 됨
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }
    //프래그먼트와 관련되는 뷰 계층을 만들어서 리턴함
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // intent와 bundle로 가져온 데이터
        if(getArguments() != null){
            user_id = getArguments().getString("user_id");
            item = (Mypage) getArguments().getSerializable("item");
        }
        // Inflate the layout for this fragment
        final View view =inflater.inflate(R.layout.fragment_infor, container, false);

        ImageView imageBanner=view.findViewById(R.id.imageBanner);
        Glide.with(getActivity()).load(item.getPic()).error(R.drawable.ic_error_w).placeholder(R.drawable.ic_empty_b).into(imageBanner);

//        txtTitle=view.findViewById(R.id.txtTitle);
//        txtTitle.setText(item.getMoimname());
        TextView textMoimContent =view.findViewById(R.id.textMoimContent);
        textMoimContent.setText(item.getProd());

        listNotice2 = view.findViewById(R.id.listNotice2);

        list = new ArrayList<>();
        adapter= new BoardAdapter(getActivity(),R.layout.list_notice,list);
        client = new AsyncHttpClient();
        response=new HttpResponse(getActivity());
        listNotice2.setAdapter(adapter);
        listNotice2.setOnItemClickListener(this);



        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }




    //onAttach(Activity) 프래그먼트가 액티비티와 연결될 때 호출 됨
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
    //프래그먼트가 액티비티와 연결을 끊기 바로 전에 호출됨 //+ 각각의 상태를 저장?
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    class HttpResponse extends AsyncHttpResponseHandler {
        Activity activity;


        public HttpResponse(Activity activity) {
            this.activity = activity;
        }

        // 통신 성공
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String strJson = new String(responseBody);
            try {
                JSONObject json = new JSONObject(strJson);
                JSONArray feel = json.getJSONArray("feel");
                for (int i = 0; i < feel.length(); i++) {
                    JSONObject temp = feel.getJSONObject(i);
                    Board feel_list = new Board();

                    feel_list.setListnum(temp.getInt("listnum"));

                    if (!temp.getString("filename").equals("")) {
                        feel_list.setFilename(temp.getString("filename"));
                    }
                    if (!temp.getString("thumb").equals("")) {
                        feel_list.setThumb(temp.getString("thumb"));
                    }
                    feel_list.setSubject(temp.getString("subject"));
                    feel_list.setId(temp.getString("id"));
                    feel_list.setMoimcode(temp.getInt("moimcode"));
                    feel_list.setLev(temp.getInt("lev"));
                    feel_list.setEditdate(temp.getString("editdate"));
                    feel_list.setContent(temp.getString("content"));

                    adapter.add(feel_list);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 통신 실패
        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(activity, "통신실패"+statusCode, Toast.LENGTH_SHORT).show();
        }
    }


    //프래그먼트와 연결된 액티비티가 onResume()되어 사용자와 상호작용할 수 있을 때 호출됨
    @Override
    public void onResume() {
        super.onResume();
        getlist();
    }

    public void getlist() {
        adapter.clear();
        RequestParams params = new RequestParams();
        params.put("moimcode", item.getMoimcode());
        client.post(URL,params,response);
       // client.post(URLCal,params,response);
    }
}