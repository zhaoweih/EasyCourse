package com.zhaoweihao.architechturesample.timeline;

import android.support.annotation.NonNull;

import com.zhaoweihao.architechturesample.BasePresenter;
import com.zhaoweihao.architechturesample.BaseView;
import com.zhaoweihao.architechturesample.data.ZhihuDailyNewsQuestion;

import java.util.ArrayList;
import java.util.List;

public interface ZhihuDailyContract {

    interface View extends BaseView<Presenter> {

        boolean isActive();

        void setLoadingIndicator(boolean active);

        void showResult(@NonNull List<ZhihuDailyNewsQuestion> list);
    }

    interface Presenter extends BasePresenter {

        void loadNews(boolean forceUpdate, boolean clearCache, long date);

    }
}
