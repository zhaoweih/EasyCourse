package com.zhaoweihao.architechturesample.course;

import com.zhaoweihao.architechturesample.BasePresenter;
import com.zhaoweihao.architechturesample.BaseView;
import com.zhaoweihao.architechturesample.data.course.QuerySelect;

import java.util.ArrayList;

public interface QuerySelectContract {
    interface View extends BaseView< QuerySelectContract.Presenter> {
        void showResult(ArrayList<QuerySelect> queryArrayList);

        void startLoading();

        void stopLoading();

        void showLoadError(String error);

        void showSelectSuccess(Boolean status);

    }

    interface  Presenter extends BasePresenter {
        void querySelect(String url);

        ArrayList<QuerySelect> getQueryList();

        Boolean checkTecOrStu();
    }
}
