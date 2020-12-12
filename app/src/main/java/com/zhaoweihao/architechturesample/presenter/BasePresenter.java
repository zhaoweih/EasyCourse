package com.zhaoweihao.architechturesample.presenter;


import android.content.Context;

import com.zhaoweihao.architechturesample.https.RemoteService;
import com.zhaoweihao.architechturesample.https.RetrofitManager;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by taojian on 2018/9/11.
 */

public abstract class BasePresenter<V> {
    protected final String TAG = getClass().getSimpleName();
    protected V mvpView;
    private CompositeSubscription mCompositeSubscription;
    public RemoteService apiStores;
    protected Context context;

    public BasePresenter(Context context, V mvpView) {
        this.mvpView = mvpView;
        this.context = context;
        attachView(mvpView);
    }

    /**
     * 绑定
     *
     * @param mvpView
     */
    public void attachView(V mvpView) {
        this.mvpView = mvpView;
        apiStores = RetrofitManager.remoteService(context);
    }

    public void refreshApiStores() {
        apiStores = RetrofitManager.remoteService(context);
    }

    /**
     * 解绑
     */
    public void detachView() {
        this.mvpView = null;
        onUnsubscribe();
    }

    protected void addSubscription(Observable observable, Subscriber subscriber) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }


    /**
     * RXjava取消注册，以避免内存泄露
     */
    public void onUnsubscribe() {
        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
        }
    }
}
