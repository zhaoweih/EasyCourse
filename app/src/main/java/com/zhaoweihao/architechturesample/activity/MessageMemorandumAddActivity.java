package com.zhaoweihao.architechturesample.activity;

import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.base.BaseActivity;
import com.zhaoweihao.architechturesample.database.Memorandum;
import com.zhaoweihao.architechturesample.database.User;
import com.zhaoweihao.architechturesample.ui.EditTextLayout;
import com.zhaoweihao.architechturesample.ui.GetInputTextOrSelectTagDialogLayout;
import com.zhaoweihao.architechturesample.ui.TitleLayout;

import org.litepal.crud.DataSupport;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author
 * @description 消息-待办事项-添加
 * @time 2019/3/12 19:35
 */
public class MessageMemorandumAddActivity extends BaseActivity {
    @BindView(R.id.amma_titleLayout)
    TitleLayout amma_titleLayout;
    @BindView(R.id.amma_setTitle)
    GetInputTextOrSelectTagDialogLayout amma_setTitle;
    @BindView(R.id.amma_setTag)
    GetInputTextOrSelectTagDialogLayout amma_setTag;
    @BindView(R.id.amma_setTime)
    GetInputTextOrSelectTagDialogLayout amma_setTime;
    private Button finishBtn;
    @BindView(R.id.amma_et_add_content)
    EditText amma_et_add_content;
    @BindView(R.id.amma_iv_ifWriteTag)
    ImageView iv_ifWriteTag;
    private boolean ifTitleAlready, ifTagAlready, ifTimeAlready, ifTextAlready;
    private Timer timer;
    private TimerTask timerTask;
    private Handler myHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_memorandum_add);
        ButterKnife.bind(this);

        ifTitleAlready = false;
        ifTagAlready = false;
        ifTimeAlready = false;
        ifTextAlready = false;
        init();
    }

    private void init() {
        myHandler = new Handler() {
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
        timer.schedule(timerTask, 200, 200);
        //延时1s，每隔500毫秒执行一次run方法
        ifTextAlreadyOrNot();
        amma_titleLayout.setTitle("添加待办事项");
        finishBtn = amma_titleLayout.getSettingButton();
        finishBtn.setVisibility(View.INVISIBLE);
        String[] tags = {"非常重要", "重要", "一般"};
        amma_setTag.setTagText(tags);
        setFinishBtn();
        amma_setTime.setTime("截止时间：", "选择截止时间", 1);
        amma_setTitle.setHintText("标题：", "请输入标题", "标题", "请输入标题");
        amma_et_add_content.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        amma_et_add_content.setGravity(Gravity.TOP);
        amma_et_add_content.setSingleLine(false);
        amma_et_add_content.setHorizontallyScrolling(false);
    }

    public void HandleMessageInformation(Message msg) {
        if (msg.what == 111) {
            if (finishBtn.getVisibility() == View.INVISIBLE) {
                finishBtn.setVisibility(View.VISIBLE);
            }
        } else {
            if (finishBtn.getVisibility() == View.VISIBLE) {
                finishBtn.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void runInformation() {
        Message message = new Message();
        ifTagAlready = amma_setTag.ifAlready();
        ifTimeAlready = amma_setTime.ifAlready();
        ifTitleAlready = amma_setTitle.ifAlready();
        if (ifTagAlready && ifTimeAlready && ifTitleAlready && ifTextAlready) {

            message.what = 111;
        } else {

            message.what = 112;
        }
        Log.v("tanxinkui", "ifTagAlready" + ifTagAlready);
        Log.v("tanxinkui", "ifTimeAlready" + ifTimeAlready);
        Log.v("tanxinkui", "ifTitleAlready" + ifTitleAlready);
        Log.v("tanxinkui", "fTextAlready" + ifTextAlready);
        myHandler.sendMessage(message);
    }

    private void setFinishBtn() {
        finishBtn.setText("完成");
        // Memorandum(int userId, String title, String tag, Date date, String content)
        finishBtn.setOnClickListener(view -> {
            Memorandum memorandumAdd = new Memorandum(DataSupport.findLast(User.class).getUserId(),
                    amma_setTitle.getFinalInputText(), amma_setTag.getFinalInputText(), amma_setTime.getExpireDate(), amma_et_add_content.getText().toString(), false);
            memorandumAdd.save();
            Log.v("tanxinkui添加待办事项！", "" + memorandumAdd.toString());
            Toast.makeText(MessageMemorandumAddActivity.this, "保存成功！", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void ifTextAlreadyOrNot() {
        amma_et_add_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                iv_ifWriteTag.setVisibility(View.INVISIBLE);
                if (TextUtils.isEmpty(amma_et_add_content.getText().toString())) {
                    ifTextAlready = false;
                } else {
                    ifTextAlready = true;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (TextUtils.isEmpty(amma_et_add_content.getText().toString())) {
                    ifTextAlready = false;
                    iv_ifWriteTag.setVisibility(View.VISIBLE);
                } else {
                    ifTextAlready = true;
                    iv_ifWriteTag.setVisibility(View.INVISIBLE);
                }
            }
        });
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
        myHandler.removeMessages(11);
        myHandler.removeMessages(12);
    }

}
