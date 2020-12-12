package com.zhaoweihao.architechturesample.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.adapter.HomeCourseStudentAdapter;
import com.zhaoweihao.architechturesample.adapter.HomeCourseTeacherAdapter;
import com.zhaoweihao.architechturesample.base.BaseActivity;
import com.zhaoweihao.architechturesample.bean.Course;
import com.zhaoweihao.architechturesample.bean.course.Query;
import com.zhaoweihao.architechturesample.bean.course.QuerySelect;
import com.zhaoweihao.architechturesample.contract.HomeCourseContract;
import com.zhaoweihao.architechturesample.database.LocalCourse;
import com.zhaoweihao.architechturesample.database.User;
import com.zhaoweihao.architechturesample.presenter.HomeCoursePresenter;
import com.zhaoweihao.architechturesample.ui.CourseSimpleInfoLayout;
import com.zhaoweihao.architechturesample.ui.SearchBoardLayout;
import com.zhaoweihao.architechturesample.ui.TitleLayout;


import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author
 * @description 首页-课程
 * @time 2019/1/28 12:49
 */
public class HomeCourseActivity extends BaseActivity implements HomeCourseContract.View {
    @BindView(R.id.ahc_title)
    TitleLayout ahc_title;

    @BindView(R.id.ahc_search_board)
    SearchBoardLayout ahc_search_board;

    @BindView(R.id.ahc_recycleView_list)
    RecyclerView ahc_recycleView_list;

    @BindView(R.id.ahc_refresh_layout)
    SwipeRefreshLayout ahc_refresh_layout;

    @BindView(R.id.ahc_empty_view)
    LinearLayout ahc_empty_view;

    @BindView(R.id.ahc_simpleInfoLayout)
    CourseSimpleInfoLayout ahc_simpleInfoLayout;

    private HomeCourseStudentAdapter homeCourseStudentAdapter;
    private HomeCourseTeacherAdapter homeCourseTeacherAdapter;


    //private List<QuerySelect> CourseList = new ArrayList<>();

