package com.zhaoweihao.architechturesample.contract;

import com.zhaoweihao.architechturesample.base.BasePresenter;
import com.zhaoweihao.architechturesample.base.BaseView;
import com.zhaoweihao.architechturesample.bean.course.QueryComment;

import java.util.ArrayList;

public interface HomeCourseTaskTalkTopicCommentContract {
    interface View extends BaseView< HomeCourseTaskTalkTopicCommentContract.Presenter> {
        void showResult(ArrayList<QueryComment> queryArrayList);

        void startLoading();

        void stopLoading();

        void showLoadError(String error);
        void showConfirmSuccess(Boolean status);

    }

    interface  Presenter extends BasePresenter {
        void QueryComment(String url);
        void sendComment(String url, String json, String urlRefresh);
        ArrayList<QueryComment> getQueryList();

        Boolean checkTecOrStu();
    }
}
