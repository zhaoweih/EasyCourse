package com.zhaoweihao.architechturesample.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.EncryptUtils;
import com.google.gson.Gson;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.activity.example.ValidationMesgService;
import com.zhaoweihao.architechturesample.base.BaseActivity;
import com.zhaoweihao.architechturesample.bean.Login;
import com.zhaoweihao.architechturesample.bean.RestResponse;
import com.zhaoweihao.architechturesample.bean.User;
import com.zhaoweihao.architechturesample.bean.UserDataAll;
import com.zhaoweihao.architechturesample.util.Constant;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.zhaoweihao.architechturesample.util.HttpUtil.sendPostRequest;
import static com.zhaoweihao.architechturesample.util.Utils.log;

/**
 * @author
 * @description 登陆界面首页
 * @time 2019/1/28 13:11
 */
public class MainLoginActivity extends BaseActivity implements View.OnClickListener {
    private static final Class thisClass = MainLoginActivity.class;
    private ImageButton ibtn_hidepassword, ibtn_clearpassword, ibtn_clearusername;
    private Button btn_register, btn_login, btn_returntohome;
    private EditText ed_username, ed_password;
    private Boolean passwordflag;
    Handler handler;
    Timer timer;
    TimerTask timerTask;

    public static int LOGIN_CALL_DOUBAN_CODE = 7;

    String HHH = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();



