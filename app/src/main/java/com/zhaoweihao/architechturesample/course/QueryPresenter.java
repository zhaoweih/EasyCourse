package com.zhaoweihao.architechturesample.course;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhaoweihao.architechturesample.data.Leave;
import com.zhaoweihao.architechturesample.data.OnStringListener;
import com.zhaoweihao.architechturesample.data.StringModelImpl;
import com.zhaoweihao.architechturesample.data.course.Query;
import com.zhaoweihao.architechturesample.data.course.Select;
import com.zhaoweihao.architechturesample.data.course.Submit;
import com.zhaoweihao.architechturesample.database.User;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class QueryPresenter implements QueryContract.Presenter, OnStringListener {

    public static final String TAG = "QueryPresenter";

    private QueryContract.View view;
    private Context context;
    private StringModelImpl model;
    private ArrayList<Query> queryList = new ArrayList<>();

    public QueryPresenter(Context context, QueryContract.View view) {
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
            queryList.addAll(new Gson().fromJson(payload, new TypeToken<List<Query>>() {
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
    public void query(String url) {
        view.startLoading();
        model.sentGetRequestInSMI(url, this);
    }

    @Override
    public ArrayList<Query> getQueryList() {
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

    @Override
    public void selectCourse(Query query, String password) {
        /**
         * courseId : 4
         * stuId : 20
         * studentId : 2015191054
         * courseName : 大学语文
         * teacherName : 赵威豪
         * password : 123456
         */
        User user = DataSupport.findLast(User.class);
        Select select = new Select();
        select.setCourseId(query.getId());
        select.setStuId(user.getUserId());
        select.setStudentId(user.getStudentId());
        select.setCourseName(query.getCourseName());
        select.setTeacherName(query.getTeacherName());
        select.setPassword(password);

        String suffix = "course/select";
        String json = new Gson().toJson(select);

        model.sentPostRequestInSMI(suffix, json, this);
    }
}
