package com.zhaoweihao.architechturesample.activity;

import android.os.Bundle;

import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.base.BaseActivity;
import com.zhaoweihao.architechturesample.fragment.HomeMoreZhiHuDailyFragment;
import com.zhaoweihao.architechturesample.presenter.HomeMoreZhiHuDailyPresenter;
import com.zhaoweihao.architechturesample.ui.TitleLayout;

/**
*@description 首页-更多-知乎日报
*@author
*@time 2019/1/28 13:08
*/
public class HomeMoreZhiHuDailyActivity extends BaseActivity {

    private HomeMoreZhiHuDailyFragment zhihuDailyFragment;
    private TitleLayout azd_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_more_zhihu_daily);
        init();

        zhihuDailyFragment = HomeMoreZhiHuDailyFragment.newInstance();

        new HomeMoreZhiHuDailyPresenter(this,zhihuDailyFragment);

        getSupportFragmentManager().beginTransaction().replace(R.id.main_container,zhihuDailyFragment).commit();
    }
    protected void init(){
        azd_title=(TitleLayout)findViewById(R.id.azd_title);
        azd_title.setTitle("知乎日报");
    }
}
