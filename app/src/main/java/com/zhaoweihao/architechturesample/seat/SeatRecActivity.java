package com.zhaoweihao.architechturesample.seat;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.course.QueryAdapter;
import com.zhaoweihao.architechturesample.data.course.Query;
import com.zhaoweihao.architechturesample.data.seat.Record;

import java.util.ArrayList;

public class SeatRecActivity extends AppCompatActivity implements SeatRecContract.View{

    private SeatRecContract.Presenter presenter;

    private SeatRecAdapter adapter;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout emptyView;

    private String classCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_rec);

        new SeatRecPresenter(this, this);

        initViews(null);

//        classCode = "20774";

        Intent intent = getIntent();
        classCode = intent.getStringExtra("code");

        getSupportActionBar().setTitle("密令为" + classCode + "的占位纪录");

        presenter.query(classCode);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            presenter.query(classCode);
            adapter.notifyDataSetChanged();
            stopLoading();
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void showResult(ArrayList<Record> recordArrayList) {
        runOnUiThread(() -> {
            if (adapter == null) {
                adapter = new SeatRecAdapter(this, recordArrayList);
                recyclerView.setAdapter(adapter);
            }
            else {
                adapter.notifyDataSetChanged();
            }
            adapter.setItemClickListener((v, position) -> {

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
    public void showLoadError(String error) {
        Snackbar.make(recyclerView, error, Snackbar.LENGTH_SHORT)
                .setAction("重试", view -> presenter.query(classCode)).show();
    }

    @Override
    public void setPresenter(SeatRecContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void initViews(View view) {
        recyclerView = findViewById(R.id.rv_record_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        swipeRefreshLayout = findViewById(R.id.refresh);
        emptyView = findViewById(R.id.empty_view);
        setSupportActionBar(findViewById(R.id.toolbar));

    }
}
