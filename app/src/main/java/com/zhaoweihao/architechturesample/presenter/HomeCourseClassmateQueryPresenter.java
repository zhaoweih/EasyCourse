package com.zhaoweihao.architechturesample.presenter;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhaoweihao.architechturesample.bean.OnStringListener;
import com.zhaoweihao.architechturesample.bean.StringModelImpl;
import com.zhaoweihao.architechturesample.bean.course.QueryClassmate;
import com.zhaoweihao.architechturesample.bean.course.QuerySelect;
import com.zhaoweihao.architechturesample.bean.quiz.Add;
import com.zhaoweihao.architechturesample.contract.HomeCourseClassmateQueryContract;
import com.zhaoweihao.architechturesample.database.User;
import com.zhaoweihao.architechturesample.util.Constant;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class HomeCourseClassmateQueryPresenter implements HomeCourseClassmateQueryContract.Presenter, OnStringListener {
    public static final String TAG = "QuerySelectPresenter";

    private HomeCourseClassmateQueryContract.View view;
    private Context context;
    private StringModelImpl model;
    private ArrayList<QueryClassmate> queryList = new ArrayList<>();

    public HomeCourseClassmateQueryPresenter(Context context, HomeCourseClassmateQueryContract.View view) {
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
            view.showConfirmSuccess(true);
            view.stopLoading();
            return;
        }
        try {
            queryList.clear();
           /* List<QueryClassmate> temp = JSON.parseArray(payload, QueryClassmate.class);
            queryList.addAll(temp);*/
            queryList.addAll(new Gson().fromJson(payload, new TypeToken<List<QueryClassmate>>() {
            }.getType()));
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
    public void querySelect(String url) {
        view.startLoading();
        model.sentGetRequestInSMI(url, this);
    }

    @Override
    public ArrayList<QueryClassmate> getQueryList() {
        return queryList;
    }


    @Override
    public void confirmRecord(QueryClassmate querySelect) {
        Add add = new Add();
        add.setCourseId(querySelect.getCourseId());
        add.setStudentId(querySelect.getStudentId());

        User user = DataSupport.findLast(User.class);

        if (user == null) {
            view.showLoadError("请先登录");
            return;
        }

        if (user.getTeacherId() == null) {
            view.showLoadError("你不是老师，不能执行此操作");
            return;
        }

        add.setTeacherId(user.getTeacherId());

        String json = new Gson().toJson(add);

        String suffix = Constant.ADD_QUIZ_URL;

        model.sentPostRequestInSMI(suffix, json, this);

    }
}
