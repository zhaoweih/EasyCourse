package com.zhaoweihao.architechturesample.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.customtabs.CustomTabsHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

import static org.litepal.LitePalApplication.getContext;

public class AboutActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fl_about)
    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        String description = "轻课 \n 介绍：试图减少教师和学生的沟通障碍\n Android Client:赵威豪&谭新奎 \n Back-End:赵威豪 \n Made in Chaozhou \n Hanshan Normal University";

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setDescription(description)
                .addItem(new Element().setTitle("Version 1.0"))
                .addGroup("开源")
                .addItem(new Element().setIconDrawable(R.drawable.new_open).setTitle("开源协议").setOnClickListener(v -> startActivity(new Intent(this, LicenseActivity.class))))
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
