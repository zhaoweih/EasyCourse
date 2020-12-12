package com.zhaoweihao.architechturesample.activity;

import android.content.Intent;
import android.os.Bundle;



import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.base.BaseActivity;
import com.zhaoweihao.architechturesample.ui.TitleLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
*@description:首页-更多
*@author tanxinkui
*@time 2019/1/9 11:39
*/


public class HomeMoreActivity extends BaseActivity {
    @BindView(R.id.amm_title)
    TitleLayout amm_title;
    @BindView(R.id.amm_tv_translatorView)
    com.qmuiteam.qmui.widget.QMUIRadiusImageView amm_tv_translatorView;
    @BindView(R.id.amm_tv_zhiHuDailyView)
    com.qmuiteam.qmui.widget.QMUIRadiusImageView amm_tv_zhiHuDailyView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_more);
        ButterKnife.bind(this);
        init();
    }
    public void init(){
        amm_title.setTitle("应用");
        amm_tv_translatorView.setOnClickListener(view -> {
            Intent intent = new Intent(HomeMoreActivity.this, HomeMoreTranslateActivity.class);
            startActivity(intent);
        });
        amm_tv_zhiHuDailyView.setOnClickListener(view -> {
            Intent intent=new Intent(HomeMoreActivity.this, HomeMoreZhiHuDailyActivity.class);
            startActivity(intent);
        });
    }

}
