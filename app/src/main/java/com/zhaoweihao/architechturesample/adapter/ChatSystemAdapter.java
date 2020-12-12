package com.zhaoweihao.architechturesample.adapter;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.bean.ChatBean;
import com.zhaoweihao.architechturesample.bean.Course;

import java.util.List;

public class ChatSystemAdapter extends BaseQuickAdapter<ChatBean, BaseViewHolder> {
    private int senderId;
    private int receiverId;
    public ChatSystemAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ChatBean item) {
//        helper.setText(R.id.text, item.getTitle());
//        helper.setImageResource(R.id.icon, item.getImageResource());
//        // 加载网络图片
//      Glide.with(mContext).load(item.getUserAvatar()).crossFade().into((ImageView) helper.getView(R.id.iv));
//        helper.setText(R.id.tv_couse_name, item.getCourseName());
//        helper.setText(R.id.tv_teacher_name, item.getTeacherName());
//        //添加子控件点击监听器
//        helper.addOnClickListener(R.id.btn_join);
//        helper.addOnClickListener(R.id.tv_couse_name);

        RelativeLayout rl_msg_sender = helper.getView(R.id.rl_msg_sender);
        RelativeLayout rl_msg_receiver = helper.getView(R.id.rl_msg_receiver);
        if (item.getSender_id() == senderId) {
            rl_msg_receiver.setVisibility(View.VISIBLE);
            helper.setText(R.id.tv_receiver_msg, item.getMsg_content());
        } else {
            rl_msg_sender.setVisibility(View.VISIBLE);
            helper.setText(R.id.tv_sender_msg, item.getMsg_content());
        }
//        helper.setText(R.id.tv_sender_msg, item.getMsg_content());

    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}