package com.zhaoweihao.architechturesample.presenter;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhaoweihao.architechturesample.bean.OnStringListener;
import com.zhaoweihao.architechturesample.bean.RestResponse;
import com.zhaoweihao.architechturesample.bean.StringModelImpl;
import com.zhaoweihao.architechturesample.bean.course.Query;
import com.zhaoweihao.architechturesample.bean.course.QuerySelect;
import com.zhaoweihao.architechturesample.contract.MessageInboxContract;
import com.zhaoweihao.architechturesample.contract.MessageInboxContract;
import com.zhaoweihao.architechturesample.database.User;
import com.zhaoweihao.architechturesample.util.Constant;
import com.zhaoweihao.architechturesample.util.HttpUtil;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MessageInboxPresenter implements MessageInboxContract.Presenter, OnStringListener {
    private Context context;

    private MessageInboxContract.View view;

    private StringModelImpl model;

    private ArrayList<QuerySelect> queryList = new ArrayList<>();

    private ArrayList<Query> queryTeacherList = new ArrayList<>();

    public MessageInboxPresenter(Context context, MessageInboxContract.View view) {
        this.context = context;
        model = new StringModelImpl(context);
        this.view = view;
    }

    @Override
    public void start() {
        querySelect();
    }

    @Override
    public void onSuccess(String payload) {
        if (payload == null) {
            view.showSelectSuccess(true);
            view.stopLoading();
            return;
        }
        if (checkIsStu()) {
            try {
                queryList.clear();
                Log.v("tanxiiii+ddd", payload);

                /*queryList.addAll(new Gson().fromJson(payload, new TypeToken<List<QuerySelect>>() {
                }.getType()));*/
                queryList.addAll(JSON.parseArray(payload, QuerySelect.class));
                view.showResult(queryList);
                view.stopLoading();
            } catch (Exception e) {
                view.showLoadError(e.toString());
                view.stopLoading();
            }
        } else {
            try {
                queryTeacherList.clear();
               /* queryTeacherList.addAll(new Gson().fromJson(payload, new TypeToken<List<Query>>() {
                }.getType()));*/
                queryTeacherList.addAll(JSON.parseArray(payload, Query.class));
                view.showTeacherResult(queryTeacherList);
                view.stopLoading();
            } catch (Exception e) {
                view.showLoadError(e.toString());
                view.stopLoading();
            }
        }

    }

    @Override
    public void onError(String error) {
        view.showLoadError(error);
        view.stopLoading();
    }

    @Override
    public void querySelect() {
        view.startLoading();
        model.sentGetRequestInSMI(getUrl(), this);
    }

    @Override
    public ArrayList<QuerySelect> getQueryList() {
        return queryList;
    }

    @Override
    public ArrayList<Query> getQueryTeacherList() {
        return queryTeacherList;
    }

    @Override
    public Boolean checkIsStu() {
        User user = DataSupport.findLast(User.class);
        Boolean isStudent = true;
        if (user.getTeacherId() != null) {
            isStudent = false;
        }
        return isStudent;
    }

    private String getUrl() {
        User userCurrent = DataSupport.findLast(User.class);
        if (userCurrent.getTeacherId() != null) {
            return Constant.QUERY_COURSE_BY_TEACHER_ID_URL + userCurrent.getTeacherId();
        } else {
            return Constant.QUERY_COURSE_BY_STUDENT_ID_URL + userCurrent.getUserId();
        }
    }

    @Override
    public void deleteCourse(int courseId) {
        String deleteUrl = Constant.DELETE_COURSE_URL + "?course_id=" + courseId;
        HttpUtil.sendGetRequest(deleteUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //网络错误处理
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                RestResponse restResponse = new Gson().fromJson(body, RestResponse.class);
                if (restResponse == null) {
                    return;
                }
                if (restResponse.getCode() == Constant.FAIL_CODE) {

                }

                if (restResponse.getCode() == Constant.SUCCESS_CODE) {
                    view.onDeleteCourseSuccess();
                    querySelect();
                }
            }

        });
    }
}
