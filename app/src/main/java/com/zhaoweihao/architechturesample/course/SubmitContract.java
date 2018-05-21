package com.zhaoweihao.architechturesample.course;

import com.zhaoweihao.architechturesample.BasePresenter;
import com.zhaoweihao.architechturesample.BaseView;
import com.zhaoweihao.architechturesample.data.course.Submit;
import com.zhaoweihao.architechturesample.timeline.ZhihuDailyContract;

public interface SubmitContract {

    interface View extends BaseView<Presenter> {
        void showResult(Boolean status);
    }

    interface  Presenter extends BasePresenter {
        void submit(Submit submit);
    }
}
