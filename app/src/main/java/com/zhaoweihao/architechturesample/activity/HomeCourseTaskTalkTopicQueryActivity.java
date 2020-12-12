package com.zhaoweihao.architechturesample.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.base.BaseActivity;
import com.zhaoweihao.architechturesample.bean.course.DeleteTopic;
import com.zhaoweihao.architechturesample.bean.course.QueryTopic;
import com.zhaoweihao.architechturesample.adapter.HomeCourseTaskTalkTopicQueryAdapter;
import com.zhaoweihao.architechturesample.contract.HomeCourseTaskTalkTopicQueryContract;
import com.zhaoweihao.architechturesample.presenter.HomeCourseTaskTalkTopicQueryPresenter;
import com.zhaoweihao.architechturesample.database.User;
import com.zhaoweihao.architechturesample.util.Constant;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
/**
*@description 首页-课程-详细界面-任务-讨论列表
*@author
*@time 2019/1/28 13:04
*/
public class HomeCourseTaskTalkTopicQueryActivity extends BaseActivity implements HomeCourseTaskTalkTopicQueryContract.View {
    public static final String TAG = "QueryTopic";
    private RecyclerView rv_query_topic_1_list;
    private SwipeRefreshLayout query_topic_refresh;
    private LinearLayout query_topic_empty_view;
    private HomeCourseTaskTalkTopicQueryContract.Presenter presenter;
    private HomeCourseTaskTalkTopicQueryAdapter adapter;
    private String url;
    private Boolean checkTecOrStu;
    private Toolbar toolbar;
    private int courseId;
    private FloatingActionButton ftbn_query_topic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_topic);
        new HomeCourseTaskTalkTopicQueryPresenter(this, this);

        Intent intent = getIntent();
        checkTecOrStu = presenter.checkTecOrStu();
        courseId = intent.getIntExtra("courseId", 0);
        initViews(null);
        String suffix = Constant.QUERY_DISCUSS_BY_COURSE_ID_URL + intent.getIntExtra("courseId", 0);
        User user3 = DataSupport.findLast(User.class);
        url = suffix;
//        url = suffix + "?" + "courseId=" + "4";
//        url = suffix+"?"+"courseId="+intent.getIntExtra("courseId",0);
        query_topic_refresh.setOnRefreshListener(() -> {

            Log.d(TAG, String.valueOf(adapter == null));
            presenter.queryTopic(url);
            if(adapter != null)
            adapter.notifyDataSetChanged();
            stopLoading();
        });

        presenter.queryTopic(url);
    }

    @Override
    public void showResult(ArrayList<QueryTopic> queryArrayList) {
        runOnUiThread(() -> {
            if (queryArrayList.size() == 0) {
                getSupportActionBar().setTitle("暂无讨论帖");
            } else {
                getSupportActionBar().setTitle("已有" + queryArrayList.size() + "个讨论帖");
            }
            if (adapter == null) {
                adapter = new HomeCourseTaskTalkTopicQueryAdapter(this, queryArrayList, false);
                rv_query_topic_1_list.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }
            adapter.setItemClickListener((v, position) -> {
               /* queries1 = presenter.getQueryList();
                QueryTopic query = queries1.get(position);*/
                Intent intent = new Intent(HomeCourseTaskTalkTopicQueryActivity.this, HomeCourseTaskTalkTopicCommentActivity.class);
                intent.putExtra("commentContent", queryArrayList.get(position).getContent());
                intent.putExtra("commentEndDate", queryArrayList.get(position).getEndDate());
                intent.putExtra("commentStartDate", queryArrayList.get(position).getStartDate());
                intent.putExtra("teacherId", queryArrayList.get(position).getTeacherId());
                intent.putExtra("courseId", courseId);
                intent.putExtra("discussId", queryArrayList.get(position).getId());
                startActivity(intent);

            });

            adapter.setItemLongClickListener((view, position) -> {
                User user3 = DataSupport.findLast(User.class);
                if (user3.getTeacherId() != null) {
                    ArrayList<QueryTopic> queries = presenter.getQueryList();
                    QueryTopic query = queries.get(position);
                    // 处理长按行为
                    AlertDialog alert = new AlertDialog.Builder(this)
                            .setIcon(R.drawable.warming)
                            .setTitle("温馨提示")
                            .setMessage("确定要删除讨论吗？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {//设置确定按钮
                                @Override//处理确定按钮点击事件
                                public void onClick(DialogInterface dialog, int which) {
                                    String suffix = Constant.DELETE_DISCUSS_URL;
                                    //String suffix1="discuss/comment/delete";
                                    DeleteTopic deletetopic = new DeleteTopic();
                                    deletetopic.setId(query.getId());
                                    String json = new Gson().toJson(deletetopic);
                                    presenter.deleteTopic(suffix, json, url);
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();//对话框关闭。
                                }
                            }).create();
                    alert.show();
                }
            });
        });
    }

    @Override
    public void startLoading() {
        query_topic_refresh.post(() -> query_topic_refresh.setRefreshing(true));
    }

    @Override
    public void stopLoading() {
        if (query_topic_refresh.isRefreshing()) {
            query_topic_refresh.post(() -> query_topic_refresh.setRefreshing(false));
        }
    }

    @Override
    public void showLoadError(String error) {
        Snackbar.make(rv_query_topic_1_list, error, Snackbar.LENGTH_SHORT)
                .setAction("重试", view -> presenter.queryTopic(url)).show();
    }

    @Override
    public void setPresenter(HomeCourseTaskTalkTopicQueryContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void initViews(View view) {
        rv_query_topic_1_list = findViewById(R.id.rv_query_topic_1_list);
        rv_query_topic_1_list.setLayoutManager(new LinearLayoutManager(this));
        query_topic_refresh = findViewById(R.id.query_topic_refresh);
        query_topic_empty_view = findViewById(R.id.query_select_empty_view);
        ftbn_query_topic = findViewById(R.id.ftbn_query_topic);
        if (DataSupport.findLast(User.class).getTeacherId() == null) {
            ftbn_query_topic.setVisibility(View.INVISIBLE);
        } else {
            ftbn_query_topic.setVisibility(View.VISIBLE);
        }
        ftbn_query_topic.setOnClickListener(v -> {
            Intent intent = new Intent(HomeCourseTaskTalkTopicQueryActivity.this, HomeCourseTaskTalkTopicSendActivity.class);
            intent.putExtra("courseId", courseId);
            startActivity(intent);
        });
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void showConfirmSuccess(Boolean status) {
        if (status) {
            Snackbar.make(rv_query_topic_1_list, "为该同学添加答题次数成功", Snackbar.LENGTH_SHORT).show();
        }
    }
}
