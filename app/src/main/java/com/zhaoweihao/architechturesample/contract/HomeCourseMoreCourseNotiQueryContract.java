package com.zhaoweihao.architechturesample.contract;

import com.zhaoweihao.architechturesample.base.BasePresenter;
import com.zhaoweihao.architechturesample.base.BaseView;

import java.util.ArrayList;

public interface HomeCourseMoreCourseNotiQueryContract {
    interface View extends BaseView< HomeCourseMoreCourseNotiQueryContract.Presenter> {
        void showResult(ArrayList<com.zhaoweihao.architechturesample.bean.course.SendNoti> queryArrayList);

        void startLoading();

        void stopLoading();

        void showLoadError(String error);

        void showSelectSuccess(Boolean status);

    }

    interface  Presenter extends BasePresenter {
        void querySelect(String url);

        ArrayList<com.zhaoweihao.architechturesample.bean.course.SendNoti> getQueryList();

        Boolean checkTecOrStu();
    }
}
