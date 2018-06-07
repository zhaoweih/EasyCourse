package com.zhaoweihao.architechturesample.course;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhaoweihao.architechturesample.data.OnStringListener;
import com.zhaoweihao.architechturesample.data.StringModelImpl;
import com.zhaoweihao.architechturesample.data.course.Query;
import com.zhaoweihao.architechturesample.data.course.QuerySelect;
import com.zhaoweihao.architechturesample.data.course.Select;
import com.zhaoweihao.architechturesample.data.quiz.Add;
import com.zhaoweihao.architechturesample.database.User;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class QuerySelectCoursePresenter implements QuerySelectCourseContract.Presenter, OnStringListener {
    public static final String TAG = "QuerySelectPresenter";

    private QuerySelectCourseContract.View view;
    private Context context;
    private StringModelImpl model;
    private ArrayList<QuerySelect> queryList = new ArrayList<>();

    public QuerySelectCoursePresenter(Context context, QuerySelectCourseContract.View view) {
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
            queryList.addAll(new Gson().fromJson(payload, new TypeToken<List<QuerySelect>>() {
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
    public ArrayList<QuerySelect> getQueryList() {
        return queryList;
    }


    @Override
    public void confirmRecord(QuerySelect querySelect) {
        Add add = new Add();
        add.setCourseId(querySelect.getCourseId());
        add.setStudentId(querySelect.getStudentId());

        User user = DataSupport.findLast(User.class);

        if (user == null ) {
            view.showLoadError("请先登录");
            return;
        }

        if (user.getTeacherId() == null) {
            view.showLoadError("你不是老师，不能执行此操作");
            return;
        }

        add.setTeacherId(user.getTeacherId());

        String json = new Gson().toJson(add);

        String suffix = "quiz/add";

        model.sentPostRequestInSMI(suffix, json, this);

    }
}