        timer.schedule(timerTask, 200, 200);
        stopService(new Intent(this, ValidationMesgService.class));
        //延时200ms，每隔200毫秒执行一次run方法
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
                Intent intent = new Intent(MainLoginActivity.this, MainRegisterActivity.class);
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
                } else if (!(ed_username.getText().toString().equals("") || ed_password.getText().toString().equals(""))) {
                    getUsers();
                }


                break;
            case R.id.btn_returntohome:
                startActivity(new Intent(MainLoginActivity.this, MainFindLostPassword.class));
               /* if (DataSupport.findLast(com.zhaoweihao.architechturesample.database.User.class) == null) {
                    Toast.makeText(MainLoginActivity.this, "请先登录！", Toast.LENGTH_SHORT).show();
                } else {
                    finish();
                    break;
                }*/
            default:
                break;
        }
    }

    public void initView() {
        ed_username = (EditText) findViewById(R.id.ed_username);
        ed_password = (EditText) findViewById(R.id.ed_password);

        ibtn_hidepassword = (ImageButton) findViewById(R.id.ibtn_hidepassword);
        ibtn_clearpassword = (ImageButton) findViewById(R.id.ibtn_clearpassword);
        ibtn_clearusername = (ImageButton) findViewById(R.id.ibtn_clearusername);

        btn_register = (Button) findViewById(R.id.btn_register);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_returntohome = (Button) findViewById(R.id.btn_returntohome);

        passwordflag = true;


        ibtn_clearpassword.setOnClickListener(this);
        ibtn_hidepassword.setOnClickListener(this);
        ibtn_clearusername.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        btn_returntohome.setOnClickListener(this);


        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 11 && (ibtn_clearusername.getVisibility() == View.VISIBLE || ibtn_clearpassword.getVisibility() == View.VISIBLE)) {
                    ibtn_clearusername.setVisibility(View.INVISIBLE);
                    ibtn_clearpassword.setVisibility(View.INVISIBLE);
                } else if (msg.what == 12 && (ibtn_clearusername.getVisibility() == View.VISIBLE || ibtn_clearpassword.getVisibility() == View.INVISIBLE)) {
                    ibtn_clearusername.setVisibility(View.INVISIBLE);
                    ibtn_clearpassword.setVisibility(View.VISIBLE);
                } else if (msg.what == 21 && (ibtn_clearusername.getVisibility() == View.INVISIBLE || ibtn_clearpassword.getVisibility() == View.VISIBLE)) {
                    ibtn_clearusername.setVisibility(View.VISIBLE);
                    ibtn_clearpassword.setVisibility(View.INVISIBLE);
                } else if (msg.what == 22 && (ibtn_clearusername.getVisibility() == View.INVISIBLE || ibtn_clearpassword.getVisibility() == View.INVISIBLE)) {
                    ibtn_clearusername.setVisibility(View.VISIBLE);
                    ibtn_clearpassword.setVisibility(View.VISIBLE);
                }
                super.handleMessage(msg);
            }
        };
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                if (ed_username.getText().toString().equals("")) {
                    if (ed_password.getText().toString().equals("")) {
                        message.what = 11;
                    } else {
                        message.what = 12;
                    }

                } else {
                    if (ed_password.getText().toString().equals("")) {
                        message.what = 21;
                    } else {
                        message.what = 22;
                    }
                }

                handler.sendMessage(message);
            }
        };
    }


    public void getUsers() {
        // 组装login对象
        Login login = new Login();
        login.setUsername(ed_username.getText().toString());
        login.setPassword(addSalt(ed_password.getText().toString()));
        //转换为json
        String json = new Gson().toJson(login);
        sendPostRequest(Constant.LOGIN_URL, json, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //网络错误处理
                runOnUiThread(() -> Toast.makeText(MainLoginActivity.this, "请检查网络！", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                log(thisClass, "保存到数据body" + body);

                //解析json数据组装RestResponse对象
                RestResponse restResponse = JSON.parseObject(body, RestResponse.class);
                //RestResponse restResponse = new Gson().fromJson(body, RestResponse.class);

                // 修复登录密码不正确异常
                if (restResponse == null) {
                    runOnUiThread(() -> Toast.makeText(MainLoginActivity.this, "请检查密码", Toast.LENGTH_SHORT).show());
                    return;
                }
                if (restResponse.getCode() == Constant.FAIL_CODE) {
                    log(thisClass, "登录失败，请检查用户名和密码");
                    try {
                        //切换主进程更新UI
                        runOnUiThread(() -> {
                            Toast.makeText(MainLoginActivity.this, "登陆失败:" + restResponse.getMsg(), Toast.LENGTH_SHORT).show();
                            ed_username.setText("");
                            ed_password.setText("");

                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (restResponse.getCode() == Constant.SUCCESS_CODE) {
                    try {
                        List<com.zhaoweihao.architechturesample.database.User> allData = DataSupport.findAll(com.zhaoweihao.architechturesample.database.User.class);
                        if (allData.size() > 0) {
                            com.zhaoweihao.architechturesample.database.User userTemp = DataSupport.findLast(com.zhaoweihao.architechturesample.database.User.class);
                            userTemp.delete();
                        }
                       /* 解析登陆信息
                       String useName=restResponse.getPayload().toString();
                        int begin=useName.indexOf("{id=");
                        int last=useName.indexOf("},");
                        String OnlyUser=useName.substring(begin,last)+"}";
                        log(thisClass, "OnlyUser is :  "+OnlyUser);

                        begin=useName.indexOf("{token=");
                        last=useName.indexOf(" user");
                        String TempToken =useName.substring(begin,last);
                        String OnlyToken=TempToken.substring(7,TempToken.length()-1);
                        log(thisClass, "OnlyToken is :  "+OnlyToken);

                        begin=useName.indexOf("expiredDate=");
                        last=useName.length();
                        String TempDate =useName.substring(begin,last);
                        String OnlyExpireDate=TempDate.substring(12,TempDate.length()-1);
                        log(thisClass, "OnlyExpireDate is :  "+OnlyExpireDate);*/

                        UserDataAll userDataAll = JSON.parseObject(restResponse.getPayload().toString(), UserDataAll.class);
                        User user = JSON.parseObject(userDataAll.getUser().toString(), User.class);
                        //User user = new Gson().fromJson(OnlyUser,User.class);
                        com.zhaoweihao.architechturesample.database.User user1 = new com.zhaoweihao.architechturesample.database.User();
                        // user1.setUserId(user.getId());
                       /* user1.setUsername(user.getUsername());
                        user1.setStudentId(user.getStudentId());
                        user1.setClassId(user.getClassId());
                        user1.setDepartment(user.getDepartment());
                        user1.setEducation(user.getEducation());
                        user1.setDate(user.getDate());
                        user1.setTeacherId(user.getTeacherId());
                        user1.setSchool(user.getSchool());
                        user1.setSex(user.getSex());
                        user1.setName(user.getName());*/
                        user1.setToken(userDataAll.getToken());
                        user1.setExpiredDate(userDataAll.getExpiredDate());
                        /*user1.setToken(OnlyToken);
                        user1.setExpiredDate(OnlyExpireDate);*/
                        user1.setUserId(user.getId());
                        user1.setUsername(user.getUsername());
                        user1.setStudentId(user.getStudentId());
                        user1.setClassId(user.getClassId());
                        user1.setDepartment(user.getDepartment());
                        user1.setEducation(user.getEducation());
                        user1.setDate(user.getDate());
                        user1.setTeacherId(user.getTeacherId());
                        user1.setDescrition(user.getDescrition());
                        user1.setPhone(user.getPhone());
                        user1.setSchool(user.getSchool());
                        user1.setSex(user.getSex());
                        user1.setName(user.getName());
                        if (!TextUtils.isEmpty(user.getQuestion())) {
                            user1.setQuestion(user.getQuestion());
                        }
                        if (!TextUtils.isEmpty(user.getAnswer())) {
                            user1.setAnswer(user.getAnswer());
                        }
                        if (!TextUtils.isEmpty(user.getMd5_password())) {
                            user1.setMd5_password(user.getMd5_password());
                        }
                        user1.setAvatar(user.getAvatar());
                        //保存到数据库
                        user1.save();

                        sharedPreferences.user_id().put(user.getId());
                        sharedPreferences.username().put(user.getUsername());

                        List<com.zhaoweihao.architechturesample.database.User> allNews = DataSupport.findAll(com.zhaoweihao.architechturesample.database.User.class);
                        log(thisClass, "保存到数据库成功 id 为：" + user1.getUserId() + "********usename为" + user1.getUsername() + "当前所有的user个数" + allNews.size());
                    } catch (Exception e) {
                        e.printStackTrace();
                        log(thisClass, "就是这里错了！" + e.toString());
                    }
                    //首先获取payload (Object) , toString()转换成json
                    //接着用gson将json组装起来
                    //切换主进程更新UI
                    try {
                        runOnUiThread(() -> {
                            Toast.makeText(MainLoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                            // 发送全局事件
                            EventBus.getDefault().post(new MessageEvent());
                            finish();
                            startActivity(new Intent(MainLoginActivity.this,MainActivity.class));
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }
        });

    }

    public static class MessageEvent { /* Additional fields if needed */
    }

    //设置返回按钮：不应该退出程序---而是返回桌面
    //复写onKeyDown事件
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    private String addSalt(String plainPassword){
        //md5加密
        String md5PlainPassword = EncryptUtils.encryptMD5ToString(plainPassword);
        //md5密码后加盐
        String salt = "6NCkDWrVJy5K9v2w";
        String saltPassword = md5PlainPassword + salt;
        //加盐后的密码再MD5加密(最终密码)
        return EncryptUtils.encryptMD5ToString(saltPassword);
    }

}
