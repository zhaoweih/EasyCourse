package com.zhaoweihao.architechturesample;

import android.view.View;

public interface BaseView<T> {
    //为View设置Presenter
    void setPresenter(T presenter);
    //初始化界面控件
    void initViews(View view);
}
