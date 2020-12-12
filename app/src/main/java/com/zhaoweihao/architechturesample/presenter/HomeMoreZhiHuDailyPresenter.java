package com.zhaoweihao.architechturesample.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.zhaoweihao.architechturesample.bean.NewStringModelImpl;
import com.zhaoweihao.architechturesample.bean.OnStringListener;
import com.zhaoweihao.architechturesample.bean.ZhihuDaily;
import com.zhaoweihao.architechturesample.contract.HomeMoreZhiHuDailyContract;


import java.util.ArrayList;

/**
 * Created by zhao weihao on 2018/4/5.
 */

public class HomeMoreZhiHuDailyPresenter implements HomeMoreZhiHuDailyContract.Presenter,OnStringListener {

    private HomeMoreZhiHuDailyContract.View view;
    private ArrayList<ZhihuDaily.Story> zhihuDailyArticles = new ArrayList<>();
    private Context context;
    private NewStringModelImpl model;

    public HomeMoreZhiHuDailyPresenter(Context context, HomeMoreZhiHuDailyContract.View view) {
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
