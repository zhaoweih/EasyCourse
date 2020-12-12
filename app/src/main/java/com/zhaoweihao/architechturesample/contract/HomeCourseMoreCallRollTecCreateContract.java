package com.zhaoweihao.architechturesample.contract;

import com.zhaoweihao.architechturesample.base.BasePresenter;
import com.zhaoweihao.architechturesample.base.BaseView;
import com.zhaoweihao.architechturesample.bean.seat.Create;

public interface HomeCourseMoreCallRollTecCreateContract {

    interface View extends BaseView<Presenter> {
        void showResult(Boolean status);

        void startLoading();

        void stopLoading();

        void showLoadError();
    }

    interface  Presenter extends BasePresenter {
        void create(Create create);
    }
}
