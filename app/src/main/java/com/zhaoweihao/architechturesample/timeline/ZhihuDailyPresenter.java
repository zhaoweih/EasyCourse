package com.zhaoweihao.architechturesample.timeline;

import android.content.Context;
import android.support.annotation.NonNull;



import java.util.List;

public class ZhihuDailyPresenter implements ZhihuDailyContract.Presenter {

    private final ZhihuDailyContract.View mView;
    private Context context;


    public ZhihuDailyPresenter(Context context, ZhihuDailyContract.View view) {
        this.mView = view;
        this.mView.setPresenter(this);
        this.context = context;
    }




    @Override
    public void start() {

    }
}
