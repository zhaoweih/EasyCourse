package com.zhaoweihao.architechturesample.adapter;

import android.view.View;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.bean.Chat;
import com.zhaoweihao.architechturesample.bean.ChatBean;
import com.zhaoweihao.architechturesample.util.Logger;

import java.util.List;

public class ChatWithWSAdapter extends BaseQuickAdapter<ChatBean, BaseViewHolder> {
    private int senderId;
    private int receiverId;
    public ChatWithWSAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ChatBean item) {
        Logger.d("item === " + item.toString());
        RelativeLayout rl_msg_sender = helper.getView(R.id.rl_msg_sender);
        RelativeLayout rl_msg_receiver = helper.getView(R.id.rl_msg_receiver);
        if (item.getSender_id() == senderId) {
            rl_msg_receiver.setVisibility(View.VISIBLE);
            helper.setText(R.id.tv_receiver_msg, item.getMsg_content());
        } else {
            rl_msg_sender.setVisibility(View.VISIBLE);
            helper.setText(R.id.tv_sender_msg, item.getMsg_content());
        }

    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}