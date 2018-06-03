package com.zhaoweihao.architechturesample.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.data.RestResponse;
import com.zhaoweihao.architechturesample.data.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.zhaoweihao.architechturesample.util.HttpUtil.sendPostRequest;
import static com.zhaoweihao.architechturesample.util.Utils.log;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private static final Class thisClass = RegisterActivity.class;
    private ImageButton ibtn_hidepassword, ibtn_clearpassword, ibtn_clearusername, ibtn_clearpasswordconfirm,
            ibtn_hidepasswordconfirm;
    private Button btn_submit;
    private EditText ed_username, ed_password, ed_passwordconfirm, et_name, et_teachernum, et_classnum, et_studentnum;
    private Boolean passwordflag, passwordconfirmflag, finishState, failState;
    String failmsg;
    Handler handler, myhandler;
    ImageView iv_shcool0,iv_faculty0,iv_postion0,iv_sex0,iv_year0,iv_degree0;
    TextView tv_bottom1,tv_school0, tv_faculty0, tv_year0, tv_position0, tv_degree0, tv_sex0,tv_classnum0,tv_studentnum0,tv_name0,tv_teachernum0;
    TextView tv_school, tv_faculty, tv_year, tv_position, tv_degree,tv_sex;
    Timer timer;
    TimerTask timerTask;

    Runnable updateThread;

    final  int REQUEST_ACTIVITY1 = 1, REQUEST_ACTIVITY2 = 2, REQUEST_ACTIVITY3 = 3;
     int checknum, checknum1, checknum2;
    String selectedUniversity, selectedFaculty, selectedYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        initRunnable();
        Toast.makeText(this, "注意更改默认教育信息！", Toast.LENGTH_SHORT).show();
        timer.schedule(timerTask, 200, 200);//延时1s，每隔500毫秒执行一次run方法
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibtn_clearusername:
                ed_username.setText("");
                break;
            case R.id.ibtn_clearpassword:
                ed_password.setText("");
                break;
            case R.id.ibtn_clearpasswordconfirm:
                ed_passwordconfirm.setText("");
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
            case R.id.ibtn_hidepasswordconfirm:
                if (passwordconfirmflag) {
                    ed_passwordconfirm.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    passwordconfirmflag = false;
                    ed_passwordconfirm.setSelection(ed_passwordconfirm.getText().toString().length());
                    Resources resources = this.getResources();
                    Drawable btnDrawable1 = resources.getDrawable(R.drawable.showpassword1);
                    ibtn_hidepasswordconfirm.setBackground(btnDrawable1);

                } else {
                    ed_passwordconfirm.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    passwordconfirmflag = true;
                    ed_passwordconfirm.setSelection(ed_passwordconfirm.getText().toString().length());
                    Resources resources = this.getResources();
                    Drawable btnDrawable1 = resources.getDrawable(R.drawable.showpassword2);
                    ibtn_hidepasswordconfirm.setBackground(btnDrawable1);
                }
                break;
            case R.id.btn_submit:
                if (ed_username.getText().toString().equals("") || ed_password.getText().toString().equals("") || ed_passwordconfirm.getText().toString().equals("")) {
                    Toast.makeText(this, "请输入完整！", Toast.LENGTH_SHORT).show();
                } else if (!ed_password.getText().toString().equals(ed_passwordconfirm.getText().toString())) {
                    Toast.makeText(this, "两次输入密码不一致", Toast.LENGTH_SHORT).show();
                } else if (!(ed_username.getText().toString().equals("") || ed_password.getText().toString().equals("") || ed_passwordconfirm.getText().toString().equals(""))) {
                    if (tv_position.getText().toString().equals("学生")) {
                        if (et_studentnum.getText().toString().equals("") || et_classnum.getText().toString().equals("") || et_name.getText().toString().equals("")) {
                            Toast.makeText(this, "请输入完整！", Toast.LENGTH_SHORT).show();
                        } else {
                            AlertDialog alert = new AlertDialog.Builder(RegisterActivity.this).setTitle("温馨提示")
                                    .setMessage("注册后部分数据将无法更改，确定要注册？")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {//设置确定按钮
                                        @Override//处理确定按钮点击事件
                                        public void onClick(DialogInterface dialog, int which) {
                                            submit();
                                            myhandler.post(updateThread);
                                        }
                                    })
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener(){
                                        @Override
                                        public void onClick (DialogInterface dialog,int which){
                                            dialog.cancel();//对话框关闭。
                                        }
                                    }).create();
                            alert.show();
                        }
                    } else if (tv_position.getText().toString().equals("老师")) {
                        if (et_teachernum.getText().toString().equals("") || et_name.getText().toString().equals("")) {
                            Toast.makeText(this, "请输入完整！", Toast.LENGTH_SHORT).show();
                        } else {
                            AlertDialog alert = new AlertDialog.Builder(RegisterActivity.this).setTitle("温馨提示")
                                    .setMessage("注册后部分数据将无法更改，确定要注册？")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {//设置确定按钮
                                        @Override//处理确定按钮点击事件
                                        public void onClick(DialogInterface dialog, int which) {
                                            submit();
                                            myhandler.post(updateThread);
                                        }
                                    })
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener(){
                                        @Override
                                        public void onClick (DialogInterface dialog,int which){
                                            dialog.cancel();//对话框关闭。
                                        }
                                    }).create();
                            alert.show();
                        }
                    }
                }
                break;
            case R.id.tv_bottom1:
                // 修改代码
