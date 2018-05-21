package com.zhaoweihao.architechturesample.course;

import com.zhaoweihao.architechturesample.BasePresenter;
import com.zhaoweihao.architechturesample.BaseView;
import com.zhaoweihao.architechturesample.data.course.Query;
import com.zhaoweihao.architechturesample.data.course.Submit;

import java.util.ArrayList;

public interface QueryContract {

    interface View extends BaseView<Presenter> {
        void showResult(ArrayList<Query> queryArrayList);

        void startLoading();

        void stopLoading();

        void showLoadError();

    }

    interface  Presenter extends BasePresenter {
        void query(String url);
    }
}
