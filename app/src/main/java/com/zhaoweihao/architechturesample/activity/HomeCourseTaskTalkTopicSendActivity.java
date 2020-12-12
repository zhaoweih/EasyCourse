package com.zhaoweihao.architechturesample.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.base.BaseActivity;
import com.zhaoweihao.architechturesample.bean.RestResponse;
import com.zhaoweihao.architechturesample.database.User;
import com.zhaoweihao.architechturesample.util.Constant;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import timeselector.inter.CustomListener;
import timeselector.view.TimePickerView;

import static com.zhaoweihao.architechturesample.util.HttpUtil.sendPostRequest;
import static com.zhaoweihao.architechturesample.util.Utils.log;
/**
*@description 首页-课程-详细界面-任务-讨论
*@author
*@time 2019/1/28 13:06
*/
public class HomeCourseTaskTalkTopicSendActivity extends BaseActivity implements View.OnClickListener{
    private static final Class thisClass = HomeCourseTaskTalkTopicSendActivity.class;
    //课程编号、公告的截止日期、增加的截止日期的时间（一星期，两星期、三星期、四星期、一个月、两个月、三个月）
    TextView tv_sendtopic_date, tv_sendtopic_order;
    //EditText et_sendtopic_coursenum;
    //返回到主页
    ImageView iv_sendtopicreturntohome;
    //公告的内容
    EditText et_sendtopic_content;
    //提交发送按钮
    Button bt_sendtopicsubmit;
    //课程编号,截止日期，增加长度
    int courseNum, expireTime, expireDuration;
    //显示选择截止日期
    TimePickerView pvCustomTime;
    //截止时间
    Date expireDate;
    private int courseId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_topic);
        initViews();
        initCustomTimePicker();
    }
    private void initViews() {
        Intent intent=getIntent();
        courseId=intent.getIntExtra("courseId",0);
        expireDate=new Date();
        expireTime = 0;
        expireDuration = 0;
       // et_sendtopic_coursenum = (EditText) findViewById(R.id.et_sendtopic_coursenum);
        tv_sendtopic_date = (TextView) findViewById(R.id.tv_sendtopic_date);
        tv_sendtopic_order = (TextView) findViewById(R.id.tv_sendtopic_order);
        iv_sendtopicreturntohome=(ImageView) findViewById(R.id.iv_sendtopicreturntohome);

        et_sendtopic_content = (EditText) findViewById(R.id.et_sendtopic_content);
        bt_sendtopicsubmit = (Button) findViewById(R.id.bt_sendtopicsubmit);


        //et_sendtopic_coursenum.setOnClickListener(this);
        tv_sendtopic_date.setOnClickListener(this);
        tv_sendtopic_order.setOnClickListener(this);
        iv_sendtopicreturntohome.setOnClickListener(this);
        bt_sendtopicsubmit.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_sendtopicreturntohome:
               finish();
                break;
            case R.id.bt_sendtopicsubmit:
                AlertDialog alert = new AlertDialog.Builder(HomeCourseTaskTalkTopicSendActivity.this)
                        .setTitle("温馨提示")
                        .setIcon(R.drawable.warming)
                        .setMessage("您提交讨论后将法修改，但可以删除，确定提交？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {//设置确定按钮
                            @Override//处理确定按钮点击事件
                            public void onClick(DialogInterface dialog, int which) {
                                submittopic();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick (DialogInterface dialog,int which){
                                dialog.cancel();//对话框关闭。
                            }
                        }).create();
                alert.show();
                break;
            case R.id.tv_sendtopic_date:
                if (view.getId() == R.id.tv_sendtopic_date && pvCustomTime != null)
                    pvCustomTime.show();
                break;
            case R.id.et_sendtopic_content:
                break;
        }
    }
    public void submittopic() {
        if( tv_sendtopic_date.getText().toString().equals("选择公告截止日期")||et_sendtopic_content.getText().toString().equals("")){
            Toast.makeText(this,"请先填写好公告或选择完截止日期！",Toast.LENGTH_SHORT).show();
            return;
        }
        com.zhaoweihao.architechturesample.bean.course.SendTopic sendtopic=new com.zhaoweihao.architechturesample.bean.course.SendTopic();
        sendtopic.setCourseId(getIntent().getIntExtra("courseId",0));
        sendtopic.setStatus(1);
        //DateFormat bf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        DateFormat bf = new SimpleDateFormat("yyyy-MM-dd");
        sendtopic.setContent(et_sendtopic_content.getText().toString());
        sendtopic.setStartDate(bf.format(new Date()));
        sendtopic.setEndDate(bf.format(expireDate));
        sendtopic.setTecId(DataSupport.findLast(User.class).getUserId());
        sendtopic.setTeacherId(DataSupport.findLast(User.class).getTeacherId());
        //转换成json数据，借助gson
        String json = new Gson().toJson(sendtopic);
        log(thisClass, json);
        //发送post请求注册
        String after = Constant.ADD_DISCUSS_URL;
        sendPostRequest(after, json, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //发送请求失败，有可能是网络断了或者其他的
                log(thisClass, "发送请求失败，请检查网络");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                //Gson解析数据 json -> 对象
                try {
                    RestResponse restResponse = new Gson().fromJson(body, RestResponse.class);
                    if (restResponse == null) {
                        runOnUiThread(() -> Toast.makeText(HomeCourseTaskTalkTopicSendActivity.this, "发送请求失败！", Toast.LENGTH_SHORT).show());
                        return;
                    }
                    log(thisClass, restResponse.getCode().toString());
                    //状态码500表示失败，打印错误信息
                    if (restResponse.getCode() == Constant.FAIL_ORIGIN_CODE) {
                        log(thisClass, restResponse.getMsg());
                        runOnUiThread(() -> Toast.makeText(HomeCourseTaskTalkTopicSendActivity.this, "发送请求失败！"+restResponse.getMsg(), Toast.LENGTH_SHORT).show());
                    }
                    //200代表成功，打印成功信息
                    if (restResponse.getCode() == Constant.SUCCESS_CODE) {
                        log(thisClass, "已成功发送");
                        runOnUiThread(() -> {
                            Toast.makeText(HomeCourseTaskTalkTopicSendActivity.this, "已成功发送！", Toast.LENGTH_SHORT).show();
                            finish();
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                log(thisClass, body);
            }
        });

    }
    private String getTime(Date date) {
        //可根据需要自行截取数据显示
       // SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }
    private void initCustomTimePicker() {

        //系统当前时间
        Calendar selectorDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        //startDate.set(2000, 1, 23);
        //startDate.set();
        //系统当前时间
        Calendar endDate = Calendar.getInstance();
        endDate.set(2028, 2, 28);
        //时间选择器 ，自定义布局
        pvCustomTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectorListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                tv_sendtopic_date.setText("截止时间："+getTime(date));
                expireDate=date;
            }
        })
                .setDate(selectorDate)
                .setRangDate(startDate, endDate)
                .setLayoutRes(R.layout.pickerview_custom_time,
                        new CustomListener() {
                            @Override
                            public void customLayout(View v) {
                                final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                                tvSubmit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        pvCustomTime.returnData();
                                        pvCustomTime.dismiss();
                                    }
                                });
                            }
                        }).setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("年", "月", "日", "时", "分")
                .isCenterLabel(false)
               // .setDividerColor(0xFF24AD9D)
                .build();
    }
}
