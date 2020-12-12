package com.zhaoweihao.architechturesample.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.blankj.utilcode.util.GsonUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.adapter.ScoreAdapter;
import com.zhaoweihao.architechturesample.base.BaseActivity;
import com.zhaoweihao.architechturesample.bean.vote.Select;
import com.zhaoweihao.architechturesample.util.Logger;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 查看分数
 *
 * @author zhaoweihao
 * @date 2019/2/11
 */
public class ScoreActivity extends BaseActivity {

    @BindView(R.id.rv_selects)
    RecyclerView recyclerView;

    @BindView(R.id.score)
    TextView score;

    @BindView(R.id.detail)
    TextView detail;

    @BindView(R.id.toolbar)
    QMUITopBar toolbar;

    ScoreAdapter adapter;

    private List<Select> selects = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        ButterKnife.bind(this);
        toolbar.setTitle("成绩单");
        Type listType = new TypeToken<ArrayList<Select>>() {
        }.getType();
        List<Select> selects = new Gson().fromJson(getIntent().getStringExtra("data"), listType);
        this.selects.addAll(selects);

        adapter = new ScoreAdapter(R.layout.test_result_item, selects);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        //计算分数
//        int score = 0;
        int totalNum = selects.size();
        int right = 0;
        int wrong = 0;
        for (Select select : selects) {
            if (select.getRight_choice() == select.getUser_choice() - 1) {
                right++;
                continue;
            }
            wrong++;
        }
        double f = (double)right / totalNum * 100.0;
        BigDecimal bg = new BigDecimal(f);
        double f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

        score.setText(f1 + "分");
        detail.setText("正确:" + right + "题, " + "错误:" + wrong + "题");
//        Logger.d(GsonUtils.toJson(this.selects));
    }
}
