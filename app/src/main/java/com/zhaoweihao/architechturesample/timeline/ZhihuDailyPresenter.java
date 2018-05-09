package com.zhaoweihao.architechturesample.timeline;

import android.support.annotation.NonNull;

import com.zhaoweihao.architechturesample.data.ZhihuDailyNewsQuestion;
import com.zhaoweihao.architechturesample.data.source.datasource.ZhihuDailyNewsDataSource;
import com.zhaoweihao.architechturesample.data.source.repository.ZhihuDailyNewsRepository;

import java.util.List;

public class ZhihuDailyPresenter implements ZhihuDailyContract.Presenter {

    @NonNull
    private final ZhihuDailyContract.View mView;

    @NonNull
    private final ZhihuDailyNewsRepository mRepository;

    public ZhihuDailyPresenter(@NonNull ZhihuDailyContract.View view,
                               @NonNull ZhihuDailyNewsRepository repository) {
        this.mView = view;
        this.mRepository = repository;
        this.mView.setPresenter(this);
    }


    @Override
    public void loadNews(boolean forceUpdate, boolean clearCache, long date) {

        mRepository.getZhihuDailyNews(forceUpdate, clearCache, date, new ZhihuDailyNewsDataSource.LoadZhihuDailyNewsCallback(){

            @Override
            public void onNewsLoaded(@NonNull List<ZhihuDailyNewsQuestion> list) {
                if (mView.isActive()) {
                    mView.showResult(list);
                    mView.setLoadingIndicator(false);
                }
            }

            @Override
            public void onDataNotAvailable() {
                if (mView.isActive()) {
                    mView.setLoadingIndicator(false);
                }
            }
        });

    }

    @Override
    public void start() {

    }
}
