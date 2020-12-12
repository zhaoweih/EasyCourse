package com.zhaoweihao.architechturesample.contract;

import com.zhaoweihao.architechturesample.base.BasePresenter;
import com.zhaoweihao.architechturesample.base.BaseView;
import com.zhaoweihao.architechturesample.bean.course.Submit;

public interface HomeCourseSubmitCourseContract {

    interface View extends BaseView<Presenter> {
        void showResult(Boolean status);

        void startLoading();

        void stopLoading();

        void showLoadError();
    }

    interface  Presenter extends BasePresenter {
        void submit(Submit submit);
    }
}
