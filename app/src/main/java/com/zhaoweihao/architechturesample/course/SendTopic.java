package com.zhaoweihao.architechturesample.course;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.data.RestResponse;
import com.zhaoweihao.architechturesample.database.User;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.zhaoweihao.architechturesample.util.HttpUtil.sendPostRequest;
import static com.zhaoweihao.architechturesample.util.Utils.log;

public class SendTopic extends AppCompatActivity implements View.OnClickListener{
    private static final Class thisClass = SendTopic.class;
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
    //显示选择截止日期的数组
    String expires[] = new String[7];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_topic);
        initViews();
    }
    private void initViews() {
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
                submittopic();
                break;
            case R.id.tv_sendtopic_date:
                new AlertDialog.Builder(SendTopic.this).setItems(expires, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        expireTime = i;
                    }
                }).create().show();
                break;
            case R.id.tv_sendtopic_order:
                String addDays[]={"不增加天数", "已增加7天后截止", "已增加21天后截止", "已增加28天后截止", "已增加30天后截止", "已增加31天后截止"};
                new AlertDialog.Builder(SendTopic.this).setItems(addDays, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        tv_sendtopic_order.setText(addDays[i]);
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
                    }
                }).create().show();
                break;
            case R.id.et_sendtopic_content:
                break;
        }
    }
    public void submittopic() {

        com.zhaoweihao.architechturesample.data.course.SendTopic sendtopic=new com.zhaoweihao.architechturesample.data.course.SendTopic();
        sendtopic.setCourseId(4);
        sendtopic.setStatus(1);
        DateFormat bf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        sendtopic.setContent(et_sendtopic_content.getText().toString());
        sendtopic.setStartDate(bf.format(new Date()));
        sendtopic.setEndDate(bf.format(new Date()));
        sendtopic.setTecId(DataSupport.findLast(User.class).getUserId());
        sendtopic.setTeacherId(DataSupport.findLast(User.class).getTeacherId());
        //转换成json数据，借助gson
        String json = new Gson().toJson(sendtopic);
        log(thisClass, json);
        //发送post请求注册
        String after = "discuss/add";
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
                        runOnUiThread(() -> Toast.makeText(SendTopic.this, "发送请求失败！", Toast.LENGTH_SHORT).show());
                        return;
                    }
                    log(thisClass, restResponse.getCode().toString());
                    //状态码500表示失败，打印错误信息
                    if (restResponse.getCode() == 500) {
                        log(thisClass, restResponse.getMsg());
                        runOnUiThread(() -> Toast.makeText(SendTopic.this, "发送请求失败！"+restResponse.getMsg(), Toast.LENGTH_SHORT).show());
                    }
                    //200代表成功，打印成功信息
                    if (restResponse.getCode() == 200) {
                        log(thisClass, "已成功发送");
                        runOnUiThread(() -> {
                            Toast.makeText(SendTopic.this, "已成功发送！", Toast.LENGTH_SHORT).show();
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
