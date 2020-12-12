package com.zhaoweihao.architechturesample.contract;

import com.zhaoweihao.architechturesample.base.BasePresenter;
import com.zhaoweihao.architechturesample.base.BaseView;
import com.zhaoweihao.architechturesample.bean.course.QueryClassmate;
import com.zhaoweihao.architechturesample.bean.course.QuerySelect;

import java.util.ArrayList;

public interface HomeCourseClassmateQueryContract {
    interface View extends BaseView< HomeCourseClassmateQueryContract.Presenter> {
        void showResult(ArrayList<QueryClassmate> queryArrayList);

        void startLoading();

        void stopLoading();

        void showLoadError(String error);

        void showConfirmSuccess(Boolean status);

    }

    interface  Presenter extends BasePresenter {
        void querySelect(String url);

        ArrayList<QueryClassmate> getQueryList();

        void confirmRecord(QueryClassmate queryClassmate);
    }
}
