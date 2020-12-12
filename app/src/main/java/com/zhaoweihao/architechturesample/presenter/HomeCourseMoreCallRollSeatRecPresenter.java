package com.zhaoweihao.architechturesample.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhaoweihao.architechturesample.bean.OnStringListener;
import com.zhaoweihao.architechturesample.bean.StringModelImpl;
import com.zhaoweihao.architechturesample.bean.seat.Record;
import com.zhaoweihao.architechturesample.contract.HomeCourseMoreCallRollSeatRecContract;
import com.zhaoweihao.architechturesample.util.Constant;

import java.util.ArrayList;
import java.util.List;

public class HomeCourseMoreCallRollSeatRecPresenter implements HomeCourseMoreCallRollSeatRecContract.Presenter, OnStringListener {

    private HomeCourseMoreCallRollSeatRecContract.View view;
    private Context context;
    private StringModelImpl model;
    private ArrayList<Record> recordList = new ArrayList<>();

    public HomeCourseMoreCallRollSeatRecPresenter(Context context, HomeCourseMoreCallRollSeatRecContract.View view) {
        this.context = context;
        this.view = view;
        this.view.setPresenter(this);
        model = new StringModelImpl(context);
    }

    @Override
    public void start() {

    }

    @Override
    public void onSuccess(String payload) {
        if (payload == null)
            return;
        try {
            recordList.clear();
            recordList.addAll(new Gson().fromJson(payload, new TypeToken<List<Record>>() {
            }.getType()));
            view.showResult(recordList);
            view.stopLoading();
        } catch (Exception e) {
            view.showLoadError(e.toString());
            view.stopLoading();
        }
    }

    @Override
    public void onError(String error) {
        view.showLoadError(error);
        view.stopLoading();
    }


    @Override
    public void query(String classCode) {
        String suffix = Constant.QUERY_SEAT_RECORD_BY_CLASS_CODE_URL + classCode;

        model.sentGetRequestInSMI(suffix, this);
    }
}
