package com.zhaoweihao.architechturesample.contract;

import com.zhaoweihao.architechturesample.base.BasePresenter;
import com.zhaoweihao.architechturesample.base.BaseView;
import com.zhaoweihao.architechturesample.bean.ZhihuDaily;


import java.util.ArrayList;

/**
 * Created by zhao weihao on 2018/4/5.
 */

public interface HomeMoreZhiHuDailyContract {

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
