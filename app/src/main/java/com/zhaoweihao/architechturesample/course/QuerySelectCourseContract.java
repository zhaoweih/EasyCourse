package com.zhaoweihao.architechturesample.course;

import com.zhaoweihao.architechturesample.BasePresenter;
import com.zhaoweihao.architechturesample.BaseView;
import com.zhaoweihao.architechturesample.data.course.QuerySelect;

import java.util.ArrayList;

public interface QuerySelectCourseContract {
    interface View extends BaseView< QuerySelectCourseContract.Presenter> {
        void showResult(ArrayList<QuerySelect> queryArrayList);

        void startLoading();

        void stopLoading();

        void showLoadError(String error);

        void showConfirmSuccess(Boolean status);

    }

    interface  Presenter extends BasePresenter {
        void querySelect(String url);

        ArrayList<QuerySelect> getQueryList();

        void confirmRecord(QuerySelect querySelect);
    }
}
