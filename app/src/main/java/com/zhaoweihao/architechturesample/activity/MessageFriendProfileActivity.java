package com.zhaoweihao.architechturesample.activity;

import android.os.Bundle;
import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.base.BaseActivity;
import com.zhaoweihao.architechturesample.bean.RestResponse;
import com.zhaoweihao.architechturesample.bean.UserInfoByUsername;
import com.zhaoweihao.architechturesample.bean.ValidationMesg;
import com.zhaoweihao.architechturesample.database.User;
import com.zhaoweihao.architechturesample.ui.NetWorkImageView;
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

import static com.zhaoweihao.architechturesample.EasyCourseApplication.getContext;
import static com.zhaoweihao.architechturesample.EasyCourseApplication.getInstance;

/**
 * @author
 * @description 用于查看好友的基本资料
 * @time 2019/3/20 13:25
 */
public class MessageFriendProfileActivity extends BaseActivity {
    @BindView(R.id.amfp_titleLayout)
    TitleLayout amfp_titleLayout;
    @BindView(R.id.amfp_head)
    NetWorkImageView amfp_head;
    @BindView(R.id.amfp_tv_name)
    TextView amfp_tv_name;
    @BindView(R.id.amfp_iv_sex)
    ImageView amfp_iv_sex;
    @BindView(R.id.amfp_tv_type)
    TextView amfp_tv_type;
    @BindView(R.id.amfp_tv_desc)
    TextView amfp_tv_desc;
    @BindView(R.id.amfp_tv_university)
    TextView amfp_tv_university;
    @BindView(R.id.amfp_btn)
    Button amfp_btn;
    private Boolean ifSendable = true;
    private Boolean ifAlreadyBeSent = false;
    private Boolean ifAlreadyBeenFriend = false;

