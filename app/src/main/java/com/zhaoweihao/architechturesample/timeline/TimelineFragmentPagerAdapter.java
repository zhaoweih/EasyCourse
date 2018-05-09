package com.zhaoweihao.architechturesample.timeline;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.zhaoweihao.architechturesample.R;

/**
 *
 * {@link FragmentPagerAdapter} of {@link TimelineFragment}
 */

public class TimelineFragmentPagerAdapter extends FragmentPagerAdapter {

    private final int pageCount = 3;
    private String[] titles;

    private ZhihuDailyFragment mZhihuFragment;
    private DoubanMomentFragment mDoubanFragment;
    private GuokrHandpickFragment mGuokrFragment;

    public TimelineFragmentPagerAdapter(@NonNull FragmentManager fm,
                                        @NonNull Context context,
                                        @NonNull ZhihuDailyFragment zhihuDailyFragment,
                                        @NonNull DoubanMomentFragment doubanMomentFragment,
                                        @NonNull GuokrHandpickFragment guokrFragment) {
        super(fm);
        titles = new String[]{
                context.getString(R.string.zhihu_daily),
                context.getString(R.string.douban_moment),
                context.getString(R.string.guokr_handpick)};
        this.mZhihuFragment = zhihuDailyFragment;
        this.mDoubanFragment = doubanMomentFragment;
        this.mGuokrFragment = guokrFragment;
    }

    @Override
    public Fragment getItem(int i) {
        if (i == 0) {
            return mZhihuFragment;
        } else if (i == 1) {
            return mDoubanFragment;
        }
        return  mGuokrFragment;
    }

    @Override
    public int getCount() {
        return pageCount;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
