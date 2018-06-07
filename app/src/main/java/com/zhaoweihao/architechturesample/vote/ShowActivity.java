package com.zhaoweihao.architechturesample.vote;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.data.RestResponse;
import com.zhaoweihao.architechturesample.data.vote.Add;


import static com.zhaoweihao.architechturesample.util.HttpUtil.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ShowActivity extends AppCompatActivity {

    public static final String TAG = "ShowActivity";

    private Toolbar toolbar;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private LinearLayout emptyView;

    private VoteListAdapter adapter;

    private ArrayList<Add> voteList = new ArrayList<>();

    private int courseId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        initViews();

        if (getIntent().getIntExtra("courseId", 0) == 0) {
            Toast.makeText(this, "未知错误", Toast.LENGTH_SHORT).show();
            return;
        }

        courseId = getIntent().getIntExtra("courseId", 0);

//        courseId = 4;

        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.create:
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("创建一个投票");
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
                        Intent intent = new Intent(ShowActivity.this, SetActivity.class);
                        intent.putExtra("num", Integer.valueOf(titleBox.getText().toString()));
                        intent.putExtra("theme", descriptionBox.getText().toString());
                        intent.putExtra("courseId", courseId);
                        startActivity(intent);

                    });
                    builder.setNegativeButton("取消", (dialog, which) -> {

                    });

                    builder.show();
            }
            return true;
        });

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

                                 //Make new Dialog
                                 AlertDialog.Builder dialog = new AlertDialog.Builder(ShowActivity.this);
                                 dialog.setTitle("To Vote|Result ");

                                 LinearLayout layout = new LinearLayout(ShowActivity.this);
                                 layout.setOrientation(LinearLayout.VERTICAL);
                                 final ImageView imageView = new ImageView(ShowActivity.this);
                                 imageView.setImageResource(R.drawable.research);
                                 layout.addView(imageView);
                                 imageView.getLayoutParams().height = 350;
                                 imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                                 final Button toVote = new Button(ShowActivity.this);
                                 toVote.setBackgroundColor(ContextCompat.getColor(ShowActivity.this, R.color.colorPrimary));
                                 toVote.setTextColor(ContextCompat.getColor(ShowActivity.this, R.color.white));
                                 toVote.setText("To Vote");
                                 toVote.setOnClickListener(v1 -> {
                                     Log.d(TAG, new Gson().toJson(voteList.get(position)));
                                     Intent intent = new Intent(ShowActivity.this, VoteActivity.class);
                                     intent.putExtra("voteObj", voteList.get(position));
                                     startActivity(intent);
                                 });
                                 layout.addView(toVote);
                                 final Button toResult = new Button(ShowActivity.this);
                                 toResult.setOnClickListener(v12 -> {
                                     Log.d(TAG, new Gson().toJson(voteList.get(position)));
                                     Intent intent = new Intent(ShowActivity.this, ResultActivity.class);
                                     intent.putExtra("position", position);
                                     intent.putExtra("courseId", courseId);
                                     startActivity(intent);
                                 });
                                 toResult.setBackgroundColor(ContextCompat.getColor(ShowActivity.this, R.color.colorPrimary));
                                 toResult.setTextColor(ContextCompat.getColor(ShowActivity.this, R.color.white));
                                 toResult.setText("To Result");
                                 layout.addView(toResult);

                                 dialog.setView(layout);

                                 dialog.show();


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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.vote_menu, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
