package com.zhaoweihao.architechturesample.reading;

import com.zhaoweihao.architechturesample.BasePresenter;
import com.zhaoweihao.architechturesample.BaseView;
import com.zhaoweihao.architechturesample.data.ZhihuDaily;


import java.util.ArrayList;

/**
 * Created by zhao weihao on 2018/4/5.
 */

public interface ZhihuDailyContract {

    interface View extends BaseView<Presenter> {

        void showResult(ArrayList<ZhihuDaily.Story> articleList);

        void startLoading();

        void stopLoading();

        void showLoadError();

    }

    interface Presenter extends BasePresenter {

        void requestArticles(Boolean forceRefresh);

        ArrayList<ZhihuDaily.Story> getArticles();

        void copyToClipboard(int position);

    }
}
