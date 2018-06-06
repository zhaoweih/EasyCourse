package com.zhaoweihao.architechturesample.vote;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.data.vote.Add;
import com.zhaoweihao.architechturesample.data.vote.Select;

import java.util.List;

public class ResultActivity extends AppCompatActivity {

    public static final String TAG = "ResultActivity";

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private ResultListAdapter adapter;

    private LinearLayout emptyView;

    private List<Select> selects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        initViews();

        Intent intent = getIntent();

        Add add = (Add) intent.getSerializableExtra("voteObj");

        selects = add.getSelectList();

        adapter = new ResultListAdapter(this, selects);
        recyclerView.setAdapter(adapter);


        adapter.setItemClickListener((v, position) -> {
            // 处理单击事件

        });
        adapter.setItemLongClickListener((view, position) -> {
            // 处理长按事件
        });

    }

    private void initViews() {
        setSupportActionBar(findViewById(R.id.toolbar));
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        swipeRefreshLayout = findViewById(R.id.refresh);
        emptyView = findViewById(R.id.empty_view);
    }
}
