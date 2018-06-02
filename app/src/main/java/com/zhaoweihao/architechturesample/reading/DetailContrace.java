package com.zhaoweihao.architechturesample.reading;

import com.zhaoweihao.architechturesample.BasePresenter;
import com.zhaoweihao.architechturesample.BaseView;
import com.zhaoweihao.architechturesample.data.Detail;


/**
 * Created by por on 2018/4/5.
 */

public interface DetailContrace {

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
