package com.zhaoweihao.architechturesample.timeline;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.course.QueryActivity;


public class TimelineFragment extends Fragment {

    private FloatingActionButton mFab;
    private TabLayout mTabLayout;

    private ZhihuDailyFragment mZhihuFragment;
    private DoubanMomentFragment mDoubanFragment;
    private GuokrHandpickFragment mGuokrFragment;

    public TimelineFragment() {

    }

    public static TimelineFragment newInstance() { return new TimelineFragment(); }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            FragmentManager fm = getChildFragmentManager();
            mZhihuFragment = (ZhihuDailyFragment) fm.getFragment(savedInstanceState, ZhihuDailyFragment.class.getSimpleName());
            mDoubanFragment = (DoubanMomentFragment) fm.getFragment(savedInstanceState,DoubanMomentFragment.class.getSimpleName());
            mGuokrFragment = (GuokrHandpickFragment) fm.getFragment(savedInstanceState,GuokrHandpickFragment.class.getSimpleName());

        } else {
            mZhihuFragment = ZhihuDailyFragment.newInstance();
            mDoubanFragment = DoubanMomentFragment.newInstance();
            mGuokrFragment = GuokrHandpickFragment.newInstance();

        }

        new DoubanMomentPresenter(getActivity(), mDoubanFragment);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timeline,container,false);
        initViews(view);

        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 1) {
                    mFab.show();
                } else {
                    mFab.hide();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mFab.setOnClickListener(v -> {
//            if (mTabLayout.getSelectedTabPosition() == 0) {
//                //展示日期选择器
//
//            } else  {
//                //展示日期选择器
//            }
            // 点击浮动按钮进入搜索课程页面
            Intent intent = new Intent(getActivity(), QueryActivity.class);
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        FragmentManager fm = getChildFragmentManager();
        if (mZhihuFragment.isAdded()) {
            fm.putFragment(outState, ZhihuDailyFragment.class.getSimpleName(), mZhihuFragment);
        }
        if (mGuokrFragment.isAdded()) {
            fm.putFragment(outState, DoubanMomentFragment.class.getSimpleName(), mDoubanFragment);
        }
        if (mDoubanFragment.isAdded()) {
            fm.putFragment(outState, GuokrHandpickFragment.class.getSimpleName(), mGuokrFragment);
        }
    }

    private void initViews(View view) {
        ViewPager mViewPager = view.findViewById(R.id.view_pager);
        mViewPager.setAdapter(new TimelineFragmentPagerAdapter(
                getChildFragmentManager(),
                getContext(),
                mZhihuFragment,
                mDoubanFragment,
                mGuokrFragment
        ));
        mViewPager.setOffscreenPageLimit(3);

        mTabLayout = view.findViewById(R.id.tab_layout);
        mTabLayout.setupWithViewPager(mViewPager);
        mFab = view.findViewById(R.id.fab);
    }
}
