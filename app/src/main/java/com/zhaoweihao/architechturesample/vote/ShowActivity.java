package com.zhaoweihao.architechturesample.vote;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.data.RestResponse;
import com.zhaoweihao.architechturesample.data.vote.Add;


import static com.zhaoweihao.architechturesample.util.HttpUtil.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ShowActivity extends AppCompatActivity {

    public static final String TAG = "ShowActivity";

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private LinearLayout emptyView;

    private VoteListAdapter adapter;

    private ArrayList<Add> voteList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        initViews();

        int courseId = 4;
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
                        Log.d(TAG, restResponse.getPayload().toString());
                        voteList.clear();
                         voteList.addAll(new Gson().fromJson(restResponse.getPayload().toString(), new TypeToken<List<Add>>() {
                         }.getType()));
                         runOnUiThread(() -> {
                             if (voteList.isEmpty())
                                 emptyView.setVisibility(View.VISIBLE);
                             if (adapter == null) {
                                 adapter = new VoteListAdapter(ShowActivity.this, voteList);
                                 recyclerView.setAdapter(adapter);
                             } else {
                                 adapter.notifyDataSetChanged();
                             }

                             getSupportActionBar().setTitle("有" + voteList.size() + "条投票信息");


                             adapter.setItemClickListener((v, position) -> {
                                 // 处理单击事件
                                 Log.d(TAG, new Gson().toJson(voteList.get(position)));
                                 Intent intent = new Intent(ShowActivity.this, VoteActivity.class);
                                 intent.putExtra("voteObj", voteList.get(position));
                                 startActivity(intent);
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
        setSupportActionBar(findViewById(R.id.toolbar));
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        swipeRefreshLayout = findViewById(R.id.refresh);
        emptyView = findViewById(R.id.empty_view);
    }
}
