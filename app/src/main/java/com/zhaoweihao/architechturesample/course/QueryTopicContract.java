package com.zhaoweihao.architechturesample.course;

import com.zhaoweihao.architechturesample.BasePresenter;
import com.zhaoweihao.architechturesample.BaseView;
import com.zhaoweihao.architechturesample.data.course.QueryTopic;

import java.util.ArrayList;

public interface QueryTopicContract {
    interface View extends BaseView< QueryTopicContract.Presenter> {
        void showResult(ArrayList<QueryTopic> queryArrayList);

        void startLoading();

        void stopLoading();

        void showLoadError(String error);
        void showConfirmSuccess(Boolean status);

    }

    interface  Presenter extends BasePresenter {
        void queryTopic(String url);

        ArrayList<QueryTopic> getQueryList();

        Boolean checkTecOrStu();
    }
}
