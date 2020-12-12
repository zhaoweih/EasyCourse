package com.zhaoweihao.architechturesample.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.base.BaseActivity;
import com.zhaoweihao.architechturesample.bean.RestResponse;
import com.zhaoweihao.architechturesample.bean.vote.Add;
import com.zhaoweihao.architechturesample.bean.vote.Select;
import com.zhaoweihao.architechturesample.util.Constant;

import org.litepal.crud.DataSupport;

import static com.zhaoweihao.architechturesample.util.HttpUtil.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
*@description 首页-课程-详细界面-更多-投票-创建投票（教师）
*@author
*@time 2019/1/28 13:00
*/
public class HomeCourseMoreVoteCreateActivity extends BaseActivity {

    public static final String TAG = "HomeCourseMoreVoteCreateActivity";

    private TextView num;
    private EditText title, choiceA, choiceB, choiceC, choiceD;
    private Button prev, next;

    private List<Select> selectList = new ArrayList<>();

    private int j = 0;
    private int choiceNum;

    private int courseId;
    private String theme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);

        initViews();

        getSupportActionBar().setTitle("创建问卷");

        selectList.clear();

        Intent intent = getIntent();
        choiceNum = intent.getIntExtra("num", 0);
        courseId = intent.getIntExtra("courseId", 0);
        theme = intent.getStringExtra("theme");

        for (int i = 0; i < choiceNum; i++) {
            selectList.add(new Select());
        }

        showNum();

        next.setOnClickListener(v -> {

            Select select = selectList.get(j);
            select.setId(j + 1);
            select.setTitle(title.getText().toString());
            select.setChoiceA(choiceA.getText().toString());
            select.setChoiceB(choiceB.getText().toString());
            select.setChoiceC(choiceC.getText().toString());
            select.setChoiceD(choiceD.getText().toString());
            if (j == selectList.size() - 1) {
                com.zhaoweihao.architechturesample.database.User user = DataSupport.findLast(com.zhaoweihao.architechturesample.database.User.class);
                if (user == null || user.getTeacherId() == null) {
                    Toast.makeText(this, "你没有登录或你不是老师身份，将强行退出", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
                String teacherId = user.getTeacherId();
                String title = theme;

                Add add = new Add();
                add.setTeacherId(teacherId);
                add.setTitle(title);
                add.setChoiceNum(choiceNum);
                add.setCourseId(courseId);

                add.setSelectList(selectList);

                String json = new Gson().toJson(add);

                String suffix = Constant.ADD_VOTE_URL;

                sendPostRequest(suffix, json, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String body = response.body().string();
                        RestResponse restResponse = new Gson().fromJson(body, RestResponse.class);
                        runOnUiThread(() -> {
                            if (restResponse.getCode() == Constant.SUCCESS_CODE) {
                                Toast.makeText(HomeCourseMoreVoteCreateActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });

                    }
                });
                return;
            }
            j++;
            if (selectList.get(j).getId() != null) {
                Select select2 = selectList.get(j);
                title.setText(select2.getTitle());
                choiceA.setText(select2.getChoiceA());
                choiceB.setText(select2.getChoiceB());
                choiceC.setText(select2.getChoiceC());
                choiceD.setText(select2.getChoiceD());
            }
            if (selectList.get(j).getId() == null)
                cleanAll();
            hideOrShowNextAndPrev();
        });


        prev.setOnClickListener(v -> {
            j--;
            Select select = selectList.get(j);
            showPrev(select);

            hideOrShowNextAndPrev();
        });

        hideOrShowNextAndPrev();


    }

    private void hideOrShowNextAndPrev() {
        if (j == 0)
            prev.setVisibility(View.INVISIBLE);
        else
            prev.setVisibility(View.VISIBLE);

        if (j == choiceNum - 1)
            next.setText("完成");
        else {
            next.setVisibility(View.VISIBLE);
            next.setText("下一题");
        }

    }

    private void showNum() {
        num.setText("第 " + (j + 1) + "题");
    }

    private void showPrev(Select select) {
        title.setText(select.getTitle());
        choiceA.setText(select.getChoiceA());
        choiceB.setText(select.getChoiceB());
        choiceC.setText(select.getChoiceC());
        choiceD.setText(select.getChoiceD());
        showNum();
    }

    private void cleanAll() {
        title.setText("");
        choiceA.setText("");
        choiceB.setText("");
        choiceC.setText("");
        choiceD.setText("");
        showNum();
    }

    private void initViews() {
        num = findViewById(R.id.tv_num);
        title = findViewById(R.id.et_title);
        choiceA = findViewById(R.id.et_choiceA);
        choiceB = findViewById(R.id.et_choiceB);
        choiceC = findViewById(R.id.et_choiceC);
        choiceD = findViewById(R.id.et_choiceD);
        prev = findViewById(R.id.btn_prev);
        next = findViewById(R.id.btn_next);

        setSupportActionBar(findViewById(R.id.toolbar));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
