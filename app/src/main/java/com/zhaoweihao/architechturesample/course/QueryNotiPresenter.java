package com.zhaoweihao.architechturesample.course;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhaoweihao.architechturesample.data.OnStringListener;
import com.zhaoweihao.architechturesample.data.StringModelImpl;
import com.zhaoweihao.architechturesample.data.course.SendNoti;
import com.zhaoweihao.architechturesample.database.User;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class QueryNotiPresenter implements QueryNotiContract.Presenter, OnStringListener {
    public static final String TAG = "QueryNotiPresenter";

    private QueryNotiContract.View view;
    private Context context;
    private StringModelImpl model;
    private ArrayList<SendNoti> queryList = new ArrayList<>();

    public QueryNotiPresenter(Context context, QueryNotiContract.View view) {
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
            view.showSelectSuccess(true);
            view.stopLoading();
            return;
        }
        try {
            queryList.clear();
            queryList.addAll(new Gson().fromJson(payload, new TypeToken<List<SendNoti>>() {
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
    public ArrayList<SendNoti> getQueryList() {
        return queryList;
    }

    @Override
    public Boolean checkTecOrStu() {
        User user = DataSupport.findLast(User.class);
        if ( user == null ) {
            return false;
        }

        if ( user.getStudentId() != null) {
            return true;
        }

        if ( user.getTeacherId() != null) {
            return false;
        }

        return false;

    }
}
