package com.zhaoweihao.architechturesample.course;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.data.course.QuerySelect;
import com.zhaoweihao.architechturesample.database.User;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Random;

public class QuerySelectCourseActivity extends AppCompatActivity implements QuerySelectCourseContract.View{
    public static final String TAG = "QuerySelectCourse";
    private RecyclerView rv_query_select_course_1_list;
    private SwipeRefreshLayout query_select_course_refresh;
    private LinearLayout query_select_course_empty_view;
    private QuerySelectCourseContract.Presenter presenter;
    private QuerySelectCourseAdapter adapter;
    private String url;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_select_course);
        new QuerySelectCoursePresenter(this, this);
        initViews(null);
        Intent intent = getIntent();
        String suffix = "course/querySelectByCourseId";
        User user3 = DataSupport.findLast(User.class);
        url = suffix + "?" + "courseId=" + "4";

//        url = suffix+"?"+"courseId="+intent.getIntExtra("courseId",0);

        query_select_course_refresh.setOnRefreshListener(() -> {
            presenter.querySelect(url);
            if (adapter != null)
            adapter.notifyDataSetChanged();
            stopLoading();
        });

        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.random:
                    // 执行随机选取操作
                    QuerySelect querySelect = randomSelect(presenter.getQueryList());
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
                    textView.setTextColor(ContextCompat.getColor(QuerySelectCourseActivity.this,R.color.colorAccent));
                    break;
                    default:
            }
            return true;
        });
        presenter.querySelect(url);
    }


    private QuerySelect randomSelect(ArrayList<QuerySelect> querySelects) {
        int max = querySelects.size();
        int min = 0;
        Random random = new Random();
        int s = random.nextInt(max)%(max-min+1) + min;
        return querySelects.get(s);
    }
    @Override
    public void showResult(ArrayList<QuerySelect> queryArrayList) {
        runOnUiThread(() -> {
            if (adapter == null) {
                adapter = new QuerySelectCourseAdapter(this, queryArrayList);
                rv_query_select_course_1_list.setAdapter(adapter);
            }
            else {
                adapter.notifyDataSetChanged();
            }
            adapter.setItemClickListener((v, position) -> {
                ArrayList<QuerySelect> queries = presenter.getQueryList();
                QuerySelect query = queries.get(position);


            });
            getSupportActionBar().setTitle("已有"+queryArrayList.size()+"位同学选择该课程");
            adapter.setItemLongClickListener((view, position) -> {
                // 处理长按行为
            });
        });
    }
    @Override
    public void startLoading() {
        query_select_course_refresh.post(() -> query_select_course_refresh.setRefreshing(true));
    }

    @Override
    public void stopLoading() {
        if (query_select_course_refresh.isRefreshing()){
            query_select_course_refresh.post(() -> query_select_course_refresh.setRefreshing(false));
        }
    }

    @Override
    public void showLoadError(String error) {
        Snackbar.make(rv_query_select_course_1_list, error, Snackbar.LENGTH_SHORT)
                .setAction("重试", view -> presenter.querySelect(url)).show();
    }

    @Override
    public void showConfirmSuccess(Boolean status) {
        if (status) {
            Snackbar.make(rv_query_select_course_1_list, "为该同学添加答题次数成功", Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setPresenter(QuerySelectCourseContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void initViews(View view) {
        rv_query_select_course_1_list= findViewById(R.id.rv_query_select_course_1_list);
        rv_query_select_course_1_list.setLayoutManager(new LinearLayoutManager(this));
        query_select_course_refresh = findViewById(R.id.query_select_course_refresh);
       query_select_course_empty_view= findViewById(R.id.query_select_empty_view);
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
