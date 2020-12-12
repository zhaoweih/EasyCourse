package com.zhaoweihao.architechturesample.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.adapter.HomeCourseMoreVoteResultAdapter;
import com.zhaoweihao.architechturesample.base.BaseActivity;
import com.zhaoweihao.architechturesample.bean.RestResponse;
import com.zhaoweihao.architechturesample.bean.vote.Add;
import com.zhaoweihao.architechturesample.bean.vote.Select;
import com.zhaoweihao.architechturesample.util.Constant;

import static com.zhaoweihao.architechturesample.util.HttpUtil.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
/**
*@description 首页-课程-详细界面-更多-投票-查看投票结果
*@author
*@time 2019/1/28 13:01
*/
public class HomeCourseMoreVoteResultActivity extends BaseActivity {

    public static final String TAG = "HomeCourseMoreVoteResultActivity";

    private Toolbar toolbar;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private HomeCourseMoreVoteResultAdapter adapter;

    private LinearLayout emptyView;

    private List<Select> selects;

    private ArrayList<Add> voteList = new ArrayList<>();

    private int position;

    private Intent intent3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        initViews();

        getSupportActionBar().setTitle("投票结果分析");

        Intent intent = getIntent();

        int courseId = intent.getIntExtra("courseId", 0);

        position = intent.getIntExtra("position", 0);

        String suffix = Constant.QUERY_VOTE_BY_COURSE_ID_URL + courseId;

        sendGetRequest(suffix, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                try {
                    RestResponse restResponse = new Gson().fromJson(body, RestResponse.class);
                    if (restResponse.getCode() == Constant.SUCCESS_CODE && restResponse.getPayload() != null) {
                        voteList.clear();
                        voteList.addAll(new Gson().fromJson(restResponse.getPayload().toString(), new TypeToken<List<Add>>() {
                        }.getType()));
                        runOnUiThread(() -> {

                            selects = voteList.get(position).getSelectList();

                            if (adapter == null) {
                                adapter = new HomeCourseMoreVoteResultAdapter(HomeCourseMoreVoteResultActivity.this, selects);
                                recyclerView.setAdapter(adapter);
                            } else {
                                adapter.notifyDataSetChanged();
                            }

                            adapter.setItemClickListener((v, position) -> {
                                //Log.d(TAG, "selects size" + selects.size() + "position " + position);
                               // Log.d(TAG, new Gson().toJson(selects.get(position)));
                                // 处理单击事件
                                Select select = selects.get(position);
                                intent3 = new Intent(HomeCourseMoreVoteResultActivity.this, HomeCourseMoreVoteResultChartActivity.class);
                                intent3.putExtra("select", select);
                                startActivity(intent3);
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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
