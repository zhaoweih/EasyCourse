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
import com.zhaoweihao.architechturesample.data.Login;
import com.zhaoweihao.architechturesample.data.RestResponse;
import com.zhaoweihao.architechturesample.data.User;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
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
    private ImageButton ibtn_hidepassword, ibtn_clearpassword,ibtn_clearusername;
    private Button btn_register,btn_login,btn_returntohome;
    private EditText ed_username, ed_password;
    private Boolean passwordflag;
    Handler handler;
    Timer timer;
    TimerTask timerTask;

    public static int LOGIN_CALL_DOUBAN_CODE = 7;

    String HHH="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();

        timer.schedule(timerTask,200,200);//延时200ms，每隔200毫秒执行一次run方法
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
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
            case R.id.btn_register:
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
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
            case R.id.btn_login:
               if (ed_username.getText().toString().equals("") || ed_password.getText().toString().equals("")) {
                    Toast.makeText(this, "请输入完整！", Toast.LENGTH_SHORT).show();
                } else if(!(ed_username.getText().toString().equals("") || ed_password.getText().toString().equals(""))){
                getUsers();
                }




            break;
            case R.id.btn_returntohome:
                if(DataSupport.findLast(com.zhaoweihao.architechturesample.database.User.class)==null){
                    Toast.makeText(LoginActivity.this,"请先登录！",Toast.LENGTH_SHORT).show();
                }else {
                    finish();
                    break;
                }
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
        btn_returntohome=(Button)findViewById(R.id.btn_returntohome);

        passwordflag = true;


        ibtn_clearpassword.setOnClickListener(this);
        ibtn_hidepassword.setOnClickListener(this);
        ibtn_clearusername.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        btn_returntohome.setOnClickListener(this);


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


    public  void testlitepal(){

        try {
            //插入一个user数据之前,将上一个删掉，现在保持user在13个，（现在是随机刚好测试到13个,没有把前面的没用的删掉）
            // annotated by TanXinKui 18/5/15
            com.zhaoweihao.architechturesample.database.User user3 = DataSupport.findLast(com.zhaoweihao.architechturesample.database.User.class);
            user3.delete();
            com.zhaoweihao.architechturesample.database.User user = new com.zhaoweihao.architechturesample.database.User();
            user.setUserId(1000);
            user.setUsername("zhaoweihao22");
            user.setStudentId("2015191054");
            user.setClassId("20151912");
            user.setDepartment("教育系");
            user.setEducation(1);
            user.setDate("2015");
            user.setSchool("韩山师范学院");
            user.setSex(1);
            user.setName("谭新奎测试13");
            //保存到数据库
            user.save();

        } catch (Exception e) {
            e.printStackTrace();
        }
        log(thisClass, "保存成功");

    }
    public void getUsers(){

        String suffix = "user/login";
        // 组装login对象
        Login login = new Login();
        login.setUsername(ed_username.getText().toString());
        login.setPassword(ed_password.getText().toString());
        //转换为json
        String json = new Gson().toJson(login);
        sendPostRequest(suffix, json, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //网络错误处理
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                log(thisClass, "保存到数据body"+body);
                //解析json数据组装RestResponse对象
                RestResponse restResponse = new Gson().fromJson(body, RestResponse.class);

                // 修复登录密码不正确异常
                if (restResponse == null) {
                    runOnUiThread(() -> Toast.makeText(LoginActivity.this, "请检查密码", Toast.LENGTH_SHORT).show());
                    return;
                }

                if ( restResponse.getCode() == 500 ) {
                    log(thisClass, "登录失败，请检查用户名和密码");
                    try {
                        //切换主进程更新UI
                        runOnUiThread(() -> {
                            Toast.makeText(LoginActivity.this, "登陆失败"+restResponse.getMsg(), Toast.LENGTH_SHORT).show();
                            ed_username.setText("");
                            ed_password.setText("");

                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                // code 200等于登录成功
                if ( restResponse.getCode() == 200 ) {
                    try {
                        List<com.zhaoweihao.architechturesample.database.User> allDatas = DataSupport.findAll(com.zhaoweihao.architechturesample.database.User.class);
                        if(allDatas.size()>0){
                            com.zhaoweihao.architechturesample.database.User user3 = DataSupport.findLast(com.zhaoweihao.architechturesample.database.User.class);
                            user3.delete();
                        }
                        com.zhaoweihao.architechturesample.data.User user = new Gson().fromJson(restResponse.getPayload().toString(),com.zhaoweihao.architechturesample.data.User.class);
                        com.zhaoweihao.architechturesample.database.User user1 = new com.zhaoweihao.architechturesample.database.User();
                        user1.setUserId(user.getId());
                        user1.setUsername(user.getUsername());
                        user1.setStudentId(user.getStudentId());
                        user1.setClassId(user.getClassId());
                        user1.setDepartment(user.getDepartment());
                        user1.setEducation(user.getEducation());
                        user1.setDate(user.getDate());
                        user1.setTeacherId(user.getTeacherId());
                        user1.setSchool(user.getSchool());
                        user1.setSex(user.getSex());
                        user1.setName(user.getName());
                        //保存到数据库
                        user1.save();
                        List<com.zhaoweihao.architechturesample.database.User> allNews = DataSupport.findAll(com.zhaoweihao.architechturesample.database.User.class);

                        log(thisClass, "保存到数据库成功"+user.getId()+"**********"+user1.getUserId()+"当前所有的user个数"+allNews.size());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //首先获取payload (Object) , toString()转换成json
                    //接着用gson将json组装起来
                    //切换主进程更新UI
                    try {
                        runOnUiThread(() -> {
                            Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                            // 发送全局事件
                            EventBus.getDefault().post(new MessageEvent());
                          finish();
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }



                }
            }
        });

    }

    public static class MessageEvent { /* Additional fields if needed */ }




}
