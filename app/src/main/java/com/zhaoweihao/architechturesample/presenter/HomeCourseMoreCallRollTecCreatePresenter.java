package com.zhaoweihao.architechturesample.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.zhaoweihao.architechturesample.bean.OnStringListener;
import com.zhaoweihao.architechturesample.bean.StringModelImpl;
import com.zhaoweihao.architechturesample.bean.seat.Create;
import com.zhaoweihao.architechturesample.contract.HomeCourseMoreCallRollTecCreateContract;
import com.zhaoweihao.architechturesample.util.Constant;

public class HomeCourseMoreCallRollTecCreatePresenter implements HomeCourseMoreCallRollTecCreateContract.Presenter, OnStringListener {

    private HomeCourseMoreCallRollTecCreateContract.View view;
    private Context context;
    private StringModelImpl model;

    public HomeCourseMoreCallRollTecCreatePresenter(Context context, HomeCourseMoreCallRollTecCreateContract.View view) {
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

        view.showResult(true);
        view.stopLoading();
    }

    @Override
    public void onError(String error) {

        view.showResult(false);
        view.stopLoading();
    }

    @Override
    public void create(Create create) {
        String suffix = Constant.CREATE_SEAT_URL;
        String json = new Gson().toJson(create);

        view.startLoading();
        model.sentPostRequestInSMI(suffix, json, this);
    }
}
