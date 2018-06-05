package com.zhaoweihao.architechturesample.vote;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.data.vote.Add;
import com.zhaoweihao.architechturesample.data.vote.Select;


import java.util.List;

import static com.zhaoweihao.architechturesample.util.HttpUtil.*;

public class VoteActivity extends AppCompatActivity {

    public static final String TAG = "VoteActivity";

    // 展示标题，第几题
    private TextView num,title;
    private RadioGroup radioGroup;
    // 四个选择按钮
    private RadioButton radioButton1,radioButton2,radioButton3,radioButton4;
    private Button prev,next;

    private List<Select> selects;

    private static int count = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        initViews();

        Intent intent = getIntent();

        Add add = (Add)intent.getSerializableExtra("voteObj");

        selects = add.getSelectList();

        loadVote(selects.get(0));

        next.setOnClickListener(v -> {
            count ++;
            loadVote(selects.get(count));
            hideOrShowBtn();

        });

        prev.setOnClickListener(v -> {
            count --;
            loadVote(selects.get(count));
            hideOrShowBtn();
        });
    }

    private void loadVote(Select select) {
        num.setText("第 " + select.getId() + " 题");
        title.setText(select.getTitle());
        radioButton1.setText(select.getChoiceA());
        radioButton2.setText(select.getChoiceB());
        radioButton3.setText(select.getChoiceC());
        radioButton4.setText(select.getChoiceD());
        Log.d(TAG, count + "");
    }

    private void hideOrShowBtn() {
        if (count == selects.size() - 1)
            next.setVisibility(View.INVISIBLE);
        else
            next.setVisibility(View.VISIBLE);

        if (count == 0)
            prev.setVisibility(View.INVISIBLE);
        else
            prev.setVisibility(View.VISIBLE);
    }

    private void initViews() {

        radioGroup = findViewById(R.id.rg_choices);
        prev = findViewById(R.id.btn_prev);
        next = findViewById(R.id.btn_next);
        title = findViewById(R.id.tv_title);
        num = findViewById(R.id.tv_num);

        radioButton1 = findViewById(R.id.rb_1);
        radioButton2 = findViewById(R.id.rb_2);
        radioButton3 = findViewById(R.id.rb_3);
        radioButton4 = findViewById(R.id.rb_4);

    }
}
