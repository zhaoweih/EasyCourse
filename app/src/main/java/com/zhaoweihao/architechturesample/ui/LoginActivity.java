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
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.zhaoweihao.architechturesample.R;

import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    ImageButton ibtn_hidepassword, ibtn_clearpassword,ibtn_clearusername;
    EditText ed_username, ed_password;
    Boolean passwordflag;
    Handler handler;
    Timer timer;
    TimerTask timerTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        timer.schedule(timerTask,200,200);//延时1s，每隔500毫秒执行一次run方法
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
                Intent intent = new Intent(this,RegisterActivity.class);
                startActivity(intent);
        }
    }

    public void initView() {
        ed_username = (EditText) findViewById(R.id.ed_username);
        ed_password = (EditText) findViewById(R.id.ed_password);

        ibtn_hidepassword = (ImageButton) findViewById(R.id.ibtn_hidepassword);
        ibtn_clearpassword = (ImageButton) findViewById(R.id.ibtn_clearpassword);
        ibtn_clearusername=(ImageButton)findViewById(R.id.ibtn_clearusername);

        passwordflag = true;

        ibtn_clearpassword.setOnClickListener(this);
        ibtn_hidepassword.setOnClickListener(this);
        ibtn_clearusername.setOnClickListener(this);

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

}
