package com.zhaoweihao.architechturesample.presenter;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.util.Log;

import com.j256.ormlite.stmt.query.In;
import com.zhaoweihao.architechturesample.https.ApiCallback;
import com.zhaoweihao.architechturesample.view.uiview.BaseView;
import com.zhaoweihao.architechturesample.view.uiview.TestHttpView;

/**
 * Created by taojian on 2018/9/11.
 */

public class TestHttpPresenter<V extends BaseView> extends BasePresenter<V> {

    public TestHttpPresenter(V mvpView, Context context) {
        super(context.getApplicationContext(), mvpView);
    }

//    public void updateNickName(String token, String username, String nickName) {
//        mvpView.showLoading();
//        addSubscription(apiStores.updateNickName(token, username,nickName), new ApiCallback(context) {
//            @Override
//            public void onSuccess(int code, String msg, Object model) {
//                ((ChangeNickNameView) mvpView).changeNickNameSuccess(code, msg);
//            }
//
//            @Override
//            public void onFailure(int code, String msg) {
//                mvpView.getDataFail(msg);
//            }
//
//            @Override
//            public void onFinish() {
//                mvpView.hideLoading();
//            }
//
//        });
//    }

    public void testHttp(String courseId) {
        mvpView.showLoading();
        Log.d(TAG, "http请求");
        Log.d(TAG, "apistores === " + (apiStores == null));
        addSubscription(apiStores.querySelectByCourseId(courseId), new ApiCallback(context) {
            @Override
            public void onSuccess(int code, String msg, Object model) {
                Log.d(TAG, "onSuccess");
                ((TestHttpView)mvpView).querySuccess(code, model);
            }

            @Override
            public void onFailure(int code, String msg) {
                Log.d(TAG, "onFailure === " + msg);
            }

            @Override
            public void onFinish() {
                Log.d(TAG, "onFinish");
            }
        });
    }
}
