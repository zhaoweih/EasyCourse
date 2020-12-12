package com.zhaoweihao.architechturesample.contract;

import com.zhaoweihao.architechturesample.base.BasePresenter;
import com.zhaoweihao.architechturesample.base.BaseView;
import com.zhaoweihao.architechturesample.bean.course.Query;
import com.zhaoweihao.architechturesample.bean.course.QuerySelect;

import java.util.ArrayList;

public interface MessageInboxContract {
    interface View extends BaseView< MessageInboxContract.Presenter> {
        void showResult(ArrayList<QuerySelect> queryArrayList);

        void showTeacherResult(ArrayList<Query> queryArrayList);

        void startLoading();

        void stopLoading();

        void showLoadError(String error);

        void showSelectSuccess(Boolean status);

        void onDeleteCourseSuccess();

    }

    interface  Presenter extends BasePresenter {
        void querySelect();

        ArrayList<QuerySelect> getQueryList();

        ArrayList<Query> getQueryTeacherList();

        Boolean checkIsStu();

        void deleteCourse(int courseId);
    }
}
