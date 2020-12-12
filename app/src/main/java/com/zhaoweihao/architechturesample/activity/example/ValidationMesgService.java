package com.zhaoweihao.architechturesample.activity.example;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.google.gson.JsonObject;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.activity.MessageValidationListActivity;
import com.zhaoweihao.architechturesample.bean.RestResponse;
import com.zhaoweihao.architechturesample.bean.ValidationMesg;
import com.zhaoweihao.architechturesample.bean.ValidationMesgEvent;
import com.zhaoweihao.architechturesample.database.User;
import com.zhaoweihao.architechturesample.interfaze.ValidationMesgSerrvice;
import com.zhaoweihao.architechturesample.util.Constant;
import com.zhaoweihao.architechturesample.util.HttpUtil;

import org.greenrobot.eventbus.EventBus;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @author
 * @description 用于检测验证消息
 * @time 2019/3/8 15:00
 */
public class ValidationMesgService extends Service implements ValidationMesgSerrvice {
    private Handler handler;
    private Timer timer;
    private TimerTask timerTask;
    private List<ValidationMesg> validationMesgList = new ArrayList<>();
    private List<ValidationMesg> validationMesgListTemp = new ArrayList<>();
    private Boolean isChageHappen = false;


    public ValidationMesgService() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        detectedChanges();
        return super.onStartCommand(intent, flags, startId);
    }

    private void detectedChanges() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpUtil.sendGetRequest(Constant.GET_ALL_FRIENDS_TO_ME_REQUEST_URL + DataSupport.findLast(User.class).getUsername() + "&token=" + DataSupport.findLast(User.class).getToken(), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.v("tanxkui", "i think the network crashes!");
                        stopSelf();
                        detectedChanges();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String body = response.body().string();
                        //解析json数据组装RestResponse对象
                        RestResponse restResponse = JSON.parseObject(body, RestResponse.class);
                        Log.v("tanxkui", "body" + body);
                        Log.v("tanxkui", "the payload:" + restResponse.getPayload());
                        if (restResponse.getCode() == Constant.SUCCESS_CODE) {
                            List<ValidationMesg> validationMesg = JSON.parseArray(restResponse.getPayload().toString(), ValidationMesg.class);
                            if (validationMesg.size() != 0) {
                                for (ValidationMesg validationMesg1 : validationMesg) {
                                    if (validationMesg1.getIs_confirmed() == 0) {
                                        validationMesgList.add(validationMesg1);
                                    }
                                }
                            }

                            if (validationMesgList.containsAll(validationMesgListTemp) && validationMesgListTemp.containsAll(validationMesgList)) {
                                isChageHappen = false;
                                stopSelf();
                                //detectedChanges();
                                Log.v("tanxkui", "nothing changes!");
                            } else {
                                validationMesgListTemp = validationMesgList;
                                isChageHappen = true;
                                for (ValidationMesg validationMesg1 : validationMesgList) {
                                    sendSimplestNotificationWithAction(validationMesg1.getFrom_username(), validationMesg1.getId());
                                }
                                EventBus.getDefault().post(new ValidationMesgEvent(validationMesgList.size()));
                                stopSelf();
                                //detectedChanges();
                                Log.v("tanxkui", " changes happen!");
                            }

                        } else {
                            stopSelf();
                            detectedChanges();
                        }
                    }
                });
            }
        }).start();
    }

    private void findChanges() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 12) {
                    Log.v("tanxkui", "i am detecting validation messages!");
                } else if (msg.what == 11) {
                    Log.v("tanxkui", "i am sendingNotification!");
                } else {
                    Log.v("tanxkui", "i am dont know what to do!");
                }

                super.handleMessage(msg);
            }
        };
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                HttpUtil.sendGetRequest(Constant.GET_ALL_FRIENDS_TO_ME_REQUEST_URL + DataSupport.findLast(User.class).getUsername() + "&token=" + DataSupport.findLast(User.class).getToken(), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.v("tanxkui", "i think the network crashes!");
                        message.what = 12;
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String body = response.body().string();
                        //解析json数据组装RestResponse对象
                        RestResponse restResponse = JSON.parseObject(body, RestResponse.class);
                        Log.v("tanxkui", "body" + body);
                        Log.v("tanxkui", "the payload:" + restResponse.getPayload());
                        if (restResponse.getCode() == Constant.SUCCESS_CODE) {
                            List<ValidationMesg> validationMesg = JSON.parseArray(restResponse.getPayload().toString(), ValidationMesg.class);

                            if (validationMesg.size() != 0) {
                                for (ValidationMesg validationMesg1 : validationMesg) {
                                    if (validationMesg1.getIs_confirmed() == 0 || validationMesg1.getIs_confirmed() == 2) {
                                        validationMesgList.add(validationMesg1);
                                    }
                                }
                            }

                            if (validationMesgList.containsAll(validationMesgListTemp) && validationMesgListTemp.containsAll(validationMesgList)) {
                                isChageHappen = false;
                                message.what = 12;
                                Log.v("tanxkui", "nothing changes!");
                            } else {
                                message.what = 11;
                                validationMesgListTemp = validationMesgList;
                                isChageHappen = true;
                                sendSimplestNotificationWithAction("123", 0);
                                Log.v("tanxkui", " changes happen!");
                            }

                        } else {
                            message.what = 12;
                        }
                    }
                });
                handler.sendMessage(message);
            }
        };
        timer.schedule(timerTask, 1000, 8000);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Log.v("tanxkui", "i've stopped detecting validation messages!");
        /*if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
        handler.removeMessages(11);
        handler.removeMessages(12);*/
        super.onDestroy();
    }

    @Override
    public List<ValidationMesg> detectedNew() {
        if (isChageHappen) {
            return validationMesgListTemp;
        } else {
            return null;
        }
    }

    private void sendSimplestNotificationWithAction(String s, int id) {
        NotificationManager mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //获取PendingIntent
        Intent mainIntent = new Intent(this, MessageValidationListActivity.class);
        PendingIntent mainPendingIntent = PendingIntent.getActivity(this, id, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //创建 Notification.Builder 对象
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.checkinfo)
                //点击通知后自动清除
                .setAutoCancel(true)
                .setContentTitle("好友请求")
                .setContentText(s + "请求添加你为好友")
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                .setContentIntent(mainPendingIntent);
        //发送通知
        mNotifyManager.notify(id, builder.build());
    }
}
