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
import android.widget.Toast;

import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.data.course.Query;
import com.zhaoweihao.architechturesample.data.course.QuerySelect;
import com.zhaoweihao.architechturesample.database.User;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;

public class QuerySelectActivity extends AppCompatActivity implements QuerySelectContract.View{
    public static final String TAG = "QuerySelectActivity";

    private RecyclerView rv_query_select_course_list;
    private SwipeRefreshLayout query_select_refresh;
    private LinearLayout query_select_empty_view;
    private QuerySelectContract.Presenter presenter;
    private QuerySelectAdapter adapter;
    private String url;
    private Boolean checkTecOrStu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_select);
        new QuerySelectPresenter(this, this);
        initViews(null);
        //getSupportActionBar().setTitle("已选课程");  添加这行出现闪退bug
        checkTecOrStu = presenter.checkTecOrStu();
        String suffix = "course/querySelectByStuId";
        User user3 = DataSupport.findLast(User.class);
        if (user3.getStudentId() == null && !(user3.getTeacherId() == null)) {
            Toast.makeText(QuerySelectActivity.this, "您不是学生！", Toast.LENGTH_SHORT).show();
        } else if (!(user3.getStudentId() == null) && user3.getTeacherId() == null) {
            url = suffix+"?"+"stuId="+user3.getUserId();
            presenter.querySelect(url);
            query_select_refresh.setOnRefreshListener(() -> {
                presenter.querySelect(url);
                adapter.notifyDataSetChanged();
                stopLoading();
            });
        }



    }
    @Override
    public void showResult(ArrayList<QuerySelect> queryArrayList) {
        runOnUiThread(() -> {
            if (adapter == null) {
                adapter = new QuerySelectAdapter(this, queryArrayList,false);
                rv_query_select_course_list.setAdapter(adapter);
            }
            else {
                adapter.notifyDataSetChanged();
            }
            adapter.setItemClickListener((v, position) -> {
                ArrayList<QuerySelect> queries = presenter.getQueryList();
                QuerySelect query = queries.get(position);

            });
            adapter.setItemLongClickListener((view, position) -> {
                // 处理长按行为
            });
        });
    }
    @Override
    public void startLoading() {
        query_select_refresh.post(() -> query_select_refresh.setRefreshing(true));
    }

    @Override
    public void stopLoading() {
        if (query_select_refresh.isRefreshing()){
            query_select_refresh.post(() -> query_select_refresh.setRefreshing(false));
        }
    }

    @Override
    public void showLoadError(String error) {
        Snackbar.make(rv_query_select_course_list, error, Snackbar.LENGTH_SHORT)
                .setAction("重试", view -> presenter.querySelect(url)).show();
    }

    @Override
    public void showSelectSuccess(Boolean status) {
        if (status) {
            Snackbar.make(rv_query_select_course_list, "选课成功", Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setPresenter(QuerySelectContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void initViews(View view) {
        rv_query_select_course_list= findViewById(R.id.rv_query_select_course_list);
        rv_query_select_course_list.setLayoutManager(new LinearLayoutManager(this));
        query_select_refresh = findViewById(R.id.query_select_refresh);
        query_select_empty_view= findViewById(R.id.query_select_empty_view);
        setSupportActionBar(findViewById(R.id.toolbar));
    }
    @Override
    protected void onResume() {
        super.onResume();
        presenter.start();
    }
}
