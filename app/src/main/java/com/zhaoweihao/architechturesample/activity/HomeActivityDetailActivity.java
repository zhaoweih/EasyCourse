package com.zhaoweihao.architechturesample.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.TimeUtils;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.base.BaseActivity;
import com.zhaoweihao.architechturesample.bean.AddAndShowActivity;
import com.zhaoweihao.architechturesample.bean.Leave;
import com.zhaoweihao.architechturesample.ui.NetWorkImageView;
import com.zhaoweihao.architechturesample.ui.TitleLayout;
import com.zhaoweihao.architechturesample.util.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
*@description 首页-活动-活动详情
*@author
*@time 2019/2/24 23:23
*/
public class HomeActivityDetailActivity extends BaseActivity {
    private AddAndShowActivity addAndShowActivity;
    @BindView(R.id.ahc_title_layout)
    TitleLayout ahc_title_layout;
    @BindView(R.id.ahc_iv_activity_image)
    NetWorkImageView ahc_iv_activity_image;
    @BindView(R.id.ahc_activity_duration)
    TextView ahc_activity_duration;
    @BindView(R.id.ahc_activity_content)
    TextView ahc_activity_content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_detail);
        ButterKnife.bind(this);
        Intent intent = getIntent();
//        Bundle bundle = intent.getExtras();
        addAndShowActivity = (AddAndShowActivity) intent.getSerializableExtra("activityDetail");
        init();
    }
    private void init(){
        ahc_title_layout.setTitle(addAndShowActivity.getTitle());
        ahc_activity_duration.setText("活动时间："+TimeUtils.millis2String(addAndShowActivity.getStart_time()).substring(0,10)+" ~ "+TimeUtils.millis2String(addAndShowActivity.getEnd_time()).substring(0,10));
        ahc_iv_activity_image.setImageURL(Constant.getBaseUrl() + "upload/" +addAndShowActivity.getImg_url());
        ahc_activity_content.setText(addAndShowActivity.getContent());
    }

}
