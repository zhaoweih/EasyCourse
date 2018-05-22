package com.zhaoweihao.architechturesample.course;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.data.course.Query;
import com.zhaoweihao.architechturesample.interfaze.OnRecyclerViewClickListener;
import com.zhaoweihao.architechturesample.interfaze.OnRecyclerViewLongClickListener;

import java.util.ArrayList;

public class QueryActivity extends AppCompatActivity implements QueryContract.View{

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout emptyView;
    private QueryContract.Presenter presenter;
    private QueryAdapter adapter;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);

        new QueryPresenter(this, this);

        initViews(null);

        String courseName = "牛津和爱因斯坦的搏斗";
        String teacherId = "20151120";

        // 以用课程名称查询为例
        String suffix = "course/query";
        url = suffix + "?" + "courseName=" + courseName;

        presenter.query(url);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            presenter.query(url);
            adapter.notifyDataSetChanged();
            stopLoading();
        });
    }


    @Override
    public void showResult(ArrayList<Query> queryArrayList) {
        runOnUiThread(() -> {
            if (adapter == null) {
                adapter = new QueryAdapter(this, queryArrayList);
                recyclerView.setAdapter(adapter);
            }
            else {
                adapter.notifyDataSetChanged();
            }
            adapter.setItemClickListener((v, position) -> {
                // 处理单击行为
            });
            adapter.setItemLongClickListener((view, position) -> {
                // 处理长按行为
            });
        });
    }

    @Override
    public void startLoading() {
        swipeRefreshLayout.post(() -> swipeRefreshLayout.setRefreshing(true));
    }

    @Override
    public void stopLoading() {
        if (swipeRefreshLayout.isRefreshing()){
            swipeRefreshLayout.post(() -> swipeRefreshLayout.setRefreshing(false));
        }
    }

    @Override
    public void showLoadError() {
        Snackbar.make(recyclerView, "加载失败", Snackbar.LENGTH_SHORT)
                .setAction("重试", view -> presenter.query(url)).show();
    }

    @Override
    public void setPresenter(QueryContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }


    @Override
    public void initViews(View view) {
        recyclerView = findViewById(R.id.rv_course_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        swipeRefreshLayout = findViewById(R.id.refresh);
        emptyView = findViewById(R.id.empty_view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.start();
    }
}
