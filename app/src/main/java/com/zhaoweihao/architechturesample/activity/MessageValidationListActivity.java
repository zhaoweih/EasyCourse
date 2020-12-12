package com.zhaoweihao.architechturesample.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.adapter.MessageAddressListAdapter;
import com.zhaoweihao.architechturesample.adapter.MessageValidationListAdapter;
import com.zhaoweihao.architechturesample.base.BaseActivity;
import com.zhaoweihao.architechturesample.bean.RestResponse;
import com.zhaoweihao.architechturesample.bean.UserInfoByUsername;
import com.zhaoweihao.architechturesample.bean.ValidationMesg;
import com.zhaoweihao.architechturesample.database.User;
import com.zhaoweihao.architechturesample.ui.TitleLayout;
import com.zhaoweihao.architechturesample.util.Constant;
import com.zhaoweihao.architechturesample.util.HttpUtil;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.zhaoweihao.architechturesample.EasyCourseApplication.getContext;

/**
 * @author
 * @description 消息-验证消息列表
 * @time 2019/3/9 17:38
 */
public class MessageValidationListActivity extends BaseActivity {

    @BindView(R.id.amvl_recyclerView)
    RecyclerView amvl_recyclerView;

    @BindView(R.id.amvl_titleLayout)
    TitleLayout amvl_titleLayout;

    @BindView(R.id.amvl_empty_view)
    LinearLayout amvl_empty_view;

    private QMUITipDialog tipDialog;
    private List<ValidationMesg> mValidationMesg = new ArrayList<>();
    private List<ValidationMesg> mValidationMesgTemp = new ArrayList<>();
    private MessageValidationListAdapter mMessageValidationListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_validation_list);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        amvl_titleLayout.setTitle("验证消息");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        amvl_recyclerView.setLayoutManager(layoutManager);
        tipDialog = new QMUITipDialog.Builder(MessageValidationListActivity.this)
                .setTipWord("正在加载验证消息...")
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .create();
        amvl_empty_view.setVisibility(View.GONE);
        initData();
        initAdapter();
    }

    private void initData() {
        tipDialog.show();
        HttpUtil.sendGetRequest(Constant.GET_ALL_FRIENDS_TO_ME_REQUEST_URL + DataSupport.findLast(User.class).getUsername() + "&token=" + DataSupport.findLast(User.class).getToken(),
                new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        tipDialog.dismiss();
                        runOnUiThread(() -> {
                            Toast.makeText(MessageValidationListActivity.this, "加载异常！", Toast.LENGTH_SHORT).show();
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String body = response.body().string();
                        //解析json数据组装RestResponse对象
                        RestResponse restResponse = JSON.parseObject(body, RestResponse.class);
                        //RestResponse restResponse = new Gson().fromJson(body, RestResponse.class);
                        if (restResponse.getCode() == Constant.SUCCESS_CODE) {
                            mValidationMesg.clear();
                            mValidationMesg = JSON.parseArray(restResponse.getPayload().toString(), ValidationMesg.class);
                            if (mValidationMesg.size() != 0) {
                                for (ValidationMesg mesg : mValidationMesg) {
                                    if (mesg.getStatus() == 0 || mesg.getStatus() == 2) {
                                        mValidationMesgTemp.add(mesg);
                                    }
                                }
                            }
                            mValidationMesg = mValidationMesgTemp;
                            Log.v("tanxinkui2","m+length"+mValidationMesg.size());
                            runOnUiThread(() -> {
                                if (mValidationMesg.size() != 0) {
                                    amvl_empty_view.setVisibility(View.GONE);
                                } else {
                                    amvl_empty_view.setVisibility(View.VISIBLE);
                                }
                                //mMessageValidationListAdapter.notifyDataSetChanged();
                                initAdapter();
                                tipDialog.dismiss();
                            });
                        }
                    }
                });
    }

    private void initAdapter() {
        mMessageValidationListAdapter = new MessageValidationListAdapter(mValidationMesg);
        mMessageValidationListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.lmv_iv_accept:
                        acceptRequest(mMessageValidationListAdapter.getValidationId()[position]);
                        break;
                    case R.id.lmv_iv_refuse:
                        refuseRequest(mMessageValidationListAdapter.getValidationId()[position]);
                        break;
                    default:
                        break;
                }
            }
        });

        amvl_recyclerView.setAdapter(mMessageValidationListAdapter);
    }

    private void acceptRequest(int id) {
        HttpUtil.sendGetRequest(Constant.SET_REQUEST_STATUS_URL + id + "?status=1&token=" + DataSupport.findLast(User.class).getToken(),
                new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(() -> {
                            Toast.makeText(MessageValidationListActivity.this, "网络异常！", Toast.LENGTH_SHORT).show();
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        runOnUiThread(() -> {
                            Toast.makeText(MessageValidationListActivity.this, "已同意好友申请！", Toast.LENGTH_SHORT).show();
                            initData();
                        });
                    }
                });
    }

    private void refuseRequest(int id) {
        HttpUtil.sendGetRequest(Constant.SET_REQUEST_STATUS_URL + id + "?status=2&token=" + DataSupport.findLast(User.class).getToken(),
                new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(() -> {
                            Toast.makeText(MessageValidationListActivity.this, "网络异常！", Toast.LENGTH_SHORT).show();
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        runOnUiThread(() -> {
                            Toast.makeText(MessageValidationListActivity.this, "已拒绝好友申请！", Toast.LENGTH_SHORT).show();
                            initData();
                        });
                    }
                });
    }


}
