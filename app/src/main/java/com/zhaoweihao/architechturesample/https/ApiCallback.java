package com.zhaoweihao.architechturesample.https;

import android.content.Context;

import java.net.SocketTimeoutException;

import retrofit2.adapter.rxjava.HttpException;
import com.zhaoweihao.architechturesample.bean.Result;
import rx.Subscriber;



public abstract class ApiCallback<M> extends Subscriber<Result<M>> {
    private Context context;

    public ApiCallback(Context context) {
        this.context = context;
    }

    public abstract void onSuccess(int code, String msg, M model);

    public abstract void onFailure(int code, String msg);

    public abstract void onFinish();

    @Override
    public void onStart() {
        super.onStart();
        //接下来可以检查网络连接等操作
//        if (!Utils.isNetworkAvailable(context)) {
//            ToastUtil.showToast(context, context.getString(R.string.pc_network_disable));
//            if (!isUnsubscribed()) {
//                unsubscribe();
//            }
//            onFailure(-1, context.getResources().getString(R.string.pc_network_disable));
//            onFinish();
//            return;
//        }

//        if (WifiAdmin.NetworkCapabilities(context)) {
//            ToastUtil.showToast(context, "当前网络不可用，请检查网络情况");
//            if (!isUnsubscribed()) {
//                unsubscribe();
//            }
//            onFinish();
//            return;
//        }

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        String errorMsg = "";
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            httpException.code();
            errorMsg = httpException.getMessage();
        } else if (e instanceof SocketTimeoutException) {
            errorMsg = "超时";
        }
        onFailure(-1, errorMsg);
        onFinish();
    }

    @Override
    public void onNext(Result<M> result) {
        if (result.getCode() == 200) {
            if (null == result.getPayload()) {
                onSuccess(result.getCode(), result.getMsg(), null);
            } else {
                M t = result.getPayload();
                onSuccess(result.getCode(), result.getMsg(), t);
            }
        } else {
            onFailure(result.getCode(), result.getMsg());
        }
    }

    @Override
    public void onCompleted() {
        onFinish();
    }
}
