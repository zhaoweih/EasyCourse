package com.zhaoweihao.architechturesample.course;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.data.course.SendNoti;
import com.zhaoweihao.architechturesample.database.User;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;

public class QueryNotiActivity extends AppCompatActivity implements QueryNotiContract.View{
    public static final String TAG = "QuerySelectCourseActivity";
    private RecyclerView rv_query_noti_list;
    private SwipeRefreshLayout query_noti_refresh;
    private LinearLayout query_noti_empty_view;
    private QueryNotiContract.Presenter presenter;
    private QueryNotiAdapter adapter;
    private String url;
    private Boolean checkTecOrStu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_noti);
        new QueryNotiPresenter(this, this);
        initViews(null);
        Intent intent = getIntent();
        checkTecOrStu = presenter.checkTecOrStu();
        String suffix = "noti/queryCourseNotiByCourseId";
        User user3 = DataSupport.findLast(User.class);
        url = suffix+"?"+"courseId="+intent.getIntExtra("courseId",0);
        /*if (!(user3.getStudentId() == null) && user3.getTeacherId() == null) {
            Toast.makeText(QuerySelectCourseActivity.this, "您不是老师！", Toast.LENGTH_SHORT).show();

            presenter.querySelect(url);
        } else if (user3.getStudentId() == null && !(user3.getTeacherId() == null)) {
            presenter.querySelect(url);

        }*/
        query_noti_refresh.setOnRefreshListener(() -> {
            presenter.querySelect(url);
            adapter.notifyDataSetChanged();
            stopLoading();
        });
        presenter.querySelect(url);
    }
    @Override
    public void showResult(ArrayList<SendNoti> queryArrayList) {
        runOnUiThread(() -> {
            if (adapter == null) {
                adapter = new QueryNotiAdapter(this, queryArrayList,false);
                rv_query_noti_list.setAdapter(adapter);
            }
            else {
                adapter.notifyDataSetChanged();
            }
            adapter.setItemClickListener((v, position) -> {
                ArrayList<SendNoti> queries = presenter.getQueryList();
                com.zhaoweihao.architechturesample.data.course.SendNoti query = queries.get(position);
                
            });
            getSupportActionBar().setTitle("共有"+queryArrayList.size()+"条公告");
            adapter.setItemLongClickListener((view, position) -> {
                // 处理长按行为
            });
        });
    }
    @Override
    public void startLoading() {
        query_noti_refresh.post(() -> query_noti_refresh.setRefreshing(true));
    }

    @Override
    public void stopLoading() {
        if (query_noti_refresh.isRefreshing()){
            query_noti_refresh.post(() -> query_noti_refresh.setRefreshing(false));
        }
    }

    @Override
    public void showLoadError(String error) {
        Snackbar.make(rv_query_noti_list, error, Snackbar.LENGTH_SHORT)
                .setAction("重试", view -> presenter.querySelect(url)).show();
    }

    @Override
    public void showSelectSuccess(Boolean status) {
        if (status) {
            Snackbar.make(rv_query_noti_list, "选课成功", Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setPresenter(QueryNotiContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void initViews(View view) {
        rv_query_noti_list= findViewById(R.id.rv_query_noti_list);
        rv_query_noti_list.setLayoutManager(new LinearLayoutManager(this));
        query_noti_refresh = findViewById(R.id.query_noti_refresh);
       query_noti_empty_view= findViewById(R.id.query_noti_empty_view);
        setSupportActionBar(findViewById(R.id.toolbar));
    }
    @Override
    protected void onResume() {
        super.onResume();
        presenter.start();
    }
}
