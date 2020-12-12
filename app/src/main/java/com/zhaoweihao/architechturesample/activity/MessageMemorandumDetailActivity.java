package com.zhaoweihao.architechturesample.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
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
import android.widget.Toast;

import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.base.BaseActivity;
import com.zhaoweihao.architechturesample.database.Memorandum;
import com.zhaoweihao.architechturesample.database.User;
import com.zhaoweihao.architechturesample.ui.GetInputTextOrSelectTagDialogLayout;
import com.zhaoweihao.architechturesample.ui.TitleLayout;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author
 * @description 消息-待办事项-详细界面
 * @time 2019/3/15 15:16
 */
public class MessageMemorandumDetailActivity extends BaseActivity {
    @BindView(R.id.ammd_titleLayout)
    TitleLayout ammd_titleLayout;
    @BindView(R.id.ammd_setTitle)
    GetInputTextOrSelectTagDialogLayout ammd_setTitle;
    @BindView(R.id.ammd_setTag)
    GetInputTextOrSelectTagDialogLayout ammd_setTag;
    @BindView(R.id.ammd_setTime)
    GetInputTextOrSelectTagDialogLayout ammd_setTime;
    @BindView(R.id.ammd_et_add_content)
    EditText ammd_et_add_content;
    @BindView(R.id.ammd_iv_ifWriteTag)
    ImageView iv_ifWriteTag;
    private Memorandum memorandum;
    private Button modifyBtn;
    private Timer timer;
    private TimerTask timerTask;
    private Handler myHandler;
    private boolean ifTitleAlready, ifTagAlready, ifTimeAlready, ifTextAlready;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_memorandum_detail);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        Intent intent = getIntent();
        memorandum = (Memorandum) intent.getSerializableExtra("MemorandumDetail");
        ammd_titleLayout.setTitle("查看或修改待办事项");
        modifyBtn = ammd_titleLayout.getSettingButton();
        modifyBtn.setVisibility(View.INVISIBLE);
        String[] tags = {"非常重要", "重要", "一般"};
        ammd_setTag.setTagDefaultText(tags, memorandum.getTag());
        setModifyBtn();
        ammd_setTime.setTimeWhithDefaultText("截止时间：", "截止时间：" + getTime(memorandum.getDate()), 1);
        ammd_setTitle.setDefaultText("标题：", "标题：" + memorandum.getTitle(), "标题", "请输入标题");
        ammd_et_add_content.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        ammd_et_add_content.setGravity(Gravity.TOP);
        ammd_et_add_content.setSingleLine(false);
        ammd_et_add_content.setHorizontallyScrolling(false);
        ammd_et_add_content.setText(memorandum.getContent());
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
        ifTextAlreadyOrNot();
    }

    private void setModifyBtn() {
        modifyBtn.setText("修改");
        // Memorandum(int userId, String title, String tag, Date date, String content)
        modifyBtn.setOnClickListener(view -> {
            //(int userId, String title, String tag, Date date, String content)
           /* ContentValues values = new ContentValues();
            values.put("title", TextUtils.isEmpty(ammd_setTitle.getFinalInputText()) ? memorandum.getTitle() : ammd_setTitle.getFinalInputText());
            values.put("tag", TextUtils.isEmpty(ammd_setTag.getFinalInputText()) ? memorandum.getTag() : ammd_setTag.getFinalInputText());
            values.put("date", TextUtils.isEmpty(ammd_setTime.getExpireDate().toString()) ? memorandum.getDate().toString() : ammd_setTime.getExpireDate().toString());
            values.put("content", TextUtils.isEmpty(ammd_et_add_content.getText().toString()) ? memorandum.getContent() : ammd_et_add_content.getText().toString());
            DataSupport.update(Memorandum.class, values, memorandum.getId());*/
            Memorandum memorandumt = new Memorandum();
            int userId = DataSupport.findLast(User.class).getUserId();
            memorandumt.setUserId(userId);
            String title = TextUtils.isEmpty(ammd_setTitle.getFinalInputText()) ? memorandum.getTitle() : ammd_setTitle.getFinalInputText();
            memorandumt.setTitle(title);
            String tag = TextUtils.isEmpty(ammd_setTag.getFinalInputText()) ? memorandum.getTag() : ammd_setTag.getFinalInputText();
            memorandumt.setTag(tag);
            String content = TextUtils.isEmpty(ammd_et_add_content.getText().toString()) ? memorandum.getContent() : ammd_et_add_content.getText().toString();
            memorandumt.setContent(content);
            memorandumt.setDate(ammd_setTime.getExpireDate() == null ? memorandum.getDate() : ammd_setTime.getExpireDate());
            memorandumt.setNotify(false);
            memorandumt.update(memorandum.getId());
            Toast.makeText(MessageMemorandumDetailActivity.this, "修改成功！", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private String getTime(Date date) {
        //可根据需要自行截取数据显示
        // SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd  HH:mm");
        return format.format(date);
    }

    public void HandleMessageInformation(Message msg) {
        if (msg.what == 111) {
            if (modifyBtn.getVisibility() == View.INVISIBLE) {
                modifyBtn.setVisibility(View.VISIBLE);
            }
        } else {
            if (modifyBtn.getVisibility() == View.VISIBLE) {
                modifyBtn.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void runInformation() {
        Message message = new Message();
        ifTagAlready = ammd_setTag.ifAlready();
        ifTimeAlready = ammd_setTime.ifAlready();
        ifTitleAlready = ammd_setTitle.ifAlready();
        if (ifTagAlready || ifTimeAlready || ifTitleAlready || ifTextAlready) {

            message.what = 111;
        } else {

            message.what = 112;
        }
        myHandler.sendMessage(message);
    }

    private void ifTextAlreadyOrNot() {
        ammd_et_add_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                iv_ifWriteTag.setVisibility(View.INVISIBLE);
                if (TextUtils.isEmpty(ammd_et_add_content.getText().toString())) {
                    ifTextAlready = false;
                } else {
                    ifTextAlready = true;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (TextUtils.isEmpty(ammd_et_add_content.getText().toString())) {
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
