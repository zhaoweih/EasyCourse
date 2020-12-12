package com.zhaoweihao.architechturesample.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.zhaoweihao.architechturesample.bean.OnStringListener;
import com.zhaoweihao.architechturesample.bean.StringModelImpl;
import com.zhaoweihao.architechturesample.bean.course.Submit;
import com.zhaoweihao.architechturesample.contract.HomeCourseSubmitCourseContract;
import com.zhaoweihao.architechturesample.util.Constant;

public class HomeCourseSubmitCoursePresenter implements HomeCourseSubmitCourseContract.Presenter, OnStringListener {

    private HomeCourseSubmitCourseContract.View view;
    private Context context;
    private StringModelImpl model;

    public HomeCourseSubmitCoursePresenter(Context context, HomeCourseSubmitCourseContract.View view) {
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
        String suffix = Constant.SUBMIT_COURSE_URL;
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
