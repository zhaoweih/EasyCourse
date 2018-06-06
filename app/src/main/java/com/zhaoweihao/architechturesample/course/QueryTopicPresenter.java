package com.zhaoweihao.architechturesample.course;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhaoweihao.architechturesample.data.OnStringListener;
import com.zhaoweihao.architechturesample.data.StringModelImpl;
import com.zhaoweihao.architechturesample.data.course.QueryTopic;
import com.zhaoweihao.architechturesample.database.User;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import static com.zhaoweihao.architechturesample.util.Utils.log;

public class QueryTopicPresenter implements QueryTopicContract.Presenter, OnStringListener {
    public static final String TAG = "QueryTopicPresenter";

    private QueryTopicContract.View view;
    private Context context;
    private StringModelImpl model;
    private ArrayList<QueryTopic> queryList = new ArrayList<>();

    public QueryTopicPresenter(Context context, QueryTopicContract.View view) {
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

        Log.e(TAG, payload);
        if (payload == null) {
            view.showConfirmSuccess(true);
            view.stopLoading();
            return;
        }
        try {
            List<QueryTopic> queryTopics = new Gson().fromJson(payload, new TypeToken<List<QueryTopic>>() {
            }.getType());
            queryList.clear();
            queryList.addAll(new Gson().fromJson(payload, new TypeToken<List<QueryTopic>>() {
            }.getType()));
            view.showResult(queryList);
            view.stopLoading();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
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
    public void queryTopic(String url) {
        view.startLoading();
        model.sentGetRequestInSMI(url, this);
    }

    @Override
    public ArrayList<QueryTopic> getQueryList() {
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