    /**
     * 用户id
     */
    private int id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 当前用户id
     */
    private int currentUserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_friend_profile);
        ButterKnife.bind(this);
        //getUserInfomation(getIntent().getStringExtra("username"));
        amfp_titleLayout.setTitle("基本信息");

        checkIfAlreadyBeenFriend(getIntent().getStringExtra("username"));

    }

    private void init() {
        if (getIntent().getStringExtra("mode").equals("发送消息")) {
            amfp_btn.setText("发送消息");
            amfp_btn.setOnClickListener(view -> sendMesg());
            getUserInfomation(getIntent().getStringExtra("username"));
        }
        if (getIntent().getStringExtra("mode").equals("输入添加好友")) {
            amfp_btn.setText("添加到通讯录");
            amfp_titleLayout.setTitle("基本信息");
            amfp_btn.setOnClickListener(view -> addToAddressList());
            showInfo((UserInfoByUsername) getIntent().getSerializableExtra("userInfoByUsername"));
        }
        if (ifAlreadyBeenFriend) {
            amfp_btn.setText("发送消息");
            amfp_btn.setOnClickListener(view -> sendMesg());
        }else {
            amfp_btn.setText("添加到通讯录");
            amfp_titleLayout.setTitle("基本信息");
            amfp_btn.setOnClickListener(view -> addToAddressList());
            showInfo((UserInfoByUsername) getIntent().getSerializableExtra("userInfoByUsername"));
        }
    }

    /**
     * 发送消息
     */
    private void sendMesg() {
        username = getIntent().getStringExtra("username");
        id = getIntent().getIntExtra("id", 0);
        currentUserId = DataSupport.findLast(User.class).getUserId();

        ChatWithWSActivity_.intent(this)
                .extra(Constant.RECEIVER_ID, id)
                .extra("username",username)
                .extra(Constant.IS_FRIEND_TALK, true)
                .start();

    }

    private void addToAddressList() {
        ifSendable(getIntent().getStringExtra("username"));
    }

    private void showInfo(UserInfoByUsername userInfoByUsername) {
        if (!TextUtils.isEmpty(userInfoByUsername.getAvatar())) {
            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.userprofile)
                    .diskCacheStrategy(DiskCacheStrategy.NONE);
            Glide.with(getContext())
                    .load(Constant.getBaseUrl() + "upload/" + userInfoByUsername.getAvatar())
                    .apply(requestOptions)
                    .into(amfp_head);
        }
        if (userInfoByUsername.getStudentId() == null) {
            amfp_tv_type.setText("教师");
        } else {
            amfp_tv_type.setText("学生");
        }
        if (userInfoByUsername.getSex() == 1) {
            amfp_iv_sex.setImageDrawable(getResources().getDrawable(R.drawable.female));
        } else {
            amfp_iv_sex.setImageDrawable(getResources().getDrawable(R.drawable.male));
        }
        amfp_tv_university.setText(userInfoByUsername.getSchool());

        amfp_tv_name.setText(userInfoByUsername.getName());
        if (TextUtils.isEmpty(userInfoByUsername.getDescrition())) {
            amfp_tv_desc.setText("这个人很懒，啥也没写~");
        } else {
            amfp_tv_desc.setText(userInfoByUsername.getDescrition());
        }
    }

    private void getUserInfomation(String username) {
        HttpUtil.sendGetRequest(Constant.GET_USERINFO_BY_USERNAME_AND_TOKEN_URL + username + "&token="
                + DataSupport.findLast(User.class).getToken(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                RestResponse restResponse = JSON.parseObject(body, RestResponse.class);
                if (restResponse.getCode() == Constant.SUCCESS_CODE) {
                    UserInfoByUsername userInfoByUsername = JSON.parseObject(restResponse.getPayload().toString(), UserInfoByUsername.class);
                    runOnUiThread(() -> {
                        try {
                            if (!TextUtils.isEmpty(userInfoByUsername.getAvatar())) {


                                RequestOptions requestOptions = new RequestOptions()
                                        .placeholder(R.drawable.userprofile)
                                        .diskCacheStrategy(DiskCacheStrategy.NONE);
                                Glide.with(getContext())
                                        .load(Constant.getBaseUrl() + "upload/" + userInfoByUsername.getAvatar())
                                        .apply(requestOptions)
                                        .into(amfp_head);
                            }
                            if (userInfoByUsername.getStudentId() == null) {
                                amfp_tv_type.setText("教师");
                            } else {
                                amfp_tv_type.setText("学生");
                            }
                            if (userInfoByUsername.getSex() == 0) {
                                amfp_iv_sex.setImageDrawable(getResources().getDrawable(R.drawable.female));
                            } else {
                                amfp_iv_sex.setImageDrawable(getResources().getDrawable(R.drawable.male));
                            }
                            amfp_tv_university.setText(userInfoByUsername.getSchool());

                            amfp_tv_name.setText(userInfoByUsername.getName());
                            if (TextUtils.isEmpty(userInfoByUsername.getDescrition())) {
                                amfp_tv_desc.setText("这个人很懒，啥也没写~");
                            } else {
                                amfp_tv_desc.setText(userInfoByUsername.getDescrition());
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.v("tanxkk", e.toString());
                        }
                    });
                }
            }
        });
    }

    private void ifSendable(String s) {
        HttpUtil.sendGetRequest(Constant.GET_ALL_FROM_ME_REQUEST_URL + DataSupport.findLast(User.class).getUsername() + "&token=" + DataSupport.findLast(User.class).getToken(),
                new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                        runOnUiThread(() -> {
                            Toast.makeText(MessageFriendProfileActivity.this, "加载异常！", Toast.LENGTH_SHORT).show();
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

    private void sendFriendRequest(String s) {
        if (ifSendable) {
            HttpUtil.sendGetRequest(
                    Constant.SEND_FRIEND_REQUEST_URL + "from=" + DataSupport.findLast(User.class).getUsername() + "&to=" + s + "&token=" + DataSupport.findLast(User.class).getToken(),
                    new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            runOnUiThread(() -> {
                                Toast.makeText(MessageFriendProfileActivity.this, "添加好友失败，找不到该用户！", Toast.LENGTH_LONG).show();
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String body = response.body().string();
                            //解析json数据组装RestResponse对象
                            RestResponse restResponse = JSON.parseObject(body, RestResponse.class);
                            runOnUiThread(() -> {
                                if (restResponse.getCode() == Constant.SUCCESS_CODE) {
                                    Toast.makeText(MessageFriendProfileActivity.this, "发送好友验证成功！", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(MessageFriendProfileActivity.this, "添加好友失败，找不到该用户！", Toast.LENGTH_LONG).show();
                                }

                            });
                        }
                    });
        } else {
            if (ifAlreadyBeSent) {
                Toast.makeText(MessageFriendProfileActivity.this, "对方已发送过好友验证，正在等待您确认！", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MessageFriendProfileActivity.this, "您已发送过好友验证，正在等待好友确认！", Toast.LENGTH_LONG).show();
            }

        }

    }

    private void ifisAlreadyBeenSent(String s) {
        HttpUtil.sendGetRequest(Constant.GET_ALL_FRIENDS_TO_ME_REQUEST_URL + DataSupport.findLast(User.class).getUsername() + "&token=" + DataSupport.findLast(User.class).getToken(),
                new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                        runOnUiThread(() -> {
                            Toast.makeText(MessageFriendProfileActivity.this, "加载异常！", Toast.LENGTH_SHORT).show();
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

    private void checkIfAlreadyBeenFriend(String checkName) {
        HttpUtil.sendGetRequest(Constant.GET_MY_ALL_FRIENDS_URL + DataSupport.findLast(User.class).getUsername() + "&token=" + DataSupport.findLast(User.class).getToken(),
                new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(() -> {
                            Toast.makeText(MessageFriendProfileActivity.this, "加载异常！", Toast.LENGTH_SHORT).show();
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String body = response.body().string();
                        //解析json数据组装RestResponse对象
                        RestResponse restResponse = JSON.parseObject(body, RestResponse.class);
                        //RestResponse restResponse = new Gson().fromJson(body, RestResponse.class);
                        if (restResponse.getCode() == Constant.SUCCESS_CODE) {

                            List<ValidationMesg> ValidationMesg = JSON.parseArray(restResponse.getPayload().toString(), ValidationMesg.class);

                            runOnUiThread(() -> {
                                for (ValidationMesg vm : ValidationMesg) {
                                    String username = vm.getFrom_username().equals(DataSupport.findLast(User.class).getUsername()) ? vm.getTo_username() : vm.getFrom_username();
                                    if (username.equals(checkName)) {
                                        ifAlreadyBeenFriend = true;
                                    }
                                }
                                init();
                            });

                        }
                    }
                });
    }

}