//                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
//                startActivity(intent);
                finish();
                break;
            case R.id.tv_school0:
                try {
                    Intent intent1 = new Intent(RegisterActivity.this, RegisterListView.class);
                    Bundle data1 = new Bundle();
                    data1.putString("pic", "first");
                    data1.putString("selectedUniversity", selectedUniversity);
                    data1.putInt("checknum", checknum);
                    intent1.putExtras(data1);
                    startActivityForResult(intent1, REQUEST_ACTIVITY1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.tv_faculty0:
                try {
                    Intent intent2 = new Intent(RegisterActivity.this, RegisterListView.class);
                    Bundle data2 = new Bundle();
                    data2.putString("pic", "second");
                    data2.putString("selectedFaculty", selectedFaculty);
                    data2.putInt("checknum1", checknum1);
                    intent2.putExtras(data2);
                    startActivityForResult(intent2, REQUEST_ACTIVITY2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.tv_year0:
                try {
                    Intent intent3 = new Intent(RegisterActivity.this, RegisterListView.class);
                    Bundle data = new Bundle();
                    data.putString("pic", "third");
                    data.putString("selectedYear", selectedYear);
                    data.putInt("checknum2", checknum2);
                    intent3.putExtras(data);
                    startActivityForResult(intent3, REQUEST_ACTIVITY3);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.tv_position0:
                final String positions[] = {"学生", "老师"};
                AlertDialog.Builder bd = new AlertDialog.Builder(RegisterActivity.this);
                bd.setItems(positions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            tv_year0.setVisibility(View.VISIBLE);
                            tv_degree0.setVisibility(View.VISIBLE);
                            tv_studentnum0.setVisibility(View.VISIBLE);
                            tv_classnum0.setVisibility(View.VISIBLE);

                            iv_year0.setVisibility(View.VISIBLE);
                            iv_degree0.setVisibility(View.VISIBLE);

                            tv_year.setVisibility(View.VISIBLE);
                            tv_degree.setVisibility(View.VISIBLE);
                            et_studentnum.setVisibility(View.VISIBLE);
                            et_classnum.setVisibility(View.VISIBLE);


                            tv_teachernum0.setVisibility(View.GONE);
                            et_teachernum.setVisibility(View.GONE);

                        } else {

                            tv_teachernum0.setVisibility(View.VISIBLE);
                            et_teachernum.setVisibility(View.VISIBLE);

                            tv_year0.setVisibility(View.GONE);
                            tv_degree0.setVisibility(View.GONE);
                            tv_year.setVisibility(View.GONE);
                            tv_degree.setVisibility(View.GONE);

                            iv_year0.setVisibility(View.GONE);
                            iv_degree0.setVisibility(View.GONE);

                            tv_classnum0.setVisibility(View.GONE);
                            et_classnum.setVisibility(View.GONE);

                            tv_studentnum0.setVisibility(View.GONE);
                            et_studentnum.setVisibility(View.GONE);

                        }
                        tv_position.setText(positions[i]);
                    }
                });
                bd.create().show();
                break;
            case R.id.tv_degree0:
                final String degrees[] = {"本科/专科", "研究生"};
                AlertDialog.Builder bd1 = new AlertDialog.Builder(RegisterActivity.this);
                bd1.setItems(degrees, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        tv_degree.setText(degrees[i]);
                    }
                });
                bd1.create().show();
                break;
            case R.id.tv_sex0:
                final String sex[] = {"男", "女"};
                AlertDialog.Builder bd2 = new AlertDialog.Builder(RegisterActivity.this);
                bd2.setItems(sex, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        tv_sex.setText(sex[i]);
                    }
                });
                bd2.create().show();
                break;

        }
    }

    public void initView() {
        checknum = -1;
        checknum1 = -1;
        checknum2 = -1;


        selectedUniversity = "韩山师范学院";
        selectedFaculty = "教育学系";
        selectedYear = "2015";

        ed_username = (EditText) findViewById(R.id.ed_username);
        ed_password = (EditText) findViewById(R.id.ed_password);
        ed_passwordconfirm = (EditText) findViewById(R.id.ed_passwordconfirm);
        et_name = (EditText) findViewById(R.id.et_name);
        et_classnum = (EditText) findViewById(R.id.et_classnum);
        et_teachernum = (EditText) findViewById(R.id.et_teachernum);
        et_studentnum = (EditText) findViewById(R.id.et_studentnum);

        ibtn_hidepassword = (ImageButton) findViewById(R.id.ibtn_hidepassword);
        ibtn_clearpassword = (ImageButton) findViewById(R.id.ibtn_clearpassword);
        ibtn_clearusername = (ImageButton) findViewById(R.id.ibtn_clearusername);
        ibtn_clearpasswordconfirm = (ImageButton) findViewById(R.id.ibtn_clearpasswordconfirm);
        ibtn_hidepasswordconfirm = (ImageButton) findViewById(R.id.ibtn_hidepasswordconfirm);

        btn_submit = (Button) findViewById(R.id.btn_submit);


        tv_school0 = (TextView) findViewById(R.id.tv_school0);
        tv_faculty0 = (TextView) findViewById(R.id.tv_faculty0);
        tv_year0 = (TextView) findViewById(R.id.tv_year0);
        tv_position0 = (TextView) findViewById(R.id.tv_position0);
        tv_degree0 = (TextView) findViewById(R.id.tv_degree0);

        tv_bottom1 = (TextView) findViewById(R.id.tv_bottom1);
        iv_shcool0=(ImageView)findViewById(R.id.iv_school0);
        iv_faculty0=(ImageView)findViewById(R.id.iv_faculty0);
        iv_postion0=(ImageView)findViewById(R.id.iv_position0);
        iv_sex0=(ImageView)findViewById(R.id.iv_sex0);
        iv_year0=(ImageView)findViewById(R.id.iv_year0);
        iv_degree0=(ImageView)findViewById(R.id.iv_degree0);


        tv_sex0 = (TextView) findViewById(R.id.tv_sex0);
        tv_classnum0 = (TextView) findViewById(R.id.tv_classnum0);
        tv_studentnum0 = (TextView) findViewById(R.id.tv_studentnum0);
        tv_name0 = (TextView) findViewById(R.id.tv_name0);
        tv_teachernum0=(TextView) findViewById(R.id.tv_teachernum0);


        tv_school = (TextView) findViewById(R.id.tv_school);
        tv_faculty = (TextView) findViewById(R.id.tv_faculty);
        tv_year = (TextView) findViewById(R.id.tv_year);
        tv_position = (TextView) findViewById(R.id.tv_position);
        tv_degree = (TextView) findViewById(R.id.tv_degree);
        tv_sex=(TextView)findViewById(R.id.tv_sex);

        passwordflag = true;
        passwordconfirmflag = true;
        finishState = false;
        failState = false;

        ibtn_clearpassword.setOnClickListener(this);
        ibtn_hidepassword.setOnClickListener(this);
        ibtn_clearusername.setOnClickListener(this);
        ibtn_clearpasswordconfirm.setOnClickListener(this);
        ibtn_hidepasswordconfirm.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        tv_bottom1.setOnClickListener(this);
        tv_school0.setOnClickListener(this);
        tv_faculty0.setOnClickListener(this);
        tv_position0.setOnClickListener(this);
        tv_year0.setOnClickListener(this);
        tv_degree0.setOnClickListener(this);
        tv_sex0.setOnClickListener(this);


        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                HandleMessageInformation(msg);
                super.handleMessage(msg);
            }
        };
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                runInformation();
            }
        };
    }

    public void HandleMessageInformation(Message msg) {
        if (msg.what == 111 && (ibtn_clearusername.getVisibility() == View.VISIBLE || ibtn_clearpassword.getVisibility() == View.VISIBLE || ibtn_clearpasswordconfirm.getVisibility() == View.VISIBLE)) {
            ibtn_clearusername.setVisibility(View.INVISIBLE);
            ibtn_clearpassword.setVisibility(View.INVISIBLE);
            ibtn_clearpasswordconfirm.setVisibility(View.INVISIBLE);
        } else if (msg.what == 112 && (ibtn_clearusername.getVisibility() == View.VISIBLE || ibtn_clearpassword.getVisibility() == View.VISIBLE || ibtn_clearpasswordconfirm.getVisibility() == View.INVISIBLE)) {
            ibtn_clearusername.setVisibility(View.INVISIBLE);
            ibtn_clearpassword.setVisibility(View.INVISIBLE);
            ibtn_clearpasswordconfirm.setVisibility(View.VISIBLE);
        } else if (msg.what == 121 && (ibtn_clearusername.getVisibility() == View.VISIBLE || ibtn_clearpassword.getVisibility() == View.INVISIBLE || ibtn_clearpasswordconfirm.getVisibility() == View.VISIBLE)) {
            ibtn_clearusername.setVisibility(View.INVISIBLE);
            ibtn_clearpassword.setVisibility(View.VISIBLE);
            ibtn_clearpasswordconfirm.setVisibility(View.INVISIBLE);
        } else if (msg.what == 122 && (ibtn_clearusername.getVisibility() == View.VISIBLE || ibtn_clearpassword.getVisibility() == View.INVISIBLE || ibtn_clearpasswordconfirm.getVisibility() == View.INVISIBLE)) {
            ibtn_clearusername.setVisibility(View.INVISIBLE);
            ibtn_clearpassword.setVisibility(View.VISIBLE);
            ibtn_clearpasswordconfirm.setVisibility(View.VISIBLE);
        } else if (msg.what == 211 && (ibtn_clearusername.getVisibility() == View.INVISIBLE || ibtn_clearpassword.getVisibility() == View.VISIBLE || ibtn_clearpasswordconfirm.getVisibility() == View.VISIBLE)) {
            ibtn_clearusername.setVisibility(View.VISIBLE);
            ibtn_clearpassword.setVisibility(View.INVISIBLE);
            ibtn_clearpasswordconfirm.setVisibility(View.INVISIBLE);
        } else if (msg.what == 212 && (ibtn_clearusername.getVisibility() == View.INVISIBLE || ibtn_clearpassword.getVisibility() == View.VISIBLE || ibtn_clearpasswordconfirm.getVisibility() == View.INVISIBLE)) {
            ibtn_clearusername.setVisibility(View.VISIBLE);
            ibtn_clearpassword.setVisibility(View.INVISIBLE);
            ibtn_clearpasswordconfirm.setVisibility(View.VISIBLE);
        } else if (msg.what == 221 && (ibtn_clearusername.getVisibility() == View.INVISIBLE || ibtn_clearpassword.getVisibility() == View.INVISIBLE || ibtn_clearpasswordconfirm.getVisibility() == View.VISIBLE)) {
            ibtn_clearusername.setVisibility(View.VISIBLE);
            ibtn_clearpassword.setVisibility(View.VISIBLE);
            ibtn_clearpasswordconfirm.setVisibility(View.INVISIBLE);
        } else if (msg.what == 222 && (ibtn_clearusername.getVisibility() == View.INVISIBLE || ibtn_clearpassword.getVisibility() == View.INVISIBLE || ibtn_clearpasswordconfirm.getVisibility() == View.INVISIBLE)) {
            ibtn_clearusername.setVisibility(View.VISIBLE);
            ibtn_clearpassword.setVisibility(View.VISIBLE);
            ibtn_clearpasswordconfirm.setVisibility(View.VISIBLE);
        }
    }

    public void runInformation() {
        Message message = new Message();
        if (ed_username.getText().toString().equals("")) {

            if (ed_password.getText().toString().equals("")) {
                if (ed_passwordconfirm.getText().toString().equals("")) {
                    message.what = 111;
                } else {
                    message.what = 112;
                }
            } else {
                if (ed_passwordconfirm.getText().toString().equals("")) {
                    message.what = 121;
                } else {
                    message.what = 122;
                }
            }

        } else {
            if (ed_password.getText().toString().equals("")) {
                if (ed_passwordconfirm.getText().toString().equals("")) {
                    message.what = 211;
                } else {
                    message.what = 212;
                }
            } else {
                if (ed_passwordconfirm.getText().toString().equals("")) {
                    message.what = 221;
                } else {
                    message.what = 222;
                }
            }
        }
        handler.sendMessage(message);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode, data);
        if (requestCode == REQUEST_ACTIVITY1) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                tv_school.setText(bundle.getString("backInformation"));
                selectedUniversity = bundle.getString("backInformation");
                checknum = bundle.getInt("checknum", -1);
            }
        } else if (requestCode == REQUEST_ACTIVITY2) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                tv_faculty.setText(bundle.getString("backInformation"));
                selectedFaculty = bundle.getString("backInformation");
                checknum1 = bundle.getInt("checknum1", -1);
            }
        } else if (requestCode == REQUEST_ACTIVITY3) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                tv_year.setText(bundle.getString("backInformation"));
                selectedYear = bundle.getString("backInformation");
                checknum2 = bundle.getInt("checknum2", -1);
            }
        }
    }

    public void submit() {
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
        user.setUsername(ed_username.getText().toString());
        user.setPassword(ed_password.getText().toString());

        user.setDepartment(tv_faculty.getText().toString());
        if (tv_degree.getText().toString().equals("专科/本科")) {
            user.setEducation(1);
        } else {
            user.setEducation(2);
        }
        if (tv_position.getText().toString().equals("学生")) {
            user.setStudentId(et_studentnum.getText().toString());
            user.setDate(tv_year.getText().toString());
            user.setClassId(et_classnum.getText().toString());
        } else {
            user.setTeacherId(et_teachernum.getText().toString());
            user.setDate("000000");
            user.setClassId("000000");
        }
        user.setSchool(tv_school.getText().toString());

        switch (tv_sex.getText().toString()){
            case "男":
                user.setSex(0);
                break;
            case "女":
                user.setSex(1);
                break;
        }

        user.setName(et_name.getText().toString());

        //转换成json数据，借助gson
        String json = new Gson().toJson(user);
        log(thisClass, json);

        //发送post请求注册
        String after = "user/register";

        sendPostRequest(after, json, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //发送请求失败，有可能是网络断了或者其他的
                //Toast.makeText(RegisterActivity.this,"发送请求失败，请检查网络！", Toast.LENGTH_SHORT).show();
                log(thisClass, "发送请求失败，请检查网络");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                //Gson解析数据 json -> 对象
                try {
                    RestResponse restResponse = new Gson().fromJson(body, RestResponse.class);
                    log(thisClass, restResponse.getCode().toString());
                    //状态码500表示失败，打印错误信息
                    if (restResponse.getCode() == 500) {
                        failState = true;
                        failmsg = restResponse.getMsg();
                        log(thisClass, restResponse.getMsg());
                    }
                    //200代表成功，打印成功信息
                    if (restResponse.getCode() == 200) {
                        log(thisClass, "已成功注册");
                        finishState = true;
                        // Toast.makeText(RegisterActivity.this,"注册成功！", Toast.LENGTH_SHORT).show();
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

    public void initRunnable() {
        updateThread = new Runnable() {
            @Override
            public void run() {
                Message msg = myhandler.obtainMessage();
                msg.arg1 = 0;
                if (finishState == true || failState == true) {
                    if (finishState == true) {
                        Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                        finishState = false;
                        myhandler.removeCallbacks(updateThread);
                    } else if (failState == true) {
                        Toast.makeText(RegisterActivity.this, "注册错误：" + failmsg, Toast.LENGTH_SHORT).show();
                        ed_username.setText("");
                        failState = false;
                        myhandler.removeCallbacks(updateThread);
                    }

                } else {
                    myhandler.sendMessage(msg);
                    Log.v("yzy", "I am finding !");
                }
                // myhandler.postDelayed(updateThread, 3000);
            }
        };

        myhandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                //pb1.setText("" + msg.arg1);
                myhandler.post(updateThread);
            }
        };

    }


}
