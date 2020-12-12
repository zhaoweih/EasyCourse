package com.zhaoweihao.architechturesample.activity;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.adapter.HomeCourseStudentAdapter;
import com.zhaoweihao.architechturesample.adapter.HomeCourseTeacherAdapter;
import com.zhaoweihao.architechturesample.base.BaseActivity;
import com.zhaoweihao.architechturesample.bean.course.Query;
import com.zhaoweihao.architechturesample.bean.course.QuerySelect;
import com.zhaoweihao.architechturesample.contract.MessageInboxContract;
import com.zhaoweihao.architechturesample.contract.MessageInboxContract;
import com.zhaoweihao.architechturesample.database.User;
import com.zhaoweihao.architechturesample.presenter.MessageInboxPresenter;
import com.zhaoweihao.architechturesample.ui.CourseSimpleInfoLayout;
import com.zhaoweihao.architechturesample.ui.TitleLayout;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
*@description 消息-收件箱
*@author 
*@time 2019/3/12 16:49
*/
public class MessageInboxActivity extends BaseActivity implements MessageInboxContract.View {
    @BindView(R.id.ami_recyclerView)
    RecyclerView ami_recyclerView;
    @BindView(R.id.ami_titleLayout)
    TitleLayout ami_titleLayout;
    @BindView(R.id.ami_empty_view)
    LinearLayout ami_empty_view;
    @BindView(R.id.ami_refresh_layout)
    SwipeRefreshLayout ami_refresh_layout;
    @BindView(R.id.ami_simpleInfoLayout)
    CourseSimpleInfoLayout ami_simpleInfoLayout;
  
    private void init(){
        ami_titleLayout.setTitle("收件箱");
    }
    private HomeCourseStudentAdapter homeCourseStudentAdapter;
    private HomeCourseTeacherAdapter homeCourseTeacherAdapter;
    private MessageInboxContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_inbox);
        ButterKnife.bind(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        ami_recyclerView.setLayoutManager(layoutManager);
        presenter = new MessageInboxPresenter(MessageInboxActivity.this, this);
        presenter.start();
        ami_refresh_layout.setOnRefreshListener(() -> showCourse());
        init();
    }

    private Boolean checkIsStu() {
        User user = DataSupport.findLast(User.class);
        Boolean isStudent = true;
        if (user.getTeacherId() != null) {
            isStudent = false;
        }
        return isStudent;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void showCourse() {
        presenter.querySelect();

    }
    private void setShowCourseSimpleInfo() {
        FrameLayout frameLayout = ami_simpleInfoLayout.getLcsi_fl_close();
        frameLayout.setOnClickListener(view1 -> {
            ami_simpleInfoLayout.setVisibility(View.INVISIBLE);
        });
    }

    private void initStudentAdapter(ArrayList<QuerySelect> queryArrayList) {
        setShowCourseSimpleInfo();
        homeCourseStudentAdapter = new HomeCourseStudentAdapter(queryArrayList);
        homeCourseStudentAdapter.openLoadAnimation();
        homeCourseStudentAdapter.isFirstOnly(false);
        homeCourseStudentAdapter.setEnableLoadMore(false);

        homeCourseStudentAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(MessageInboxActivity.this, HomeCourseMoreCourseNotiQueryActivity.class);
                intent.putExtra("courseId",queryArrayList.get(position).getCourseId());
                startActivity(intent);
            }
        });
        homeCourseStudentAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Query course =JSON.parseObject(queryArrayList.get(position).getCourse().toString(), Query.class);
                ami_simpleInfoLayout.initWithSimpleInfo(course.getCourseName(), course.getTeacherId(),
                        "" + course.getCourseNum(), course.getPassword(), course.getDescription());
                ami_simpleInfoLayout.setVisibility(View.VISIBLE);
                /*Intent intent = new Intent(MessageInboxActivity.this, HomeCourseMoreCourseNotiQueryActivity.class);
                intent.putExtra("courseId", queryArrayList.get(position).getCourseId());
                startActivity(intent);*/
            }
        });
        ami_recyclerView.setAdapter(homeCourseStudentAdapter);
    }

    private void initTeacherAdapter(ArrayList<Query> queryArrayList) {
        setShowCourseSimpleInfo();
        homeCourseTeacherAdapter = new HomeCourseTeacherAdapter(queryArrayList);
        homeCourseTeacherAdapter.openLoadAnimation();
        homeCourseTeacherAdapter.isFirstOnly(false);
        homeCourseTeacherAdapter.setEnableLoadMore(false);

        homeCourseTeacherAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(MessageInboxActivity.this, HomeCourseMoreCourseNotiQueryActivity.class);
                intent.putExtra("courseId", queryArrayList.get(position).getId());
                startActivity(intent);
            }
        });
        homeCourseTeacherAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Query course = queryArrayList.get(position);
                ami_simpleInfoLayout.initWithSimpleInfo(course.getCourseName(), course.getTeacherId(),
                        "" + course.getCourseNum(), course.getPassword(), course.getDescription());
                ami_simpleInfoLayout.setVisibility(View.VISIBLE);
               /* new QMUIDialog.MessageDialogBuilder(MessageInboxActivity.this)
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
                        .show();*/

            }
        });
        ami_recyclerView.setAdapter(homeCourseTeacherAdapter);
    }


    @Override
    public void initViews(View view) {

    }

    @Override
    public void setPresenter(MessageInboxContract.Presenter presenter) {

    }

    @Override
    public void showResult(ArrayList<QuerySelect> queryArrayList) {
        MessageInboxActivity.this.runOnUiThread(() -> {
            initStudentAdapter(queryArrayList);
            ami_empty_view.setVisibility(queryArrayList.isEmpty() ? View.INVISIBLE : View.INVISIBLE);
        });
    }

    @Override
    public void showTeacherResult(ArrayList<Query> queryArrayList) {
        MessageInboxActivity.this.runOnUiThread(() -> {
            initTeacherAdapter(queryArrayList);
            ami_empty_view.setVisibility(queryArrayList.isEmpty() ? View.INVISIBLE : View.INVISIBLE);
        });
    }

    @Override
    public void startLoading() {
        ami_refresh_layout.post(() -> ami_refresh_layout.setRefreshing(true));
    }

    @Override
    public void stopLoading() {
        if (ami_refresh_layout.isRefreshing()) {
            ami_refresh_layout.post(() -> ami_refresh_layout.setRefreshing(false));
        }
    }

    @Override
    public void showLoadError(String error) {
        Snackbar.make(ami_recyclerView, error, Snackbar.LENGTH_SHORT)
                .setAction("重试", view -> presenter.querySelect()).show();
    }

    @Override
    public void showSelectSuccess(Boolean status) {

    }

    @Override
    public void onDeleteCourseSuccess() {
        runOnUiThread(() -> Toast.makeText(MessageInboxActivity.this, "成功删除该课程！", Toast.LENGTH_SHORT).show());
    }


}
