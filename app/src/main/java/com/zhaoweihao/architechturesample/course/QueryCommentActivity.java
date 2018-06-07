package com.zhaoweihao.architechturesample.course;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.data.course.DeleteTopic;
import com.zhaoweihao.architechturesample.data.course.QueryComment;
import com.zhaoweihao.architechturesample.database.User;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class QueryCommentActivity extends AppCompatActivity implements QueryCommentContract.View {
    public static final String TAG = "QueryComment";
    private RecyclerView rv_query_comment_1_list;
    private SwipeRefreshLayout query_comment_refresh;
    private LinearLayout query_comment_empty_view;
    private QueryCommentContract.Presenter presenter;
    private QueryCommentAdapter adapter;
    private String url;
    private TextView tv_query_comment_teacherId,tv_query_comment_content,tv_query_comment_endDate,tv_query_comment_startDate;
    private Boolean checkTecOrStu;
    private Toolbar toolbar;
    private FloatingActionButton ftbn_query_comment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_comment);
        new QueryCommentPresenter(this, this);
        initViews(null);
        Intent intent = getIntent();
        checkTecOrStu = presenter.checkTecOrStu();
        String suffix = "discuss/comment/query?discussId="+intent.getIntExtra("discussId",0);

        User user3 = DataSupport.findLast(User.class);
        url=suffix;
//        url = suffix + "?" + "courseId=" + "4";
//        url = suffix+"?"+"courseId="+intent.getIntExtra("courseId",0);
        query_comment_refresh.setOnRefreshListener(() -> {
            presenter.QueryComment(url);
            adapter.notifyDataSetChanged();
            stopLoading();
        });
        presenter.QueryComment(url);
        ftbn_query_comment.setOnClickListener(v -> {


            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT );
            input.setBackgroundColor(0x665544);
            input.setTextAlignment(input.TEXT_ALIGNMENT_CENTER);
            input.setHint("请输入您的评论");
            builder.setView(input);
            builder.setPositiveButton("发送", (dialog, which) ->{

            });
            builder.setNegativeButton("取消", (dialog, which) -> {
            });
            builder.show();
                }
        );
    }
    @Override
    public void showResult(ArrayList<QueryComment> queryArrayList) {
        runOnUiThread(() -> {
            if (adapter == null) {
                adapter = new QueryCommentAdapter(this, queryArrayList,false);
                rv_query_comment_1_list.setAdapter(adapter);
            }
            else {
                adapter.notifyDataSetChanged();
            }
            adapter.setItemClickListener((v, position) -> {
                ArrayList<QueryComment> queries = presenter.getQueryList();
                QueryComment query = queries.get(position);
            });
            if(queryArrayList.size()==0){
                getSupportActionBar().setTitle("暂无评论");
            }else{
                getSupportActionBar().setTitle("已有"+queryArrayList.size()+"条评论");
            }
            adapter.setItemLongClickListener((view, position) -> {
              
            });
        });
    }
    @Override
    public void startLoading() {
        query_comment_refresh.post(() -> query_comment_refresh.setRefreshing(true));
    }

    @Override
    public void stopLoading() {
        if (query_comment_refresh.isRefreshing()){
            query_comment_refresh.post(() -> query_comment_refresh.setRefreshing(false));
        }
    }

    @Override
    public void showLoadError(String error) {
        Snackbar.make(rv_query_comment_1_list, error, Snackbar.LENGTH_SHORT)
                .setAction("重试", view -> presenter.QueryComment(url)).show();
    }
    @Override
    public void setPresenter(QueryCommentContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void initViews(View view) {
        ftbn_query_comment=findViewById(R.id.ftbn_query_comment);
        tv_query_comment_teacherId=findViewById(R.id.tv_query_comment_teacherId);
        tv_query_comment_content=findViewById(R.id.tv_query_comment_content);
        tv_query_comment_endDate=findViewById(R.id.tv_query_comment_endDate);
        tv_query_comment_startDate=findViewById(R.id.tv_query_comment_startDate);
        Intent intent = getIntent();
        tv_query_comment_teacherId.setText(intent.getStringExtra("teacherId"));
        tv_query_comment_content.setText(intent.getStringExtra("commentContent"));
        tv_query_comment_startDate.setText(intent.getStringExtra("commentStartDate")+"发布");
        tv_query_comment_endDate.setText(intent.getStringExtra("commentEndDate")+"截止");
        /*intent.putExtra("commentContent",queryArrayList.get(position).getContent());
        intent.putExtra("commentEndDate",queryArrayList.get(position).getEndDate());
        intent.putExtra("teacherId",queryArrayList.get(position).getTeacherId());
        intent.putExtra("courseId",courseId);
        intent.putExtra("discussId",queryArrayList.get(position).getId());*/


        rv_query_comment_1_list= findViewById(R.id.rv_query_comment_1_list);
        rv_query_comment_1_list.setLayoutManager(new LinearLayoutManager(this));
        query_comment_refresh = findViewById(R.id.query_comment_refresh);
        query_comment_empty_view= findViewById(R.id.query_select_empty_view);
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
            Snackbar.make(rv_query_comment_1_list, "为该同学添加答题次数成功", Snackbar.LENGTH_SHORT).show();
        }
    }
}
