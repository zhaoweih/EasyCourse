package com.zhaoweihao.architechturesample.reading;

import android.content.Context;

import com.google.gson.Gson;
import com.zhaoweihao.architechturesample.data.NewStringModelImpl;
import com.zhaoweihao.architechturesample.data.OnStringListener;
import com.zhaoweihao.architechturesample.data.StringModelImpl;
import com.zhaoweihao.architechturesample.data.ZhihuDaily;


import java.util.ArrayList;

/**
 * Created by zhao weihao on 2018/4/5.
 */

public class ZhihuDailyPresenter implements ZhihuDailyContract.Presenter,OnStringListener {

    private ZhihuDailyContract.View view;
    private ArrayList<ZhihuDaily.Story> zhihuDailyArticles = new ArrayList<>();
    private Context context;
    private NewStringModelImpl model;

    public ZhihuDailyPresenter(Context context, ZhihuDailyContract.View view) {
        this.view = view;
        this.context = context;
        this.view.setPresenter(this);
        model = new NewStringModelImpl(context);
    }
    @Override
    public void start() {

    }

    @Override
    public void onSuccess(String result) {
        Gson gson = new Gson();
        ZhihuDaily zhihuDaily = gson.fromJson(result, ZhihuDaily.class);
        zhihuDailyArticles.addAll(zhihuDaily.getStories());
        view.showResult(zhihuDailyArticles);
        view.stopLoading();
    }

    @Override
    public void onError(String error) {
        view.showLoadError();
        view.stopLoading();
    }

    @Override
    public void requestArticles(Boolean forceRefresh) {
        view.startLoading();
        if (forceRefresh) {
            zhihuDailyArticles.clear();
        }
        model.load("https://news-at.zhihu.com/api/4/news/latest",this);
    }

    @Override
    public ArrayList<ZhihuDaily.Story> getArticles() {
        return zhihuDailyArticles;
    }

    @Override
    public void copyToClipboard(int position) {

    }
}
