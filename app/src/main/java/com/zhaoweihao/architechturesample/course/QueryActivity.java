package com.zhaoweihao.architechturesample.course;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.data.course.Query;

import java.util.ArrayList;

public class QueryActivity extends AppCompatActivity implements QueryContract.View {

    public static final String TAG = "QueryActivity";

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout emptyView;
    private QueryContract.Presenter presenter;
    private QueryAdapter adapter;
    private String url;

    private EditText input;
    private Button query;

    private Boolean checkTecOrStu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);

        new QueryPresenter(this, this);

        initViews(null);

        checkTecOrStu = presenter.checkTecOrStu();

        String suffix = "course/query";

        String regex = "[0-9]+";

        query.setOnClickListener(v -> {
            String inputText = input.getText().toString();
            // 如果全是数字则认为输入的是教师编号
            if ( inputText.matches(regex) )
                url = suffix + "?" + "teacherId=" + inputText;
            else
                url = suffix + "?" + "courseName=" + inputText;

            presenter.query(url);
        });

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
                adapter = new QueryAdapter(this, queryArrayList, checkTecOrStu);
                recyclerView.setAdapter(adapter);
            }
            else {
                adapter.notifyDataSetChanged();
            }
            adapter.setItemClickListener((v, position) -> {
                // 处理单击行为
                ArrayList<Query> queries = presenter.getQueryList();
                Query query = queries.get(position);
                // 传递过去
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
        input = findViewById(R.id.input);
        query = findViewById(R.id.query);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.start();
    }
}
