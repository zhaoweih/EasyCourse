package com.zhaoweihao.architechturesample.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.adapter.ChatWithWSAdapter;
import com.zhaoweihao.architechturesample.bean.ChatBean;
import com.zhaoweihao.architechturesample.bean.ChatRequestBean;
import com.zhaoweihao.architechturesample.bean.WSChatBean;
import com.zhaoweihao.architechturesample.database.User;
import com.zhaoweihao.architechturesample.presenter.ChatWIthWSPresenter;
import com.zhaoweihao.architechturesample.util.Constant;
import com.zhaoweihao.architechturesample.util.GsonUtil;
import com.zhaoweihao.architechturesample.util.Logger;
import com.zhaoweihao.architechturesample.util.ToastUtil;
import com.zhaoweihao.architechturesample.view.uiview.ChatWIthWSView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.java_websocket.handshake.ServerHandshake;
import org.litepal.crud.DataSupport;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


/**
 * Websocket聊天界面
 *
 * @author zhaowiehao
 * @date 2019/1/19
 */
@EActivity(R.layout.activity_chat_system)
public class ChatWithWSActivity extends MvpActivity<ChatWIthWSPresenter> implements ChatWIthWSView {

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

    @ViewById
    TextView tv_connect_state;

    @Extra(Constant.COURSE_ID)
    int courseId;

    private ChatWithWSAdapter adapter;

    private List<ChatBean> chatBeans = new ArrayList<>();

    int lastMsgNum;

    /**
     * 发送者id
     */
    int senderId = DataSupport.findLast(User.class).getUserId();

    /**
     * 接收者id
     */
    @Extra(Constant.RECEIVER_ID)
    int receiverId;

    /**
     * 是否好友聊天
     */
    @Extra(Constant.IS_FRIEND_TALK)
    boolean isFriendTalk;

    /**
     * 公司里头本机ip
     */
    private String address = "ws://192.168.8.110:8887";

    /**
     * 家里头本机ip
     */
    private String homeAddress = "ws://172.20.10.4:8887";

    private String onlineAddress = Constant.CHAT_WS_SERVER;

//    private String onlineAddress = address;

    /**
     * 在家还是公司
     */
    private boolean isHome = false;

    @Click
    void btn_send_msg() {

        WSChatBean wsChatBean = new WSChatBean();
        wsChatBean.setSender_id(senderId);
        wsChatBean.setClass_id(13);
        wsChatBean.setCourse_id(courseId);
        wsChatBean.setReceiver_id(receiverId);

        wsChatBean.setMsg_content(et_input_msg.getText().toString());

        ChatRequestBean chatRequestBean = new ChatRequestBean();
        chatRequestBean.setInfoType(isFriendTalk ? 3 : 1);
        chatRequestBean.setClassId(courseId);
        chatRequestBean.setData(wsChatBean);

        Logger.d("发送的消息对象 : " + GsonUtils.toJson(chatRequestBean));

        presenter.sendMsg(GsonUtil.GsonString(chatRequestBean));

        //延迟400毫秒
        et_input_msg.postDelayed(() -> et_input_msg.setText(""), 400);

    }

    @AfterViews
    void initView() {
        Log.d(TAG, "userId = " + sharedPreferences.user_id().get());

        initTopBar();

        tv_connect_state.setText("连接状态：正在连接...");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        rv_chat_list.setLayoutManager(layoutManager);
        adapter = new ChatWithWSAdapter(R.layout.chat_item, chatBeans);
        adapter.setSenderId(senderId);
        rv_chat_list.setAdapter(adapter);


        presenter.connect2WS(onlineAddress, courseId, senderId, receiverId, isFriendTalk);
    }

    private void initTopBar() {
        topbar.addLeftImageButton(R.mipmap.angle_pointing_to_left_, R.id.qmui_topbar_item_left_back).setOnClickListener(v -> {
            ToastUtil.showShort(ChatWithWSActivity.this, "点了返回键");
        });
        topbar.setTitle("聊天");
        try {
            if(getIntent().getStringExtra("username").equals("班级群聊")){
                topbar.setTitle("班级群聊");
            }else {
                topbar.setTitle(getIntent().getStringExtra("username"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        presenter.closeWS();
        super.onDestroy();
    }


    @Override
    protected ChatWIthWSPresenter createPresenter() {
        return new ChatWIthWSPresenter(this, this);
    }

    @Override
    public void getDataFail(String msg) {
        ToastUtil.showShort(this, msg);
    }

    @Override
    public void onWSMessage(String message) {
        Logger.d("isMainThread === " + ThreadUtils.isMainThread());
        Logger.d("onWSMessage === " + message);
        ChatRequestBean chatRequestBean = GsonUtil.GsonToBean(message, ChatRequestBean.class);

        if (chatRequestBean.getInfoType() == 0) {
            //第一次进入传递历史聊天记录
            Type listType = new TypeToken<ArrayList<ChatBean>>() {
            }.getType();
            List<ChatBean> chatBeansData = new Gson().fromJson(chatRequestBean.getData().toString(), listType);
            chatBeans.clear();
            chatBeans.addAll(chatBeansData);
//            if (chatBeans.size() != lastMsgNum) {
            runOnUiThread(() -> {
                adapter.notifyDataSetChanged();
                //滑动到底部
                rv_chat_list.scrollToPosition(adapter.getItemCount() - 1);
            });

            return;
        }

        if (chatRequestBean.getInfoType() == 2) {
            //第一次进入传递历史聊天记录
            Type listType = new TypeToken<ArrayList<ChatBean>>() {
            }.getType();
            List<ChatBean> chatBeansData = new Gson().fromJson(chatRequestBean.getData().toString(), listType);
            chatBeans.clear();
            chatBeans.addAll(chatBeansData);
//            if (chatBeans.size() != lastMsgNum) {
            runOnUiThread(() -> {
                adapter.notifyDataSetChanged();
                //滑动到底部
                rv_chat_list.scrollToPosition(adapter.getItemCount() - 1);
            });

            return;
        }

        if (chatRequestBean.getInfoType() == 1) {
            if (chatRequestBean.getClassId().equals(courseId) ) {
                ChatBean chatBean = GsonUtil.GsonToBean(chatRequestBean.getData().toString(), ChatBean.class);
                chatBeans.add(chatBean);
                runOnUiThread(() -> {
                    adapter.notifyDataSetChanged();
                    //滑动到底部
                    rv_chat_list.scrollToPosition(adapter.getItemCount() - 1);
                });
            }
        }

        if (chatRequestBean.getInfoType() == 3) {
            if (chatRequestBean.getClassId().equals(courseId) ) {
                ChatBean chatBean = GsonUtil.GsonToBean(chatRequestBean.getData().toString(), ChatBean.class);
                chatBeans.add(chatBean);
                runOnUiThread(() -> {
                    adapter.notifyDataSetChanged();
                    //滑动到底部
                    rv_chat_list.scrollToPosition(adapter.getItemCount() - 1);
                });
            }
        }




    }

    @Override
    public void onWSOpen(ServerHandshake handshake) {
        Logger.d("onWSOpen === ");
        runOnUiThread(() -> tv_connect_state.setText("连接状态：已经连接"));

    }

    @Override
    public void onWSClose(int code, String reason, boolean remote) {
        Logger.d("onWSClose === ");
        runOnUiThread(() -> tv_connect_state.setText("连接状态：断开连接"));


    }

    @Override
    public void onWSError(Exception ex) {
        ex.printStackTrace();
        Logger.d("onWSError === " + ex.getMessage());
        runOnUiThread(() -> tv_connect_state.setText("连接状态：断开连接"));

    }

}
