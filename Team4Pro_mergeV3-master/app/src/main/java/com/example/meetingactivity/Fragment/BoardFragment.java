package com.example.meetingactivity.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meetingactivity.R;
import com.example.meetingactivity.adapter.BoardAdapter;
import com.example.meetingactivity.adapter.ShowAdapter;
import com.example.meetingactivity.helper.FileUtils;
import com.example.meetingactivity.helper.PhotoHelper;
import com.example.meetingactivity.model.Board;
import com.example.meetingactivity.model.MoimUser;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BoardFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BoardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BoardFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    ImageButton noticeButton;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Animation fab_open,fab_close;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab,fab1,fab2;
    LinearLayout fabLayout,fabLayout1,fabLayout2,boardLayout;
    LinearLayout binputLayout,layoutDetail;
    TextView textFab1,textFab2;
    Button bbntBack, bbntWrite;
    EditText bSubject,bContent;
    ListView listNotice,listBoard;
    ArrayList<Board> list1,list2;
    ImageButton imageButton;
    ImageView imageView;
    TextView detailSubject,detailName,detailContent;
    EditText repleText;
    Button detailBack,repleSave;
    ImageView detailImage;




    ShowAdapter adapter2;
    BoardAdapter adapter1;
    //통신용 객체 선언
    AsyncHttpClient client;
    HttpResponse_Boardlist response;
    HttpResponse_BoardSelect response_boardSelect;



    String masterLev="1";
    String lev="";

    // 사진이 없는 게시글작성 URL
    //String URLNoPhoto = "http://192.168.0.93:8080/moim.4t.spring/boardWriteNoFile.tople";
    // 사진이 있는 게시글작성 URL
    String URLWithPhoto = "http://192.168.0.93:8080/moim.4t.spring/boardWrite.tople";
    // listView용 URL
    String URLlist= "http://192.168.0.93:8080/moim.4t.spring/testselectMoimBoard.tople";

    String URL_BoardDeatil = "http://192.168.0.93:8080/moim.4t.spring/testselectBoard.tople";

    // 데이터 가져올 객체 선언
    String user_id;
    Mypage item;
    Board board;
    // 업로드할 사진파일의 경로
    String filePathBig = null;

    public BoardFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static BoardFragment newInstance(String param1, String param2) {
        BoardFragment fragment = new BoardFragment();
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
        fab_open= AnimationUtils.loadAnimation(getActivity(),R.anim.fab_open);
        fab_close=AnimationUtils.loadAnimation(getActivity(),R.anim.fab_close);

        client=new AsyncHttpClient();
        response= new HttpResponse_Boardlist(getActivity());



    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // intent와 bundle로 가져온 데이터
        if(getArguments() != null){
            user_id = getArguments().getString("user_id");
            item = (Mypage) getArguments().getSerializable("item");
        }
        //System.out.println("getArguments()" + getArguments());


        // Inflate the layout for this fragment
        final View view =inflater.inflate(R.layout.fragment_board, container, false);
        //플로팅 액션버튼
        fab=(FloatingActionButton) view.findViewById(R.id.fab);
        fab1=(FloatingActionButton)view.findViewById(R.id.fab1);
        fab2=(FloatingActionButton)view.findViewById(R.id.fab2);

        textFab1=view.findViewById(R.id.textFab1);
        textFab2=view.findViewById(R.id.textFab2);

        fabLayout=view.findViewById(R.id.fabLayout);
        fabLayout1=view.findViewById(R.id.fabLayout1);
        fabLayout2=view.findViewById(R.id.fabLayout2);
        //게시판 레이아웃
        boardLayout=view.findViewById(R.id.boardLayout);
        //공지사항 레이아웃
        //일반게시글 레이아웃
        binputLayout=view.findViewById(R.id.binputLayout);

        //글상세화면
        layoutDetail=view.findViewById(R.id.layoutDetail);

       detailSubject=view.findViewById(R.id.detailSubject);

       detailName=view.findViewById(R.id.detailName);

       detailContent=view.findViewById(R.id.detailContent);

       repleText=view.findViewById(R.id.repleText);
       detailBack =view.findViewById(R.id.detailBack);
       repleSave=view.findViewById(R.id.repleSave);
       detailImage=view.findViewById(R.id.detailImage);

        //공지사항 입력 후 리스트에 추가
        listNotice=view.findViewById(R.id.listNotice);
        listBoard=view.findViewById(R.id.listBoard);
        list1 = new ArrayList<>();
        list2=new ArrayList<>();

        adapter1=new BoardAdapter(getActivity(),R.layout.list_notice,list1);
        adapter2=new ShowAdapter(getActivity(),R.layout.list_board,list2);
        listNotice.setAdapter(adapter1);
        listBoard.setAdapter(adapter2);
        listBoard.setOnItemClickListener(this);

        imageView=view.findViewById(R.id.imageView3);

        //게시글
        bbntBack=view.findViewById(R.id.bbntBack);
        bbntWrite=view.findViewById(R.id.bbntWrite);

        bSubject=view.findViewById(R.id.bSubject);
        bContent=view.findViewById(R.id.bContent);

        imageButton=view.findViewById(R.id.imageButton);

        imageButton.setOnClickListener(this);

        bbntBack.setOnClickListener(this);
        bbntWrite.setOnClickListener(this);

        detailBack.setOnClickListener(this);
        repleSave.setOnClickListener(this);

        fab.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("!!!!!!222222222222222222222222222222");
        getlist();
    }

    public void getlist() {
        RequestParams params = new RequestParams();
        params.put("moimcode", item.getMoimcode());
        client.post(URLlist,params,response);
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


    //이벤트설정
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            // 이미지 업로드 버튼
            case R.id.imageButton: // 작은이미지 업로드
                showLisDialogThumb();
                break;
            //플로팅 액션 버튼(+) 눌렀을때
            case R.id.fab:
                anim();
                break;
            case R.id.fab1://일반게시글 입력화면으로
                masterLev="2";
                anim();
                boardLayout.setVisibility(View.GONE);
                fabLayout.setVisibility(View.GONE);
                binputLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.fab2: //공지사항 입력화면으로  //+모임장 또는 관리자 권한을 가진사람만 보여주게해야함
                masterLev="1";
                anim();
                boardLayout.setVisibility(View.GONE);
                fabLayout.setVisibility(View.GONE);
                binputLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.bbntBack: //돌아가기 버튼
                bSubject.setText("");
                bContent.setText(""); //입력내용초기화
                binputLayout.setVisibility(View.GONE);
                boardLayout.setVisibility(View.VISIBLE);
                fabLayout.setVisibility(View.VISIBLE);
                break;
            case  R.id.bbntWrite: //공지사항입력
                String subject=bSubject.getText().toString().trim();
                String content=bContent.getText().toString().trim();

                //입력값이 있으면, 서버로 데이터 전송 및 요청
                RequestParams params = new RequestParams();

                if(masterLev.equals("1")){
                    lev = "1"; //공지사항 -1 일반게시글-2
                }else if(masterLev.equals("2")){
                    lev="2";
                }

                params.put("id",user_id);
                params.put("subject",subject);
                params.put("content",content);
                params.put("moimcode",item.getMoimcode());
                params.put("lev",lev);


                if (filePathBig == null) {
                    params.setForceMultipartEntityContentType(true);
                } else{
                    try {
                        params.put("filename",new File(filePathBig));
                        //  params.put("thumb",new File(filePathThumb));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }

                client.post(URLWithPhoto, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String strJson = new String(responseBody);
                        try {
                            JSONObject json = new JSONObject(strJson);
                            String rt  = json.getString("result");
                            if(rt.equals("OK")){
                                Toast.makeText(getActivity(),"저장성공",Toast.LENGTH_SHORT).show();
                                getlist();
                            }else {
                                Toast.makeText(getActivity(),"실패",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(getActivity(), "통신실패"+statusCode, Toast.LENGTH_SHORT).show();
                    }
                });
                filePathBig=null;
                bSubject.setText("");
                bContent.setText("");
                binputLayout.setVisibility(View.GONE);
                boardLayout.setVisibility(View.VISIBLE);
                fabLayout.setVisibility(View.VISIBLE);
                break;
        }
    }


    //플로팅액션버튼 동작 함수
    public void anim() {
        if (isFabOpen) {
            fab.animate().rotationBy(60);
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fabLayout1.setVisibility(View.GONE);
            fabLayout2.setVisibility(View.GONE);
            fab1.setClickable(false);
            fab2.setClickable(false);
            isFabOpen = false;
        } else {
            fab.animate().rotationBy(-60);

            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fabLayout1.setVisibility(View.VISIBLE);
            fabLayout2.setVisibility(View.VISIBLE);
            fab1.setClickable(true);
            fab2.setClickable(true);
            isFabOpen = true;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        boardLayout.setVisibility(View.GONE);
        fabLayout.setVisibility(View.GONE);
        layoutDetail.setVisibility(View.VISIBLE);
        RequestParams params = new RequestParams();
        params.put("listnum",board.getListnum());
        client.post(URL_BoardDeatil,params,response_boardSelect);
        detailContent.setText(board.getContent());
        detailSubject.setText(board.getSubject());
        detailName.setText(board.getName());
        //detailImage.setImageResource(board.getFilename());

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    //특정게시글 조회 통신
     class HttpResponse_BoardSelect extends AsyncHttpResponseHandler {
        Activity activity;
        ProgressDialog dialog;

        public HttpResponse_BoardSelect(Activity activity) {
            this.activity = activity;
        }
        //통신 성공
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

            String strJson = new String(responseBody);
            try {
                JSONObject json = new JSONObject(strJson);

                JSONArray item = json.getJSONArray("item");
                for (int i=0; i<item.length(); i++) {
                    JSONObject temp = item.getJSONObject(i);
                    Board board_select = new Board();

                    board_select.setListnum(temp.getInt("listnum"));
                    if (!temp.getString("filename").equals("")) {
                        board_select.setFilename(temp.getString("filename"));
                    }
                    if (!temp.getString("thumb").equals("")) {
                        board_select.setThumb(temp.getString("thumb"));
                    }
                    board_select.setName(temp.getString("name"));
                    board_select.setSubject(temp.getString("subject"));
                    board_select.setId(temp.getString("id"));
                    board_select.setMoimcode(temp.getInt("moimcode"));
                    board_select.setLev(temp.getInt("lev"));
                    board_select.setEditdate(temp.getString("editdate"));
                    board_select.setContent(temp.getString("content"));

                    adapter2.add(board_select);
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //통신 실패
        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(activity, "통신실패"+statusCode, Toast.LENGTH_SHORT).show();
        }
    }

    //게시글 목록 조회 통신
    class HttpResponse_Boardlist extends AsyncHttpResponseHandler {
        Activity activity;

        public HttpResponse_Boardlist(Activity activity) {
            this.activity = activity;
        }

        //통신 성공
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

            String strJson = new String(responseBody);
            System.out.println("!!!!!!!!!!" + strJson);
            try {
                JSONObject json = new JSONObject(strJson);
                JSONArray normal = json.getJSONArray("normal");
                JSONArray feel = json.getJSONArray("feel");
                for (int i=0; i<normal.length(); i++) {
                    JSONObject temp = normal.getJSONObject(i);
                    Board normal_list = new Board();

                    normal_list.setListnum(temp.getInt("listnum"));
                    if (!temp.getString("filename").equals("")) {
                        normal_list.setFilename(temp.getString("filename"));
                    }
                    if (!temp.getString("thumb").equals("")) {
                        normal_list.setThumb(temp.getString("thumb"));
                    }
                    normal_list.setName(temp.getString("name"));
                    normal_list.setSubject(temp.getString("subject"));
                    normal_list.setId(temp.getString("id"));
                    normal_list.setMoimcode(temp.getInt("moimcode"));
                    normal_list.setLev(temp.getInt("lev"));
                    normal_list.setEditdate(temp.getString("editdate"));
                    normal_list.setContent(temp.getString("content"));

                    adapter2.add(normal_list);
                    }
                for (int i=0; i<feel.length(); i++) {
                    JSONObject temp = feel.getJSONObject(i);
                    Board feel_list = new Board();

                    feel_list.setListnum(temp.getInt("listnum"));

                    if (!temp.getString("filename").equals("")) {
                        feel_list.setFilename(temp.getString("filename"));
                    }
                    if (!temp.getString("thumb").equals("")) {
                        feel_list.setThumb(temp.getString("thumb"));
                    }
                    feel_list.setName(temp.getString("name"));
                    feel_list.setSubject(temp.getString("subject"));
                    feel_list.setId(temp.getString("id"));
                    feel_list.setMoimcode(temp.getInt("moimcode"));
                    feel_list.setLev(temp.getInt("lev"));
                    feel_list.setEditdate(temp.getString("editdate"));
                    feel_list.setContent(temp.getString("content"));

                    adapter1.add(feel_list);
                }
                }
             catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //통신 실패
        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(activity, "통신실패"+statusCode, Toast.LENGTH_SHORT).show();
        }
    }



    //사진
    private void showLisDialogThumb() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String[] items = {"새로 촬영하기", "갤러리에서 가져오기"}; // It should be Array
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // 새로 촬영하기
                        filePathBig = PhotoHelper.getInstance().getNewPhotoPath();
                        // 카메라 앱 호출출 위한 암묵적 인텐트
                        File file = new File(filePathBig);
                        Uri uri = null;
                        Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            uri = FileProvider.getUriForFile(getActivity(),
                                    getActivity().getPackageName() + ".fileprovider", file);
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



    private void showLisDialogBig() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String[] items = {"새로 촬영하기", "갤러리에서 가져오기"}; // It should be Array
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // 새로 촬영하기
                        filePathBig = PhotoHelper.getInstance().getNewPhotoPath();
                        // 카메라 앱 호출출 위한 암묵적 인텐트
                        File file = new File(filePathBig);
                        Uri uri = null;
                        Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            uri = FileProvider.getUriForFile(getActivity(),
                                    getActivity().getPackageName() + ".fileprovider", file);
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d("[INFO]", "resultCode = " + resultCode);
        Log.d("[INFO]", "data = " + data);
        switch (requestCode){
            case 100: // 카메라 앱
                // 촬영 결과를 갤러리 앱에 등록
                Intent photoIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                        Uri.parse("file://" + filePathBig));
                if(getActivity() != null) {
                    getActivity().sendBroadcast(photoIntent);
                }
                break;
            case 101: // 갤러리 앱
                // requestCode : filePath가 아애 안나옴
                // resultCode :  filePath = /storage/emulated/0/DCIM/Camera/IMG_20190802_011808.jpg
                if (resultCode == getActivity().RESULT_OK){ // resultCode
                    // 선택한 파일 경로 얻기
                    filePathBig = FileUtils.getPath(getActivity(), data.getData());
                    //Log.d("[INFO]", "filePath = " + filePath);
                }
                break;
        }

    }

}