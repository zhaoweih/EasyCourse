package com.zhaoweihao.architechturesample.seat;

import com.zhaoweihao.architechturesample.BasePresenter;
import com.zhaoweihao.architechturesample.BaseView;
import com.zhaoweihao.architechturesample.data.seat.Create;
import com.zhaoweihao.architechturesample.data.seat.Record;

import java.util.ArrayList;

public interface SeatRecContract {

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
