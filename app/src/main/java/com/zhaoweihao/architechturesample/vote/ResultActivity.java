package com.zhaoweihao.architechturesample.vote;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.data.RestResponse;
import com.zhaoweihao.architechturesample.data.vote.Add;
import com.zhaoweihao.architechturesample.data.vote.Select;

import static com.zhaoweihao.architechturesample.util.HttpUtil.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ResultActivity extends AppCompatActivity {

    public static final String TAG = "ResultActivity";

    private Toolbar toolbar;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private ResultListAdapter adapter;

    private LinearLayout emptyView;

    private List<Select> selects;

    private ArrayList<Add> voteList = new ArrayList<>();

    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        initViews();

        getSupportActionBar().setTitle("投票结果分析");

        Intent intent = getIntent();

        int courseId = intent.getIntExtra("courseId", 0);

        position = intent.getIntExtra("position", 0);

        String suffix = "vote/query?courseId=" + courseId;

        sendGetRequest(suffix, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                try {
                    RestResponse restResponse = new Gson().fromJson(body, RestResponse.class);
                    if (restResponse.getCode() == 200 && restResponse.getPayload() != null) {
                        voteList.clear();
                        voteList.addAll(new Gson().fromJson(restResponse.getPayload().toString(), new TypeToken<List<Add>>() {
                        }.getType()));
                        runOnUiThread(() -> {

                            selects = voteList.get(position).getSelectList();

                            if (adapter == null) {
                                adapter = new ResultListAdapter(ResultActivity.this, selects);
                                recyclerView.setAdapter(adapter);
                            } else {
                                adapter.notifyDataSetChanged();
                            }

                            adapter.setItemClickListener((v, position) -> {
                                // 处理单击事件

                            });
                            adapter.setItemLongClickListener((view, position) -> {
                                // 处理长按事件
                            });

                        });
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });


    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        swipeRefreshLayout = findViewById(R.id.refresh);
        emptyView = findViewById(R.id.empty_view);
    }
}
