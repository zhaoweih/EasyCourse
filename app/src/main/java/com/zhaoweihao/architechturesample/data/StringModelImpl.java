package com.zhaoweihao.architechturesample.data;

import android.content.Context;

import com.google.gson.Gson;

import static com.zhaoweihao.architechturesample.util.HttpUtil.*;

import java.io.IOException;

import okhttp3.Call;

/**
 * Created by zhao weihao 2018/4/5.
 */

public class StringModelImpl {

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
                RestResponse restResponse = new Gson().fromJson(body, RestResponse.class);
                if (restResponse == null) {
                    listener.onError("请求失败");
                    return;
                }
                if (restResponse.getCode() == 500) {
                    listener.onError("请求失败");
                    return;
                }
                if (restResponse.getCode() == 200) {
                    if (restResponse.getPayload() == null) {
                        listener.onSuccess(null);
                        return;
                    }
                    listener.onSuccess(restResponse.getPayload().toString());
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
                RestResponse restResponse = new Gson().fromJson(body, RestResponse.class);
                if (restResponse == null) {
                    listener.onError("请求失败");
                    return;
                }
                if (restResponse.getCode() == 500) {
                    listener.onError(restResponse.getMsg());
                    return;
                }
                if (restResponse.getCode() == 200) {
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
