package com.zhaoweihao.architechturesample.seat;

import android.content.Context;

import com.google.gson.Gson;
import com.zhaoweihao.architechturesample.course.SubmitContract;
import com.zhaoweihao.architechturesample.data.OnStringListener;
import com.zhaoweihao.architechturesample.data.StringModelImpl;
import com.zhaoweihao.architechturesample.data.course.Submit;
import com.zhaoweihao.architechturesample.data.seat.Create;

public class CreatePresenter implements CreateContract.Presenter, OnStringListener {

    private CreateContract.View view;
    private Context context;
    private StringModelImpl model;

    public CreatePresenter(Context context, CreateContract.View view) {
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
        String suffix = "seat/create";
        String json = new Gson().toJson(create);

        view.startLoading();
        model.sentPostRequestInSMI(suffix, json, this);
    }
}
