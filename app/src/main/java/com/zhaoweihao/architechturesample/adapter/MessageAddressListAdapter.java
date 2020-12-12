package com.zhaoweihao.architechturesample.adapter;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.activity.MainLoginActivity;
import com.zhaoweihao.architechturesample.activity.MessageFriendProfileActivity;
import com.zhaoweihao.architechturesample.bean.RestResponse;
import com.zhaoweihao.architechturesample.bean.UserInfoByUsername;
import com.zhaoweihao.architechturesample.bean.ValidationMesg;
import com.zhaoweihao.architechturesample.database.User;
import com.zhaoweihao.architechturesample.ui.NetWorkImageView;
import com.zhaoweihao.architechturesample.util.Constant;
import com.zhaoweihao.architechturesample.util.HttpUtil;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.zhaoweihao.architechturesample.EasyCourseApplication.getContext;

public class MessageAddressListAdapter extends BaseQuickAdapter<ValidationMesg, BaseViewHolder> {
    // private List<ValidationMesg> validationMesgList = new ArrayList<>();
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    public MessageAddressListAdapter(List<ValidationMesg> validationMesgList) {
        super(R.layout.layout_address_list, validationMesgList);
        //this.validationMesgList = validationMesgList;
    }

    @Override
    protected void convert(BaseViewHolder helper, ValidationMesg item) {
        helper.setImageResource(R.id.lal_iv_course_icon, R.drawable.default_friend_avator)
                .setText(R.id.lal_tv_title, item.getFrom_username().equals(DataSupport.findLast(User.class).getUsername()) ? item.getTo_username() : item.getFrom_username());
        String username = item.getFrom_username().equals(DataSupport.findLast(User.class).getUsername()) ? item.getTo_username() : item.getFrom_username();
        NetWorkImageView iv = helper.getView(R.id.lal_iv_course_icon);
        TextView textView = helper.getView(R.id.lal_tv_title);
        LinearLayout lal_ly_main=helper.getView(R.id.lal_ly_main);
        lal_ly_main.setOnClickListener(view -> getUserInfomation(username));
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
                    if (!TextUtils.isEmpty(userInfoByUsername.getAvatar())) {
                        try {
                            mainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    RequestOptions requestOptions = new RequestOptions()
                                            .placeholder(R.drawable.userprofile)
                                            .diskCacheStrategy(DiskCacheStrategy.NONE);
                                    Glide.with(getContext())
                                            .load(Constant.getBaseUrl() + "upload/" + userInfoByUsername.getAvatar())
                                            .apply(requestOptions)
                                            .into(iv);
                                    textView.setText(username + "(" + userInfoByUsername.getName() + ")");
                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.v("tanxkk", e.toString());
                        }
                    }
                }
            }
        });
    }

    private void getUserInfomation(String username) {
        HttpUtil.sendGetRequest(Constant.GET_USERINFO_BY_USERNAME_AND_TOKEN_URL + username + "&token="
                + DataSupport.findLast(User.class).getToken(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "找不到该好友", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                RestResponse restResponse = JSON.parseObject(body, RestResponse.class);
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (restResponse.getCode() == Constant.SUCCESS_CODE) {
                            UserInfoByUsername userInfoByUsername = JSON.parseObject(restResponse.getPayload().toString(), UserInfoByUsername.class);
                            Intent intent = new Intent(getContext(), MessageFriendProfileActivity.class);
                            intent.putExtra("id", userInfoByUsername.getId());
                            intent.putExtra("userInfoByUsername", userInfoByUsername);
                            intent.putExtra("mode", "发送消息");
                            intent.putExtra("username", username);
                            getContext().startActivity(intent);
                        } else {
                            Toast.makeText(getContext(), "找不到该好友", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
