package com.zhaoweihao.architechturesample.activity;

import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.kongzue.stacklabelview.StackLabel;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.base.BaseActivity;
import com.zhaoweihao.architechturesample.bean.course.Query;
import com.zhaoweihao.architechturesample.adapter.HomeCourseSearchQueryAdapter;
import com.zhaoweihao.architechturesample.contract.HomeCourseSearchQueryCourseContract;
import com.zhaoweihao.architechturesample.database.HistoryTag;
import com.zhaoweihao.architechturesample.presenter.HomeCourseSearchQueryCoursePresenter;
import com.zhaoweihao.architechturesample.ui.SearchBoardInputLayout;
import com.zhaoweihao.architechturesample.util.Constant;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author
 * @description 首页-课程-查询并选课
 * @time 2019/1/28 13:02
 */
public class HomeCourseSearchQueryCourseActivity extends BaseActivity implements HomeCourseSearchQueryCourseContract.View {

    public static final String TAG = "HomeCourseSearchQueryCourseActivity";

    private RecyclerView recyclerView;
    /*  private SwipeRefreshLayout swipeRefreshLayout;
      private LinearLayout emptyView;*/
    private HomeCourseSearchQueryCourseContract.Presenter presenter;
    private HomeCourseSearchQueryAdapter adapter;
    private String url;

    private EditText input;
    private Button query;

    private Handler handler;
    private Timer timer;
    private TimerTask timerTask;
    private int originalNum;

    private Boolean checkTecOrStu;
    @BindView(R.id.aq_searchBoardInputLayout)
    SearchBoardInputLayout aq_searchBoardInputLayout;
    @BindView(R.id.aq_empty_view)
    LinearLayout aq_empty_view;
    private StackLabel mstackLabel;
    private String regex = "[0-9]+";
    private String suffix = Constant.QUERY_AND_SELECT_COURSE_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);
        ButterKnife.bind(this);

        new HomeCourseSearchQueryCoursePresenter(this, this);

        initViews(null);

        getSupportActionBar().setTitle("课程查询和选择");

        checkTecOrStu = presenter.checkTecOrStu();

        query.setOnClickListener(v -> {
            String inputText = input.getText().toString();
            // 如果全是数字则认为输入的是教师编号
            if (inputText.matches(regex))
                url = suffix + "?" + "teacherId=" + inputText;
            else
                url = suffix + "?" + "courseName=" + inputText;

            presenter.query(url);
        });

      /*  swipeRefreshLayout.setOnRefreshListener(() -> {
            presenter.query(url);
            if (adapter != null)
                adapter.notifyDataSetChanged();
            stopLoading();
        });*/
    }


    @Override
    public void showResult(ArrayList<Query> queryArrayList) {
        runOnUiThread(() -> {
            if (adapter == null) {
                adapter = new HomeCourseSearchQueryAdapter(this, queryArrayList, checkTecOrStu);
                recyclerView.setAdapter(adapter);
                if(queryArrayList.size()!=0){
                    aq_empty_view.setVisibility(View.GONE);
                }else {
                    aq_empty_view.setVisibility(View.VISIBLE);
                }
            } else {
                if(queryArrayList.size()!=0){
                    aq_empty_view.setVisibility(View.GONE);
                }else {
                    aq_empty_view.setVisibility(View.VISIBLE);
                }
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
        //swipeRefreshLayout.post(() -> swipeRefreshLayout.setRefreshing(true));
    }

    @Override
    public void stopLoading() {
    /*    if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.post(() -> swipeRefreshLayout.setRefreshing(false));
        }*/
    }

    @Override
    public void showLoadError(String error) {
    /*    Snackbar.make(recyclerView, error, Snackbar.LENGTH_SHORT)
                .setAction("重试", view -> presenter.query(url)).show();*/
        runOnUiThread(() -> {
            Toast.makeText(HomeCourseSearchQueryCourseActivity.this, error, Toast.LENGTH_SHORT).show();
            aq_empty_view.setVisibility(View.VISIBLE);
        });
    }

    @Override
    public void showSelectSuccess(Boolean status) {
        if (status) {
            Snackbar.make(recyclerView, "选课成功", Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setPresenter(HomeCourseSearchQueryCourseContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void initViews(View view) {
        recyclerView = findViewById(R.id.rv_course_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
     /*   swipeRefreshLayout = findViewById(R.id.refresh);
        emptyView = findViewById(R.id.empty_view);*/
        input = findViewById(R.id.input);
        query = findViewById(R.id.query);
        setSupportActionBar(findViewById(R.id.toolbar));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        aq_searchBoardInputLayout.initWithArgs("请输入课程名字或教师编号以选课", 10, "course_search_all");
        List<HistoryTag> allData = DataSupport.where("tagTag=?", "course_search_all").find(com.zhaoweihao.architechturesample.database.HistoryTag.class);
        originalNum = allData.size();
        mstackLabel = aq_searchBoardInputLayout.getStackbel();
        detectInput("course_search_all");
        setStableClickListener();

    }

    public void setStableClickListener() {
        mstackLabel.setOnLabelClickListener((int index, View view, String s) -> {
            goSearchNow(s);
        });
    }

    private void goSearchNow(String inputText) {
        // 如果全是数字则认为输入的是教师编号
        if (inputText.matches(regex)) {
            url = suffix + "?" + "teacherId=" + inputText;
        } else {
            url = suffix + "?" + "courseName=" + inputText;
        }
        presenter.query(url);
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

    private void detectInput(String searchTagTag) {


        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 11) {
                    mstackLabel = aq_searchBoardInputLayout.getStackbel();
                    setStableClickListener();
                    goSearchNow(aq_searchBoardInputLayout.getFinalSearchString());
                }
                if (msg.what == 12) {
                }
                super.handleMessage(msg);
            }
        };
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                List<HistoryTag> allData = DataSupport.where("tagTag=?", searchTagTag).find(com.zhaoweihao.architechturesample.database.HistoryTag.class);
                if ((allData.size() != originalNum) && (allData.size() != 0)) {
                    message.what = 11;
                    originalNum = allData.size();
                } else {
                    message.what = 12;
                }

                handler.sendMessage(message);
            }
        };
        timer.schedule(timerTask, 200, 200);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
        handler.removeMessages(11);
        handler.removeMessages(12);
    }
}
