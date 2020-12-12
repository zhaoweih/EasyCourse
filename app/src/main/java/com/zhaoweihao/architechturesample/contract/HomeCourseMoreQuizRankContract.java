package com.zhaoweihao.architechturesample.contract;

import com.zhaoweihao.architechturesample.base.BasePresenter;
import com.zhaoweihao.architechturesample.base.BaseView;
import com.zhaoweihao.architechturesample.bean.quiz.Query;

import java.util.ArrayList;

public interface HomeCourseMoreQuizRankContract {

    interface View extends BaseView<Presenter> {
        void showResult(ArrayList<Query> queryArrayList);

        void startLoading();

        void stopLoading();

        void showLoadError(String error);

        void showNothing();
    }

    interface  Presenter extends BasePresenter {

        ArrayList<Query> getQueryList();

        void queryQuizList(String courseId);
    }
}
