package com.zhaoweihao.architechturesample.reading;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.zhaoweihao.architechturesample.R;

/**
 * Created by por on 2018/4/5.
 */

public class DetailActivity extends AppCompatActivity {

    private DetailFragment detailFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        detailFragment = DetailFragment.newInstance();

        new DetailPresenter(this,detailFragment);

        getSupportFragmentManager().beginTransaction().replace(R.id.main_container,detailFragment).commit();
    }
}
