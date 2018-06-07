package com.zhaoweihao.architechturesample.seat;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.data.RestResponse;
import com.zhaoweihao.architechturesample.data.course.QuerySelect;
import com.zhaoweihao.architechturesample.data.seat.Record;

import static com.zhaoweihao.architechturesample.util.HttpUtil.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SeatRecActivity extends AppCompatActivity implements SeatRecContract.View{

    public static final String TAG = "SeatRecActivity";

    private SeatRecContract.Presenter presenter;

    private SeatRecAdapter adapter;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout emptyView;

    private String classCode;
    private int courseId;
    private int stuNum = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_rec);

        new SeatRecPresenter(this, this);

        initViews(null);

        Intent intent = getIntent();
        classCode = intent.getStringExtra("code");
        courseId = intent.getIntExtra("courseId", 0);

        if (courseId == 0) {
            Toast.makeText(this, "unknown error", Toast.LENGTH_SHORT).show();
        }

        request();

        presenter.query(classCode);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            presenter.query(classCode);
            adapter.notifyDataSetChanged();
            stopLoading();
        });


    }

    private void request() {
        String suffix = "course/querySelectByCourseId?courseId=" + courseId;

        sendGetRequest(suffix, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                RestResponse restResponse = new Gson().fromJson(body, RestResponse.class);
                if (restResponse.getCode() == 200) {
                    List<QuerySelect> list = new Gson().fromJson(restResponse.getPayload().toString(), new TypeToken<List<QuerySelect>>() {
                    }.getType());
                    runOnUiThread(() -> stuNum = list.size());
                }
            }
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
            if (stuNum != 0) {
                getSupportActionBar().setTitle("应到" + stuNum + "人," + "已到" + recordArrayList.size() + "人" );
            }



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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
