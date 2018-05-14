package com.zhaoweihao.architechturesample.ui;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.data.RestResponse;
import com.zhaoweihao.architechturesample.data.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.zhaoweihao.architechturesample.util.HttpUtil.sendGetRequest;
import static com.zhaoweihao.architechturesample.util.HttpUtil.sendPostRequest;
import static com.zhaoweihao.architechturesample.util.Utils.log;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final Class thisClass = LoginActivity.class;
    ImageButton ibtn_hidepassword, ibtn_clearpassword,ibtn_clearusername;
    Button btn_register,btn_login;
    EditText ed_username, ed_password;
    Boolean passwordflag;
    Handler handler;
    Timer timer;
    TimerTask timerTask;

    String HHH="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();

        timer.schedule(timerTask,200,200);//延时200ms，每隔200毫秒执行一次run方法
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibtn_clearusername:
                ed_username.setText("");
                break;
            case R.id.ibtn_clearpassword:
                ed_password.setText("");
                break;
            case R.id.ibtn_hidepassword:
                if (passwordflag) {
                    ed_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    passwordflag = false;
                    ed_password.setSelection(ed_password.getText().toString().length());
                    Resources resources = this.getResources();
                    Drawable btnDrawable1 = resources.getDrawable(R.drawable.showpassword1);
                    ibtn_hidepassword.setBackground(btnDrawable1);

                } else {
                    ed_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    passwordflag = true;
                    ed_password.setSelection(ed_password.getText().toString().length());
                    Resources resources = this.getResources();
                    Drawable btnDrawable1 = resources.getDrawable(R.drawable.showpassword2);
                    ibtn_hidepassword.setBackground(btnDrawable1);
                }
                break;
            case R.id.btn_register:
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            case R.id.btn_login:
                getUsers();
            break;
        }
    }

    public void initView() {
        ed_username = (EditText) findViewById(R.id.ed_username);
        ed_password = (EditText) findViewById(R.id.ed_password);

        ibtn_hidepassword = (ImageButton) findViewById(R.id.ibtn_hidepassword);
        ibtn_clearpassword = (ImageButton) findViewById(R.id.ibtn_clearpassword);
        ibtn_clearusername=(ImageButton)findViewById(R.id.ibtn_clearusername);

        btn_register=(Button)findViewById(R.id.btn_register);
        btn_login=(Button)findViewById(R.id.btn_login);

        passwordflag = true;


        ibtn_clearpassword.setOnClickListener(this);
        ibtn_hidepassword.setOnClickListener(this);
        ibtn_clearusername.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        btn_login.setOnClickListener(this);


        handler= new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 11&&(ibtn_clearusername.getVisibility()==View.VISIBLE||ibtn_clearpassword.getVisibility()==View.VISIBLE)){
                    ibtn_clearusername.setVisibility(View.INVISIBLE);
                    ibtn_clearpassword.setVisibility(View.INVISIBLE);
                }else if(msg.what == 12&&(ibtn_clearusername.getVisibility()==View.VISIBLE||ibtn_clearpassword.getVisibility()==View.INVISIBLE)){
                    ibtn_clearusername.setVisibility(View.INVISIBLE);
                    ibtn_clearpassword.setVisibility(View.VISIBLE);
                }else if(msg.what == 21&&(ibtn_clearusername.getVisibility()==View.INVISIBLE||ibtn_clearpassword.getVisibility()==View.VISIBLE)){
                    ibtn_clearusername.setVisibility(View.VISIBLE);
                    ibtn_clearpassword.setVisibility(View.INVISIBLE);
                }else if(msg.what == 22&&(ibtn_clearusername.getVisibility()==View.INVISIBLE||ibtn_clearpassword.getVisibility()==View.INVISIBLE)){
                    ibtn_clearusername.setVisibility(View.VISIBLE);
                    ibtn_clearpassword.setVisibility(View.VISIBLE);
                }
                super.handleMessage(msg);
            }
        };
        timer= new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                if(ed_username.getText().toString().equals("")){
                    if(ed_password.getText().toString().equals("")){
                        message.what =11;
                    }else{
                        message.what =12;
                    }

                }else{
                    if(ed_password.getText().toString().equals("")){
                        message.what =21;
                    }else{
                        message.what =22;
                    }
                }

                handler.sendMessage(message);
            }
        };
    }
    public void getUsers(){

        //假设一些数据
        //以注册功能为例
        /**
         * @id id int (不需要提交，数据库自动生成)
         * @username 用户名
         * @password 密码
         * @studentId 学号
         * @teacherId 教师编号
         * @classId 班级编号
         * @department 学院
         * @education 学历 int
         * @date 入学时间
         * @school 学校
         * @sex 性别 int
         * @name 真实姓名
         */
        User user = new User();
        user.setUsername("zhaoweihao22");
        user.setPassword("abssss");
        //转换成json数据，借助gson
        String json = new Gson().toJson(user);
        log(thisClass,json);
        //发送post请求注册
        String after = "user/login";

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
                    RestResponse restResponse= new Gson().fromJson(body, RestResponse.class);
                    log(thisClass, restResponse.getCode().toString());
                    //状态码500表示失败，打印错误信息
                    if (restResponse.getCode() == 500) {
                        log(thisClass, restResponse.getMsg());
                        log(thisClass, restResponse.getCode().toString());
                    }
                    //200代表成功，打印成功信息
                    if (restResponse.getCode() == 200) {
                        log(thisClass, "已成功注册");
                        //执行注册成功后的操作
                        //...
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                log(thisClass, body);
            }
        });


    }

}
