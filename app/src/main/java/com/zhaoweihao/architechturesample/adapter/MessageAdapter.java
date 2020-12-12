package com.zhaoweihao.architechturesample.adapter;

import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.bean.MessageUi;
import com.zhaoweihao.architechturesample.bean.ValidationMesgEvent;
import com.zhaoweihao.architechturesample.database.Memorandum;
import com.zhaoweihao.architechturesample.database.User;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageAdapter extends BaseItemDraggableAdapter<MessageUi, BaseViewHolder> {
    private int num;
    private int MemorandumNum = 0;
    private Button btn;

    public MessageAdapter(List<MessageUi> data, int num) {
        super(R.layout.layout_message, data);
        this.num = num;
        detectMemorandum();
    }

    @Override
    protected void convert(BaseViewHolder helper, MessageUi item) {
        helper.setImageDrawable(R.id.lm_iv_icon, item.getDrawable())
                .setText(R.id.lm_tv_title, item.getTitle())
                .setText(R.id.lm_tv_description, item.getDescription())
                .setText(R.id.lm_tv_date, item.getDate());
        btn = helper.getView(R.id.lm_btn_mesg);
        if (item.getTitle().equals("验证消息")) {
            Log.v("tanxkkkx", "validationMesgNum验证" + num);
            if (num != 0) {
                btn.setVisibility(View.VISIBLE);
                btn.setText("" + num);
                Log.v("tanxkkkx", "validationMesgNum验证2" + num);
            } else {
                btn.setVisibility(View.INVISIBLE);
                Log.v("tanxkkkx", "validationMesgNum验证3" + num);
            }
        }
        if (item.getTitle().equals("待办事项")) {
            if (MemorandumNum != 0) {
                btn.setVisibility(View.VISIBLE);
                btn.setText("" + MemorandumNum);
            } else {
                btn.setVisibility(View.INVISIBLE);
            }
        }
        if (item.getTitle().equals("通讯录")) {
            btn.setVisibility(View.INVISIBLE);
        }
        if (item.getTitle().equals("收件箱")) {
            btn.setVisibility(View.INVISIBLE);
        }

    }

    private Boolean ifIsExPire(long endTime) {
        Date date = new Date();
        long longExpend = endTime - date.getTime();
        if (longExpend < 0) {
            return true;
        } else {
            return false;
        }
    }

    private void detectMemorandum() {
        List<Memorandum> memorandumTemp = new ArrayList<>();
        memorandumTemp.clear();
        memorandumTemp.addAll(DataSupport.where("UserId=?", "" + DataSupport.findLast(User.class).getUserId()).find(Memorandum.class));
        for (Memorandum md : memorandumTemp) {
            if (ifIsExPire(md.getDate().getTime())) {
                MemorandumNum++;
            }
        }
    }


}
