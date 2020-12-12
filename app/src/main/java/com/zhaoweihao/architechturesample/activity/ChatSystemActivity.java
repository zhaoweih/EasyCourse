package com.zhaoweihao.architechturesample.activity;

import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.adapter.ChatSystemAdapter;
import com.zhaoweihao.architechturesample.bean.ChatBean;
import com.zhaoweihao.architechturesample.presenter.ChatSystemPresenter;
import com.zhaoweihao.architechturesample.util.Constant;
import com.zhaoweihao.architechturesample.util.ToastUtil;
import com.zhaoweihao.architechturesample.view.uiview.ChatSystemView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


/**
 * 聊天系统界面
 * @author zhaowiehao
 * @date 2019/1/10
 */
@EActivity(R.layout.activity_chat_system)
public class ChatSystemActivity extends MvpActivity<ChatSystemPresenter> implements ChatSystemView, Runnable {

    @ViewById
    TextView tv_show_test;

    @ViewById
    QMUITopBarLayout topbar;

    @ViewById
    RecyclerView rv_chat_list;

    @ViewById
    EditText et_input_msg;

    @ViewById
    QMUIRoundButton btn_send_msg;

    @Extra(Constant.COURSE_ID)
    Integer courseId;

    private ChatSystemAdapter adapter;

    private List<ChatBean> chatBeans = new ArrayList<>();

    private Handler handler = new Handler();

    int lastMsgNum;

    /**
     * 发送者id
     */
    int senderId = 16;

    /**
     * 接收者id
     */
    int receiverId = 10;

    @Click
    void btn_send_msg() {

        ChatBean chat = new ChatBean();
        chat.setSender_id(senderId);
        chat.setClass_id(13);
        chat.setCourse_id(courseId);
        chat.setReceiver_id(receiverId);
        chat.setMsg_content(et_input_msg.getText().toString());

        presenter.sendMsg(chat);
    }


    @AfterViews
    void initView() {

        Log.d(TAG, "userId = " + sharedPreferences.user_id().get());

        //每秒请求数据，轮询获取消息
        handler.postDelayed(this, 1000);

        initTopBar();
        rv_chat_list.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChatSystemAdapter(R.layout.chat_item, chatBeans);
        adapter.setSenderId(senderId);
        rv_chat_list.setAdapter(adapter);
    }

    private void initTopBar() {
        topbar.addLeftImageButton(R.mipmap.angle_pointing_to_left_, R.id.qmui_topbar_item_left_back).setOnClickListener(v -> {
            ToastUtil.showShort(ChatSystemActivity.this, "点了返回键");
        });

        topbar.setTitle(TAG);
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(this);
        super.onDestroy();
    }



    @Override
    protected ChatSystemPresenter createPresenter() {
        return new ChatSystemPresenter(this, this);
    }

    @Override
    public void getDataFail(String msg) {
        ToastUtil.showShort(this, msg);
    }

    @Override
    public void getMsgsSucceed(int code, Object msg) {
        Type listType = new TypeToken<ArrayList<ChatBean>>(){}.getType();
        List<ChatBean> chatBeansData = new Gson().fromJson(msg.toString(), listType);
        chatBeans.clear();
        chatBeans.addAll(chatBeansData);
        if (chatBeans.size() != lastMsgNum) {
            adapter.notifyDataSetChanged();
            //滑动到底部
            rv_chat_list.scrollToPosition(adapter.getItemCount() - 1);
        }
        lastMsgNum = chatBeans.size();

    }

    @Override
    public void run() {
        presenter.getPersonalMsgs(senderId, receiverId);
        handler.postDelayed(this, 1000);
    }
}
