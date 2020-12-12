package com.zhaoweihao.architechturesample.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.kongzue.stacklabelview.StackLabel;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.base.BaseActivity;
import com.zhaoweihao.architechturesample.bean.RestResponse;
import com.zhaoweihao.architechturesample.bean.ShowNote;
import com.zhaoweihao.architechturesample.bean.UserInfoByUsername;
import com.zhaoweihao.architechturesample.database.HistoryTag;
import com.zhaoweihao.architechturesample.database.User;
import com.zhaoweihao.architechturesample.ui.SearchBoardInputLayout;
import com.zhaoweihao.architechturesample.util.Constant;
import com.zhaoweihao.architechturesample.util.HttpUtil;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @author tanxinkui
 * @description 消息页-搜索框-跳转的界面（注：消息搜索框-->当前界面）
 * @date 2019/1/8
 */


public class MessageSearchAllPeopleActivity extends BaseActivity {
    @BindView(R.id.amsap_search_board_input)
    SearchBoardInputLayout amsap_search_board_input;
    private Handler handler;
    private Timer timer;
    private TimerTask timerTask;
    private int originalNum;
    private StackLabel mstackLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_search_all_people);
        ButterKnife.bind(this);
        //先设置editext的hintText,然后是限制的历史记录数量，然后是搜索的页面的标签，（标签设置完，在HistoryTag备注一下:message_search_all）
        amsap_search_board_input.initWithArgs("输入对方的用户名", 10, "message_search_all");
        List<HistoryTag> allData = DataSupport.where("tagTag=?", "message_search_all").find(com.zhaoweihao.architechturesample.database.HistoryTag.class);
        originalNum = allData.size();
        mstackLabel = amsap_search_board_input.getStackbel();
        detectInput("message_search_all");
        setStableClickListener();
    }

    private void getUserInfomation(String username) {
        HttpUtil.sendGetRequest(Constant.GET_USERINFO_BY_USERNAME_AND_TOKEN_URL + username + "&token="
                + DataSupport.findLast(User.class).getToken(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    Toast.makeText(MessageSearchAllPeopleActivity.this, "找不到该好友", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                RestResponse restResponse = JSON.parseObject(body, RestResponse.class);
                runOnUiThread(() -> {
                    if (restResponse.getCode() == Constant.SUCCESS_CODE) {
                        UserInfoByUsername userInfoByUsername = JSON.parseObject(restResponse.getPayload().toString(), UserInfoByUsername.class);
                        Intent intent = new Intent(MessageSearchAllPeopleActivity.this, MessageFriendProfileActivity.class);
                        intent.putExtra("userInfoByUsername", userInfoByUsername);
                        intent.putExtra("mode", "输入添加好友");
                        intent.putExtra("username", username);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MessageSearchAllPeopleActivity.this, "找不到该好友", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void setStableClickListener() {
        mstackLabel.setOnLabelClickListener((int index, View view, String s) -> {
            goSearchNow(s);
        });
    }

    private void goSearchNow(String inputText) {
        init(inputText);

    }

    private void init(String searchKeyWord) {
        if (TextUtils.isEmpty(searchKeyWord)) {
            Toast.makeText(MessageSearchAllPeopleActivity.this, "请先输入对方的邀请码！", Toast.LENGTH_SHORT).show();
        } else if (searchKeyWord.equals(DataSupport.findLast(User.class).getUsername())) {
            Toast.makeText(MessageSearchAllPeopleActivity.this, "不能添加自己为好友！", Toast.LENGTH_SHORT).show();
            //ifSendable(ahci_inputInviteCode.getEditTextString());
        } else {
            getUserInfomation(searchKeyWord);
        }

    }

    private void detectInput(String searchTagTag) {


        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 11) {
                    mstackLabel = amsap_search_board_input.getStackbel();
                    setStableClickListener();
                    goSearchNow(amsap_search_board_input.getFinalSearchString());
                }
                if (msg.what == 12) {
                }
                super.handleMessage(msg);
            }
        };
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                List<HistoryTag> allData = DataSupport.where("tagTag=?", searchTagTag).find(com.zhaoweihao.architechturesample.database.HistoryTag.class);
                if ((allData.size() != originalNum) && (allData.size() != 0)) {
                    message.what = 11;
                    originalNum = allData.size();
                } else {
                    message.what = 12;
                }

                handler.sendMessage(message);
            }
        };
        timer.schedule(timerTask, 200, 200);
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
