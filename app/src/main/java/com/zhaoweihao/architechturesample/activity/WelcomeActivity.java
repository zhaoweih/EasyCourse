package com.zhaoweihao.architechturesample.activity;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.base.BaseActivity;
import com.zhaoweihao.architechturesample.database.User;
import com.zhaoweihao.architechturesample.util.CheckLogin;

import org.litepal.crud.DataSupport;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author
 * @description 启动页
 * @time 2019/3/3 21:45
 */
public class WelcomeActivity extends BaseActivity {
    @BindView(R.id.aw_welcombtn)
    Button aw_welcombtn;
    @BindView(R.id.slogan)
    TextView slogan;
    private Handler handler;
    private Timer timer;
    private TimerTask timerTask;
    private int countdown = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        detectInput();
        aw_welcombtn.setOnClickListener(view -> {
            init();
            //Toast.makeText(WelcomeActivity.this, "123", Toast.LENGTH_SHORT).show();
        });
    }

    private void init() {
        finish();
        if (CheckLogin.ifUserLogin()) {
            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            overridePendingTransition(0, 0);
        } else {
            Intent intent = new Intent(WelcomeActivity.this, MainLoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            overridePendingTransition(0, 0);
        }
    }

    private void detectInput() {
        countdown = 6;
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 11) {
                    countdown--;
                    if (countdown >= 0) {
                        aw_welcombtn.setText("" + countdown+"  跳过");
                    } else {
                        init();
                        //Toast.makeText(WelcomeActivity.this, "123", Toast.LENGTH_SHORT).show();
                    }
                }
                super.handleMessage(msg);
            }
        };
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 11;
                handler.sendMessage(message);
            }
        };
        timer.schedule(timerTask, 200, 1000);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
        handler.removeMessages(11);
        handler.removeMessages(12);
    }

}
