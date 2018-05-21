package com.zhaoweihao.architechturesample.data;


/**
 * Created by zhao weihao on 2018/4/5.
 */

public interface OnStringListener {

    /**
     * 请求成功时回调
     * @param payload
     */
    void onSuccess(String payload);

    /**
     * 请求失败时回调
     * @param error
     */
    void onError(String error);
}
