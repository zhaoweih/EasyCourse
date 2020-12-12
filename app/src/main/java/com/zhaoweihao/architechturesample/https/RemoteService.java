package com.zhaoweihao.architechturesample.https;


import com.zhaoweihao.architechturesample.bean.ChatBean;
import com.zhaoweihao.architechturesample.bean.Result;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface RemoteService {

    String HEADERS = "Content-Type: application/json;charset=UTF-8";
//    @FormUrlEncoded
//    @POST("/instructions/{sn}/{cmdCode}")
//    Observable<Result> control(@Field("username") String username, @Path("sn") String sn, @Path("cmdCode") int cmdcode, @Field("ctrlCode") String ctrlCode);

    @GET("/api/course/querySelectByCourseId")
    Observable<Result> querySelectByCourseId(@Query("courseId") String courseId);

    @GET("/api/chat/get_personal_msgs")
    Observable<Result> getPersonalMsgs(@Query("sender_id") Integer sender_id, @Query("receiver_id") Integer receiver_id);

    @POST("/api/chat/send")
    Observable<Result> sendMsg(@Body ChatBean chatBean);
}
