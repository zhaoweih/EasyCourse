package com.zhaoweihao.architechturesample.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.base.BaseActivity;
import com.zhaoweihao.architechturesample.customtabs.CustomTabsHelper;
import com.zhaoweihao.architechturesample.ui.TitleLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

import static org.litepal.LitePalApplication.getContext;
/**
*@description 个人-设置-关于界面
*@author
*@time 2019/1/28 13:19
*/
public class PersonalSettingAboutActivity extends BaseActivity {

    @BindView(R.id.apsa_title)
    TitleLayout apsa_title;
    @BindView(R.id.fl_about)
    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_setting_about);

        ButterKnife.bind(this);

        apsa_title.setTitle("关于轻课");

       /* getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/

        String description = "轻课 \n 介绍：试图减少教师和学生的沟通障碍\n Android Client:赵威豪&谭新奎 \n Back-End:赵威豪 \n Made in Chaozhou \n Hanshan Normal University";

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setDescription(description)
                .addItem(new Element().setTitle("Version 1.0"))
                .addGroup("开源")
                .addItem(new Element().setIconDrawable(R.drawable.new_open).setTitle("开源协议").setOnClickListener(v -> startActivity(new Intent(this, PersonalSettingAboutLicenseActivity.class))))
                .addItem(new Element().setIconDrawable(R.drawable.github).setTitle("源代码").setOnClickListener(v -> CustomTabsHelper.openUrl(getContext(), getString(R.string.source_code_desc))))
                .addGroup("与我们联系")
                .addEmail("zhaoweihao.dev@gmail.com")
                .create();

        frameLayout.addView(aboutPage);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
