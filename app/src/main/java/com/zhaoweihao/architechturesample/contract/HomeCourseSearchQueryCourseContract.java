package com.zhaoweihao.architechturesample.contract;

import com.zhaoweihao.architechturesample.base.BasePresenter;
import com.zhaoweihao.architechturesample.base.BaseView;
import com.zhaoweihao.architechturesample.bean.course.Query;

import java.util.ArrayList;

public interface HomeCourseSearchQueryCourseContract {

    interface View extends BaseView<Presenter> {
        void showResult(ArrayList<Query> queryArrayList);

        void startLoading();

        void stopLoading();

        void showLoadError(String error);

        void showSelectSuccess(Boolean status);

    }

    interface  Presenter extends BasePresenter {
        void query(String url);

        ArrayList<Query> getQueryList();

        Boolean checkTecOrStu();

        void selectCourse(Query query, String password);
    }
}
