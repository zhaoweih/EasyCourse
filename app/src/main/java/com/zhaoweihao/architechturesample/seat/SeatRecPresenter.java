package com.zhaoweihao.architechturesample.seat;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhaoweihao.architechturesample.data.OnStringListener;
import com.zhaoweihao.architechturesample.data.StringModelImpl;
import com.zhaoweihao.architechturesample.data.course.Query;
import com.zhaoweihao.architechturesample.data.seat.Create;
import com.zhaoweihao.architechturesample.data.seat.Record;

import java.util.ArrayList;
import java.util.List;

public class SeatRecPresenter implements SeatRecContract.Presenter, OnStringListener {

    private SeatRecContract.View view;
    private Context context;
    private StringModelImpl model;
    private ArrayList<Record> recordList = new ArrayList<>();

    public SeatRecPresenter(Context context, SeatRecContract.View view) {
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
        String suffix = "seat/record/query?classCode=" + classCode;

        model.sentGetRequestInSMI(suffix, this);
    }
}
