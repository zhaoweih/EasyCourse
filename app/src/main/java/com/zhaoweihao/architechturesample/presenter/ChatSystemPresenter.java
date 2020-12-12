package com.zhaoweihao.architechturesample.presenter;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.stmt.query.In;
import com.zhaoweihao.architechturesample.bean.ChatBean;
import com.zhaoweihao.architechturesample.https.ApiCallback;
import com.zhaoweihao.architechturesample.view.uiview.BaseView;
import com.zhaoweihao.architechturesample.view.uiview.ChatSystemView;
import com.zhaoweihao.architechturesample.view.uiview.TestHttpView;

/**
 * @author zhaoweihao
 * @date 2019/1/10
 */

public class ChatSystemPresenter extends BasePresenter<ChatSystemView> {

    public ChatSystemPresenter(ChatSystemView mvpView, Context context) {
        super(context.getApplicationContext(), mvpView);
    }

    public void sendMsg(ChatBean chatBean) {
        addSubscription(apiStores.sendMsg(chatBean), new ApiCallback(context) {
            @Override
            public void onSuccess(int code, String msg, Object model) {
//                mvpView.getMsgsSucceed(code, model);
            }

            @Override
            public void onFailure(int code, String msg) {
                mvpView.getDataFail(msg);
            }

            @Override
            public void onFinish() {

            }
        });
    }

    public void getPersonalMsgs(Integer sender_id, Integer receiver_id) {
        addSubscription(apiStores.getPersonalMsgs(sender_id, receiver_id), new ApiCallback(context) {
            @Override
            public void onSuccess(int code, String msg, Object model) {
                mvpView.getMsgsSucceed(code, model);
            }

            @Override
            public void onFailure(int code, String msg) {
                mvpView.getDataFail(msg);
            }

            @Override
            public void onFinish() {

            }
        });
    }
}