    private HomeCourseContract.Presenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_course);
        ButterKnife.bind(this);
        ahc_title.setTitle("课程");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        ahc_recycleView_list.setLayoutManager(layoutManager);
        presenter = new HomeCoursePresenter(HomeCourseActivity.this, this);
        presenter.start();
        ahc_refresh_layout.setOnRefreshListener(() -> showCourse());
        goSearchCourse();
        addCourse();
    }

    private void addCourse() {
        if (!checkIsStu()) {
            Button button = ahc_title.getSettingButton();
            button.setText("添加");
            button.setOnClickListener(view -> {
                Intent intent = new Intent(HomeCourseActivity.this, HomeCourseSubmitCourseActivity.class);
                startActivity(intent);
            });
        }
    }

    private Boolean checkIsStu() {
        User user = DataSupport.findLast(User.class);
        Boolean isStudent = true;
        if (user.getTeacherId() != null) {
            isStudent = false;
        }
        return isStudent;
    }

    private void goSearchCourse() {
        ahc_search_board.setOnClickListener(view -> {
            Intent intent = new Intent(HomeCourseActivity.this, HomeCourseSearchQueryCourseActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void showCourse() {
        presenter.querySelect();
       /* if(homeCourseStudentAdapter!=null){
            homeCourseStudentAdapter.notifyDataSetChanged();
            stopLoading();
        }*/
    }

    private void initStudentAdapter(ArrayList<QuerySelect> queryArrayList) {
        setShowCourseSimpleInfo();
        homeCourseStudentAdapter = new HomeCourseStudentAdapter(queryArrayList);
        homeCourseStudentAdapter.openLoadAnimation();
        homeCourseStudentAdapter.isFirstOnly(false);
        homeCourseStudentAdapter.setEnableLoadMore(false);
       /* homeCourseStudentAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override public void onLoadMoreRequested() {
                ahc_recycleView_list.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        *//*if (mCurrentCounter >= TOTAL_COUNTER) {
                            //数据全部加载完毕
                            homeCourseStudentAdapter.loadMoreEnd();
                        } else {
                            if (isErr) {
                                //成功获取更多数据
                                homeCourseStudentAdapter.addData(DataServer.getSampleData(PAGE_SIZE));
                                mCurrentCounter = homeCourseStudentAdapter.getData().size();
                                homeCourseStudentAdapter.loadMoreComplete();
                            } else {
                                //获取更多数据失败
                                isErr = true;
                                Toast.makeText(HomeCourseActivity.this, R.string.network_err, Toast.LENGTH_LONG).show();
                                homeCourseStudentAdapter.loadMoreFail();

                            }
                        }*//*
                    }

                }, 1000);
            }
        }, ahc_recycleView_list);*/
        homeCourseStudentAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                try {
                    setLocalStuOrTecRecentUse(queryArrayList, null, homeCourseStudentAdapter.getCilckedCourseIdArray()[position]);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.v("tanxinkuitest", "d:" + e.toString());
                }
                Intent intent = new Intent(HomeCourseActivity.this, HomeCourseDetailAtivity.class);
                intent.putExtra("courseId", homeCourseStudentAdapter.getCilckedCourseIdArray()[position]);
                intent.putExtra("courseName", homeCourseStudentAdapter.getCourseSelectedName()[position]);
                startActivity(intent);
            }
        });
        homeCourseStudentAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.lhc_iv_collect:
                        Query course = JSON.parseObject(homeCourseStudentAdapter.getQuerySelectNow().get(position).getCourse().toString(), Query.class);
                        ahc_simpleInfoLayout.initWithSimpleInfo(course.getCourseName(), course.getTeacherId(),
                                "" + course.getCourseNum(), course.getPassword(), course.getDescription());
                        ahc_simpleInfoLayout.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
            }
        });
        ahc_recycleView_list.setAdapter(homeCourseStudentAdapter);
    }

    private void setShowCourseSimpleInfo() {
        FrameLayout frameLayout = ahc_simpleInfoLayout.getLcsi_fl_close();
        frameLayout.setOnClickListener(view1 -> {
            ahc_simpleInfoLayout.setVisibility(View.INVISIBLE);
        });
    }

    private void initTeacherAdapter(ArrayList<Query> queryArrayList) {
        setShowCourseSimpleInfo();
        homeCourseTeacherAdapter = new HomeCourseTeacherAdapter(queryArrayList);
        homeCourseTeacherAdapter.openLoadAnimation();
        homeCourseTeacherAdapter.isFirstOnly(false);
        homeCourseTeacherAdapter.setEnableLoadMore(false);
       /* homeCourseStudentAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override public void onLoadMoreRequested() {
                ahc_recycleView_list.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        *//*if (mCurrentCounter >= TOTAL_COUNTER) {
                            //数据全部加载完毕
                            homeCourseStudentAdapter.loadMoreEnd();
                        } else {
                            if (isErr) {
                                //成功获取更多数据
                                homeCourseStudentAdapter.addData(DataServer.getSampleData(PAGE_SIZE));
                                mCurrentCounter = homeCourseStudentAdapter.getData().size();
                                homeCourseStudentAdapter.loadMoreComplete();
                            } else {
                                //获取更多数据失败
                                isErr = true;
                                Toast.makeText(HomeCourseActivity.this, R.string.network_err, Toast.LENGTH_LONG).show();
                                homeCourseStudentAdapter.loadMoreFail();

                            }
                        }*//*
                    }

                }, 1000);
            }
        }, ahc_recycleView_list);*/
        homeCourseTeacherAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                setLocalStuOrTecRecentUse(null, queryArrayList, homeCourseTeacherAdapter.getCilckedCourseIdArray()[position]);
                Intent intent = new Intent(HomeCourseActivity.this, HomeCourseDetailAtivity.class);
                intent.putExtra("courseId", homeCourseTeacherAdapter.getCilckedCourseIdArray()[position]);
                intent.putExtra("courseName", homeCourseTeacherAdapter.getCourseSelectedName()[position]);
                startActivity(intent);
            }
        });
        homeCourseTeacherAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                new QMUIDialog.MessageDialogBuilder(HomeCourseActivity.this)
                        .setTitle("删除课程")
                        .setMessage("确定要删除该课程吗？")
                        .addAction("取消", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                            }
                        })
                        .addAction(0, "确定", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                                presenter.deleteCourse(homeCourseTeacherAdapter.getCilckedCourseIdArray()[position]);
                            }
                        })
                        .show();
                return false;
            }
        });
        homeCourseTeacherAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.lhc_iv_collect:
                        Query course = homeCourseTeacherAdapter.getQueryList().get(position);
                        ahc_simpleInfoLayout.initWithSimpleInfo(course.getCourseName(), course.getTeacherId(),
                                "" + course.getCourseNum(), course.getPassword(), course.getDescription());
                        ahc_simpleInfoLayout.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
            }
        });
        ahc_recycleView_list.setAdapter(homeCourseTeacherAdapter);
    }


    @Override
    public void initViews(View view) {

    }

    @Override
    public void setPresenter(HomeCourseContract.Presenter presenter) {

    }

    @Override
    public void showResult(ArrayList<QuerySelect> queryArrayList) {
        HomeCourseActivity.this.runOnUiThread(() -> {
            initStudentAdapter(queryArrayList);
            ahc_empty_view.setVisibility(queryArrayList.isEmpty() ? View.INVISIBLE : View.INVISIBLE);
        });
    }

    @Override
    public void showTeacherResult(ArrayList<Query> queryArrayList) {
        HomeCourseActivity.this.runOnUiThread(() -> {
            initTeacherAdapter(queryArrayList);
            ahc_empty_view.setVisibility(queryArrayList.isEmpty() ? View.INVISIBLE : View.INVISIBLE);
        });
    }

    @Override
    public void startLoading() {
        ahc_refresh_layout.post(() -> ahc_refresh_layout.setRefreshing(true));
    }

    @Override
    public void stopLoading() {
        if (ahc_refresh_layout.isRefreshing()) {
            ahc_refresh_layout.post(() -> ahc_refresh_layout.setRefreshing(false));
        }
    }

    @Override
    public void showLoadError(String error) {
        /*Snackbar.make(ahc_recycleView_list, error, Snackbar.LENGTH_SHORT)
                .setAction("重试", view -> presenter.querySelect()).show();*/
        runOnUiThread(() -> {
            Log.v("ok",error);
            Toast.makeText(HomeCourseActivity.this, "暂无课程！", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void showSelectSuccess(Boolean status) {

    }

    @Override
    public void onDeleteCourseSuccess() {
        runOnUiThread(() -> Toast.makeText(HomeCourseActivity.this, "成功删除该课程！", Toast.LENGTH_SHORT).show());
    }


    private void setLocalStuOrTecRecentUse(List<QuerySelect> localCourseList, List<Query> localCourseTeacherList, int addedCourseIdToRecent) {
        Log.v("tanxinkuitest", "first");
        //遍历所有存储的课程，包括其中的最近查看的课程（最多三个，最少0个）
        List<LocalCourse> localCourses = DataSupport.where("User_id=?",
                "" + DataSupport.findLast(User.class).getUserId()).find(LocalCourse.class);
        List<LocalCourse> localCourseTheThree = new ArrayList<>();
        //记住这三个(或者1、2个)的课程编号
        for (int i = 0; i < localCourses.size(); i++) {
            if (localCourses.get(i).getState() != 0) {
                localCourseTheThree.add(localCourses.get(i));
            }
        }
        Log.v("tanxinkuitest", "second");
        int num;
        if (localCourseTheThree.size() == 0) {
            num = 1;
        } else {
            num = localCourseTheThree.size();
        }
        int[] courseThreeOrTwoOrOne = new int[num];
        int[] courseThree = new int[3];
        int[] courseSecond = new int[2];
        if (localCourseTheThree.size() == 0) {
            courseThreeOrTwoOrOne[0] = addedCourseIdToRecent;
        }
        Log.v("tanxinkuitest", "third");
        for (int i = 0; i < localCourseTheThree.size(); i++) {
            if (localCourseTheThree.get(i).getState() == 1) {
                courseThreeOrTwoOrOne[0] = localCourseTheThree.get(i).getCourse_id();
            } else if (localCourseTheThree.get(i).getState() == 2) {
                courseThreeOrTwoOrOne[1] = localCourseTheThree.get(i).getCourse_id();
            } else {
                courseThreeOrTwoOrOne[2] = localCourseTheThree.get(i).getCourse_id();
            }
        }
        Log.v("tanxinkuitest", "fourth");
        //清空以前存储的所有课程
        DataSupport.deleteAll(LocalCourse.class, "User_id=?", "" + DataSupport.findLast(User.class).getUserId());
        //检查是否为其中三个（1/2个）之一,e.g. [15,16,8],[15,16],[15],[]
        for (int i = 0; i < courseThreeOrTwoOrOne.length; i++) {
            if (addedCourseIdToRecent == courseThreeOrTwoOrOne[i]) {
                //等于第几个，i=2不用处理
                if (i == 1) {
                    if (courseThreeOrTwoOrOne.length == 3) {
                        int second = courseThreeOrTwoOrOne[2];
                        int third = courseThreeOrTwoOrOne[1];
                        courseThreeOrTwoOrOne[2] = third;
                        courseThreeOrTwoOrOne[1] = second;
                        break;
                    }
                }
                if (i == 0) {
                    if (courseThreeOrTwoOrOne.length == 3) {
                        int one = courseThreeOrTwoOrOne[1];
                        int second = courseThreeOrTwoOrOne[2];
                        int third = courseThreeOrTwoOrOne[0];
                        courseThreeOrTwoOrOne[0] = one;
                        courseThreeOrTwoOrOne[1] = second;
                        courseThreeOrTwoOrOne[2] = third;
                        break;
                    }
                    if (courseThreeOrTwoOrOne.length == 2) {
                        courseThreeOrTwoOrOne[0] = courseThreeOrTwoOrOne[1];
                        courseThreeOrTwoOrOne[1] = addedCourseIdToRecent;
                        break;
                    }
                }
            } else {
                if (i == (courseThreeOrTwoOrOne.length - 1)) {
                    if (courseThreeOrTwoOrOne.length == 2) {
                        courseThree[0] = courseThreeOrTwoOrOne[0];
                        courseThree[1] = courseThreeOrTwoOrOne[1];
                        courseThree[2] = addedCourseIdToRecent;
                        courseThreeOrTwoOrOne = null;
                        break;
                    } else if (courseThreeOrTwoOrOne.length == 3) {
                        courseThree[0] = courseThreeOrTwoOrOne[1];
                        courseThree[1] = courseThreeOrTwoOrOne[2];
                        courseThree[2] = addedCourseIdToRecent;
                        courseThreeOrTwoOrOne = null;
                        break;
                    } else {
                        courseSecond[0] = courseThreeOrTwoOrOne[0];
                        courseSecond[1] = addedCourseIdToRecent;
                        courseThreeOrTwoOrOne = null;
                        courseThree = null;
                        break;
                    }
                }
            }
        }
        Log.v("tanxinkuitest", "fifth");
        if (localCourseList != null) {
            if (courseThreeOrTwoOrOne != null) {
                resetLocal(courseThreeOrTwoOrOne, localCourseList);
            } else if (courseThree != null) {
                resetLocal(courseThree, localCourseList);
            } else {
                resetLocal(courseSecond, localCourseList);
            }
        } else {
            if (courseThreeOrTwoOrOne != null) {
                resetTecLocal(courseThreeOrTwoOrOne, localCourseTeacherList);
            } else if (courseThree != null) {
                resetTecLocal(courseThree, localCourseTeacherList);
            } else {
                resetTecLocal(courseSecond, localCourseTeacherList);
            }
        }

    }

    private void resetLocal(int[] arrange, List<QuerySelect> localCourseList) {
        Log.v("tanxinkuitest", "sixth");
        //重新存储课程
        for (int i = 0; i < localCourseList.size(); i++) {
            LocalCourse localCourseAdd = new LocalCourse();
            localCourseAdd.setState(0);
            for (int j = 0; j < arrange.length; j++) {
                if (localCourseList.get(i).getCourseId() == arrange[j]) {
                    int k = j + 1;
                    localCourseAdd.setState(k);
                }
            }
            Log.v("tanxinkuitest", "seventh");
            Query course = JSON.parseObject(localCourseList.get(i).getCourse().toString(), Query.class);
            localCourseAdd.setClass_image(course.getClass_image());
            localCourseAdd.setCourse_id(course.getId());
            localCourseAdd.setCourse_Selected_Num(course.getCourseNum());
            localCourseAdd.setCourseName(course.getCourseName());
            localCourseAdd.setDescription(course.getDescription());
            localCourseAdd.setPassword(course.getPassword());
            localCourseAdd.setTeacherId(course.getTeacherId());
            localCourseAdd.setTeacherName(course.getTeacherName());
            localCourseAdd.setTecId(course.getTecId());
            localCourseAdd.setUser_id(DataSupport.findLast(User.class).getUserId());
            localCourseAdd.save();
            Log.v("tanxinkuitest", localCourseAdd.toString());
        }
    }

    private void resetTecLocal(int[] arrange, List<Query> localCourseList) {
        Log.v("tanxinkuitest", "sixth");
        //重新存储课程
        for (int i = 0; i < localCourseList.size(); i++) {
            LocalCourse localCourseAdd = new LocalCourse();
            localCourseAdd.setState(0);
            for (int j = 0; j < arrange.length; j++) {
                if (localCourseList.get(i).getId() == arrange[j]) {
                    int k = j + 1;
                    localCourseAdd.setState(k);
                }
            }
            Log.v("tanxinkuitest", "seventh");
            Query course = localCourseList.get(i);
            localCourseAdd.setClass_image(course.getClass_image());
            localCourseAdd.setCourse_id(course.getId());
            localCourseAdd.setCourse_Selected_Num(course.getCourseNum());
            localCourseAdd.setCourseName(course.getCourseName());
            localCourseAdd.setDescription(course.getDescription());
            localCourseAdd.setPassword(course.getPassword());
            localCourseAdd.setTeacherId(course.getTeacherId());
            localCourseAdd.setTeacherName(course.getTeacherName());
            localCourseAdd.setTecId(course.getTecId());
            localCourseAdd.setUser_id(DataSupport.findLast(User.class).getUserId());
            localCourseAdd.save();
            Log.v("tanxinkuitest", localCourseAdd.toString());
        }
    }

}
