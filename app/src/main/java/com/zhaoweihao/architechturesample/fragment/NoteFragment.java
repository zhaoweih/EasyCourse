package com.zhaoweihao.architechturesample.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.activity.NoteActivity;
import com.zhaoweihao.architechturesample.activity.NoteAddNoteActivity;
import com.zhaoweihao.architechturesample.activity.NoteSearchAllResourceActivity;
import com.zhaoweihao.architechturesample.activity.NoteShowDetailActivity;
import com.zhaoweihao.architechturesample.adapter.NoteAdapter;
import com.zhaoweihao.architechturesample.bean.GetNoteComment;
import com.zhaoweihao.architechturesample.bean.NoteUi;
import com.zhaoweihao.architechturesample.bean.RestResponse;
import com.zhaoweihao.architechturesample.bean.ShowNote;
import com.zhaoweihao.architechturesample.bean.UserInfoByUsername;
import com.zhaoweihao.architechturesample.database.User;
import com.zhaoweihao.architechturesample.util.Constant;
import com.zhaoweihao.architechturesample.util.HttpUtil;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link NoteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoteFragment extends Fragment {


    @BindView(R.id.im_toolbar_icon)
    ImageView im_toolbar_icon;

    @BindView(R.id.fn_llaayoutshowboard)
    LinearLayout fn_llaayoutshowboard;

    @BindView(R.id.fn_searchboard)
    FrameLayout fn_searchboard;

    @BindView(R.id.fn_recyclerView)
    RecyclerView fn_recyclerView;

    @BindView(R.id.fn_fl_closeboard)
    FrameLayout fn_fl_closeboard;

    @BindView(R.id.fn_note)
    FrameLayout fn_note;

    @BindView(R.id.im_toolbar_addnote)
    ImageView im_toolbar_addnote;

    @BindView(R.id.fn_refresh_layout)
    SwipeRefreshLayout fn_refresh_layout;

    @BindView(R.id.fn_empty_view)
    LinearLayout fn_empty_view;

    private NoteAdapter noteAdapter;

    private int lastPosition = 0;

    private List<ShowNote> NoteUiList = new ArrayList<>();

    private Boolean isNowNoteFragmentVisible=false;
    public NoteFragment() {
        // Required empty public constructor
    }

    public static NoteFragment newInstance() {
        NoteFragment fragment = new NoteFragment();
        return fragment;
    }

    private QMUITipDialog tipDialog;
    Handler mainHandler = new Handler(Looper.getMainLooper());
    private boolean mShouldScroll;
    private int mToPosition;
    private List<UserInfoByUsername> OriginuserInfoByUsernames = new ArrayList<>();
    private List<List<GetNoteComment>> OriginNoteCommentList = new ArrayList<List<GetNoteComment>>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("tanxinkuidd", "hhhhOncreate");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_note, container, false);
        ButterKnife.bind(this, view);
        im_toolbar_icon.setOnClickListener(v -> {
            if (fn_llaayoutshowboard.getVisibility() == View.VISIBLE) {
                im_toolbar_icon.setRotation(0);
                fn_llaayoutshowboard.setVisibility(View.INVISIBLE);
            } else {
                im_toolbar_icon.setRotation(180);
                fn_llaayoutshowboard.setVisibility(View.VISIBLE);
            }
        });
        fn_fl_closeboard.setOnClickListener(v -> {
            im_toolbar_icon.setRotation(0);
            fn_llaayoutshowboard.setVisibility(View.INVISIBLE);
        });
        fn_searchboard.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), NoteSearchAllResourceActivity.class);
            startActivity(intent);
        });
        tipDialog = new QMUITipDialog.Builder(getContext())
                .setTipWord("正在加载笔记...")
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .create();

        fn_refresh_layout.setOnRefreshListener(() -> {
            tipDialog.show();
            init();
        });
        //init();
        goToNote();
        // fn_recyclerView.addOnScrollListener(mOnScrollListener);
        Log.v("tanxinkuidd", "hhhhOncreateView");
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isNowNoteFragmentVisible){
            init();
        }
        Log.v("tanxinkuidd", "hhhhOnresume"+getUserVisibleHint());
    }

    private void init() {
        tipDialog.show();
        im_toolbar_addnote.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), NoteAddNoteActivity.class);
            startActivity(intent);
        });
        if (DataSupport.findLast(User.class) != null) {
            toLoadNote();
        }
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden){
            //...显示的操作
            isNowNoteFragmentVisible=false;
        } else {
            //...隐藏的操作
            isNowNoteFragmentVisible=true;
            init();
        }
    }

    private void goToNote() {
        fn_note.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), NoteActivity.class);
            startActivity(intent);
        });
    }

    private void toLoadNote() {
        //Constant.GET_NOTE_URL + "?user_id=" + DataSupport.findLast(User.class).getUserId()
        HttpUtil.sendGetRequest(Constant.GET_ALL_SHARE_NOTEBOOK_URL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        tipDialog.dismiss();
                        if (fn_refresh_layout.isRefreshing()) {
                            fn_refresh_layout.post(() -> fn_refresh_layout.setRefreshing(false));
                        }
                        Toast.makeText(getContext(), "加载错误，请重试！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                //解析json数据组装RestResponse对象
                RestResponse restResponse = JSON.parseObject(body, RestResponse.class);
                //RestResponse restResponse = new Gson().fromJson(body, RestResponse.class);
                if (restResponse.getCode() == Constant.SUCCESS_CODE) {
                    NoteUiList.clear();
                    NoteUiList = JSON.parseArray(restResponse.getPayload().toString(), ShowNote.class);
                    /*NoteUiList.addAll(new Gson().fromJson(restResponse.getPayload().toString(), new TypeToken<List<ShowNote>>() {
                    }.getType()));*/
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            //initAdapter();
                            initOriginUserInfoList();
                            //tipDialog.dismiss();
                           /* if (fn_refresh_layout.isRefreshing()) {
                                fn_refresh_layout.post(() -> fn_refresh_layout.setRefreshing(false));
                            }*/
                        }
                    });
                }
                if (restResponse.getCode() != Constant.SUCCESS_CODE) {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            //initAdapter();
                            //initOriginUserInfoList();
                            //tipDialog.dismiss();
                           /* if (fn_refresh_layout.isRefreshing()) {
                                fn_refresh_layout.post(() -> fn_refresh_layout.setRefreshing(false));
                            }
                            Toast.makeText(getContext(), "暂无笔记！", Toast.LENGTH_SHORT).show();*/
                        }
                    });
                }
            }
        });
    }


    private void initData() {

        Drawable[] pic = new Drawable[]{getContext().getResources().getDrawable(R.drawable.school)};

        Drawable[] pic1 = new Drawable[]{getContext().getResources().getDrawable(R.drawable.school), getContext().getResources().getDrawable(R.drawable.school)};

        Drawable[] pic2 = new Drawable[]{getContext().getResources().getDrawable(R.drawable.school), getContext().getResources().getDrawable(R.drawable.school),
                getContext().getResources().getDrawable(R.drawable.school)};

        Drawable[] pic3 = new Drawable[]{getContext().getResources().getDrawable(R.drawable.school), getContext().getResources().getDrawable(R.drawable.school),
                getContext().getResources().getDrawable(R.drawable.school), getContext().getResources().getDrawable(R.drawable.school),};

        Drawable[] pi4 = new Drawable[]{getContext().getResources().getDrawable(R.drawable.school), getContext().getResources().getDrawable(R.drawable.school),
                getContext().getResources().getDrawable(R.drawable.school), getContext().getResources().getDrawable(R.drawable.school),
                getContext().getResources().getDrawable(R.drawable.school)};
        Drawable[] pi5 = new Drawable[]{getContext().getResources().getDrawable(R.drawable.school), getContext().getResources().getDrawable(R.drawable.school),
                getContext().getResources().getDrawable(R.drawable.school), getContext().getResources().getDrawable(R.drawable.school),
                getContext().getResources().getDrawable(R.drawable.school), getContext().getResources().getDrawable(R.drawable.school)};
        Drawable[][] pics = new Drawable[][]{pic, pic1, pic2, pic3, pi4, pi5};

        for (int j = 0; j < 2; j++) {
            for (int i = 0; i < 6; i++) {
                NoteUi ui = setData(getContext().getResources().getDrawable(R.drawable.school),
                        "张三", "张三", "2019-01-02", "小时候过春节",
                        "小时候过春节很热闹", "共享笔记", pics[i], 1, 2);
                //  NoteUiList.add(ui);
            }
        }

    }

    private void initAdapter() {
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext());
        fn_recyclerView.setLayoutManager(layoutManager2);
        layoutManager2.scrollToPositionWithOffset(lastPosition, 0);
        noteAdapter = new NoteAdapter(NoteUiList, OriginNoteCommentList, OriginuserInfoByUsernames);
        if (NoteUiList.size() == 0) {
            fn_empty_view.setVisibility(View.VISIBLE);
        } else {
            fn_empty_view.setVisibility(View.INVISIBLE);
        }
        //noteAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
       /* noteAdapter.setEnableLoadMore(true);
        noteAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override public void onLoadMoreRequested() {
                fn_recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                    }

                }, 500);
            }
        }, fn_recyclerView);
        noteAdapter.setPreLoadNumber(1);*/
        noteAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                lastPosition = position;

                //Toast.makeText(getContext(), "dangqianid:" + NoteUiList.size() + "cundeid:" + lastPosition, Toast.LENGTH_SHORT).show();

                Log.v("gggggg", "noteAdapter.getAvatar()." + noteAdapter.getAvatar().size());
                Log.v("gggggg", " noteAdapter.getName().get(position)." + noteAdapter.getName().size());
                Log.v("gggggg", "noteAdapter.getStuOrTea().get(position)" + noteAdapter.getStuOrTea().size());
                Log.v("gggggg", " noteAdapter.getNoteCommentList()." + noteAdapter.getNoteCommentList().size());
                Log.v("gggggg", "noteAdapter.getAvatar()." + NoteUiList.get(position).toString());
                Intent intent = new Intent(getContext(), NoteShowDetailActivity.class);
                intent.putExtra("note", NoteUiList.get(position));
                intent.putExtra("avatar", OriginuserInfoByUsernames.get(position).getAvatar());
                intent.putExtra("name", OriginuserInfoByUsernames.get(position).getName());
                if (OriginuserInfoByUsernames.get(position).getStudentId() == null) {
                    intent.putExtra("teaOrStu", "教师");
                } else {
                    intent.putExtra("teaOrStu", "学生");
                }
                intent.putExtra("commentList", (Serializable) OriginNoteCommentList.get(position));
              /*  intent.putExtra("note", NoteUiList.get(position));
                intent.putExtra("avatar", noteAdapter.getAvatar().get(position));
                intent.putExtra("name", noteAdapter.getName().get(position));
                intent.putExtra("teaOrStu", noteAdapter.getStuOrTea().get(position));
                intent.putExtra("commentList", (Serializable) noteAdapter.getNoteCommentList().get(position));*/
                startActivity(intent);
            }
        });

        fn_recyclerView.setAdapter(noteAdapter);
    }

    private NoteUi setData(Drawable userDrawale, String title, String author, String date,
                           String contentTitle, String content, String type, Drawable[] pic,
                           int comment, int likes) {
        NoteUi ui = new NoteUi();
        ui.setUserDrawable(userDrawale);
        ui.setTitle(title);
        ui.setAuthor(author);
        ui.setDate(date);
        ui.setContentTitle(contentTitle);
        ui.setContent(content);
        ui.setType(type);
        ui.setPic(pic);
        ui.setComment(comment);
        ui.setLikes(likes);
        return ui;
    }

    private int countGetUIBU = 0;

    private void initOriginUserInfoList() {
        countGetUIBU = 0;
        OriginuserInfoByUsernames.clear();
        if (NoteUiList.size() != 0) {
            getUIBU(NoteUiList.get(0).getUser_id());
        }
        if (NoteUiList.size() == 0) {
            initNoteComment();
        }
    }

    private void getUIBU(int id) {
        countGetUIBU++;
        HttpUtil.sendGetRequest(Constant.GET_USERINFO_BY_ID_AND_TOKEN_URL + id + "&token="
                + DataSupport.findLast(User.class).getToken(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                RestResponse restResponse = JSON.parseObject(body, RestResponse.class);
                if (restResponse.getCode() == Constant.SUCCESS_CODE) {
                    UserInfoByUsername userInfoByUsername = JSON.parseObject(restResponse.getPayload().toString(), UserInfoByUsername.class);
                    OriginuserInfoByUsernames.add(userInfoByUsername);
                    if (countGetUIBU <= (NoteUiList.size() - 1)) {
                        getUIBU(NoteUiList.get(countGetUIBU).getUser_id());
                    } else {
                        initNoteComment();
                    }
                }
            }
        });
    }

    private int countGetNC = 0;

    private void initNoteComment() {
        OriginNoteCommentList.clear();
        countGetNC = 0;
        if (NoteUiList.size() != 0) {
            getNC(NoteUiList.get(0).getId());
        }
        if (NoteUiList.size() == 0) {
            initAdapter();
            tipDialog.dismiss();
            if (fn_refresh_layout.isRefreshing()) {
                fn_refresh_layout.post(() -> fn_refresh_layout.setRefreshing(false));
            }
        }

    }

    private void getNC(int id) {
        countGetNC++;
        HttpUtil.sendGetRequest(Constant.GET_NOTEBOOK_COMMENT_BY_ID_URL + id, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                RestResponse restResponse = JSON.parseObject(body, RestResponse.class);
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (restResponse.getCode() == Constant.SUCCESS_CODE) {
                            List<GetNoteComment> gnc = JSON.parseArray(restResponse.getPayload().toString(), GetNoteComment.class);
                            OriginNoteCommentList.add(gnc);
                            if (countGetNC <= (NoteUiList.size() - 1)) {
                                getNC(NoteUiList.get(countGetNC).getId());
                            } else {
                                initAdapter();
                                tipDialog.dismiss();
                                if (fn_refresh_layout.isRefreshing()) {
                                    fn_refresh_layout.post(() -> fn_refresh_layout.setRefreshing(false));
                                }
                            }
                        } else {

                        }
                    }

                });
            }
        });

    }


}
