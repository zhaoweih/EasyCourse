package com.zhaoweihao.architechturesample.base;
public interface BaseActivityView<T> {
    //为View设置Presenter
    void setPresenter(T presenter);
    //初始化界面控件
    void initViews();
}
