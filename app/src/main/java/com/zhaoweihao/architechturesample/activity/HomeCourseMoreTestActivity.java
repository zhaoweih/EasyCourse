package com.zhaoweihao.architechturesample.activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.adapter.HomeCourseMoreVoteAdapter;
import com.zhaoweihao.architechturesample.base.BaseActivity;
import com.zhaoweihao.architechturesample.bean.RestResponse;
import com.zhaoweihao.architechturesample.bean.vote.Add;
import com.zhaoweihao.architechturesample.database.User;
import com.zhaoweihao.architechturesample.util.Constant;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.zhaoweihao.architechturesample.util.HttpUtil.sendGetRequest;

/**
 * @author zhaoweihao
 * @description 展示测试页面
 * @time 19/2/11
 */
public class HomeCourseMoreTestActivity extends BaseActivity {

    public static final String TAG = "HomeCourseMoreTestActivity";

    private Toolbar toolbar;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private LinearLayout emptyView;

    private HomeCourseMoreVoteAdapter adapter;

    private ArrayList<Add> voteList = new ArrayList<>();

    private int courseId;

    private Integer chapter_id;
    private QMUIDialog.EditTextDialogBuilder setNum,setTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        initViews();

//        if (getIntent().getIntExtra("courseId", 0) == 0) {
//            Toast.makeText(this, "未知错误", Toast.LENGTH_SHORT).show();
//            return;
//        }

        courseId = getIntent().getIntExtra("courseId", 0);

        //todo: tanxinkui 传入章节id
        chapter_id = getIntent().getIntExtra("chapter_id", 0);

        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.create:
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("创建一个测试");
                    builder.setMessage("请在下方填入创建的题数和题目后点击确定");

                    LinearLayout layout = new LinearLayout(this);
                    layout.setOrientation(LinearLayout.VERTICAL);

                    final EditText titleBox = new EditText(this);
                    titleBox.setHint("输入题数");
                    titleBox.setInputType(InputType.TYPE_CLASS_NUMBER);
                    layout.addView(titleBox);

                    final EditText descriptionBox = new EditText(this);
                    descriptionBox.setHint("输入此次调查的主题");
                    descriptionBox.setInputType(InputType.TYPE_CLASS_TEXT);
                    layout.addView(descriptionBox);

                    builder.setView(layout);


                    builder.setPositiveButton("确定", (dialog, which) -> {
                        Intent intent = new Intent(HomeCourseMoreTestActivity.this, HomeCourseMoreTestCreateActivity.class);
                        intent.putExtra("num", Integer.valueOf(titleBox.getText().toString()));
                        intent.putExtra("theme", descriptionBox.getText().toString());
                        intent.putExtra("chapter_id", chapter_id);
                        startActivity(intent);

                    });
                    builder.setNegativeButton("取消", (dialog, which) -> {

                    });

                    //builder.show();
                    createDialog();

            }
            return true;
        });

        String suffix = Constant.QUERY_TEST_BY_CHAPTER_ID_URL + chapter_id;

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
                            if (voteList.isEmpty()) {
                                emptyView.setVisibility(View.VISIBLE);
                            }
                            if (adapter == null) {
                                adapter = new HomeCourseMoreVoteAdapter(HomeCourseMoreTestActivity.this, voteList);
                                recyclerView.setAdapter(adapter);
                            } else {
                                adapter.notifyDataSetChanged();
                            }

                            getSupportActionBar().setTitle("有" + voteList.size() + "条测试信息");


                            adapter.setItemClickListener((v, position) -> {
                                // 处理单击事件

                                //Make new Dialog
                                AlertDialog.Builder dialog = new AlertDialog.Builder(HomeCourseMoreTestActivity.this);
                                dialog.setTitle("去测试或者查看结果 ");

                                LinearLayout layout = new LinearLayout(HomeCourseMoreTestActivity.this);
                                layout.setOrientation(LinearLayout.VERTICAL);
                                final ImageView imageView = new ImageView(HomeCourseMoreTestActivity.this);
                                imageView.setImageResource(R.drawable.research);
                                layout.addView(imageView);
                                imageView.getLayoutParams().height = 350;
                                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                                final Button toVote = new Button(HomeCourseMoreTestActivity.this);
                                toVote.setBackgroundColor(ContextCompat.getColor(HomeCourseMoreTestActivity.this, R.color.colorPrimary));
                                toVote.setTextColor(ContextCompat.getColor(HomeCourseMoreTestActivity.this, R.color.black));
                                toVote.setText("去测试");
                                toVote.setOnClickListener(v1 -> {
                                    Intent intent = new Intent(HomeCourseMoreTestActivity.this, HomeCourseMoreTestToTestActivity.class);
                                    intent.putExtra("voteObj", voteList.get(position));
                                    startActivity(intent);
                                });
                                if (DataSupport.findLast(User.class).getStudentId() != null) {
                                    layout.addView(toVote);
                                }
                                final Button toResult = new Button(HomeCourseMoreTestActivity.this);
                                toResult.setOnClickListener(v12 -> {
                                    Intent intent = new Intent(HomeCourseMoreTestActivity.this, HomeCourseMoreTestResultActivity.class);
                                    intent.putExtra("position", position);
                                    intent.putExtra("chapter_id", chapter_id);
                                    startActivity(intent);
                                });
                                toResult.setBackgroundColor(ContextCompat.getColor(HomeCourseMoreTestActivity.this, R.color.colorPrimary));
                                toResult.setTextColor(ContextCompat.getColor(HomeCourseMoreTestActivity.this, R.color.black));
                                toResult.setText("查看结果");
                                layout.addView(toResult);

                                dialog.setView(layout);

                                dialog.show();


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
    public boolean onCreateOptionsMenu(Menu menu) {
        if (DataSupport.findLast(User.class).getStudentId() == null) {
            getMenuInflater().inflate(R.menu.test_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void createDialog() {
                setNum = new QMUIDialog.EditTextDialogBuilder(HomeCourseMoreTestActivity.this)
                .setTitle("创建一个测试")
                .setPlaceholder("输入题数!")
                .setInputType(InputType.TYPE_CLASS_NUMBER)
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction("下一步", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        if(TextUtils.isEmpty(setNum.getEditText().getText().toString())){
                            Toast.makeText(HomeCourseMoreTestActivity.this,"请输入题数！",Toast.LENGTH_SHORT).show();
                        }else {
                            createDialog2(setNum.getEditText().getText().toString());
                            dialog.dismiss();
                        }

                    }
                });

        setNum.show();
    }
    private void createDialog2(String num) {
                setTitle = new QMUIDialog.EditTextDialogBuilder(HomeCourseMoreTestActivity.this)
                .setTitle("创建一个测试")
                .setPlaceholder("输入此次测试的主题!")
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction("完成", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        if(TextUtils.isEmpty(setTitle.getEditText().getText().toString())){
                            Toast.makeText(HomeCourseMoreTestActivity.this,"请输入测试的主题！",Toast.LENGTH_SHORT).show();
                        }else {
                            dialog.dismiss();
                            Intent intent = new Intent(HomeCourseMoreTestActivity.this, HomeCourseMoreTestCreateActivity.class);
                            intent.putExtra("num", Integer.valueOf(num));
                            intent.putExtra("theme", setTitle.getEditText().getText().toString());
                            intent.putExtra("chapter_id", chapter_id);
                            startActivity(intent);
                        }
                    }
                });
       setTitle.show();
    }

}
