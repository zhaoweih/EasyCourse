package com.zhaoweihao.architechturesample.activity;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.FrameLayout;


import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.base.BaseActivity;
import com.zhaoweihao.architechturesample.ui.TitleLayout;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 *@description 个人-设置(个人页面的设置页面，包括账号管理、修改密码、建议反馈、关于)
 *@author tanxinkui
 *@time 2019/1/9 19:26
 */

public class PersonalSettingActivity extends BaseActivity {
    @BindView(R.id.aps_title)
    TitleLayout aps_title;
    @BindView(R.id.aps_fl_count_management)
    FrameLayout aps_fl_count_management;
    @BindView(R.id.aps_fl_modify_password)
    FrameLayout aps_fl_modify_password;
    @BindView(R.id.aps_fl_about_lightCourse)
    FrameLayout aps_fl_about_lightCourse;
    @BindView(R.id.aps_fl_feedback)
    FrameLayout aps_fl_feedback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_setting);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        aps_title.setTitle("设置");
        aps_fl_count_management.setOnClickListener(view -> goNextActivity("com.zhaoweihao.architechturesample.activity.PersonalSettingCountManagementActivity"));
        aps_fl_modify_password.setOnClickListener(view -> goNextActivity("com.zhaoweihao.architechturesample.activity.PersonalSettingModifyPasswordActivity"));
        aps_fl_about_lightCourse.setOnClickListener(view ->goNextActivity("com.zhaoweihao.architechturesample.activity.PersonalSettingAboutActivity"));
        aps_fl_feedback.setOnClickListener(view -> feedBack());

    }

    private void feedBack() {
        Intent data = new Intent(Intent.ACTION_SENDTO);
        data.setData(Uri.parse("mailto:tanxinkui@qq.com"));
        data.putExtra(Intent.EXTRA_SUBJECT, "意见反馈");
        startActivity(data);
    }

    private void goNextActivity(String className) {
            Intent intent = null;
            try {
                intent = new Intent(PersonalSettingActivity.this, Class.forName(className));
                startActivity(intent);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
    }

}
