package com.zhaoweihao.architechturesample.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qmuiteam.qmui.widget.QMUITabSegment;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.adapter.HomeCourseStudentAdapter;
import com.zhaoweihao.architechturesample.adapter.HomeCourseTeacherAdapter;
import com.zhaoweihao.architechturesample.base.BaseActivity;
import com.zhaoweihao.architechturesample.bean.course.Query;
import com.zhaoweihao.architechturesample.bean.course.QuerySelect;
import com.zhaoweihao.architechturesample.contract.HomeCourseContract;
import com.zhaoweihao.architechturesample.contract.HomeResourceContract;
import com.zhaoweihao.architechturesample.contract.HomeResourceContract;
import com.zhaoweihao.architechturesample.database.User;
import com.zhaoweihao.architechturesample.presenter.HomeCoursePresenter;
import com.zhaoweihao.architechturesample.presenter.HomeResourcePresenter;
import com.zhaoweihao.architechturesample.ui.CourseSimpleInfoLayout;
import com.zhaoweihao.architechturesample.ui.SearchBoardLayout;
import com.zhaoweihao.architechturesample.ui.TitleLayout;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author tanxinkui
 * @description 首页-资源
 * @time 2019/1/22 12:34
 */
public class HomeResourceActivity extends BaseActivity implements HomeResourceContract.View {
    @BindView(R.id.ahr_title)
    TitleLayout ahr_title;
    @BindView(R.id.ahr_recycleView_list)
    RecyclerView ahr_recycleView_list;
    @BindView(R.id.ahr_refresh_layout)
    SwipeRefreshLayout ahr_refresh_layout;
    @BindView(R.id.ahr_simpleInfoLayout)
    CourseSimpleInfoLayout ahr_simpleInfoLayout;

    @BindView(R.id.ahr_empty_view)
    LinearLayout ahr_empty_view;
    private HomeCourseStudentAdapter homeCourseStudentAdapter;
    private HomeCourseTeacherAdapter homeCourseTeacherAdapter;
    private HomeResourceContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_resource);
        ButterKnife.bind(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        ahr_recycleView_list.setLayoutManager(layoutManager);
        presenter = new HomeResourcePresenter(HomeResourceActivity.this, this);
        presenter.start();
        ahr_refresh_layout.setOnRefreshListener(() -> showCourse());
        init();
    }

    private void init() {
        ahr_title.setTitle("课程资源");
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
        FrameLayout frameLayout = ahr_simpleInfoLayout.getLcsi_fl_close();
        frameLayout.setOnClickListener(view1 -> {
            ahr_simpleInfoLayout.setVisibility(View.INVISIBLE);
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
                Intent intent = new Intent(HomeResourceActivity.this, HomeResourceListActivity.class);
                intent.putExtra("courseId", homeCourseStudentAdapter.getCilckedCourseIdArray()[position]);
                intent.putExtra("courseName", homeCourseStudentAdapter.getCourseSelectedName()[position]);
                startActivity(intent);
            }
        });
        homeCourseStudentAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Query course =JSON.parseObject(queryArrayList.get(position).getCourse().toString(), Query.class);
                ahr_simpleInfoLayout.initWithSimpleInfo(course.getCourseName(), course.getTeacherId(),
                        "" + course.getCourseNum(), course.getPassword(), course.getDescription());
                ahr_simpleInfoLayout.setVisibility(View.VISIBLE);
            }
        });
        ahr_recycleView_list.setAdapter(homeCourseStudentAdapter);
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
                Intent intent = new Intent(HomeResourceActivity.this, HomeResourceListActivity.class);
                intent.putExtra("courseId", homeCourseTeacherAdapter.getCilckedCourseIdArray()[position]);
                intent.putExtra("courseName", homeCourseTeacherAdapter.getCourseSelectedName()[position]);
                startActivity(intent);
            }
        });
        homeCourseTeacherAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Query course = queryArrayList.get(position);
                ahr_simpleInfoLayout.initWithSimpleInfo(course.getCourseName(), course.getTeacherId(),
                        "" + course.getCourseNum(), course.getPassword(), course.getDescription());
                ahr_simpleInfoLayout.setVisibility(View.VISIBLE);
               /* new QMUIDialog.MessageDialogBuilder(HomeResourceActivity.this)
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
        ahr_recycleView_list.setAdapter(homeCourseTeacherAdapter);
    }


    @Override
    public void initViews(View view) {

    }

    @Override
    public void setPresenter(HomeResourceContract.Presenter presenter) {

    }

    @Override
    public void showResult(ArrayList<QuerySelect> queryArrayList) {
        HomeResourceActivity.this.runOnUiThread(() -> {
            initStudentAdapter(queryArrayList);
            ahr_empty_view.setVisibility(queryArrayList.isEmpty() ? View.INVISIBLE : View.INVISIBLE);
        });
    }

    @Override
    public void showTeacherResult(ArrayList<Query> queryArrayList) {
        HomeResourceActivity.this.runOnUiThread(() -> {
            initTeacherAdapter(queryArrayList);
            ahr_empty_view.setVisibility(queryArrayList.isEmpty() ? View.INVISIBLE : View.INVISIBLE);
        });
    }

    @Override
    public void startLoading() {
        ahr_refresh_layout.post(() -> ahr_refresh_layout.setRefreshing(true));
    }

    @Override
    public void stopLoading() {
        if (ahr_refresh_layout.isRefreshing()) {
            ahr_refresh_layout.post(() -> ahr_refresh_layout.setRefreshing(false));
        }
    }

    @Override
    public void showLoadError(String error) {
        Snackbar.make(ahr_recycleView_list, error, Snackbar.LENGTH_SHORT)
                .setAction("重试", view -> presenter.querySelect()).show();
    }

    @Override
    public void showSelectSuccess(Boolean status) {

    }

    @Override
    public void onDeleteCourseSuccess() {
        runOnUiThread(() -> Toast.makeText(HomeResourceActivity.this, "成功删除该课程！", Toast.LENGTH_SHORT).show());
    }

}
