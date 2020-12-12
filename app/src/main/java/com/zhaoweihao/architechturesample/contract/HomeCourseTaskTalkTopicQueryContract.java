package com.zhaoweihao.architechturesample.contract;

import com.zhaoweihao.architechturesample.base.BasePresenter;
import com.zhaoweihao.architechturesample.base.BaseView;
import com.zhaoweihao.architechturesample.bean.course.QueryTopic;

import java.util.ArrayList;

public interface HomeCourseTaskTalkTopicQueryContract {
    interface View extends BaseView< HomeCourseTaskTalkTopicQueryContract.Presenter> {
        void showResult(ArrayList<QueryTopic> queryArrayList);

        void startLoading();

        void stopLoading();

        void showLoadError(String error);
        void showConfirmSuccess(Boolean status);

    }

    interface  Presenter extends BasePresenter {
        void queryTopic(String url);
        void deleteTopic(String url1,String json,String urlRefresh);
        ArrayList<QueryTopic> getQueryList();

        Boolean checkTecOrStu();
    }
}
