package com.zhaoweihao.architechturesample.fragment;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.activity.HomeActivityActivity;
import com.zhaoweihao.architechturesample.activity.HomeCourseActivity;
import com.zhaoweihao.architechturesample.activity.HomeCourseDetailAtivity;
import com.zhaoweihao.architechturesample.activity.HomeCourseSearchQueryCourseActivity;
import com.zhaoweihao.architechturesample.activity.HomeMoreActivity;
import com.zhaoweihao.architechturesample.activity.HomeRecommandChapterListActivity;
import com.zhaoweihao.architechturesample.activity.HomeResourceActivity;
import com.zhaoweihao.architechturesample.activity.HomeScanInputActivity;
import com.zhaoweihao.architechturesample.activity.HomeSearchAllResourceActivity;
import com.zhaoweihao.architechturesample.activity.HomeScanCustomCaptureActivity;
import com.zhaoweihao.architechturesample.activity.MessageFriendProfileActivity;
import com.zhaoweihao.architechturesample.adapter.HomeCourseStudentAdapter;
import com.zhaoweihao.architechturesample.adapter.HomeRecommandAdapter;
import com.zhaoweihao.architechturesample.adapter.HomeRescentUseAdapter;
import com.zhaoweihao.architechturesample.bean.RecommandCourse;
import com.zhaoweihao.architechturesample.bean.RestResponse;
import com.zhaoweihao.architechturesample.bean.UserInfoByUsername;
import com.zhaoweihao.architechturesample.bean.course.QuerySelect;
import com.zhaoweihao.architechturesample.database.LocalCourse;
import com.zhaoweihao.architechturesample.database.User;
import com.zhaoweihao.architechturesample.ui.CourseSimpleInfoLayout;
import com.zhaoweihao.architechturesample.util.Constant;
import com.zhaoweihao.architechturesample.util.HttpUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.crud.DataSupport;

import java.io.IOException;
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
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

/**
 * @author
 * @description 首页
 * @time 2019/1/28 14:10
 */
