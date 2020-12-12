package com.zhaoweihao.architechturesample.contract;

import com.zhaoweihao.architechturesample.base.BaseActivityView;
import com.zhaoweihao.architechturesample.base.BasePresenter;
import com.zhaoweihao.architechturesample.bean.AddAndShowActivity;


public interface HomeActivityShowContract {
    interface View extends BaseActivityView<Presenter> {
        void showResult(Boolean status);

        void startLoading();

        void stopLoading();

        void showLoadError();
    }

    interface  Presenter extends BasePresenter {
        void submit(AddAndShowActivity addAndShowActivity);
    }
}
