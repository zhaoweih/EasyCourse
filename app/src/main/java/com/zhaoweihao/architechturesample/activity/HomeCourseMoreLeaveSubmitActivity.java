package com.zhaoweihao.architechturesample.activity;

import android.content.DialogInterface;
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
import com.zhaoweihao.architechturesample.bean.LeaveSubmitdata;
import com.zhaoweihao.architechturesample.bean.RestResponse;
import com.zhaoweihao.architechturesample.util.Constant;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.zhaoweihao.architechturesample.util.HttpUtil.sendPostRequest;
import static com.zhaoweihao.architechturesample.util.Utils.log;
/**
*@description 首页-课程-详细界面-更多-请假-提交请假条
*@author
*@time 2019/1/28 12:59
*/
public class HomeCourseMoreLeaveSubmitActivity extends BaseActivity implements View.OnClickListener {
    private static final Class thisClass = HomeCourseMoreLeaveSubmitActivity.class;
    private TextView tv_leave_name0, tv_leave_num0, tv_leave_teachernum0, tv_leave_coursenum0;
    private ImageView iv_leavereturntohome;
    private TextView tv_leave_date0, tv_leave_date1, tv_leave_order0, tv_leave_order1, tv_leave_type0, tv_leave_end;
    private EditText et_leave_phone0, et_leave_reason;
    private Button bt_leavesubmit;
    private com.zhaoweihao.architechturesample.database.User user3;
    int startTimeSelected;
    int endTimeSelected;
    int startOrderSelected;
    int endOrderSelected;
    Boolean SelectedStartTimeFlag, SelectedEndTimeFlag, SelectedStartOrderFlag, SelectedEndOrderFlag;
    String date0[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_submit);
        initView();
    }

    public void initView() {
        startTimeSelected = 0;
        endTimeSelected = 0;
        startOrderSelected = 0;
        endOrderSelected = 0;
        SelectedStartTimeFlag = false;
        SelectedEndTimeFlag = false;
        SelectedStartOrderFlag = false;
        SelectedEndOrderFlag = false;
        user3 = DataSupport.findLast(com.zhaoweihao.architechturesample.database.User.class);
        date0 = getDateStringArrray(new Date(), 5);
        //姓名，学号，选择课程编号显示相应的教师编号，输入联系电话
        tv_leave_name0 = (TextView) findViewById(R.id.tv_leave_name0);
        tv_leave_num0 = (TextView) findViewById(R.id.tv_leave_num0);
        tv_leave_teachernum0 = (TextView) findViewById(R.id.tv_leave_teachernum0);
        tv_leave_teachernum0.setText(getIntent().getIntExtra("courseId",0)+"");
        tv_leave_coursenum0 = (TextView) findViewById(R.id.tv_leave_coursenum0);
        tv_leave_date0 = (TextView) findViewById(R.id.tv_leave_date0);
        tv_leave_date1 = (TextView) findViewById(R.id.tv_leave_date1);
        tv_leave_order0 = (TextView) findViewById(R.id.tv_leave_order0);
        tv_leave_order1 = (TextView) findViewById(R.id.tv_leave_order1);
        tv_leave_type0 = (TextView) findViewById(R.id.tv_leave_type0);
        tv_leave_end = (TextView) findViewById(R.id.tv_leave_end);
        iv_leavereturntohome = (ImageView) findViewById(R.id.iv_leavereturntohome);

        bt_leavesubmit = (Button) findViewById(R.id.bt_leavesubmit);


        et_leave_phone0 = (EditText) findViewById(R.id.et_leave_phone0);
        et_leave_reason = (EditText) findViewById(R.id.et_leave_reason);

        tv_leave_coursenum0.setOnClickListener(this);
        tv_leave_date0.setOnClickListener(this);
        tv_leave_date1.setOnClickListener(this);
        tv_leave_order0.setOnClickListener(this);
        tv_leave_order1.setOnClickListener(this);
        tv_leave_type0.setOnClickListener(this);
        bt_leavesubmit.setOnClickListener(this);
        iv_leavereturntohome.setOnClickListener(this);

        tv_leave_name0.setText(user3.getName());
        tv_leave_num0.setText(user3.getStudentId());
    }

    @Override
    public void onClick(View view) {
        final String addclass[] = {"第1节", "第2节", "第3节", "第4节", "第5节", "第6节", "第7节", "第8节", "第9节", "第10节"};
        switch (view.getId()) {
            case R.id.bt_leavesubmit:
                if (!tv_leave_teachernum0.getText().toString().equals("") && !et_leave_reason.getText().toString().equals("") && ((SelectedStartTimeFlag || SelectedEndTimeFlag) && SelectedStartOrderFlag)) {
                    AlertDialog alert = new AlertDialog.Builder(HomeCourseMoreLeaveSubmitActivity.this)
                            .setTitle("温馨提示")
                            .setIcon(R.drawable.warming)
                            .setMessage("您提交请假条后将无法修改，确定提交？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {//设置确定按钮
                                @Override//处理确定按钮点击事件
                                public void onClick(DialogInterface dialog, int which) {
                                    submit();
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick (DialogInterface dialog,int which){
                                    dialog.cancel();//对话框关闭。
                                }
                            }).create();
                    alert.show();
                } else {
                    Toast.makeText(HomeCourseMoreLeaveSubmitActivity.this, "请检查未填写信息！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_leave_coursenum0:
                final String coursenum0[] = {"20151911", "20151912"};
                new AlertDialog.Builder(HomeCourseMoreLeaveSubmitActivity.this).setItems(coursenum0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        tv_leave_coursenum0.setText(coursenum0[i]);
                    }
                }).create().show();
                break;
            case R.id.tv_leave_type0:
                final String coursetype0[] = {"公假", "事假", "病假"};
                new AlertDialog.Builder(HomeCourseMoreLeaveSubmitActivity.this).setItems(coursetype0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        tv_leave_type0.setText(coursetype0[i]);
                    }
                }).create().show();
                break;
            case R.id.tv_leave_date0:
                new AlertDialog.Builder(HomeCourseMoreLeaveSubmitActivity.this).setItems(date0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SelectedStartTimeFlag = true;
                        startTimeSelected = i;
                        tv_leave_date0.setText(date0[i]);
                        tv_leave_date1.setText(getDateString(getNextkDay(new Date(), endTimeSelected + i)));
                    }
                }).create().show();
                break;
            case R.id.tv_leave_date1:
                final String addDays[] = {"当天", "加一天", "加两天", "加三天", "加四天"};
                new AlertDialog.Builder(HomeCourseMoreLeaveSubmitActivity.this).setItems(addDays, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SelectedEndTimeFlag = true;
                        if (i == 0 && (startOrderSelected > endOrderSelected)) {
                            Toast.makeText(HomeCourseMoreLeaveSubmitActivity.this, "请先调整开始节数或结束节数", Toast.LENGTH_SHORT).show();
                        } else {
                            endTimeSelected = i;
                            tv_leave_date0.setText(date0[startTimeSelected]);
                            tv_leave_date1.setText(getDateString(getNextkDay(new Date(), startTimeSelected + i)));
                        }
                    }
                }).create().show();
                break;
            case R.id.tv_leave_order0:
                new AlertDialog.Builder(HomeCourseMoreLeaveSubmitActivity.this).setItems(addclass, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SelectedStartOrderFlag = true;
                        startOrderSelected = i;
                        if (endTimeSelected > 0) {
                            startOrderSelected = i;
                            tv_leave_order0.setText(addclass[i]);
                            tv_leave_order1.setText(addclass[endOrderSelected]);
                        } else if (endTimeSelected == 0) {
                            if (startOrderSelected > endOrderSelected) {
                                Toast.makeText(HomeCourseMoreLeaveSubmitActivity.this, "结束时间不小于开始时间", Toast.LENGTH_SHORT).show();
                                tv_leave_order0.setText(addclass[i]);
                                tv_leave_order1.setText(addclass[i]);
                                endOrderSelected = startOrderSelected;
                            } else {
                                startOrderSelected = i;
                                tv_leave_order0.setText(addclass[i]);
                                tv_leave_order1.setText(addclass[endOrderSelected]);
                            }
                        }

                    }
                }).create().show();
                break;
            case R.id.tv_leave_order1:
                new AlertDialog.Builder(HomeCourseMoreLeaveSubmitActivity.this).setItems(addclass, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SelectedEndOrderFlag = true;
                        endOrderSelected = i;
                        if (endTimeSelected > 0) {
                            endOrderSelected = i;
                            tv_leave_order0.setText(addclass[startOrderSelected]);
                            tv_leave_order1.setText(addclass[i]);
                        } else if (endTimeSelected == 0) {
                            if (startOrderSelected > endOrderSelected) {
                                endOrderSelected = startOrderSelected;
                                tv_leave_order1.setText(addclass[endOrderSelected]);
                                Toast.makeText(HomeCourseMoreLeaveSubmitActivity.this, "结束时间不小于开始时间", Toast.LENGTH_SHORT).show();
                            } else {
                                tv_leave_order1.setText(addclass[i]);
                            }
                        }
                    }
                }).create().show();
                break;
            case R.id.iv_leavereturntohome:
                finish();
                break;

        }

    }

    public static Date getNextkDay(Date date, int k) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, k);
        date = calendar.getTime();
        return date;
    }

    public String[] getDateStringArrray(Date date, int num) {
        String dateString[] = new String[num];
        for (int j = 0; j < num; j++) {
            dateString[j] = String.format("%tY", getNextkDay(date, j)) + "年" + String.format("%tm", getNextkDay(date, j)) + "月" + String.format("%te", getNextkDay(date, j)) + "日";
        }
        return dateString;
    }

    public String getDateString(Date date) {
        return String.format("%tY", date) + "年" + String.format("%tm", date) + "月" + String.format("%te", date) + "日";
    }

    public String getNowDateShort(Date currentTime) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(currentTime);
       /* log(thisClass,dateString);
        ParsePosition pos = new ParsePosition(0);
        Date c2 = formatter.parse(dateString, pos);
        log(thisClass,currentTime.toString());*/
        return dateString;
    }

    public void submit() {
        /**
         * @id id int
         * @studentId 学号
         * @teacherId 教师编号 null
         * @content 请假内容
         * @status
         * @tecAdvise 申请状态
         */
        LeaveSubmitdata leave1 = new LeaveSubmitdata();
        leave1.setStuId(user3.getUserId());
        leave1.setStudentId(user3.getStudentId());
        leave1.setCourseId(getIntent().getIntExtra("courseId",0));
        leave1.setStatus(1);
        leave1.setTecAdvise(null);
        //getNowDateShort(getNowDateShort(getNextkDay(new Date(),startTimeSelected)))
        leave1.setStartDate(getNowDateShort(getNextkDay(new Date(), startTimeSelected)));
        //getNowDateShort(getNextkDay(new Date(),endTimeSelected))
        leave1.setEndDate(getNowDateShort(getNextkDay(new Date(), endTimeSelected)));
        leave1.setStartNum(startOrderSelected + 1);
        leave1.setEndNum(endOrderSelected + 1);
        leave1.setContent(et_leave_reason.getText().toString());
        //转换成json数据，借助gson
        String json = new Gson().toJson(leave1);
        log(thisClass, json);
        //发送post请求注册
        String after =Constant.LEAVE_SUBMIT_URL;
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
                        runOnUiThread(() -> Toast.makeText(HomeCourseMoreLeaveSubmitActivity.this, "发送请求失败！", Toast.LENGTH_SHORT).show());
                        return;
                    }
                    log(thisClass, restResponse.getCode().toString());
                    //状态码500表示失败，打印错误信息
                    if (restResponse.getCode() == Constant.FAIL_ORIGIN_CODE) {
                        log(thisClass, restResponse.getMsg());
                        runOnUiThread(() -> Toast.makeText(HomeCourseMoreLeaveSubmitActivity.this, "发送请求失败！"+restResponse.getMsg(), Toast.LENGTH_SHORT).show());
                    }
                    //200代表成功，打印成功信息
                    if (restResponse.getCode() == Constant.SUCCESS_CODE) {
                        log(thisClass, "已成功发送");
                        runOnUiThread(() -> {
                            Toast.makeText(HomeCourseMoreLeaveSubmitActivity.this, "已成功发送！", Toast.LENGTH_SHORT).show();
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

}
