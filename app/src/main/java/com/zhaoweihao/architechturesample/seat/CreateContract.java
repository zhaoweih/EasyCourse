package com.zhaoweihao.architechturesample.seat;

import com.zhaoweihao.architechturesample.BasePresenter;
import com.zhaoweihao.architechturesample.BaseView;
import com.zhaoweihao.architechturesample.data.course.Submit;
import com.zhaoweihao.architechturesample.data.seat.Create;

public interface CreateContract {

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
