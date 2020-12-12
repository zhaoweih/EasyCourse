package com.zhaoweihao.architechturesample.bean;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.zhaoweihao.architechturesample.util.Constant;

import static com.zhaoweihao.architechturesample.util.HttpUtil.*;
import static com.zhaoweihao.architechturesample.util.Utils.log;

import java.io.IOException;

import okhttp3.Call;

/**
 * Created by zhao weihao 2018/4/5.
 */

public class StringModelImpl {


    public static final String TAG = "StringModelImpl";

    private Context context;

    public StringModelImpl(Context context) { this.context = context; }

    public void sentGetRequestInSMI(String url, final OnStringListener listener) {
        sendGetRequest(url,new okhttp3.Callback(){

            @Override
            public void onFailure(Call call, IOException e) {
                listener.onError(e.toString());
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                //网络请求成功
                String body = response.body().string();
                //RestResponse restResponse = new Gson().fromJson(body, RestResponse.class);
                RestResponse restResponse = com.alibaba.fastjson.JSON.parseObject(body, RestResponse.class);
                if (restResponse == null) {
                    listener.onError("请求失败");
                    return;
                }
                if (restResponse.getCode() == Constant.FAIL_ORIGIN_CODE) {
                    listener.onError("请求失败");
                    return;
                }
                if (restResponse.getCode() == Constant.SUCCESS_CODE) {
                    if (restResponse.getPayload() == null) {
                        listener.onSuccess(null);
                        return;
                    }
                    listener.onSuccess(restResponse.getPayload().toString());
                    log(StringModelImpl.class, restResponse.getPayload().toString());
                }

            }
        });
    }

    public void sentPostRequestInSMI(String url, String json, final OnStringListener listener) {
        sendPostRequest(url,json ,new okhttp3.Callback(){

            @Override
            public void onFailure(Call call, IOException e) {
                listener.onError(e.toString());
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                //网络请求成功
                String body = response.body().string();
                //RestResponse restResponse = new Gson().fromJson(body, RestResponse.class);
                RestResponse restResponse = com.alibaba.fastjson.JSON.parseObject(body, RestResponse.class);
                if (restResponse == null) {
                    listener.onError("请求失败");
                    return;
                }
                if (restResponse.getCode() == Constant.FAIL_ORIGIN_CODE) {
                    listener.onError(restResponse.getMsg());
                    return;
                }
                if (restResponse.getCode() == Constant.SUCCESS_CODE) {
                    if (restResponse.getPayload() == null) {
                        listener.onSuccess(null);
                        return;
                    }
                    listener.onSuccess(restResponse.getPayload().toString());
                }
            }
        });
    }
}
