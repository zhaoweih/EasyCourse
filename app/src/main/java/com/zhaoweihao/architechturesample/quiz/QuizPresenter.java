package com.zhaoweihao.architechturesample.quiz;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhaoweihao.architechturesample.data.OnStringListener;
import com.zhaoweihao.architechturesample.data.StringModelImpl;
import com.zhaoweihao.architechturesample.data.quiz.Query;


import java.util.ArrayList;
import java.util.List;

public class QuizPresenter implements QuizContract.Presenter, OnStringListener {

    public static final String TAG = "QueryPresenter";

    private QuizContract.View view;
    private Context context;
    private StringModelImpl model;
    private ArrayList<Query> queryList = new ArrayList<>();

    public QuizPresenter(Context context, QuizContract.View view) {
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
        if (payload == null) {
            return;
        }
        try {
            queryList.clear();
            queryList.addAll(new Gson().fromJson(payload, new TypeToken<List<Query>>() {
            }.getType()));
            if (queryList.isEmpty())
                view.showNothing();
            else
                view.showResult(queryList);
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
    public ArrayList<Query> getQueryList() {
        return queryList;
    }


    @Override
    public void queryQuizList(String courseId) {
        view.startLoading();
        String suffix = "quiz/query?courseId=" + courseId;

        model.sentGetRequestInSMI(suffix, this);
    }
}
