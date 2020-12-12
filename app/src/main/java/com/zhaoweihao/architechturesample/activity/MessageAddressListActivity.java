package com.zhaoweihao.architechturesample.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.adapter.MessageAddressListAdapter;
import com.zhaoweihao.architechturesample.base.BaseActivity;
import com.zhaoweihao.architechturesample.bean.AddAndShowActivity;
import com.zhaoweihao.architechturesample.bean.RestResponse;
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

/**
 * @author
 * @description 消息-通讯录-通讯录列表
 * @time 2019/3/9 15:40
 */
public class MessageAddressListActivity extends BaseActivity {
    @BindView(R.id.amal_recyclerView)
    RecyclerView amal_recyclerView;
    @BindView(R.id.amal_titleLayout)
    TitleLayout amal_titleLayout;
    @BindView(R.id.amal_empty_view)
    LinearLayout amal_empty_view;
    @BindView(R.id.amal_tv_totalNum)
    TextView amal_tv_totalNum;
    private QMUITipDialog tipDialog;
    private List<ValidationMesg> mValidationMesg = new ArrayList<>();
    private MessageAddressListAdapter mMessageAddressListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_address_list);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        amal_titleLayout.setTitle("通讯录");
        amal_tv_totalNum.setVisibility(View.GONE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        amal_recyclerView.setLayoutManager(layoutManager);
        tipDialog = new QMUITipDialog.Builder(MessageAddressListActivity.this)
                .setTipWord("正在加载...")
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .create();
        amal_empty_view.setVisibility(View.GONE);
        initData();
    }

    private void initData() {
        tipDialog.show();
        HttpUtil.sendGetRequest(Constant.GET_MY_ALL_FRIENDS_URL + DataSupport.findLast(User.class).getUsername() + "&token=" + DataSupport.findLast(User.class).getToken(),
                new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        tipDialog.dismiss();
                        runOnUiThread(() -> {
                            Toast.makeText(MessageAddressListActivity.this, "加载异常！", Toast.LENGTH_SHORT).show();
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

                            runOnUiThread(() -> {
                                if (mValidationMesg.size() != 0) {
                                    amal_empty_view.setVisibility(View.GONE);
                                    amal_tv_totalNum.setVisibility(View.VISIBLE);
                                    amal_tv_totalNum.setText(mValidationMesg.size() + "位联系人");
                                } else {
                                    amal_empty_view.setVisibility(View.VISIBLE);
                                }
                                initAdapter();
                                tipDialog.dismiss();
                            });
                        }
                    }
                });
    }

    private void initAdapter() {
        mMessageAddressListAdapter = new MessageAddressListAdapter(mValidationMesg);
        mMessageAddressListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });

        amal_recyclerView.setAdapter(mMessageAddressListAdapter);
    }


}
