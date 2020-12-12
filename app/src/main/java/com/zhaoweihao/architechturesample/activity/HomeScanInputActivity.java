package com.zhaoweihao.architechturesample.activity;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.text.InputType;
import android.text.TextUtils;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.base.BaseActivity;
import com.zhaoweihao.architechturesample.bean.RestResponse;
import com.zhaoweihao.architechturesample.bean.UserInfoByUsername;
import com.zhaoweihao.architechturesample.bean.ValidationMesg;
import com.zhaoweihao.architechturesample.database.User;
import com.zhaoweihao.architechturesample.ui.EditTextLayout;
import com.zhaoweihao.architechturesample.ui.TitleLayout;
import com.zhaoweihao.architechturesample.util.Constant;
import com.zhaoweihao.architechturesample.util.HttpUtil;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @author
 * @description 扫描二维码时直接输入邀请码
 * @time 2019/3/8 11:52
 */
public class HomeScanInputActivity extends BaseActivity {
    @BindView(R.id.ahci_inputInviteCode)
    EditTextLayout ahci_inputInviteCode;
    @BindView(R.id.ahci_qmui_confirm)
    QMUIRoundButton ahci_qmui_confirm;
    @BindView(R.id.ahci_titleLayout)
    TitleLayout ahci_titleLayout;
    private Boolean ifSendable = true;
    private Boolean ifAlreadyBeSent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_scan_input);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        ahci_titleLayout.setTitle("添加好友");
        ahci_inputInviteCode.initWithArgs("输入邀请码", "邀请码为对方用户名",
                InputType.TYPE_CLASS_TEXT, 18, true, true, true);
        ahci_qmui_confirm.setOnClickListener(view -> confirmAction());
    }

    private void confirmAction() {
        if (TextUtils.isEmpty(ahci_inputInviteCode.getEditTextString())) {
            Toast.makeText(HomeScanInputActivity.this, "请先输入对方的邀请码！", Toast.LENGTH_SHORT).show();
        } else if (ahci_inputInviteCode.getEditTextString().equals(DataSupport.findLast(User.class).getUsername())) {
            Toast.makeText(HomeScanInputActivity.this, "不能添加自己为好友！", Toast.LENGTH_SHORT).show();
            //ifSendable(ahci_inputInviteCode.getEditTextString());
        } else {
            getUserInfomation(ahci_inputInviteCode.getEditTextString());
        }
    }

    private void ifSendable(String s) {
        HttpUtil.sendGetRequest(Constant.GET_ALL_FROM_ME_REQUEST_URL + DataSupport.findLast(User.class).getUsername() + "&token=" + DataSupport.findLast(User.class).getToken(),
                new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                        runOnUiThread(() -> {
                            Toast.makeText(HomeScanInputActivity.this, "加载异常！", Toast.LENGTH_SHORT).show();
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String body = response.body().string();
                        //解析json数据组装RestResponse对象
                        RestResponse restResponse = JSON.parseObject(body, RestResponse.class);
                        //RestResponse restResponse = new Gson().fromJson(body, RestResponse.class);
                        runOnUiThread(() -> {
                            if (restResponse.getCode() == Constant.SUCCESS_CODE) {
                                List<ValidationMesg> mValidationMesg = JSON.parseArray(restResponse.getPayload().toString(), ValidationMesg.class);
                                if (mValidationMesg.size() != 0) {
                                    for (ValidationMesg mesg : mValidationMesg) {
                                        if (mesg.getTo_username().equals(s) && mesg.getIs_confirmed() == 0) {
                                            ifSendable = false;
                                        }
                                    }
                                }
                                ifisAlreadyBeenSent(s);
                            }
                        });
                    }
                });
    }

    private void ifisAlreadyBeenSent(String s) {
        HttpUtil.sendGetRequest(Constant.GET_ALL_FRIENDS_TO_ME_REQUEST_URL + DataSupport.findLast(User.class).getUsername() + "&token=" + DataSupport.findLast(User.class).getToken(),
                new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                        runOnUiThread(() -> {
                            Toast.makeText(HomeScanInputActivity.this, "加载异常！", Toast.LENGTH_SHORT).show();
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String body = response.body().string();
                        //解析json数据组装RestResponse对象
                        RestResponse restResponse = JSON.parseObject(body, RestResponse.class);
                        //RestResponse restResponse = new Gson().fromJson(body, RestResponse.class);
                        runOnUiThread(() -> {
                            if (restResponse.getCode() == Constant.SUCCESS_CODE) {
                                List<ValidationMesg> mValidationMesg = JSON.parseArray(restResponse.getPayload().toString(), ValidationMesg.class);
                                if (mValidationMesg.size() != 0) {
                                    for (ValidationMesg mesg : mValidationMesg) {
                                        if (mesg.getFrom_username().equals(s) && mesg.getIs_confirmed() == 0) {
                                            ifSendable = false;
                                            ifAlreadyBeSent = true;
                                        }
                                    }
                                }
                                sendFriendRequest(s);
                            }
                        });
                    }
                });
    }

    private void sendFriendRequest(String s) {
        if (ifSendable) {
            HttpUtil.sendGetRequest(
                    Constant.SEND_FRIEND_REQUEST_URL + "from=" + DataSupport.findLast(User.class).getUsername() + "&to=" + s + "&token=" + DataSupport.findLast(User.class).getToken(),
                    new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            runOnUiThread(() -> {
                                Toast.makeText(HomeScanInputActivity.this, "添加好友失败，找不到该用户！", Toast.LENGTH_LONG).show();
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String body = response.body().string();
                            //解析json数据组装RestResponse对象
                            RestResponse restResponse = JSON.parseObject(body, RestResponse.class);
                            runOnUiThread(() -> {
                                if (restResponse.getCode() == Constant.SUCCESS_CODE) {
                                    Toast.makeText(HomeScanInputActivity.this, "发送好友验证成功！", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(HomeScanInputActivity.this, "添加好友失败，找不到该用户！", Toast.LENGTH_LONG).show();
                                }

                            });
                        }
                    });
        } else {
            if (ifAlreadyBeSent) {
                Toast.makeText(HomeScanInputActivity.this, "对方已发送过好友验证，正在等待您确认！", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(HomeScanInputActivity.this, "您已发送过好友验证，正在等待好友确认！", Toast.LENGTH_LONG).show();
            }

        }

    }

    private void getUserInfomation(String username) {
        HttpUtil.sendGetRequest(Constant.GET_USERINFO_BY_USERNAME_AND_TOKEN_URL + username + "&token="
                + DataSupport.findLast(User.class).getToken(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    Toast.makeText(HomeScanInputActivity.this, "找不到该好友", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                RestResponse restResponse = JSON.parseObject(body, RestResponse.class);
                runOnUiThread(() -> {
                    if (restResponse.getCode() == Constant.SUCCESS_CODE) {
                        UserInfoByUsername userInfoByUsername = JSON.parseObject(restResponse.getPayload().toString(), UserInfoByUsername.class);
                        Intent intent=new Intent(HomeScanInputActivity.this,MessageFriendProfileActivity.class);
                        intent.putExtra("userInfoByUsername", userInfoByUsername);
                        intent.putExtra("mode", "输入添加好友");
                        intent.putExtra("username", username);
                        startActivity(intent);
                    } else {
                        Toast.makeText(HomeScanInputActivity.this, "找不到该好友", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

}
