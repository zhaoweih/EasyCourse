package com.zhaoweihao.architechturesample.contract;

import com.zhaoweihao.architechturesample.base.BasePresenter;
import com.zhaoweihao.architechturesample.base.BaseView;
import com.zhaoweihao.architechturesample.bean.seat.Record;

import java.util.ArrayList;

public interface HomeCourseMoreCallRollSeatRecContract {

    interface View extends BaseView<Presenter> {
        void showResult(ArrayList<Record> recordArrayList);

        void startLoading();

        void stopLoading();

        void showLoadError(String error);
    }

    interface  Presenter extends BasePresenter {
        void query(String classCode);
    }
}
