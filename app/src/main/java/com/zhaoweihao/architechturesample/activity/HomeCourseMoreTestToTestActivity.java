package com.zhaoweihao.architechturesample.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.GsonUtils;
import com.google.gson.Gson;
import com.j256.ormlite.stmt.query.In;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.base.BaseActivity;
import com.zhaoweihao.architechturesample.bean.RestResponse;
import com.zhaoweihao.architechturesample.bean.TestRecord;
import com.zhaoweihao.architechturesample.bean.vote.Add;
import com.zhaoweihao.architechturesample.bean.vote.AddRec;
import com.zhaoweihao.architechturesample.bean.vote.Record;
import com.zhaoweihao.architechturesample.bean.vote.Select;
import com.zhaoweihao.architechturesample.database.User;
import com.zhaoweihao.architechturesample.util.Constant;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.zhaoweihao.architechturesample.util.HttpUtil.sendPostRequest;

/**
*@description 去测试
*@author zhaoweihao
*@time 2019/2/11
*/
public class HomeCourseMoreTestToTestActivity extends BaseActivity {

    public static final String TAG = "HomeCourseMoreTestToTestActivity";

    // 展示标题，第几题
    private TextView num,title;
    private RadioGroup radioGroup;
    // 四个选择按钮
    private RadioButton radioButton1,radioButton2,radioButton3,radioButton4;
    private Button prev,next;

    private List<Select> selects;

    private List<AddRec> addRecs = new ArrayList<>();

    private int count = 0;
    private int choiceNum = 0;
    private int voidId = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        initViews();

        getSupportActionBar().setTitle("测试题");

        Intent intent = getIntent();

        Add add = (Add)intent.getSerializableExtra("voteObj");

        selects = add.getSelectList();

        voidId = add.getId();

        for (int i = 0; i < selects.size(); i ++) {
            addRecs.add(new AddRec());
        }

        loadVote(selects.get(0));

        next.setOnClickListener(v -> {
            if (count == selects.size() - 1) {
                addRecord();
                return;
            }
            radioGroup.clearCheck();
            count ++;
            loadVote(selects.get(count));
            hideOrShowBtn();
        });

        prev.setOnClickListener(v -> {
            radioGroup.clearCheck();
            count --;
            loadVote(selects.get(count));
            hideOrShowBtn();
        });

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            //Log.d(TAG, count + " ");
            switch (checkedId) {
                case R.id.rb_1:
                    choiceNum = 1;
                    break;
                case R.id.rb_2:
                    choiceNum = 2;
                    break;
                case R.id.rb_3:
                    choiceNum = 3;
                    break;
                case R.id.rb_4:
                    choiceNum = 4;
                    break;
                    default:
                        break;
            }

            AddRec addRec = addRecs.get(count);
            addRec.setChoice(choiceNum);
            addRec.setSelectId(count + 1);


        });
    }

    private void addRecord() {
        String suffix = Constant.ADD_AND_TEST_URL;
        User user = DataSupport.findLast(User.class);
//        if (user == null || user.getStudentId() == null) {
//            Toast.makeText(this, "请登录或以学生身份登录", Toast.LENGTH_SHORT).show();
//            finish();
//            return;
//        }
//        String studentId = user.getStudentId();

        TestRecord record = new TestRecord();
//        record.setStudentId(studentId);
        record.setVoteId(voidId);
//        record.setRec_json(addRecs);
        record.setRec_json(addRecs);

        String json = new Gson().toJson(record);

       // Log.d(TAG, json);

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
                        for (int i = 0; i < selects.size(); i ++) {
                            selects.get(i).setUser_choice(addRecs.get(i).getChoice());
                        }
                        Intent intent = new Intent(HomeCourseMoreTestToTestActivity.this, ScoreActivity.class);
                        intent.putExtra("data", GsonUtils.toJson(selects));
                        startActivity(intent);
//                        Toast.makeText(HomeCourseMoreTestToTestActivity.this, "提交投票成功", Toast.LENGTH_SHORT).show();
//                        finish();

                    }
                });

            }
        });


    }

    private void loadVote(Select select) {
        num.setText("第 " + select.getId() + " 题");
        title.setText(select.getTitle());
        radioButton1.setText(select.getChoiceA());
        radioButton2.setText(select.getChoiceB());
        radioButton3.setText(select.getChoiceC());
        radioButton4.setText(select.getChoiceD());
    }

    private void hideOrShowBtn() {
        if (count == selects.size() - 1)
            next.setBackgroundResource(R.drawable.quiz_finish);
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
        prev.setVisibility(View.INVISIBLE);
        next = findViewById(R.id.btn_next);
        title = findViewById(R.id.tv_title);
        num = findViewById(R.id.tv_num);

        radioButton1 = findViewById(R.id.rb_1);
        radioButton2 = findViewById(R.id.rb_2);
        radioButton3 = findViewById(R.id.rb_3);
        radioButton4 = findViewById(R.id.rb_4);

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
