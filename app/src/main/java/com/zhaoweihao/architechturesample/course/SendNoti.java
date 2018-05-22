package com.zhaoweihao.architechturesample.course;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.leave.LeaveSubmit;

import java.util.Calendar;
import java.util.Date;

public class SendNoti extends AppCompatActivity implements View.OnClickListener {
    //课程编号、公告的截止日期、增加的截止日期的时间（一星期，两星期、三星期、四星期、一个月、两个月、三个月）
    TextView tv_sendnoti_coursenum, tv_sendnoti_date, tv_sendnoti_order;
    //公告的内容
    EditText et_sendnoti_content;
    //提交发送按钮
    Button bt_sendnotisubmit;
    //课程编号,截止日期，增加长度
    int courseNum, expireTime, expireDuration;
    //显示选择截止日期的数组
    String expires[] = new String[7];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_noti);
        initViews();
        setCurrentCoursename();
        try {
            setTvSendNotDate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initViews() {

        expireTime = 0;
        expireDuration = 0;
        tv_sendnoti_coursenum = (TextView) findViewById(R.id.tv_sendnoti_coursenum);
        tv_sendnoti_date = (TextView) findViewById(R.id.tv_sendnoti_date);
        tv_sendnoti_order = (TextView) findViewById(R.id.tv_sendnoti_order);
        et_sendnoti_content = (EditText) findViewById(R.id.et_sendnoti_content);
        bt_sendnotisubmit = (Button) findViewById(R.id.bt_sendnotisubmit);

        tv_sendnoti_coursenum.setOnClickListener(this);
        tv_sendnoti_date.setOnClickListener(this);
        tv_sendnoti_order.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_sendnotisubmit:
                submitNoti();
                break;
            case R.id.tv_sendnoti_date:
                new AlertDialog.Builder(SendNoti.this).setItems(expires, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        expireTime = i;
                        tv_sendnoti_date.setText("截止日期："+setTvSendNotDateSetText());
                    }
                }).create().show();
                break;
            case R.id.tv_sendnoti_order:
                final String addDays[] = {"增加0天后截止", "增加7天后截止", "增加21天后截止", "增加28天后截止", "增加30天后截止", "增加31天后截止"};
                new AlertDialog.Builder(SendNoti.this).setItems(addDays, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        tv_sendnoti_order.setText(addDays[i]);
                        switch (i) {
                            case 0:
                                expireDuration = 0;
                                break;
                            case 1:
                                expireDuration = 7;
                                break;
                            case 2:
                                expireDuration = 21;
                                break;
                            case 3:
                                expireDuration = 28;
                                break;
                            case 4:
                                expireDuration = 30;
                                break;
                            case 5:
                                expireDuration = 31;
                                break;
                        }
                        tv_sendnoti_date.setText("截止日期："+setTvSendNotDateSetText());
                    }
                }).create().show();
                break;
            case R.id.et_sendnoti_content:
                break;
        }
    }

    public void setCurrentCoursename() {
        tv_sendnoti_coursenum.setText("当前课程：first head java");
    }

    public void submitNoti() {

    }

    public void setTvSendNotDate() {
        LeaveSubmit leaveSubmit1 = new LeaveSubmit();
        for (int date = 0, order = 0; date < 7; date++) {
            expires[order++] = leaveSubmit1.getDateString(LeaveSubmit.getNextkDay(new Date(), date));
        }
    }
    public String setTvSendNotDateSetText(){
        int expireTime1=expireTime+expireDuration;
        return  new LeaveSubmit().getDateString(LeaveSubmit.getNextkDay(new Date(),expireTime1));
    }
}
