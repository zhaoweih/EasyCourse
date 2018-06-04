package com.zhaoweihao.architechturesample.quiz;

import com.zhaoweihao.architechturesample.BasePresenter;
import com.zhaoweihao.architechturesample.BaseView;
import com.zhaoweihao.architechturesample.data.quiz.Query;

import java.util.ArrayList;

public interface QuizContract {

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