public class HomeFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.ft_llinvitationshow)
    LinearLayout ft_llinvitationshow;
    @BindView(R.id.ft_flinvitationboard)
    FrameLayout ft_flinvitationboard;
    @BindView(R.id.fh_tvscanqr)
    TextView fh_tvscanqr;
    @BindView(R.id.fh_tvinvitecode)
    TextView fh_tvinvitecode;
    @BindView(R.id.fh_qmui_btn_main_search_all)
    com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton fh_qmui_btn_main_search_all;
    @BindView(R.id.fh_ly_more)
    LinearLayout fh_ly_more;
    @BindView(R.id.fh_ly_course)
    LinearLayout fh_ly_course;
    @BindView(R.id.fh_ly_activity)
    LinearLayout fh_ly_activity;
    @BindView(R.id.fh_ly_resource)
    LinearLayout fh_ly_resource;
    @BindView(R.id.fh_recent_use_recycleView)
    RecyclerView fh_recent_use_recycleView;
    @BindView(R.id.fh_recommend_recycleView)
    RecyclerView fh_recommend_recycleView;
    @BindView(R.id.fh_fl_recent_use)
    FrameLayout fh_fl_recent_use;
    @BindView(R.id.fh_course_simple_layout)
    CourseSimpleInfoLayout fh_course_simple_layout;
    private HomeRescentUseAdapter recentUseAdapter;
    private HomeRecommandAdapter recommendAdapter;
    private ArrayList<LocalCourse> CourseListRecentUse = new ArrayList<>();
    private ArrayList<RecommandCourse> CourseListRecommend = new ArrayList<>();
    private QMUITipDialog tipDialog;
    Handler mainHandler = new Handler(Looper.getMainLooper());


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    /*private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";*/

    // TODO: Rename and change types of parameters
   /* private String mParam1;
    private String mParam2;*/

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
      /*  Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      /*  if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        showAndHideboard();
        initViews();
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initCourseRecentUse();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {

        } else {

        }
    }

    public void initViews() {
        tipDialog = new QMUITipDialog.Builder(getContext())
                .setTipWord("正在加载推荐课程...")
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .create();
        fh_tvscanqr.setOnClickListener(this);
        fh_tvinvitecode.setOnClickListener(this);
        fh_qmui_btn_main_search_all.setOnClickListener(this);
        fh_ly_course.setOnClickListener(this);
        fh_ly_activity.setOnClickListener(this);
        fh_ly_resource.setOnClickListener(this);
        fh_ly_more.setOnClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setSmoothScrollbarEnabled(false);
        fh_recent_use_recycleView.setLayoutManager(layoutManager);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext());
        fh_recommend_recycleView.setLayoutManager(layoutManager2);
        initCourseRecommand();
    }

    private void initCourseRecentUse() {
        CourseListRecentUse.clear();
        List<LocalCourse> localCourses = DataSupport.where("User_id=?",
                "" + DataSupport.findLast(User.class).getUserId()).find(LocalCourse.class);
        List<LocalCourse> localCourseTheThree = new ArrayList<>();
        for (LocalCourse lc : localCourses) {
            if (lc.getState() != 0) {
                localCourseTheThree.add(lc);
            }
        }
        if (localCourseTheThree.size() > 0) {
            if (localCourseTheThree.size() == 1) {
                setRecent(localCourseTheThree.get(0));
            } else if (localCourseTheThree.size() == 2) {
                if (localCourseTheThree.get(0).getState() == 1) {
                    setRecent(localCourseTheThree.get(1));
                    setRecent(localCourseTheThree.get(0));
                } else {
                    setRecent(localCourseTheThree.get(0));
                    setRecent(localCourseTheThree.get(1));
                }
            } else {
                for (LocalCourse lc1 : localCourses) {
                    if (lc1.getState() == 3) {
                        setRecent(lc1);
                    }
                }
                for (LocalCourse lc1 : localCourses) {
                    if (lc1.getState() == 2) {
                        setRecent(lc1);
                    }
                }
                for (LocalCourse lc1 : localCourses) {
                    if (lc1.getState() == 1) {
                        setRecent(lc1);
                    }
                }
            }
        }
        initRecentUseAdapter();
    }

    private void setRecent(LocalCourse localCourse) {
        CourseListRecentUse.add(localCourse);
    }

    private void initCourseRecommand() {
        tipDialog.show();
        HttpUtil.sendGetRequest(Constant.GET_RANDOM_COURSE_URL + DataSupport.findLast(User.class).getToken(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        tipDialog.dismiss();
                        Toast.makeText(getContext(), "加载错误，请重试！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                //解析json数据组装RestResponse对象
                RestResponse restResponse = JSON.parseObject(body, RestResponse.class);
                if (restResponse.getCode() == Constant.SUCCESS_CODE) {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            List<RecommandCourse> recommandCourse = JSON.parseArray(restResponse.getPayload().toString(),
                                    RecommandCourse.class);
                            List<LocalCourse> localCourses = DataSupport.where("User_id=?",
                                    "" + DataSupport.findLast(User.class).getUserId()).find(LocalCourse.class);
                            for (int i = 0; i < recommandCourse.size(); i++) {
                                Log.v("tanxinkuitestRand", recommandCourse.get(i).toString());
                                if (!checkIfRecommandHasAlreadySlected(localCourses, recommandCourse.get(i).getCourse_id())) {
                                    CourseListRecommend.add(recommandCourse.get(i));
                                }
                            }
                            tipDialog.dismiss();
                            initRecommendAdapter();
                        }
                    });
                } else {
                    tipDialog.dismiss();
                }

            }
        });
    }

    private Boolean checkIfRecommandHasAlreadySlected(List<LocalCourse> localCourses, int course_id) {
        Boolean check = false;
        if (localCourses.size() > 0) {
            for (LocalCourse lc : localCourses) {
                if (lc.getCourse_id() == course_id) {
                    check = true;
                    break;
                }
            }
        }
        return check;
    }

    private void initRecentUseAdapter() {
        setShowSimpleInfo();
        if (CourseListRecentUse.size() == 0) {
            fh_fl_recent_use.setVisibility(View.GONE);
        } else {
            fh_fl_recent_use.setVisibility(View.VISIBLE);
        }
        recentUseAdapter = new HomeRescentUseAdapter(CourseListRecentUse);
        // recentUseAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        recentUseAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getContext(), HomeCourseDetailAtivity.class);
                intent.putExtra("courseId", CourseListRecentUse.get(position).getCourse_id());
                intent.putExtra("courseName", CourseListRecentUse.get(position).getCourseName());
                startActivity(intent);
            }
        });
        recentUseAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.lhc_iv_collect:
                        fh_course_simple_layout.initWithSimpleInfo(CourseListRecentUse.get(position).getCourseName(),
                                CourseListRecentUse.get(position).getTeacherId(), "" + CourseListRecentUse.get(position).getCourse_Selected_Num(),
                                CourseListRecentUse.get(position).getPassword(), CourseListRecentUse.get(position).getDescription());
                        fh_course_simple_layout.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }

            }
        });

        fh_recent_use_recycleView.setAdapter(recentUseAdapter);

    }

    private void setShowSimpleInfo() {
        FrameLayout frameLayout = fh_course_simple_layout.getLcsi_fl_close();
        frameLayout.setOnClickListener(view -> {
            fh_course_simple_layout.setVisibility(View.INVISIBLE);
        });
    }

    private void initRecommendAdapter() {
        setShowSimpleInfo();

        recommendAdapter = new HomeRecommandAdapter(CourseListRecommend);
        // recommendAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        recommendAdapter.isFirstOnly(false);
        recommendAdapter.setEnableLoadMore(false);
        recommendAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), HomeRecommandChapterListActivity.class);
                intent.putExtra("courseId", CourseListRecommend.get(position).getCourse_id());
                intent.putExtra("courseName", CourseListRecommend.get(position).getCourseName());
                startActivity(intent);
            }
        });
        recommendAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.lhc_iv_collect:
                        fh_course_simple_layout.initWithoutPasswordSimpleInfo(CourseListRecommend.get(position).getCourseName(),
                                CourseListRecommend.get(position).getTeacherId(), "" + CourseListRecommend.get(position).getCourseNum(),
                                CourseListRecommend.get(position).getDescription());
                        fh_course_simple_layout.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
            }
        });

        fh_recommend_recycleView.setAdapter(recommendAdapter);

    }

    //    public void startScanqr(){
//        IntentIntegrator integrator = new IntentIntegrator(HomeFragment.this);
//        integrator.initiateScan();
//    }
    //show and hide scanboard
    public void showAndHideboard() {
        ft_llinvitationshow.setOnClickListener(v -> {
            if (ft_flinvitationboard.getVisibility() == View.INVISIBLE) {
                ft_flinvitationboard.setVisibility(View.VISIBLE);

            } else {
                ft_flinvitationboard.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fh_tvscanqr:
                onScan();
                break;
            case R.id.fh_tvinvitecode:
                startActivity(new Intent(getContext(), HomeScanInputActivity.class));
                break;
            case R.id.fh_qmui_btn_main_search_all:
                startActivity(new Intent(getContext(), HomeCourseSearchQueryCourseActivity.class));
                break;
            case R.id.fh_ly_more:
                Intent intent2 = new Intent(getActivity(), HomeMoreActivity.class);
                startActivity(intent2);
                break;
            case R.id.fh_ly_course:
                Intent intent3 = new Intent(getActivity(), HomeCourseActivity.class);
                startActivity(intent3);
                break;
            case R.id.fh_ly_activity:
                Intent intent5 = new Intent(getActivity(), HomeActivityActivity.class);
                startActivity(intent5);
                break;
            case R.id.fh_ly_resource:
                Intent intent4 = new Intent(getActivity(), HomeResourceActivity.class);
                startActivity(intent4);
                break;
            default:
                break;
        }
    }

    public void onScan() {
        IntentIntegrator.forSupportFragment(this)
                .setCaptureActivity(HomeScanCustomCaptureActivity.class)
                .setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
                // 扫码的类型,可选：一维码，二维码，一/二维码
                // .setPrompt("请对准二维码")
                // 设置提示语
                .setCameraId(0)
                // 选择摄像头,可使用前置或者后置
                .setBeepEnabled(false)
                // 是否开启声音,扫完码之后会"哔"的一声
                .setBarcodeImageEnabled(true)
                // 扫完码之后生成二维码的图片
                .initiateScan();// 初始化扫码
    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
       /* if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {

            } else {
                Log.d("txk", "扫码成功：结果是：" + result.getContents());
                if (TextUtils.isEmpty(result.getContents())) {
                    Toast.makeText(getContext(), "请先输入对方的邀请码！", Toast.LENGTH_SHORT).show();
                } else if (result.getContents().equals(DataSupport.findLast(User.class).getUsername())) {
                    Toast.makeText(getContext(), "不能添加自己为好友！", Toast.LENGTH_SHORT).show();
                } else {
                    getUserInfomation(result.getContents());
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void getUserInfomation(String username) {
        HttpUtil.sendGetRequest(Constant.GET_USERINFO_BY_USERNAME_AND_TOKEN_URL + username + "&token="
                + DataSupport.findLast(User.class).getToken(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "找不到该好友", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                RestResponse restResponse = JSON.parseObject(body, RestResponse.class);
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (restResponse.getCode() == Constant.SUCCESS_CODE) {
                            UserInfoByUsername userInfoByUsername = JSON.parseObject(restResponse.getPayload().toString(), UserInfoByUsername.class);
                            Intent intent = new Intent(getContext(), MessageFriendProfileActivity.class);
                            intent.putExtra("userInfoByUsername", userInfoByUsername);
                            intent.putExtra("mode", "输入添加好友");
                            intent.putExtra("username", username);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getContext(), "找不到该好友", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}




