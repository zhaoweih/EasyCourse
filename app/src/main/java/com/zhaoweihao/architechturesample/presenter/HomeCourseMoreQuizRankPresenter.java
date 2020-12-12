package com.zhaoweihao.architechturesample.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhaoweihao.architechturesample.bean.OnStringListener;
import com.zhaoweihao.architechturesample.bean.StringModelImpl;
import com.zhaoweihao.architechturesample.bean.quiz.Query;
import com.zhaoweihao.architechturesample.contract.HomeCourseMoreQuizRankContract;
import com.zhaoweihao.architechturesample.util.Constant;


import java.util.ArrayList;
import java.util.List;

public class HomeCourseMoreQuizRankPresenter implements HomeCourseMoreQuizRankContract.Presenter, OnStringListener {

    public static final String TAG = "HomeCourseSearchQueryCoursePresenter";

    private HomeCourseMoreQuizRankContract.View view;
    private Context context;
    private StringModelImpl model;
    private ArrayList<Query> queryList = new ArrayList<>();

    public HomeCourseMoreQuizRankPresenter(Context context, HomeCourseMoreQuizRankContract.View view) {
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
        String suffix = Constant.QUERY_QUIZ_RANK_BY_COURSE_ID_URL + courseId;

        model.sentGetRequestInSMI(suffix, this);
    }
}
