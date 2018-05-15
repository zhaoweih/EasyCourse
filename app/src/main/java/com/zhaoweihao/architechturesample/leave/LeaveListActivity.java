package com.zhaoweihao.architechturesample.leave;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.data.Leave;
import com.zhaoweihao.architechturesample.data.RestResponse;
import com.zhaoweihao.architechturesample.interfaze.OnRecyclerViewClickListener;
import com.zhaoweihao.architechturesample.interfaze.OnRecyclerViewLongClickListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.zhaoweihao.architechturesample.util.HttpUtil.*;
import static com.zhaoweihao.architechturesample.util.Utils.*;

/**
 * 展示所有请假条的Activity
 */

public class LeaveListActivity extends AppCompatActivity {

    private static final Class THIS_CLASS = LeaveListActivity.class;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private LeaveListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_list);
        // 初始化控件
        initViews();

        // 以获取当前用户请假条为例
        String suffix = "leave/query";
        // 获取当前用户学号(学生)或教师编号(教师)
        // 假设当前是学生用户，初始化参数名字
        String studentId = "studentId";// 学生应选用这个
        String teacherId = "teacherId";

        String code = "2015191054";
        // 这里判断是老师还是学生 (省略判断语句)
        //if ()
        // 拼接完整url
        String url = suffix + "?" + studentId + "=" + code;

        log(THIS_CLASS, url);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            log(THIS_CLASS, "测试点");
            requestLeaveList(url);
        });

        // 请求数据
        requestLeaveList(url);
    }

    private void initViews() {
        setSupportActionBar(findViewById(R.id.toolbar));
        recyclerView = findViewById(R.id.rv_leave_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        swipeRefreshLayout = findViewById(R.id.refresh);
    }

    /**
     *
     * @param url 拼接好的url
     * 此方法请求请假条数据
     */
    private void requestLeaveList(String url) {
        // 设置loading状态
        swipeRefreshLayout.setRefreshing(true);
        // 发送get请求
        sendGetRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                try {
                    RestResponse restResponse = new Gson().fromJson(body, RestResponse.class);
                    if (restResponse.getCode() == 200) {
                        // 转换json为 List<Leave>
                        ArrayList<Leave> leaveList = new Gson().fromJson(restResponse.getPayload().toString(), new TypeToken<List<Leave>>(){}.getType());




                        // 将List展示到界面上，可用ListView 或者 RecyclerView
                        // 这里以RecyclerView为例


                        runOnUiThread(() -> {
                            if (adapter == null) {
                                adapter = new LeaveListAdapter(LeaveListActivity.this, leaveList);
                                recyclerView.setAdapter(adapter);
                            }
                            else {
                                adapter.notifyDataSetChanged();
                            }
                            // 撤销loading状态
                            swipeRefreshLayout.setRefreshing(false);
                            // 统计数量
                            int wait = 0,fail = 0,success = 0;
                            for (Leave leave : leaveList) {
                                switch (leave.getStatus()) {
                                    case 1:
                                        wait++;
                                        break;
                                    case 2:
                                        fail++;
                                        break;
                                    case 3:
                                        success++;
                                        break;
                                    default:

                                }
                            }
                            getSupportActionBar().setTitle("有" + wait +"条正在处理");

                            adapter.setItemClickListener((v, position) -> {
                                // 处理单击事件
                            });
                            adapter.setItemLongClickListener((view, position) -> {
                                // 处理长按事件
                            });
                        });
                    }



                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
