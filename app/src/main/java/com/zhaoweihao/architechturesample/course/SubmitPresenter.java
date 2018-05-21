package com.zhaoweihao.architechturesample.course;

import android.content.Context;

import com.google.gson.Gson;
import com.zhaoweihao.architechturesample.data.OnStringListener;
import com.zhaoweihao.architechturesample.data.StringModelImpl;
import com.zhaoweihao.architechturesample.data.course.Submit;

public class SubmitPresenter implements SubmitContract.Presenter, OnStringListener {

    private SubmitContract.View view;
    private Context context;
    private StringModelImpl model;

    public SubmitPresenter(Context context, SubmitContract.View view) {
        this.context = context;
        this.view = view;
        this.view.setPresenter(this);
        model = new StringModelImpl(context);
    }

    @Override
    public void start() {

    }

    @Override
    public void submit(Submit submit) {
        String suffix = "course/submit";
        String json = new Gson().toJson(submit);
        model.sentPostRequestInSMI(suffix, json, this);

    }

    @Override
    public void onSuccess(String payload) {
        view.showResult(true);
    }

    @Override
    public void onError(String error) {
        view.showResult(false);
    }
}
