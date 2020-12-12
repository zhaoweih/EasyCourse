package com.zhaoweihao.architechturesample.view.uiview;

/**
 * 统一处理Https请求加载进度条
 * Created by taojian on 2018/9/11.
 */

public interface BaseView {
    void showLoading();

    void hideLoading();

    void getDataFail(String msg);
}
