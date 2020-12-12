package com.zhaoweihao.architechturesample.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.bean.vote.Select;

import java.util.List;

public class ScoreAdapter extends BaseQuickAdapter<Select, BaseViewHolder> {

    public ScoreAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Select item) {

//        RelativeLayout rl_msg_sender = helper.getView(R.id.rl_msg_sender);
//        RelativeLayout rl_msg_receiver = helper.getView(R.id.rl_msg_receiver);
//        if (item.getSender_id() == senderId) {
//            rl_msg_receiver.setVisibility(View.VISIBLE);
//            helper.setText(R.id.tv_receiver_msg, item.getMsg_content());
//        } else {
//            rl_msg_sender.setVisibility(View.VISIBLE);
//            helper.setText(R.id.tv_sender_msg, item.getMsg_content());
//        }

        helper.setText(R.id.title, item.getTitle());
        helper.setText(R.id.tv_a, item.getChoiceA());
        helper.setText(R.id.tv_b, item.getChoiceB());
        helper.setText(R.id.tv_c, item.getChoiceC());
        helper.setText(R.id.tv_d, item.getChoiceD());

        helper.setText(R.id.right_answer, "正确答案是:" + (item.getRight_choice() == 0 ? "A" : (item.getRight_choice() == 1 ? "B" : (item.getRight_choice() == 2 ? "C" : "D"))));
        helper.setText(R.id.user_answer, "你的答案是:" + (item.getUser_choice() == 1 ? "A" : (item.getUser_choice() == 2 ? "B" : (item.getUser_choice() == 3 ? "C" : "D"))));


    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }
}