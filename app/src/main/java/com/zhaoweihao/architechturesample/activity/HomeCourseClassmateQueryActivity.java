package com.zhaoweihao.architechturesample.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.base.BaseActivity;
import com.zhaoweihao.architechturesample.bean.course.QueryClassmate;
import com.zhaoweihao.architechturesample.bean.course.QuerySelect;
import com.zhaoweihao.architechturesample.adapter.HomeCourseClassmateQueryAdapter;
import com.zhaoweihao.architechturesample.contract.HomeCourseClassmateQueryContract;
import com.zhaoweihao.architechturesample.presenter.HomeCourseClassmateQueryPresenter;
import com.zhaoweihao.architechturesample.database.User;
import com.zhaoweihao.architechturesample.ui.TitleLayout;
import com.zhaoweihao.architechturesample.util.Constant;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author
 * @description 首页-课程-班级（学生查看同学，老师查看选课学生或创建点名）
 * @time 2019/1/28 12:50
 */
public class HomeCourseClassmateQueryActivity extends BaseActivity implements HomeCourseClassmateQueryContract.View {
    public static final String TAG = "QuerySelectCourse";
    private RecyclerView rv_query_select_course_1_list;
    private SwipeRefreshLayout query_select_course_refresh;
    private LinearLayout query_select_course_empty_view;
    private HomeCourseClassmateQueryContract.Presenter presenter;
    private HomeCourseClassmateQueryAdapter adapter;
    private String url;
    private Toolbar toolbar;
    @BindView(R.id.aqsc_titleLayout)
    TitleLayout aqsc_titleLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_select_course);
        new HomeCourseClassmateQueryPresenter(this, this);
        initViews(null);
        ButterKnife.bind(this);
        aqsc_titleLayout.setTitle("班级成员");
        Intent intent = getIntent();
        String suffix = Constant.QUERY_SELECT_COURSE_STUDENT_BY_COURSE_ID_URL;
        url = suffix + "?" + "courseId=" + intent.getIntExtra("courseId", 0);

        query_select_course_refresh.setOnRefreshListener(() -> {
            presenter.querySelect(url);
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
            stopLoading();
        });
        if (DataSupport.findLast(User.class).getStudentId() == null) {
            toolbar.setVisibility(View.VISIBLE);
        } else {
            toolbar.setVisibility(View.INVISIBLE);
        }
        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.random:
                    // 执行随机选取操作
                    QueryClassmate querySelect = randomSelect(presenter.getQueryList());
                    AlertDialog dialog = new AlertDialog.Builder(this)
                            .setTitle("抽到的学生学号为：")
                            .setMessage(querySelect.getStudentId())
                            .setPositiveButton("确认添加纪录", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    presenter.confirmRecord(querySelect);
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                    TextView textView = dialog.findViewById(android.R.id.message);
                    textView.setTextSize(30);
                    textView.setTextColor(ContextCompat.getColor(HomeCourseClassmateQueryActivity.this, R.color.colorAccent));
                    break;
                default:
            }
            return true;
        });
        presenter.querySelect(url);
    }


    private QueryClassmate randomSelect(ArrayList<QueryClassmate> querySelects) {
        int max = querySelects.size();
        int min = 0;
        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;
        return querySelects.get(s);
    }

    @Override
    public void showResult(ArrayList<QueryClassmate> queryArrayList) {
        runOnUiThread(() -> {
            try {
                if (adapter == null) {
                    adapter = new HomeCourseClassmateQueryAdapter(this, queryArrayList);
                    rv_query_select_course_1_list.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();
                }
                adapter.setItemClickListener((v, position) -> {
                    ArrayList<QueryClassmate> queries = presenter.getQueryList();
                    QueryClassmate query = queries.get(position);

                });
                aqsc_titleLayout.setTitle("班级成员(" + queryArrayList.size() + "位)");
                getSupportActionBar().setTitle("已有" + queryArrayList.size() + "位同学选择该课程");
                adapter.setItemLongClickListener((view, position) -> {
                    // 处理长按行为
                });
            } catch (Exception e) {
                e.printStackTrace();
                Log.v("the trouble", e.toString());
            }
        });
    }

    @Override
    public void startLoading() {
        query_select_course_refresh.post(() -> query_select_course_refresh.setRefreshing(true));
    }

    @Override
    public void stopLoading() {
        if (query_select_course_refresh.isRefreshing()) {
            query_select_course_refresh.post(() -> query_select_course_refresh.setRefreshing(false));
        }
    }

    @Override
    public void showLoadError(String error) {
       /* Snackbar.make(rv_query_select_course_1_list, error, Snackbar.LENGTH_SHORT)
                .setAction("重试", view -> presenter.querySelect(url)).show();*/
        runOnUiThread(() -> {
            Log.v("thefault", error);
        });
    }

    @Override
    public void showConfirmSuccess(Boolean status) {
        if (status) {
            Snackbar.make(rv_query_select_course_1_list, "为该同学添加答题次数成功", Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setPresenter(HomeCourseClassmateQueryContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void initViews(View view) {
        rv_query_select_course_1_list = findViewById(R.id.rv_query_select_course_1_list);
        rv_query_select_course_1_list.setLayoutManager(new LinearLayoutManager(this));
        query_select_course_refresh = findViewById(R.id.query_select_course_refresh);
        query_select_course_empty_view = findViewById(R.id.query_select_empty_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.student_random, menu);
        return true;
    }
}
