package com.zhaoweihao.architechturesample.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import com.qmuiteam.qmui.widget.QMUITabSegment;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.base.BaseActivity;
import com.zhaoweihao.architechturesample.ui.HomeCourseDetailChapterLayout;
import com.zhaoweihao.architechturesample.ui.HomeCourseDetailMoreLayout;
import com.zhaoweihao.architechturesample.ui.HomeCourseDetailTaskLayout;
import com.zhaoweihao.architechturesample.ui.TitleLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author tanxinkui
 * @description 首页-课程-课程详细界面（包含任务，章节，更多）
 * @time 2019/1/20 15:24
 */
public class HomeCourseDetailAtivity extends BaseActivity {

    @BindView(R.id.tabSegment)
    QMUITabSegment mTabSegment;
    @BindView(R.id.contentViewPager)
    ViewPager mContentViewPager;
    @BindView(R.id.ahcd_title)
    TitleLayout ahcd_title;
    private int mcourseId;

    private Context mContext = HomeCourseDetailAtivity.this;
    private List<View> mViewList=new ArrayList<>();
    private PagerAdapter mPagerAdapter;
    private boolean mHasIndicator = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_course_detail_ativity);
        ButterKnife.bind(this);
        Intent intent=getIntent();
        mcourseId=intent.getIntExtra("courseId",0);
        ahcd_title.setTitle(intent.getStringExtra("courseName"));
        initTitle();
        initTabAndPager();
    }

    private void initTitle(){
        Button button=ahcd_title.getSettingButton();
        button.setText("班级");
        button.setOnClickListener(v->{
            Intent intent=new Intent(HomeCourseDetailAtivity.this,HomeCourseClassmateQueryActivity.class);
            intent.putExtra("courseId",mcourseId);
            startActivity(intent);
        });
    }
    private void initTabAndPager() {
        mViewList.add(new HomeCourseDetailTaskLayout(HomeCourseDetailAtivity.this,null,mcourseId));
        Log.v("tanxinkui","coureseId"+mcourseId);
        mViewList.add(new HomeCourseDetailChapterLayout(HomeCourseDetailAtivity.this,null,mcourseId));
        mViewList.add(new HomeCourseDetailMoreLayout(HomeCourseDetailAtivity.this,null,mcourseId));
        mPagerAdapter = new AdapterViewpager(mViewList);
        mContentViewPager.setAdapter(mPagerAdapter);
        mContentViewPager.setCurrentItem(mViewList.indexOf(0), false);
        mTabSegment.addTab(new QMUITabSegment.Tab("任务"));
        mTabSegment.addTab(new QMUITabSegment.Tab("章节"));
        mTabSegment.addTab(new QMUITabSegment.Tab("更多"));
        mTabSegment.setupWithViewPager(mContentViewPager, false);
        mTabSegment.setMode(QMUITabSegment.MODE_FIXED);
        mTabSegment.setHasIndicator(mHasIndicator);
        mTabSegment.addOnTabSelectedListener(new QMUITabSegment.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int index) {
                mTabSegment.hideSignCountView(index);
            }

            @Override
            public void onTabUnselected(int index) {

            }

            @Override
            public void onTabReselected(int index) {
                mTabSegment.hideSignCountView(index);
            }

            @Override
            public void onDoubleTap(int index) {

            }
        });
    }

    public class AdapterViewpager extends PagerAdapter {
        private List<View> mViewList;

        public AdapterViewpager(List<View> mViewList) {
            this.mViewList = mViewList;
        }

        @Override
        public int getCount() {//必须实现
            return mViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {//必须实现
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {//必须实现，实例化
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            container.addView(mViewList.get(position),params);
            return mViewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {//必须实现，销毁
            container.removeView(mViewList.get(position));
        }
    }
}

