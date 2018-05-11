package com.zhaoweihao.architechturesample.ui;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhaoweihao.architechturesample.R;

import java.util.Timer;
import java.util.TimerTask;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    ImageButton ibtn_hidepassword, ibtn_clearpassword, ibtn_clearusername, ibtn_clearpasswordconfirm,
            ibtn_hidepasswordconfirm;
    Button btn_submit;
    EditText ed_username, ed_password, ed_passwordconfirm;
    Boolean passwordflag, passwordconfirmflag;
    Handler handler;
    ImageView iv_bottom;
    Timer timer;
    TimerTask timerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
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
                Toast.makeText(this, "提交成功！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_bottom:
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                break;

        }
    }

    public void initView() {
        ed_username = (EditText) findViewById(R.id.ed_username);
        ed_password = (EditText) findViewById(R.id.ed_password);
        ed_passwordconfirm = (EditText) findViewById(R.id.ed_passwordconfirm);

        ibtn_hidepassword = (ImageButton) findViewById(R.id.ibtn_hidepassword);
        ibtn_clearpassword = (ImageButton) findViewById(R.id.ibtn_clearpassword);
        ibtn_clearusername = (ImageButton) findViewById(R.id.ibtn_clearusername);
        ibtn_clearpasswordconfirm = (ImageButton) findViewById(R.id.ibtn_clearpasswordconfirm);
        ibtn_hidepasswordconfirm = (ImageButton) findViewById(R.id.ibtn_hidepasswordconfirm);

        btn_submit = (Button) findViewById(R.id.btn_submit);

        iv_bottom = (ImageView) findViewById(R.id.iv_bottom);

        passwordflag = true;
        passwordconfirmflag = true;

        ibtn_clearpassword.setOnClickListener(this);
        ibtn_hidepassword.setOnClickListener(this);
        ibtn_clearusername.setOnClickListener(this);
        ibtn_clearpasswordconfirm.setOnClickListener(this);
        ibtn_hidepasswordconfirm.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        iv_bottom.setOnClickListener(this);


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
}
