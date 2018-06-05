package com.zhaoweihao.architechturesample.vote;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.data.vote.Add;
import com.zhaoweihao.architechturesample.data.vote.Select;

import static com.zhaoweihao.architechturesample.util.HttpUtil.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SetActivity extends AppCompatActivity {

    public static final String TAG = "SetActivity";

    private TextView num;
    private EditText title,choiceA,choiceB,choiceC,choiceD;
    private Button prev,next;

    private List<Select> selectList = new ArrayList<>();

    private static int j = 0;
    private static int choiceNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);

        initViews();

        selectList.clear();

        choiceNum = 5;

        showNum();

        next.setOnClickListener(v -> {
            if (j < choiceNum) {
               Select select = new Select();
               select.setId(j+1);
               select.setTitle(title.getText().toString());
               select.setChoiceA(choiceA.getText().toString());
               select.setChoiceB(choiceB.getText().toString());
               select.setChoiceC(choiceC.getText().toString());
               select.setChoiceD(choiceD.getText().toString());

               selectList.add(select);
               j ++ ;

           }

           if (j == choiceNum) {
               String teacherId = "20151120";
               String title = "课前调查";
               int choiceNum = 5;
               int courseId = 4;

               Add add = new Add();
               add.setTeacherId(teacherId);
               add.setTitle(title);
               add.setChoiceNum(choiceNum);
               add.setCourseId(courseId);

               add.setSelectList(selectList);

               String json = new Gson().toJson(add);
               Log.d(TAG, json);

               String suffix = "vote/add";

               sendPostRequest(suffix, json, new Callback() {
                   @Override
                   public void onFailure(Call call, IOException e) {

                   }

                   @Override
                   public void onResponse(Call call, Response response) throws IOException {
                       String body = response.body().string();
                       Log.d(TAG, body);
                   }
               });
               return;
           }
            cleanAll();
            hideOrShowNextAndPrev();
        });

        prev.setOnClickListener(v -> {
            if (j > 0) {
                j -- ;
                showPrev(selectList.get(j));
                selectList.remove(j);
            }
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
            next.setText("OK");
        else{
            next.setVisibility(View.VISIBLE);
            next.setText("Next");
        }

    }

    private void showNum() {
        num.setText("第 " + (j+1) + "题");
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

    }
}
