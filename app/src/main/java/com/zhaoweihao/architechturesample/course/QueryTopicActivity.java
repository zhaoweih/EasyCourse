package com.zhaoweihao.architechturesample.course;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.data.OnStringListener;
import com.zhaoweihao.architechturesample.data.StringModelImpl;
import com.zhaoweihao.architechturesample.data.course.DeleteTopic;
import com.zhaoweihao.architechturesample.data.course.QuerySelect;
import com.zhaoweihao.architechturesample.data.course.QueryTopic;
import com.zhaoweihao.architechturesample.data.course.SendTopic;
import com.zhaoweihao.architechturesample.database.User;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Random;

public class QueryTopicActivity extends AppCompatActivity implements QueryTopicContract.View {
    public static final String TAG = "QueryTopic";
    private RecyclerView rv_query_topic_1_list;
    private SwipeRefreshLayout query_topic_refresh;
    private LinearLayout query_topic_empty_view;
    private QueryTopicContract.Presenter presenter;
    private QueryTopicAdapter adapter;
    private String url;
    private Boolean checkTecOrStu;
    private Toolbar toolbar;
    private int courseId;
    private FloatingActionButton ftbn_query_topic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_topic);
        new QueryTopicPresenter(this, this);

        Intent intent = getIntent();
        checkTecOrStu = presenter.checkTecOrStu();
        courseId=intent.getIntExtra("courseId",0);
        initViews(null);
        String suffix = "discuss/query?courseId="+intent.getIntExtra("courseId",0);
        User user3 = DataSupport.findLast(User.class);
        url=suffix;
//        url = suffix + "?" + "courseId=" + "4";
//        url = suffix+"?"+"courseId="+intent.getIntExtra("courseId",0);
        query_topic_refresh.setOnRefreshListener(() -> {
            presenter.queryTopic(url);
            adapter.notifyDataSetChanged();
            stopLoading();
        });
        
        presenter.queryTopic(url);
    }
    @Override
    public void showResult(ArrayList<QueryTopic> queryArrayList) {
        runOnUiThread(() -> {
        if(queryArrayList.size()==0){
            getSupportActionBar().setTitle("暂无讨论帖");
        }else{
            getSupportActionBar().setTitle("已有"+queryArrayList.size()+"个讨论帖");
        }
         if (adapter == null) {
                adapter = new QueryTopicAdapter(this, queryArrayList,false);
                rv_query_topic_1_list.setAdapter(adapter);
            }
            else {
                adapter.notifyDataSetChanged();
            }
            adapter.setItemClickListener((v, position) -> {
               /* queries1 = presenter.getQueryList();
                QueryTopic query = queries1.get(position);*/
                Intent intent = new Intent(QueryTopicActivity.this, QueryCommentActivity.class);
                intent.putExtra("commentContent",queryArrayList.get(position).getContent());
                intent.putExtra("commentEndDate",queryArrayList.get(position).getEndDate());
                intent.putExtra("commentStartDate",queryArrayList.get(position).getStartDate());
                intent.putExtra("teacherId",queryArrayList.get(position).getTeacherId());
                intent.putExtra("courseId",courseId);
                intent.putExtra("discussId",queryArrayList.get(position).getId());
                startActivity(intent);

            });

            adapter.setItemLongClickListener((view, position) -> {
                User user3 = DataSupport.findLast(User.class);
                if(user3.getTeacherId()!=null){
                ArrayList<QueryTopic> queries = presenter.getQueryList();
                QueryTopic query = queries.get(position);
                // 处理长按行为
                AlertDialog alert = new AlertDialog.Builder(this).setTitle("温馨提示")
                        .setMessage("确定要删除讨论吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {//设置确定按钮
                            @Override//处理确定按钮点击事件
                            public void onClick(DialogInterface dialog, int which) {
                                String suffix = "discuss/delete";
                                //String suffix1="discuss/comment/delete";
                                DeleteTopic deletetopic=new DeleteTopic();
                                deletetopic.setId(query.getId());
                                String json = new Gson().toJson(deletetopic);
                                presenter.deleteTopic(suffix,json,url);

                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick (DialogInterface dialog,int which){
                                dialog.cancel();//对话框关闭。
                            }
                        }).create();
                alert.show();}
            });
        });
    }
    @Override
    public void startLoading() {
        query_topic_refresh.post(() -> query_topic_refresh.setRefreshing(true));
    }

    @Override
    public void stopLoading() {
        if (query_topic_refresh.isRefreshing()){
            query_topic_refresh.post(() -> query_topic_refresh.setRefreshing(false));
        }
    }

    @Override
    public void showLoadError(String error) {
        Snackbar.make(rv_query_topic_1_list, error, Snackbar.LENGTH_SHORT)
                .setAction("重试", view -> presenter.queryTopic(url)).show();
    }
    @Override
    public void setPresenter(QueryTopicContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void initViews(View view) {
        rv_query_topic_1_list= findViewById(R.id.rv_query_topic_1_list);
        rv_query_topic_1_list.setLayoutManager(new LinearLayoutManager(this));
        query_topic_refresh = findViewById(R.id.query_topic_refresh);
        query_topic_empty_view= findViewById(R.id.query_select_empty_view);
        ftbn_query_topic=findViewById(R.id.ftbn_query_topic);
        if(DataSupport.findLast(User.class).getTeacherId()==null){
            ftbn_query_topic.setVisibility(View.INVISIBLE);
        }else {
            ftbn_query_topic.setVisibility(View.VISIBLE);
        }
        ftbn_query_topic.setOnClickListener(v->{
            Intent intent=new Intent(QueryTopicActivity.this, com.zhaoweihao.architechturesample.course.SendTopic.class);
            intent.putExtra("courseId",courseId);
            startActivity(intent);
        });
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
