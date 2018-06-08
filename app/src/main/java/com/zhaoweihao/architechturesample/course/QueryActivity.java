package com.zhaoweihao.architechturesample.course;

import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
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

        getSupportActionBar().setTitle("课程查询和选择");

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
            if (adapter != null)
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

                // 创建一个带有EditText的对话框
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("选课");
                builder.setMessage("你将要选的课程为: " + query.getCourseName() + "\n \n" + "请核对后输入选课密码");

                final EditText input = new EditText(this);
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                builder.setView(input);

                builder.setPositiveButton("确定", (dialog, which) -> presenter.selectCourse(query, input.getText().toString()));
                builder.setNegativeButton("取消", (dialog, which) -> {

                });

                builder.show();

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
                .setAction("重试", view -> presenter.query(url)).show();
    }

    @Override
    public void showSelectSuccess(Boolean status) {
        if (status) {
            Snackbar.make(recyclerView, "选课成功", Snackbar.LENGTH_SHORT).show();
        }
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
        setSupportActionBar(findViewById(R.id.toolbar));

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
}
