package com.zhaoweihao.architechturesample.contract;

import com.zhaoweihao.architechturesample.base.BasePresenter;
import com.zhaoweihao.architechturesample.base.BaseView;
import com.zhaoweihao.architechturesample.bean.Detail;


/**
 * Created by por on 2018/4/5.
 */

public interface HomeMoreZhiHuDailyDetailContract {

    interface View extends BaseView<Presenter> {

        void showResult(Detail detail);

        void startLoading();

        void stopLoading();

        void showLoadError();

        String getArticleId();
    }

    interface Presenter extends BasePresenter {

        void loadArticle(Boolean forceRefresh);

    }
}
