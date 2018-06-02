package com.zhaoweihao.architechturesample.course;

import com.zhaoweihao.architechturesample.BasePresenter;
import com.zhaoweihao.architechturesample.BaseView;
import com.zhaoweihao.architechturesample.data.course.QuerySelect;

import java.util.ArrayList;

public interface QueryNotiContract {
    interface View extends BaseView< QueryNotiContract.Presenter> {
        void showResult(ArrayList<com.zhaoweihao.architechturesample.data.course.SendNoti> queryArrayList);

        void startLoading();

        void stopLoading();

        void showLoadError(String error);

        void showSelectSuccess(Boolean status);

    }

    interface  Presenter extends BasePresenter {
        void querySelect(String url);

        ArrayList<com.zhaoweihao.architechturesample.data.course.SendNoti> getQueryList();

        Boolean checkTecOrStu();
    }
}
