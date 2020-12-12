package com.zhaoweihao.architechturesample.bean;

import android.content.Context;

import com.zhaoweihao.architechturesample.util.Universal;

import java.io.IOException;

import okhttp3.Call;

/**
 * Created by zhao weihao 2018/4/5.
 */

public class NewStringModelImpl {

    private Context context;

    public NewStringModelImpl(Context context) { this.context = context; }

    public void load(String url, final OnStringListener listener) {

        Universal.sendOkHttpRequest(url,new okhttp3.Callback(){

            @Override
            public void onFailure(Call call, IOException e) {
                listener.onError(e.toString());
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                //网络请求成功
                String s = response.body().string();
                listener.onSuccess(s);
            }
        });
    }
}
