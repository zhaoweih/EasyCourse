package com.zhaoweihao.architechturesample.course;

import com.zhaoweihao.architechturesample.BasePresenter;
import com.zhaoweihao.architechturesample.BaseView;
import com.zhaoweihao.architechturesample.data.course.QueryComment;

import java.util.ArrayList;

public interface QueryCommentContract {
    interface View extends BaseView< QueryCommentContract.Presenter> {
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
