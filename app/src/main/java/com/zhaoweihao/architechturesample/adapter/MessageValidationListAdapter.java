package com.zhaoweihao.architechturesample.adapter;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.zhaoweihao.architechturesample.R;
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
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.zhaoweihao.architechturesample.EasyCourseApplication.getContext;

/**
 * @author
 * @description 消息-验证消息-列表
 * @time 2019/3/9 20:21
 */
public class MessageValidationListAdapter extends BaseQuickAdapter<ValidationMesg, BaseViewHolder> {
    private int[] validationId;
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    public MessageValidationListAdapter(@Nullable List<ValidationMesg> data) {
        super(R.layout.layout_message_validation, data);
        validationId = new int[data.size()];
        for (int i = 0; i < data.size(); i++) {
            validationId[i] = data.get(i).getId();
        }
    }

    @Override
    protected void convert(BaseViewHolder helper, ValidationMesg item) {
        String info[] = {"请求添加你为好友！", "已添加好友！", "拒绝你的好友申请！"};
        helper.setImageResource(R.id.lmv_iv_course_icon, R.drawable.default_friend_avator)
                .setText(R.id.lmv_tv_name, item.getFrom_username())
                .setText(R.id.lmv_tv_info, info[item.getStatus()])
                .addOnClickListener(R.id.lmv_iv_refuse)
                .addOnClickListener(R.id.lmv_iv_accept);
        QMUIRoundButton btn1 = helper.getView(R.id.lmv_iv_accept);
        QMUIRoundButton btn2 = helper.getView(R.id.lmv_iv_refuse);
        if (item.getStatus() != 0) {
            btn1.setVisibility(View.GONE);
            btn2.setVisibility(View.GONE);
        }
        NetWorkImageView iv = helper.getView(R.id.lmv_iv_course_icon);
        iv.setOnClickListener(view -> getUserInfomation(item.getFrom_username()));
        HttpUtil.sendGetRequest(Constant.GET_USERINFO_BY_USERNAME_AND_TOKEN_URL + item.getFrom_username() + "&token="
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

    public int[] getValidationId() {
        return validationId;
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
                            intent.putExtra("userInfoByUsername", userInfoByUsername);
                            intent.putExtra("mode", "输入添加好友");
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
