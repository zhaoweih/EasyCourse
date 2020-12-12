package com.zhaoweihao.architechturesample.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import com.zhaoweihao.architechturesample.base.BaseActivity;
import com.zhaoweihao.architechturesample.presenter.BasePresenter;

import org.androidannotations.annotations.EActivity;

/**
 * Created by taojian on 2018/9/11.
 */
@EActivity
public abstract class MvpActivity<P extends BasePresenter> extends BaseActivity {

    protected P presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = createPresenter();
    }

    protected abstract P createPresenter();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null)
            presenter.detachView();
    }


    public void hideLoading() {
        //todo:隐藏对话框
    }


    public void showLoading(){
        //todo:显示对话框
    }

}
