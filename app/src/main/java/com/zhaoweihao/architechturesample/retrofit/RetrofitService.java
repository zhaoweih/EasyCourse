package com.zhaoweihao.architechturesample.retrofit;

import com.zhaoweihao.architechturesample.data.ZhihuDailyContent;
import com.zhaoweihao.architechturesample.data.ZhihuDailyNews;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RetrofitService {
    String ZHIHU_DAILY_BASE = "https://news-at.zhihu.com/api/4/news/";

    interface ZhihuDailyService {

        @GET("berore/{date}")
        Call<ZhihuDailyNews> getZhihuList(@Path("date") String date);

        @GET("{id}")
        Call<ZhihuDailyContent> getZhihuContent(@Path("id") int id);


    }
}
