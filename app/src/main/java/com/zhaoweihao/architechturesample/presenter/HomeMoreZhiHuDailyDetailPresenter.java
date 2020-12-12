package com.zhaoweihao.architechturesample.presenter;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.zhaoweihao.architechturesample.bean.Detail;
import com.zhaoweihao.architechturesample.bean.NewStringModelImpl;
import com.zhaoweihao.architechturesample.bean.OnStringListener;
import com.zhaoweihao.architechturesample.contract.HomeMoreZhiHuDailyDetailContract;


/**
 * Created by por on 2018/4/5.
 */

public class HomeMoreZhiHuDailyDetailPresenter implements HomeMoreZhiHuDailyDetailContract.Presenter, OnStringListener {

    private static final String TAG = "HomeMoreZhiHuDailyDetailPresenter";

    private HomeMoreZhiHuDailyDetailContract.View view;
    private Context context;
    private NewStringModelImpl model;
    private Detail detail;

    public HomeMoreZhiHuDailyDetailPresenter(Context context, HomeMoreZhiHuDailyDetailContract.View view) {
        this.view = view;
        this.context = context;
        this.view.setPresenter(this);
        model = new NewStringModelImpl(context);
    }

    @Override
    public void loadArticle(Boolean forceRefresh) {
        view.startLoading();
        Log.d(TAG,view.getArticleId());
        if (forceRefresh) {

        }
        model.load("https://news-at.zhihu.com/api/4/news/"+view.getArticleId(),this);
    }
    @Override
    public void start() {

    }

    @Override
    public void onSuccess(String result) {
        Gson gson = new Gson();
        Detail detail = gson.fromJson(result, Detail.class);
        view.showResult(detail);
        view.stopLoading();
    }

    @Override
    public void onError(String error) {
        view.showLoadError();
        view.stopLoading();
    }

}
