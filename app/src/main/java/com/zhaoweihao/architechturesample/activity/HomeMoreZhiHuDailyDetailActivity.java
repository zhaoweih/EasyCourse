package com.zhaoweihao.architechturesample.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.base.BaseActivity;
import com.zhaoweihao.architechturesample.fragment.HomeMoreZhiHuDailyDetailFragment;
import com.zhaoweihao.architechturesample.presenter.HomeMoreZhiHuDailyDetailPresenter;

/**
 * Created by por on 2018/4/5.
 */
/**
*@description 首页-更多-知乎-详细界面
*@author
*@time 2019/1/28 14:06
*/
public class HomeMoreZhiHuDailyDetailActivity extends BaseActivity {

    private HomeMoreZhiHuDailyDetailFragment homeMoreZhiHuDailyDetailFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        homeMoreZhiHuDailyDetailFragment = HomeMoreZhiHuDailyDetailFragment.newInstance();

        new HomeMoreZhiHuDailyDetailPresenter(this, homeMoreZhiHuDailyDetailFragment);

        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, homeMoreZhiHuDailyDetailFragment).commit();
    }
}
